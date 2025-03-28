package hyundai.partservice.app.part.application.service;


import hyundai.partservice.app.part.adapter.dto.OnlyPartNamePartIdDto;
import hyundai.partservice.app.part.adapter.dto.OnlyPartNamePartIdListResponse;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.part.application.port.in.OnlyNameAndPartIdAllUseCase;
import hyundai.partservice.app.part.application.port.out.OnlyNameAndPartIdAllPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OnlyNameAndPartIdAllService implements OnlyNameAndPartIdAllUseCase {

    private final OnlyNameAndPartIdAllPort onlyNameAndPartIdAllPort;

    @Override
    public OnlyPartNamePartIdListResponse onlyNameAndPartIdAllUseCase() {
        List<Part> parts = onlyNameAndPartIdAllPort.getAllParts();

        List<OnlyPartNamePartIdDto> onlyPartNamePartIdDtos = parts.stream()
                .map((part -> OnlyPartNamePartIdDto.from(part)))
                .collect(Collectors.toList());

        return OnlyPartNamePartIdListResponse.from(onlyPartNamePartIdDtos);
    }
}
