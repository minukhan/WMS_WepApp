package hyundai.partservice.app.part.application.service;


import hyundai.partservice.app.part.adapter.dto.PartDto;
import hyundai.partservice.app.part.adapter.dto.PartResponse;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.part.application.port.in.FindByNamePartUseCase;
import hyundai.partservice.app.part.application.port.out.FindByNamePartPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindByNamePartService implements FindByNamePartUseCase {

    private final FindByNamePartPort findByNamePartPort;

    @Override
    public PartResponse findByNamePart(String partName) {

        Part part = findByNamePartPort.findByNamePart(partName);
        PartDto partDto = PartDto.from(part);

        return PartResponse.from(partDto);
    }
}
