package site.autoever.reportservice.infrastructure.util;

import org.springframework.stereotype.Component;
import site.autoever.reportservice.report.application.dto.purchase.part1.PurchaseExpenseDto;
import site.autoever.reportservice.report.application.dto.purchase.part1.PurchaseRequestDto;
import site.autoever.reportservice.report.application.dto.supply.part1.SupplyApprovalDto;
import site.autoever.reportservice.report.application.dto.supply.part1.SupplyCountDto;

import java.text.NumberFormat;
import java.util.Locale;

@Component
public class MessageGenerator {
    private static final String PREFIX = "전월 대비";
    private static final String UP = "↑";
    private static final String PLUS = "+";
    private static final String PLUS_MESSAGE = "증가했습니다.";
    private static final String DOWN = "↓";
    private static final String MINUS = "-";
    private static final String MINUS_MESSAGE = "감소했습니다.";

    private String formatNumber(long number) {
        NumberFormat formatter = NumberFormat.getInstance(Locale.KOREA);
        return formatter.format(number);
    }

    private String generateMessage(double changeRate, long changeQuantity, String unit) {
        // changeRate가 -Infinity일 경우 0으로 처리
        if (Double.isInfinite(changeRate) && changeRate < 0) {
            changeRate = 0;
        }

        String direction = changeQuantity < 0 ? DOWN : UP;
        String sign = changeRate < 0 ? "" : PLUS;  // 음수일 때는 - 기호 그대로 사용

        return String.format("%s %s : %s%.2f%% ( %s%s ) %s",
                PREFIX, direction, sign, changeRate, formatNumber(changeQuantity), unit,
                changeQuantity < 0 ? MINUS_MESSAGE : PLUS_MESSAGE);
    }

    public String getTotalShippingQuantityMessage(SupplyCountDto supplyCountDto) {
        return generateMessage(supplyCountDto.changeRate(), supplyCountDto.changeQuantity(), "Box");
    }

    public String getTotalExpensesMessage(PurchaseExpenseDto purchaseExpenseDto) {
        return generateMessage(purchaseExpenseDto.changeRate(), purchaseExpenseDto.changeQuantity(), "￦");
    }

    public String getTotalPartRequestCountMessage(PurchaseRequestDto purchaseRequestDto) {
        return generateMessage(purchaseRequestDto.changeRate(), purchaseRequestDto.changeQuantity(), "Box");
    }

    public String getApprovalRateMessage(SupplyApprovalDto supplyApprovalDto) {
        double currentRate = supplyApprovalDto.currentApprovalRate();
        double lastRate = supplyApprovalDto.lastMonthApprovalRate();
        double changeRate = currentRate - lastRate;

        // changeRate가 -Infinity일 경우 0으로 처리
        if (Double.isInfinite(changeRate) && changeRate < 0) {
            changeRate = 0;
        }

        String direction = changeRate < 0 ? DOWN : UP;
        String sign = changeRate < 0 ? "" : PLUS;

        return String.format("%s %s : %s%.2f%% %s",
                PREFIX, direction, sign, changeRate,
                changeRate < 0 ? MINUS_MESSAGE : PLUS_MESSAGE);
    }
}
