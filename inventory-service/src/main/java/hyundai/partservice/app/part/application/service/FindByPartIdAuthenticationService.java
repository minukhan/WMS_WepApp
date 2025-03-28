package hyundai.partservice.app.part.application.service;

import hyundai.partservice.app.part.adapter.dto.PartAuthenticationDto;
import hyundai.partservice.app.part.adapter.dto.PartAuthenticationResponse;
import hyundai.partservice.app.part.adapter.dto.PartIdRequest;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.part.application.port.in.FindByPartIdAuthenticationUseCase;
import hyundai.partservice.app.part.application.port.out.FindByPartIdAuthenticationPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FindByPartIdAuthenticationService implements FindByPartIdAuthenticationUseCase {

    private final FindByPartIdAuthenticationPort findByPartIdAuthenticationPort;

    @Override
    public PartAuthenticationResponse findByPartIds(PartIdRequest request) {

        // 조건에 맞는 part들을 불러옴.
        List<Part> parts = request.partIds().stream()
                .map(partId -> findByPartIdAuthenticationPort.findByPartId(partId))
                .collect(Collectors.toList());

        //조건에 맞는 parts들만 Dto 로 변환
        List<PartAuthenticationDto> partAuthenticationDtos =  parts.stream()
                .map(part -> PartAuthenticationDto.of(
                        part,
                        findByPartIdAuthenticationPort.partCount(part.getId())))
                .collect(Collectors.toList());

        // total 계산 과정
        int total =0;

        for(int i=0;i<partAuthenticationDtos.size();i++){
            total += partAuthenticationDtos.get(i).currentStock();
        }
        // 현재 재고 수량
        int wareHouseCurrentCount = findByPartIdAuthenticationPort.currentTotalCount();
        // 창고 최대 수량
        int maxWareHouseTotal = 51840;

        return PartAuthenticationResponse.of(total,wareHouseCurrentCount,maxWareHouseTotal,partAuthenticationDtos);
    }
}
