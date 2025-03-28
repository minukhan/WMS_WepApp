package hyundai.partservice.app.part.application.service;

import hyundai.partservice.app.part.adapter.dto.PartDto;
import hyundai.partservice.app.part.adapter.dto.PartListPaginationResponse;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.part.application.port.in.GetAllPartUseCase;
import hyundai.partservice.app.part.application.port.out.GetAllPartPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class GetAllPartService implements GetAllPartUseCase {

    private final GetAllPartPort getAllPartPort;

    @Override
    public PartListPaginationResponse getAllParts(Pageable pageable) {

        Page<Part> parts = getAllPartPort.getAllParts(pageable);

        // 리스트 DTO 생성
        Page<PartDto> partDtos = parts.map((part -> PartDto.from(part)));

        // Response 생성
        return PartListPaginationResponse.from(partDtos);
    }
}
