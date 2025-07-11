package com.example.bookstore.triggers;

import com.example.bookstore.service.BookQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    @Autowired
    private BookQueueService bookQueueService;

    // Every hour
    @Scheduled(cron = "0 0 * * * *")
    public void processQueueAutomatically() {
        System.out.println("⏱️ Scheduled queue processing started...");
        bookQueueService.processQueue();
    }
}
