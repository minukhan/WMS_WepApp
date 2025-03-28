package hyundai.safeservice.app.safe_Stock.application.service;

import hyundai.safeservice.app.propriety_Stock.adapter.dto.fein.PartDto;
import hyundai.safeservice.app.propriety_Stock.application.port.fein.PartFeinClient;
import hyundai.safeservice.app.propriety_Stock.application.port.fein.SupplyFeinClient;
import hyundai.safeservice.app.safe_Stock.adapter.dto.AiResponse;
import hyundai.safeservice.app.safe_Stock.adapter.dto.PartInfoWithAIDto;
import hyundai.safeservice.app.safe_Stock.adapter.dto.PartSafeListResponse;
import hyundai.safeservice.app.safe_Stock.adapter.dto.fein.PartCountDto;
import hyundai.safeservice.app.safe_Stock.application.port.in.ReportAiUseCase;
import hyundai.safeservice.app.safe_Stock.application.port.out.ReportAiPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReportAiService implements ReportAiUseCase {

    private final PartFeinClient partFeinClient;
    private final ReportAiPort reportAiPort;
    private final SupplyFeinClient supplyFeinClient;
    @Override
    public PartSafeListResponse getReport(int year, int month) {


        List<PartCountDto> partCountDtos = supplyFeinClient.getMonthlyTotalQuantityByPart(year, month).getBody();

        List<PartDto> parts = partFeinClient.getPartList().partGetAllDtos();

        Map<String, Integer> partCountMap = partCountDtos.stream()
                .collect(Collectors.toMap(PartCountDto::partId, PartCountDto::quantity));

        List<PartInfoWithAIDto> partInfoWithAIDtos = parts.stream().map(
                part -> {

                    // 전월 출고량 데이터 가져오기 (없으면 0)
                    int lastMonthQuantity = partCountMap.getOrDefault(part.partId(), 0);

                    // AI 안전재고 예측 데이터 요청
                    AiResponse predictedSafeStock = reportAiPort.getPredictedStock(part.partId(), lastMonthQuantity);

                    int adjustedSafetyStock = calculateSafetyStock(part.safetyStock() ,lastMonthQuantity, predictedSafeStock.predictedNextMonthSales(), part.deliveryDuration());

                    //적정재고 추천
                    int optimalStock = calculateOptimalStock(adjustedSafetyStock, part.safetyStock(), part.optimalStock());

                    return PartInfoWithAIDto.of(part,lastMonthQuantity,predictedSafeStock.predictedNextMonthSales(),adjustedSafetyStock, optimalStock);
                })
                .collect(Collectors.toList());

        return PartSafeListResponse.from(partInfoWithAIDtos);
    }

    private int calculateOptimalStock(int adjustedSafetyStock, int prevSafetyStock, int prevOptimalStock) {
        double increaseFactor = 1.10 + (Math.random() * 0.05); //

        // 기본 적정재고 계산
        int optimalStock = (int) Math.ceil(prevOptimalStock * increaseFactor);

        // 안전재고 변화율 계산
        double safetyStockChangeRate = (double) adjustedSafetyStock / prevSafetyStock;

        // 변화율 적용 (안전재고가 증가하면 적정재고도 증가)
        optimalStock = (int) Math.ceil(optimalStock * safetyStockChangeRate);

        // 최소 적정재고 보장 (안전재고보다 낮으면 안 됨)
        return Math.max(optimalStock, adjustedSafetyStock);
    }


    private int calculateSafetyStock(int prevSafeStock, int lastMonthQuantity, int predictedNextMonthSales, int deliveryDuration) {
        // (1) 일평균 사용량 계산 (30일 기준)
        double dailyUsage = predictedNextMonthSales / 30.0;

        // (2) 기본 안전재고 계산: 일평균 사용량 * 납기일
        int baseSafetyStock = (int) Math.ceil(dailyUsage * deliveryDuration);

        // (3) 이전 출고량이 없으면 기존 안전재고 유지 (예외적으로 기존 값 유지)
        if (lastMonthQuantity == 0) {
            return prevSafeStock;
        }

        // (4) 예측 판매량 대비 기존 출고량 변화율 계산 (0 방지 & 과도한 증가율 제한)
        double salesChangeRate = (double) predictedNextMonthSales / lastMonthQuantity;
        salesChangeRate = Math.min(salesChangeRate, 2.0); // **최대 2배 증가 제한**

        // (5) 안전재고 조정 (증가율 적용)
        int adjustedSafetyStock = (int) Math.ceil(baseSafetyStock * salesChangeRate);

        // (6) 기존 안전재고(prevSafeStock) 대비 최대 2배 이상 증가하지 않도록 제한
        adjustedSafetyStock = Math.min(adjustedSafetyStock, prevSafeStock * 2);

        // (7) 출고량이 증가했더라도 안전재고는 감소하지 않도록 제한
        adjustedSafetyStock = Math.max(adjustedSafetyStock, prevSafeStock);

        // (8) 최소 안전재고 보장 (최소 1개 이상)
        return Math.max(adjustedSafetyStock, 1);
    }


}
