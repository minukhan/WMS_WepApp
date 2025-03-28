package hyundai.partservice.app.part.adapter.dto;


import hyundai.partservice.app.part.application.entity.Part;

public record OnlyPartNamePartIdDto(
        String partId,
        String partName
) {

    public static OnlyPartNamePartIdDto from(Part part){
        return new OnlyPartNamePartIdDto(
                part.getId(),
                part.getName()
        );
    }
}
