package hyundai.partservice.app.part.application.service;

import hyundai.partservice.app.part.adapter.dto.PartDto;
import hyundai.partservice.app.part.adapter.dto.PartListPaginationResponse;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.part.application.port.in.CategoryPartListUseCase;
import hyundai.partservice.app.part.application.port.out.CategoryPartListPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryPartListService implements CategoryPartListUseCase {

    private final CategoryPartListPort categoryPartListPort;

    @Override
    public PartListPaginationResponse getPartList(String category, Pageable pageable){

        Page<Part> parts = categoryPartListPort.getCategoryPartList(category, pageable);
        //DTO로 반환

        Page<PartDto> partDtos = parts.map(part -> PartDto.from(part));

        return PartListPaginationResponse.from(partDtos);
    }
}
