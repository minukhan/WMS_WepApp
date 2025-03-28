package hyundai.partservice.app.section.adapter.dto;


import java.util.List;

public record SectionPurchaseResponse(
    List<SectionDto> sectionDtos
) {
    public static SectionPurchaseResponse from(List<SectionDto> sectionDtos) {
        return new SectionPurchaseResponse(sectionDtos);
    }

}
