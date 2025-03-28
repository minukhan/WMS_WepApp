package hyundai.qrservice.qr.adapter.in;


import hyundai.qrservice.qr.application.port.in.QRCodeContent;
import hyundai.qrservice.qr.application.port.in.ReadQRCodeCommand;
import hyundai.qrservice.qr.application.port.in.ReadQRCodeUseCase;
import hyundai.qrservice.qr.exception.QRCodeReadException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class ReadQRCodeController {
    private final ReadQRCodeUseCase readQRCodeUseCase;


    @PostMapping(
            value = "/api/qr/read",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE

    )
    public ResponseEntity<QRCodeContent> readQRCode(@RequestParam("file") MultipartFile file) {
        try {
            // 파일이 비어있는지 검증
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            // MultipartFile을 byte 배열로 변환하고 Command 객체 생성
            byte[] imageData = file.getBytes();
            ReadQRCodeCommand command = new ReadQRCodeCommand(imageData);

            // UseCase를 통해 QR 코드 읽기 실행
            QRCodeContent content = readQRCodeUseCase.readQRCode(command);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(content);

        } catch (IOException e) {
            // 파일 읽기 실패 시 에러 응답
            throw new QRCodeReadException();
        } catch (IllegalArgumentException e) {
            // 유효성 검증 실패 시 에러 응답
            return ResponseEntity.badRequest().build();
        }
    }
}
