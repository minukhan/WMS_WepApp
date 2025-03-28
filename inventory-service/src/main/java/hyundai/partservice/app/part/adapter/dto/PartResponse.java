package hyundai.partservice.app.part.adapter.dto;

import hyundai.partservice.app.part.application.entity.Part;

public record PartResponse(
        PartDto partDto
) {
    public static PartResponse from(PartDto partDto) {
        return new PartResponse(partDto);
    }
}
