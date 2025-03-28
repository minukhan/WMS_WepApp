package site.autoever.sseservice.sse.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import site.autoever.sseservice.sse.adapter.in.dto.SectionChangeDto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/sse/sections")
@Tag(name = "Section SSE Controller", description = "Section 관련 SSE 이벤트를 처리합니다. JSON RESPONSE : {\"sectionName\":\"B-2\",\"floor\":4,\"isAdd\":true}")
@Slf4j
public class SseSectionsController {

    // 각 클라이언트마다 개별 Sink를 보관할 리스트 (쓰레드 안전)
    private final List<Sinks.Many<ServerSentEvent<SectionChangeDto>>> clients = new CopyOnWriteArrayList<>();

    @Operation(summary = "SSE 구독", description = "클라이언트가 SSE 연결을 수립합니다. 구독 시, 서버로부터 실시간 Section 이벤트를 수신합니다.")
    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<SectionChangeDto>> subscribe() {
        log.info("SSE 구독 요청 받음.");

        // 클라이언트 전용 Sink 생성 (단일 구독자용으로 onBackpressureBuffer 사용)
        Sinks.Many<ServerSentEvent<SectionChangeDto>> clientSink =
                Sinks.many().unicast().onBackpressureBuffer();
        // 새로운 클라이언트 Sink를 리스트에 추가
        clients.add(clientSink);

        // 연결되었음을 알리는 welcome 이벤트
        ServerSentEvent<SectionChangeDto> welcomeEvent = ServerSentEvent.<SectionChangeDto>builder()
                .event("connection")
                .comment("연결되었습니다")
                .build();

        // 클라이언트가 연결 종료될 때 해당 Sink를 제거
        return clientSink.asFlux()
                .startWith(welcomeEvent)
                .doFinally(signalType -> {
                    log.info("클라이언트 연결 종료: {}", signalType);
                    clients.remove(clientSink);
                });
    }

    @Operation(summary = "SSE 이벤트 발행", description = "다른 마이크로서비스가 SectionChangeDto를 전송하면, 해당 데이터를 SSE 이벤트로 발행합니다.")
    @PostMapping
    public void postSection(@RequestBody SectionChangeDto sectionDTO) {
        log.info("SSE 이벤트 발행 요청 받음: {}", sectionDTO);

        ServerSentEvent<SectionChangeDto> event = ServerSentEvent.<SectionChangeDto>builder()
                .event("section-event")
                .data(sectionDTO)
                .build();

        // 모든 연결된 클라이언트 Sink에 이벤트 발행
        clients.forEach(clientSink -> {
            Sinks.EmitResult result = clientSink.tryEmitNext(event);
            if (result.isFailure()) {
                log.error("Emit 실패 (clientSink): {}", result);
            }
        });
    }
}
