package hyundai.supplyservice.app.supply.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import hyundai.supplyservice.app.infrastructure.util.UserIdResolver;
import hyundai.supplyservice.app.supply.application.port.in.commondto.AlarmCreateRequestDto;
import hyundai.supplyservice.app.supply.application.port.in.verify.LogCreateRequestDto;
import hyundai.supplyservice.app.supply.application.port.out.feign.AlarmController;
import hyundai.supplyservice.app.supply.application.port.out.feign.PartController;
import hyundai.supplyservice.app.supply.application.entity.SupplyRequest;
import hyundai.supplyservice.app.supply.application.entity.SupplyRequestPart;
import hyundai.supplyservice.app.supply.application.port.in.create.CreateSupplyRequestDto;
import hyundai.supplyservice.app.supply.application.port.in.create.CreateSupplyRequestResponseDto;
import hyundai.supplyservice.app.supply.application.port.in.create.CreateSupplyRequestUsecase;
import hyundai.supplyservice.app.supply.application.port.in.commondto.PartInfoDto;
import hyundai.supplyservice.app.supply.application.port.out.SupplyRequestPartPort;
import hyundai.supplyservice.app.supply.application.port.out.SupplyRequestPort;
import hyundai.supplyservice.app.supply.exception.AtLeastOnePartRequiredException;
import hyundai.supplyservice.app.supply.exception.InvalidPartRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CreateSupplyRequestService implements CreateSupplyRequestUsecase {


    private final SupplyRequestPort supplyRequestPort;
    private final SupplyRequestPartPort supplyRequestPartPort;
    private final PartController partController;
    private final UserIdResolver userIdResolver;
    private final AlarmController alarmController;

    @Transactional
    public CreateSupplyRequestResponseDto createSupplyRequest (CreateSupplyRequestDto command) {

        //주문하는 부품이 없을때 예외처리
        if (command.getParts()==null || command.getParts().isEmpty()) {
            throw new AtLeastOnePartRequiredException();
        }
        command.getParts().forEach(part -> {
            if (part.partId() == null || part.partId().trim().isEmpty()) {
                throw new InvalidPartRequestException("부품 ID는 필수 입력 항목입니다.");
            }
            if (part.quantity() <= 0) {
                throw new InvalidPartRequestException("수량은 1개 이상이어야 합니다.");
            }
        });

        // 현재 주문하는 유저
        Long userId = userIdResolver.getCurrentUserId();

        // 주문서 저장
        SupplyRequest supplyRequest = SupplyRequest.builder()
                .userId(userId)
                .deadline(command.getDeadline())
                .requestedAt(LocalDate.now())
                .status("WAITING")
                .message("waiting..")
                .build();

        // 주문서에 대한 부품들 저장
        List<SupplyRequestPart> parts = command.getParts().stream()
                .map(partCommand -> {
                    // 부품 정보 조회 및 부품 ID로 부품 이름 찾아와서 저장
                    JsonNode response = partController.getPartInfo(partCommand.partId());
                    String partName = response.get("partSupplierDto").get("partName").asText();


                    return SupplyRequestPart.builder()
                            .partId(String.valueOf(partCommand.partId()))
                            .name(partName)
                            .quantity(partCommand.quantity())
                            .supplyRequest(supplyRequest)
                            .build();
                })
                .collect(Collectors.toList());


        // 주문서 저장
        SupplyRequest savedSupplyRequest = supplyRequestPort.save(supplyRequest);

        // 부품들 저장
        List<SupplyRequestPart> savedParts = supplyRequestPartPort.saveAll(parts);

        // 알람 생성

        ResponseEntity<Void> response = alarmController.createAlarm(new AlarmCreateRequestDto("ROLE_INFRA_MANAGER", "새로운 주문서 요청이 들어왔습니다.", "SUPPLY"));


        //return
        return new CreateSupplyRequestResponseDto(savedSupplyRequest.getId(), "주문이 접수됨", savedSupplyRequest.getStatus());

    }

    @Override
    public List<PartInfoDto> getAllPartsInfo() {

        // size=450으로 모든 부품 정보를 한 번에 요청
        JsonNode response = partController.getPartInfo(450);
        JsonNode content = response.get("content");


        List<PartInfoDto> partInfoList = new ArrayList<>();
        // content에서 필요한 정보(id, name)만 추출하여 PartInfoDto로 변환
        content.forEach(part ->
                partInfoList.add(new PartInfoDto(
                        part.get("id").asText(),
                        part.get("name").asText()
                ))
        );


        return partInfoList;
    }
}
