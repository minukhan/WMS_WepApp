package hyundai.partservice.app.part.adapter.dto;


import java.util.List;

public record PartListResponse(
        List<PartDto> partGetAllDtos
) {
    public static PartListResponse from(List<PartDto> partGetAllDtos) {
        return new PartListResponse(partGetAllDtos);
    }
}