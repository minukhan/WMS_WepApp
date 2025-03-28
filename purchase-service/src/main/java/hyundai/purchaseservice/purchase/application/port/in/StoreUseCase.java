package hyundai.purchaseservice.purchase.application.port.in;

public interface StoreUseCase {
    void addProcessedQuantity(String partId, String sectionName);
}
