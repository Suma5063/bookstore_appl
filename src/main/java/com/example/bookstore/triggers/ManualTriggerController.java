package com.example.bookstore.triggers;

import com.example.bookstore.service.BookQueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class ManualTriggerController {

    @Autowired
    private BookQueueService bookQueueService;

    @PostMapping("/trigger-queue")
    public String triggerQueueProcessing() {
        bookQueueService.processQueue();
        return "✅ Queue processing triggered manually!";
    }
}

