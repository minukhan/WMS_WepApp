package site.autoever.reportservice.report.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import site.autoever.reportservice.report.application.domain.Report;
import site.autoever.reportservice.report.application.dto.purchase.PurchasePartAutoStockInfoDto;
import site.autoever.reportservice.report.application.dto.purchase.PurchasePartExpenseInfoDto;
import site.autoever.reportservice.report.application.dto.purchase.part1.PurchaseExpenseDto;
import site.autoever.reportservice.report.application.dto.purchase.part1.PurchaseRequestDto;
import site.autoever.reportservice.report.application.dto.purchase.part1.PurchaseSummaryDto;
import site.autoever.reportservice.report.application.dto.purchase.part2.PurchaseAutoStockSummaryDto;
import site.autoever.reportservice.report.application.dto.purchase.part2.PurchaseExpensesSummaryDto;
import site.autoever.reportservice.report.application.dto.supply.part1.SupplyApprovalDto;
import site.autoever.reportservice.report.application.dto.supply.part1.SupplyCountDto;
import site.autoever.reportservice.report.application.dto.supply.part1.SupplySummaryDto;
import site.autoever.reportservice.report.application.dto.supply.part2.SupplyQuantitySummaryDto;
import site.autoever.reportservice.report.application.dto.supply.SupplyPartInfoDto;
import site.autoever.reportservice.report.application.port.out.CreateAlarmPort;
import site.autoever.reportservice.report.application.port.out.CreateReportPort;
import site.autoever.reportservice.report.application.port.out.PurchaseReportPort;
import site.autoever.reportservice.report.application.port.out.SupplyReportPort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GenerateMonthlyReportServiceTest {

    @Mock
    private SupplyReportPort supplyReportPort;

    @Mock
    private PurchaseReportPort purchaseReportPort;

    @Mock
    private CreateReportPort createReportPort;

    @Mock
    private CreateAlarmPort createAlarmPort;

    @InjectMocks
    private GenerateMonthlyReportService service;

    @BeforeEach
    void setUp() {
        // MockitoExtension가 이미 초기화를 수행하므로 별도 초기화 필요 없음.
    }

    @Test
    @DisplayName("구매 데이터 수집 성공")
    void testCollectPurchaseData_Success() {
        // 구매 관련 DTO 모킹
        PurchaseExpenseDto expenseDto = new PurchaseExpenseDto(10000, 8000, 2000, 25.0);
        PurchaseRequestDto requestDto = new PurchaseRequestDto(500, 400, 100, 20.0);
        PurchaseSummaryDto purchaseSummaryDto = new PurchaseSummaryDto(expenseDto, requestDto);

        PurchasePartAutoStockInfoDto autoStockInfo = new PurchasePartAutoStockInfoDto("Part A", 100, 80, 20, 25.0);
        PurchaseAutoStockSummaryDto autoStockSummaryDto = new PurchaseAutoStockSummaryDto(List.of(autoStockInfo));

        PurchasePartExpenseInfoDto expenseInfo = new PurchasePartExpenseInfoDto("Part B", 5000, 4000, 1000, 25.0);
        PurchaseExpensesSummaryDto expensesSummaryDto = new PurchaseExpensesSummaryDto(List.of(expenseInfo));

        // Mock 설정
        when(purchaseReportPort.getPurchaseSummary(anyInt(), anyInt())).thenReturn(purchaseSummaryDto);
        when(purchaseReportPort.getPurchaseAutoStockSummary(anyInt(), anyInt())).thenReturn(autoStockSummaryDto);
        when(purchaseReportPort.getPurchaseExpensesSummary(anyInt(), anyInt())).thenReturn(expensesSummaryDto);

        // 메서드 실행
        service.collectPurchaseData();

        // 검증
        verify(purchaseReportPort, times(1)).getPurchaseSummary(anyInt(), anyInt());
        verify(purchaseReportPort, times(1)).getPurchaseAutoStockSummary(anyInt(), anyInt());
        verify(purchaseReportPort, times(1)).getPurchaseExpensesSummary(anyInt(), anyInt());

        assertEquals(10000, purchaseSummaryDto.expenseSummary().currentTotalExpense());
        assertEquals(500, purchaseSummaryDto.requestSummary().currentRequestOrders());
        assertEquals(100, autoStockSummaryDto.parts().get(0).currentMonthQuantity());
        assertEquals(5000, expensesSummaryDto.parts().get(0).currentMonthTotalExpense());
    }

    @Test
    @DisplayName("구매 데이터 수집 실패: 자동발주 summary 누락")
    void testCollectPurchaseData_Failure_AutoStock() {
        // 구매 관련 DTO 모킹
        PurchaseExpenseDto expenseDto = new PurchaseExpenseDto(10000, 8000, 2000, 25.0);
        PurchaseRequestDto requestDto = new PurchaseRequestDto(500, 400, 100, 20.0);
        PurchaseSummaryDto purchaseSummaryDto = new PurchaseSummaryDto(expenseDto, requestDto);

        // 자동발주 summary를 null로 모킹
        when(purchaseReportPort.getPurchaseSummary(anyInt(), anyInt())).thenReturn(purchaseSummaryDto);
        when(purchaseReportPort.getPurchaseAutoStockSummary(anyInt(), anyInt())).thenReturn(null);

        // 예외 발생 검증
        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.collectPurchaseData());
        assertTrue(exception.getMessage().contains("자동발주 summary 조회 실패"));
    }

    @Test
    @DisplayName("공급 데이터 수집 성공")
    void testCollectSupplyData_Success() {
        // Supply 관련 DTO 모킹
        SupplyCountDto countDto = new SupplyCountDto(1000, 900, 100, 11.1);
        SupplyApprovalDto approvalDto = new SupplyApprovalDto(600, 550, 91.7, 85.0);
        SupplySummaryDto supplySummaryDto = new SupplySummaryDto(countDto, approvalDto);

        SupplyPartInfoDto partInfo1 = new SupplyPartInfoDto("Part X", 500, 450, 600, 50, 11.1);
        SupplyPartInfoDto partInfo2 = new SupplyPartInfoDto("Part Y", 300, 350, 320, -50, -14.3);
        SupplyQuantitySummaryDto quantitySummaryDto = new SupplyQuantitySummaryDto(List.of(partInfo1, partInfo2));

        // Mock 설정
        when(supplyReportPort.getSupplySummary(anyInt(), anyInt())).thenReturn(supplySummaryDto);
        when(supplyReportPort.getSupplyQuantitySummary(anyInt(), anyInt())).thenReturn(quantitySummaryDto);

        // 메서드 실행
        service.collectSupplyData();

        // 검증
        verify(supplyReportPort, times(1)).getSupplySummary(anyInt(), anyInt());
        verify(supplyReportPort, times(1)).getSupplyQuantitySummary(anyInt(), anyInt());

        assertEquals(1000, supplySummaryDto.countSummary().currentTotalPartBox());
        assertEquals(91.7, supplySummaryDto.approveSummary().currentApprovalRate());
        assertEquals("Part X", quantitySummaryDto.parts().get(0).partName());
        assertEquals(500, quantitySummaryDto.parts().get(0).currentMonthQuantity());
    }

    @Test
    @DisplayName("보고서 생성 및 저장 성공")
    void testGenerateMonthlyReport_Success() {
        // 구매 데이터 모킹
        PurchaseExpenseDto expenseDto = new PurchaseExpenseDto(10000, 8000, 2000, 25.0);
        PurchaseRequestDto requestDto = new PurchaseRequestDto(500, 400, 100, 20.0);
        PurchaseSummaryDto purchaseSummaryDto = new PurchaseSummaryDto(expenseDto, requestDto);

        PurchasePartAutoStockInfoDto autoStockInfo = new PurchasePartAutoStockInfoDto("Part A", 100, 80, 20, 25.0);
        PurchaseAutoStockSummaryDto autoStockSummaryDto = new PurchaseAutoStockSummaryDto(List.of(autoStockInfo));

        PurchasePartExpenseInfoDto expenseInfo = new PurchasePartExpenseInfoDto("Part B", 5000, 4000, 1000, 25.0);
        PurchaseExpensesSummaryDto expensesSummaryDto = new PurchaseExpensesSummaryDto(List.of(expenseInfo));

        when(purchaseReportPort.getPurchaseSummary(anyInt(), anyInt())).thenReturn(purchaseSummaryDto);
        when(purchaseReportPort.getPurchaseAutoStockSummary(anyInt(), anyInt())).thenReturn(autoStockSummaryDto);
        when(purchaseReportPort.getPurchaseExpensesSummary(anyInt(), anyInt())).thenReturn(expensesSummaryDto);

        // 공급 데이터 모킹
        SupplyCountDto countDto = new SupplyCountDto(1000, 900, 100, 11.1);
        SupplyApprovalDto approvalDto = new SupplyApprovalDto(600, 550, 91.7, 85.0);
        SupplySummaryDto supplySummaryDto = new SupplySummaryDto(countDto, approvalDto);

        SupplyPartInfoDto partInfo1 = new SupplyPartInfoDto("Part X", 500, 450, 600, 50, 11.1);
        SupplyPartInfoDto partInfo2 = new SupplyPartInfoDto("Part Y", 300, 350, 320, -50, -14.3);
        SupplyQuantitySummaryDto quantitySummaryDto = new SupplyQuantitySummaryDto(List.of(partInfo1, partInfo2));

        when(supplyReportPort.getSupplySummary(anyInt(), anyInt())).thenReturn(supplySummaryDto);
        when(supplyReportPort.getSupplyQuantitySummary(anyInt(), anyInt())).thenReturn(quantitySummaryDto);

        // Report 저장 시 Mock 처리
        doNothing().when(createReportPort).save(any(Report.class));

        // 데이터 수집 후 보고서 생성 실행
        service.collectPurchaseData();
        service.collectSupplyData();
        service.processData();
        assertDoesNotThrow(() -> service.generateMonthlyReport());

        // 검증: createReportPort.save()가 한 번 호출되었는지 확인
        verify(createReportPort, times(1)).save(any(Report.class));
    }

    @Test
    @DisplayName("알람 발송 성공")
    void testSendAlarm_Success() {
        // 알람 발송 테스트
        assertDoesNotThrow(() -> service.sendAlarm());
        verify(createAlarmPort, times(1)).createAlarm(any());
    }
}
