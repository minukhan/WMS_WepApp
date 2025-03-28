package hyundai.qrservice.qr.application.port.in;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public record GenerateQRCodeCommand(String name, int width, int height, ErrorCorrectionLevel errorCorrectionLevel) {

    public static GenerateQRCodeCommand of(String name, int width, int height) {
        System.out.println("-------------------------------------");
        System.out.println(ErrorCorrectionLevel.L);
        System.out.println("-------------------------------------");
        return new GenerateQRCodeCommand(name, width, height, ErrorCorrectionLevel.L);
    }
}
