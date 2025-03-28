package hyundai.partservice.app.part.application.service;


import hyundai.partservice.app.part.adapter.dto.PartPurchaseResponse;
import hyundai.partservice.app.part.adapter.dto.PartSupplierDto;
import hyundai.partservice.app.part.application.port.in.FindPartUseCase;
import hyundai.partservice.app.part.application.port.out.FindPartPort;
import hyundai.partservice.app.part.application.entity.Part;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindPartService implements  FindPartUseCase {

    private final FindPartPort findPartPort;

    @Override
    public PartPurchaseResponse findPart(String id){

        Part part = findPartPort.findPart(id);

        //dto 로 변환
        PartSupplierDto partSupplierDto =  PartSupplierDto.from(part);

        //response로 변환 후 리턴
        return PartPurchaseResponse.from(partSupplierDto);
    }

}
