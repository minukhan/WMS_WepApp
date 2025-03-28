package hyundai.qrservice.infrastructure;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import hyundai.qrservice.qr.application.domain.Qr;
import hyundai.qrservice.qr.exception.QRCodeGenerationException;
import hyundai.qrservice.qr.exception.QRCodeReadException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class QRCodeGenerator {

    public byte[] generate(String content, int width, int height, ErrorCorrectionLevel errorCorrectionLevel) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(
                    content,
                    BarcodeFormat.QR_CODE,
                    width,
                    height,
                    createHints(errorCorrectionLevel)
            );

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            return outputStream.toByteArray();

        } catch (Exception e) {
            log.error("QR 코드 생성 중 오류 발생", e);
            throw new QRCodeGenerationException();
        }
    }

    public String read(byte[] imageData) {
        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageData));
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(
                    new BufferedImageLuminanceSource(image)));

            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (Exception e) {
            log.error("QR 코드 읽기 중 오류 발생", e);
            throw new QRCodeReadException();
        }
    }

    private Map<EncodeHintType, Object> createHints(ErrorCorrectionLevel level) {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, level);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        return hints;
    }
}
