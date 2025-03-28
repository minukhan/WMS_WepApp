package hyundai.supplyservice.app.supply.application.service;

import feign.FeignException;
import hyundai.supplyservice.app.infrastructure.repository.SupplyRequestRepository;
import hyundai.supplyservice.app.infrastructure.util.UserIdResolver;
import hyundai.supplyservice.app.supply.application.entity.*;
import hyundai.supplyservice.app.supply.application.port.in.verify.*;
import hyundai.supplyservice.app.supply.application.port.out.SupplyPartSchedulePort;
import hyundai.supplyservice.app.supply.application.port.out.SupplySchedulePort;
import hyundai.supplyservice.app.supply.application.port.out.feign.AlarmController;
import hyundai.supplyservice.app.supply.application.port.out.feign.LogController;
import hyundai.supplyservice.app.supply.application.port.out.feign.VerifyController;
import hyundai.supplyservice.app.supply.application.port.in.commondto.*;
import hyundai.supplyservice.app.supply.application.port.out.SupplyRequestPort;
import hyundai.supplyservice.app.supply.exception.InvalidRequestException;
import hyundai.supplyservice.app.supply.exception.ValidationFailedException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class VerifyService {

    private final SupplyRequestRepository supplyRequestRepository;
    private final SupplyRequestPort supplyRequestPort;
    private final SupplySchedulePort supplySchedulePort;
    private final VerifyController verifyController;
    private final LogController logController;
    private final AlarmController alarmController;
    private final SupplyPartSchedulePort supplyPartSchedulePort ;
    private final UserIdResolver userIdResolver;

    //검증하기 -> 결과 반환
    public VerifyResultResponseDto verify(Long requestId) {
        SupplyRequest supplyRequest = supplyRequestPort.findById(requestId);

        if (!supplyRequest.getStatus().equals("WAITING")) {
            throw new InvalidRequestException("대기 상태인 요청만 검증이 가능합니다.");
        }

        int totalQuantity = supplyRequest.getSupplyRequestParts().stream()
                .mapToInt(part -> part.getQuantity())
                .sum();

        VerifyOrderInfoRequestDto order = new VerifyOrderInfoRequestDto(
                supplyRequest.getId(),
                supplyRequest.getRequestedAt(),
                supplyRequest.getDeadline(),
                totalQuantity,
                supplyRequest.getSupplyRequestParts().stream()
                        .map(part -> new PartCountDto(
                                part.getPartId(),
                                part.getQuantity()
                        ))
                        .collect(Collectors.toList())

        );


        try {
            // Feign 클라이언트를 통해 검증 요청
            VerifyResultResponseDto response = verifyController.verifyOrder(order);
            return response;
        } catch(FeignException e) {

            throw new ValidationFailedException("검증 결과를 받아오지 못했습니다.");
        }

    }


    // 승인하기
    public StatusChangeResponseDto approveRequest(Long requestId) {
        SupplyRequest supplyRequest = supplyRequestPort.findById(requestId);


        if (!supplyRequest.getStatus().equals("WAITING")) {
            throw new InvalidRequestException("대기 상태인 요청만 승인/반려가 가능합니다.");
        }

        // 검증 결과 확인
        VerifyResultResponseDto verificationResult = verify(requestId);
        if (!verificationResult.status()) {
            // false라서 승인 불가.
            throw new InvalidRequestException("검증 결과가 실패인 요청은 승인할 수 없습니다.");
        }

        SupplyRequest updatedRequest = supplyRequestPort.updateStatus(requestId, "APPROVED", verificationResult.message());

        // 현재 승인하는 유저
        Long userId = userIdResolver.getCurrentUserId();

        // 로그 생성
        String message = String.format("%d번 주문서 승인", requestId);
        LogCreateRequestDto requestDto = new LogCreateRequestDto(userId, message);

        ResponseEntity<Void> response =logController.createLog(requestDto);

        //알람 생성
        String alarmMessage = String.format("%d번 주문서가 승인되었습니다.", requestId);
        alarmController.createAlarm(new AlarmCreateRequestDto("ROLE_INFRA_MANAGER", alarmMessage, "SUPPLY"));

        // SupplySchedule 생성 및 저장
        SupplySchedule supplySchedule = SupplySchedule.builder()
                .status("APPROVED")
                .scheduledAt(supplyRequest.getDeadline())
                .supplyRequest(supplyRequest)
                .build();

        supplySchedulePort.save(supplySchedule);

        // SupplyPartSchedule 데이터 생성 또는 업데이트
        for (SupplyRequestPart requestPart : supplyRequest.getSupplyRequestParts()) {
            SupplyPartScheduleId scheduleId = new SupplyPartScheduleId(
                    supplyRequest.getDeadline(),
                    requestPart.getPartId()
            );

            SupplyPartSchedule partSchedule = supplyPartSchedulePort
                    .findById(scheduleId)
                    .map(existing -> {
                        existing.addQuantity(requestPart.getQuantity());
                        return existing;
                    })
                    .orElseGet(() -> new SupplyPartSchedule(scheduleId, requestPart.getName(), requestPart.getQuantity()));

            supplyPartSchedulePort.save(partSchedule);  // 저장 로직 추가

        }

        return new StatusChangeResponseDto(requestId, "APPROVED", verificationResult.message());

    }

    // 반려하기
    public StatusChangeResponseDto rejectRequest(Long requestId) {
        SupplyRequest supplyRequest = supplyRequestPort.findById(requestId);

        if (!supplyRequest.getStatus().equals("WAITING")) {
            throw new InvalidRequestException("대기 상태인 요청만 승인/반려가 가능합니다.");
        }

        // 검증 결과 확인
        VerifyResultResponseDto verificationResult = verify(requestId);

        String rejectionMessage = determineRejectionMessage(verificationResult);
        SupplyRequest updatedRequest = supplyRequestPort.updateStatus(requestId, "REJECTED", rejectionMessage);

        // 현재 유저
        Long userId = userIdResolver.getCurrentUserId();

        // 로그 생성
        String message = String.format("%d번 주문서 반려", requestId);
        LogCreateRequestDto requestDto = new LogCreateRequestDto(userId, message);
        ResponseEntity<Void> response =logController.createLog(requestDto);

        //알람 생성
        String alarmMessage = String.format("%d번 주문서가 반려되었습니다.", requestId);
        alarmController.createAlarm(new AlarmCreateRequestDto("ROLE_INFRA_MANAGER", alarmMessage, "SUPPLY"));


        return new StatusChangeResponseDto(
                requestId,
                updatedRequest.getStatus(),
                rejectionMessage
        );
    }


    private String determineRejectionMessage(VerifyResultResponseDto verificationResult){
        return verificationResult.status()
                ? "관리자 판단에 의한 반려"
                : verificationResult.message();
    }



    // 출고 부품수 계산
    public PartCountResponseDto getPartCount(ValidationPartCountRequestDto requestDto) {
        LocalDate deadlineDate = requestDto.dueDate();
        LocalDate startDate = LocalDate.now();
        List<PartCountDto> partCounts = new ArrayList<>();
        long totalCount = 0;

        // 각각의 부품에 대한 수 카운트
        for (String partId : requestDto.partIds()) {
            Long result = supplyRequestRepository.findSupplyRequestsByDeadline(
                    partId,
                    startDate,
                    deadlineDate
            );

            int partQuantity = (result != null) ? Math.toIntExact(result) : 0;

            partCounts.add(new PartCountDto(
                    partId,
                    partQuantity
            ));

            totalCount += partQuantity;
        }

        return new PartCountResponseDto(
                totalCount,
                partCounts
        );
    }

    public Long getTotalSupplyPartQuantity(LocalDate date) {

        LocalDate startDate = LocalDate.now();

        Long total = supplyPartSchedulePort.sumTotalRequestedQuantityBetweenDates(startDate, date);
        return total != null ? total : 0;
    }

    public List<PartCountDto> getReleasedPartsCountForDate(ValidationPartCountRequestDto requestDto) {
        return requestDto.partIds().stream()
                .map(partId -> {
                    SupplyPartScheduleId scheduleId = new SupplyPartScheduleId(requestDto.dueDate(), partId);

                    Optional<SupplyPartSchedule> schedule = supplyPartSchedulePort.findById(scheduleId);

                    int quantity = schedule
                            .map(SupplyPartSchedule::getTotalRequestedQuantity)
                            .orElse(0);

                    return new PartCountDto(partId, quantity);
                })
                .collect(Collectors.toList());

    }
}
