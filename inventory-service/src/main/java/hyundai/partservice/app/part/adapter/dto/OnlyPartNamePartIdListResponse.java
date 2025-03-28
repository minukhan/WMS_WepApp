package hyundai.partservice.app.part.adapter.dto;


import java.util.List;

public record OnlyPartNamePartIdListResponse(
        List<OnlyPartNamePartIdDto> onlyPartNamePartIdDtos
) {
    public static OnlyPartNamePartIdListResponse from(List<OnlyPartNamePartIdDto> onlyPartNamePartIdDtos) {
        return new OnlyPartNamePartIdListResponse(onlyPartNamePartIdDtos);
    }
}
