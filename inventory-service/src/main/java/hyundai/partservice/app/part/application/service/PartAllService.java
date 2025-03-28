package hyundai.partservice.app.part.application.service;

import hyundai.partservice.app.part.adapter.dto.PartDto;
import hyundai.partservice.app.part.adapter.dto.PartListResponse;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.part.application.port.in.PartAllUseCase;
import hyundai.partservice.app.part.application.port.out.PartAllPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartAllService implements PartAllUseCase {

    private final PartAllPort partAllPort;

    @Override
    public PartListResponse getAllParts() {

        List<Part> parts = partAllPort.getAllParts();

        List<PartDto> partDtos = parts.stream().map(part -> PartDto.from(part)).collect(Collectors.toList());

        return PartListResponse.from(partDtos);
    }
}
