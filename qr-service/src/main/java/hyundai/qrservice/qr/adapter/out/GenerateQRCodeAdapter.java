package hyundai.qrservice.qr.adapter.out;

import hyundai.qrservice.infrastructure.repository.QRCodeRepository;
import hyundai.qrservice.qr.application.domain.Qr;
import hyundai.qrservice.qr.application.port.out.SaveQRCodePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GenerateQRCodeAdapter implements SaveQRCodePort {

    private final QRCodeRepository qrCodeRepository;

    @Override
    public void save(Qr qrCode) {
        // Qr 클래스가 도메인 모델이자 엔티티이므로 직접 저장
        qrCodeRepository.save(qrCode);
    }
}
