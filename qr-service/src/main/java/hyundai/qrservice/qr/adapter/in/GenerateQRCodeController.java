package hyundai.qrservice.qr.adapter.in;

import hyundai.qrservice.qr.application.port.in.GenerateQRCodeCommand;
import hyundai.qrservice.qr.application.port.in.GenerateQRCodeRequest;
import hyundai.qrservice.qr.application.port.in.GenerateQRCodeUseCase;
import hyundai.qrservice.qr.application.port.in.QRCodeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GenerateQRCodeController {
    private final GenerateQRCodeUseCase generateQRCodeUseCase;

    @PostMapping("/api/qr/generate")
    public ResponseEntity<QRCodeResponse> generateQRCode(@RequestBody GenerateQRCodeRequest request) {

        // Request DTO를 Command로 변환
        GenerateQRCodeCommand command = GenerateQRCodeCommand.of(
                request.getName(),
                request.getWidth(),
                request.getHeight()
        );

        QRCodeResponse response = generateQRCodeUseCase.generateQRCode(command);
        return ResponseEntity.ok(response);
    }



}
