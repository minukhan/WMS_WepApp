package hyundai.qrservice.infrastructure.repository;

import hyundai.qrservice.qr.application.domain.Qr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QRCodeRepository extends JpaRepository<Qr, Long> {
}
