package site.autoever.verifyservice.verify.application.domain;

public enum VerificationMessage {
    // 성공 메시지
    SUCCESS_INVENTORY_SUFFICIENT("재고 충분"),
    SUCCESS_INVENTORY_INSUFFICIENT_BUT_PROCESSABLE("재고 불충분이지만, 기한 내 입고 처리 가능"),

    // 실패 메시지
    FAILURE_DELIVERY_TIME_EXCEEDED("기한 내 배송 불가"),
    FAILURE_INSUFFICIENT_WAREHOUSE_CAPACITY("창고 용량 부족"),
    FAILURE_DUE_DATE_TIME_EXCEED("마감 기한 만료");
    private final String message;

    VerificationMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
