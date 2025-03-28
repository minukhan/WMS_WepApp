package site.autoever.logservice.infrastructure.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import site.autoever.logservice.user_log.application.entity.UserLog;
import site.autoever.logservice.user_log.application.port.out.SaveLogPort;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Component
public class AsyncLogProcessor {

    private final SaveLogPort saveLogPort;

    // 배치 사이즈와 flush 타임아웃은 static 상수로 선언
    private static final int BULK_SIZE = 100;
    private static final long FLUSH_TIMEOUT = 500L;

    // 내부 큐: 단순 LinkedList 사용
    private final LinkedList<UserLog> logQueue = new LinkedList<>();

    // 로그 적재와 소비를 위한 락과 조건 변수
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    // 단일 consumer 스레드
    private final ExecutorService consumerExecutor = Executors.newSingleThreadExecutor();
    // 종료 여부를 나타내는 flag
    private volatile boolean running = true;

    public AsyncLogProcessor(SaveLogPort saveLogPort) {
        this.saveLogPort = saveLogPort;
        startConsumer();
    }

    /**
     * 서비스에서 호출되어 로그를 큐에 적재합니다.
     */
    public void enqueueLog(UserLog userLog) {
        lock.lock();
        try {
            logQueue.add(userLog);
            // 큐의 크기가 BULK_SIZE에 도달하면 소비자에게 알림
            if (logQueue.size() >= BULK_SIZE) {
                condition.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 소비자 스레드를 시작합니다.
     * 큐가 비어있으면 FLUSH_TIMEOUT 동안 대기하며, BULK_SIZE 이상의 로그가 모이면 바로 배치 처리합니다.
     */
    private void startConsumer() {
        consumerExecutor.submit(() -> {
            while (running || !logQueue.isEmpty()) {
                List<UserLog> batch = new ArrayList<>();
                lock.lock();
                try {
                    // 큐가 비어있으면 FLUSH_TIMEOUT 동안 대기
                    while (logQueue.isEmpty() && running) {
                        try {
                            condition.await(FLUSH_TIMEOUT, TimeUnit.MILLISECONDS);
                            break; // 타임아웃 후에도 진행
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }
                    // BULK_SIZE 만큼 또는 큐가 빌 때까지 로그 추출
                    int count = 0;
                    while (count < BULK_SIZE && !logQueue.isEmpty()) {
                        batch.add(logQueue.poll());
                        count++;
                    }
                } finally {
                    lock.unlock();
                }
                // 배치 처리
                if (!batch.isEmpty()) {
                    try {
                        saveLogPort.saveAllLogs(batch);
                        log.info("배치 로그 저장 완료: {} 개", batch.size());
                    } catch (Exception e) {
                        log.error("로그 저장 중 오류 발생", e);
                    }
                }
            }
        });
    }

    /**
     * 서비스 종료 시, 남은 로그도 모두 저장합니다.
     */
    public void shutdown() {
        running = false;
        lock.lock();
        try {
            condition.signalAll();
        } finally {
            lock.unlock();
        }
        consumerExecutor.shutdown();
        try {
            if (!consumerExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                consumerExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
