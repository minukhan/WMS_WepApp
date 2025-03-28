package hyundai.supplyservice.app.supply.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import hyundai.supplyservice.app.infrastructure.repository.SupplyRequestRepository;
import hyundai.supplyservice.app.supply.application.port.in.commondto.*;
import hyundai.supplyservice.app.supply.application.port.out.feign.PartController;
import hyundai.supplyservice.app.supply.application.port.out.feign.UserController;
import hyundai.supplyservice.app.supply.application.entity.SupplyRequest;
import hyundai.supplyservice.app.supply.application.port.in.read.ReadSupplyRequestUsecase;
import hyundai.supplyservice.app.supply.application.port.in.read.SupplyRequestDetailResponseDTO;
import hyundai.supplyservice.app.supply.application.port.in.read.SupplyRequestInfoDto;
import hyundai.supplyservice.app.supply.application.port.out.SupplyRequestPort;
import hyundai.supplyservice.app.supply.exception.UsersNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReadSupplyRequestService implements ReadSupplyRequestUsecase {

    private final SupplyRequestPort supplyRequestPort;
    private final UserController userController;  // Feign Client 주입
    private final SupplyRequestRepository supplyRequestRepository;
    private final PartController partController;


    // 주문서 상세정보 조회
    @Override
    public SupplyRequestDetailResponseDTO getRequestDetail(Long requestId){

        SupplyRequest supplyRequest = supplyRequestPort.findById(requestId);

        // feign클라이언트 getUsersByIds 유저 정보 조회
        List<UserInfoDto> userInfos = userController.getUsersByIds(
                new UserInfoRequestDto(List.of(supplyRequest.getUserId()))
        );

        // 유저 정보가 없을 경우 예외 발생
        if (userInfos.isEmpty()) {
            throw new UsersNotFoundException("유저 정보가 없습니다.");
        }
        // 주문 한건의 총 부품 수 계산
        int totalPartQuantity = supplyRequest.getSupplyRequestParts().stream()
                .mapToInt(part -> part.getQuantity())
                .sum();

        // 각 부품별로 part-service에서 가격 정보 조회하고 계산
        List<PartCountPriceDto> partDetails = supplyRequest.getSupplyRequestParts().stream()
                .map(part -> {
                    // Feign Client로 부품 정보 조회
                    JsonNode partInfo = partController.getPartInfo(part.getPartId());

                    // price 추출 (partSupplierDto 내의 price 필드)
                    long unitPrice = partInfo.get("partSupplierDto").get("price").asLong();

                    // 수량과 단가를 곱하여 총 가격 계산
                    long totalPrice = unitPrice * part.getQuantity();

                    return new PartCountPriceDto(
                            part.getPartId(),
                            part.getName(),
                            totalPrice,  // 계산된 총 가격
                            part.getQuantity()
                    );
                })
                .collect(Collectors.toList());

        // 전체 부품의 총 금액 계산
        long totalAmount = partDetails.stream()
                .mapToLong(PartCountPriceDto::price)
                .sum();


        return new SupplyRequestDetailResponseDTO(
                userInfos,  // UserInfoDto 전달
                supplyRequest.getId(),
                supplyRequest.getStatus(),
                supplyRequest.getMessage(),
                supplyRequest.getDeadline(),
                supplyRequest.getRequestedAt(),
                partDetails,
                totalPartQuantity,
                totalAmount

        );

    }

    // 주문서 목록 조회, 이름 검색
    public PageableResponse<SupplyRequestInfoDto> getSupplyRequestList(String status, String name, Pageable pageable){
        Page<SupplyRequest> requests;
        Map<Long, String> userIdToNameMap = new HashMap<>();

        if (name != null && !name.isEmpty()) {
            // user-service에서 이름으로 userId검색
            JsonNode userResponse = userController.findUserIdsByName(name);
            List<Long> userIds = new ArrayList<>();

            if (userResponse.isArray()) {
                for (JsonNode user : userResponse) {
                    userIds.add(user.get("userId").asLong());
                }
            }

            if ("ALL".equals(status)) {
                requests = supplyRequestRepository.findByUserIdIn(userIds, pageable);
            } else {
                requests = supplyRequestRepository.findByUserIdInAndStatus(userIds, status, pageable);
            }

        }
        else{
            if ("ALL".equals(status)) {
                requests = supplyRequestRepository.findAll(pageable);
            } else {
                requests = supplyRequestRepository.findByStatus(status, pageable);
            }
        }

        // 모든 요청서에서 userId 추출
        List<Long> requestUserIds = requests.getContent().stream()
                .map(SupplyRequest::getUserId)
                .collect(Collectors.toList());

        // user-service에서 이름 가져와
        if (!requestUserIds.isEmpty()) {
            UserInfoRequestDto requestDto = new UserInfoRequestDto(requestUserIds);
            JsonNode userResponse = userController.getUserNameById(requestDto);
            if (userResponse.isArray()) {
                for (JsonNode user : userResponse) {
                    userIdToNameMap.put(user.get("userId").asLong(), user.get("name").asText());
                }
            }
        }

        Page<SupplyRequestInfoDto> dtoPage = requests.map(request -> new SupplyRequestInfoDto(
                request.getId(),
                userIdToNameMap.getOrDefault(request.getUserId(),"unknown"),
                request.getRequestedAt(),
                request.getDeadline(),
                request.getStatus()
        ));

        return PageableResponse.from(dtoPage);


    }




}
