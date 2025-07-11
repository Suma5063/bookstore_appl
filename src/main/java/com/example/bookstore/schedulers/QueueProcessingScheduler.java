package com.example.bookstore.schedulers;

import com.example.bookstore.service.BookQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QueueProcessingScheduler {

    private final BookQueueService bookQueueService;

    // Runs every hour
    @Scheduled(cron = "0 0 * * * *")
    public void autoProcessQueue() {
        System.out.println("⏰ Running scheduled queue processing...");
        bookQueueService.processQueue();
    }
}