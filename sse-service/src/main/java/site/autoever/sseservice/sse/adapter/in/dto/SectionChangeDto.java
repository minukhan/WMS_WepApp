package site.autoever.sseservice.sse.adapter.in.dto;

public record SectionChangeDto(
        String sectionName,
        int floor,
        boolean isAdd
) {
}
