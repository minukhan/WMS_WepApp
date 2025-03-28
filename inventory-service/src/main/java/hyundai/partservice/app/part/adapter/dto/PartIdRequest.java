package hyundai.partservice.app.part.adapter.dto;

import java.util.List;

public record PartIdRequest (
        List<String> partIds
){
    public static PartIdRequest from(List<String> partIds){
        return new PartIdRequest(partIds);
    }
}
