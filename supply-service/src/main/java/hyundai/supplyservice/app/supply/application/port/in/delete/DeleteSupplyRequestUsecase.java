package hyundai.supplyservice.app.supply.application.port.in.delete;

public interface DeleteSupplyRequestUsecase {
    DeleteSupplyResponseDto deleteSupplyRequest(Long requestId);
}
