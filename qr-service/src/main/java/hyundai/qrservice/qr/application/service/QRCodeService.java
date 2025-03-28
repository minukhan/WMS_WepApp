package hyundai.qrservice.qr.application.service;

import hyundai.qrservice.infrastructure.QRCodeGenerator;
import hyundai.qrservice.qr.application.domain.Qr;
import hyundai.qrservice.qr.application.port.in.*;
import hyundai.qrservice.qr.application.port.out.LoadQRCodePort;
import hyundai.qrservice.qr.application.port.out.SaveQRCodePort;
import hyundai.qrservice.qr.exception.QRCodeReadException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional
public class QRCodeService implements GenerateQRCodeUseCase, ReadQRCodeUseCase {

    private final SaveQRCodePort saveQRCodePort;
    private final LoadQRCodePort loadQRCodePort;
    private final QRCodeGenerator qrCodeGenerator;

    @Override
    public QRCodeResponse generateQRCode(GenerateQRCodeCommand command) {
        // 도메인 객체 생성
        Qr qrCode = Qr.builder()
                .name(command.name())
                .width(command.width())
                .height(command.height())
                .errorCorrectionLevel(command.errorCorrectionLevel())
                .build();

        // QR 코드 이미지 생성 시 ErrorCorrectionLevel 변환
        byte[] qrCodeImage = qrCodeGenerator.generate(
                qrCode.getName(),
                qrCode.getWidth(),
                qrCode.getHeight(),
                qrCode.getErrorCorrectionLevel()  // 변환 메서드 사용
        );

        saveQRCodePort.save(qrCode);
        return new QRCodeResponse(qrCode.getId(), qrCodeImage);
    }


    @Override
    public QRCodeContent readQRCode(ReadQRCodeCommand command) {
        try {
            // QR 코드 이미지에서 내용 읽기
            String content = qrCodeGenerator.read(command.getImageData());


            return new QRCodeContent(content);
        } catch (Exception e) {
            throw new QRCodeReadException();
        }
    }
}
