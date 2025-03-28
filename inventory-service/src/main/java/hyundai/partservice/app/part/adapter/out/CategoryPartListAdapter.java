package hyundai.partservice.app.part.adapter.out;


import hyundai.partservice.app.infrastructure.repository.PartRepository;
import hyundai.partservice.app.part.application.entity.Part;
import hyundai.partservice.app.part.application.port.out.CategoryPartListPort;
import hyundai.partservice.app.part.exception.CategoryPartNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryPartListAdapter implements CategoryPartListPort {

    private final PartRepository partRepository;

    @Override
    public Page<Part> getCategoryPartList(String category, Pageable pageable){
        Page<Part> parts = partRepository.findByCategory(category, pageable).orElseThrow(()->new CategoryPartNotFoundException());
        return parts;
    }
}
