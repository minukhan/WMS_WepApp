package hyundai.qrservice.qr.adapter.out;

import hyundai.qrservice.infrastructure.repository.QRCodeRepository;
import hyundai.qrservice.qr.application.domain.Qr;
import hyundai.qrservice.qr.application.port.out.LoadQRCodePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReadQRCodeAdapter implements LoadQRCodePort {

    private final QRCodeRepository qrCodeRepository;

    @Override
    public Optional<Qr> loadById(Long id) {    // LoadQRCodePort의 파라미터 타입에 맞춰 Long에서 String으로 변경
        return qrCodeRepository.findById(id);     // 매퍼 제거하고 직접 반환
    }
}
