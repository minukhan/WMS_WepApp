package hyundai.qrservice.qr.application.port.in;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GenerateQRCodeRequest {
    private String name;
    private int width;
    private int height;
}
