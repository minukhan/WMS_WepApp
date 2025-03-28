package hyundai.partservice.app.section.application.service;

import hyundai.partservice.app.section.adapter.dto.EmptySpaceResponse;
import hyundai.partservice.app.section.application.port.in.EmptySpaceUseCase;
import hyundai.partservice.app.section.application.port.out.EmptySpacePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class EmptySpaceService implements EmptySpaceUseCase {

    private final EmptySpacePort emptySpacePort;

    @Override
    public EmptySpaceResponse emptySpaceResponse() {

        int currentQuantity = emptySpacePort.currentQuantity();
        int maxQuantity = emptySpacePort.getTotalQuantity();
        int emptySpace = maxQuantity - currentQuantity ;


        // 퍼센트 계산 (차있는 공간 / 전체 공간 * 100) 소수 첫째 자리까지
        double percent = 0.0;  // 초기값 설정
        if (maxQuantity > 0) {
            percent = (double) currentQuantity / maxQuantity * 100;  // 비율을 소수로 계산
        }

        // 소수 첫째 자리까지 반올림
        BigDecimal roundedPercent = new BigDecimal(percent).setScale(1, RoundingMode.HALF_UP);

        return EmptySpaceResponse.of(currentQuantity, maxQuantity, emptySpace, roundedPercent.doubleValue());

    }
}
