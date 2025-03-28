package hyundai.partservice.app.part.adapter.out;

import hyundai.partservice.app.infrastructure.repository.PartRepository;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.part.application.port.out.FindByIdListPort;
import hyundai.partservice.app.part.exception.PartNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindByIdListAdapter implements FindByIdListPort {

    private final PartRepository partRepository;

    @Override
    public List<Part> findByIdList(List<String> idList) {
        List<Part> parts = partRepository.findByIdIn(idList);
        // 예외 처리: 결과가 비어있는 경우 예외 발생
        if (parts.isEmpty()) {
            throw new PartNotFoundException();
        }

        return parts;
    }
}
