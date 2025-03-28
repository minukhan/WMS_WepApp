package hyundai.partservice.app.part.adapter.dto;

import java.util.List;

public record PartAuthenticationResponse(

        int total,
        int wareHouseCurrentTotalCount,
        int maxWareHouseTotal,
        List<PartAuthenticationDto>  partAuthenticationDtos
) {
    public static PartAuthenticationResponse of(int total, int wareHouseCurrentTotalCount,int maxWareHouseTotal,List<PartAuthenticationDto> partAuthenticationDtos) {
        return new PartAuthenticationResponse(total, wareHouseCurrentTotalCount,maxWareHouseTotal, partAuthenticationDtos);
    }
}
