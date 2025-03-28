package hyundai.partservice.app.section.adapter.dto;

public record SectionResponse(
        SectionDto sectionDto
) {
    public static SectionResponse from(SectionDto sectionDto) {
        return new SectionResponse(
                sectionDto
        );
    }
}
