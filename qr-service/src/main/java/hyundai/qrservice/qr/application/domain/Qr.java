package hyundai.qrservice.qr.application.domain;

import com.fasterxml.jackson.annotation.JsonTypeId;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "qr")
public class Qr {

    @Id
    @Column(name="qr_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    private int width;         // QR 코드 이미지 너비
    private int height;

    @Enumerated(EnumType.STRING)
    private  ErrorCorrectionLevel errorCorrectionLevel;

}
