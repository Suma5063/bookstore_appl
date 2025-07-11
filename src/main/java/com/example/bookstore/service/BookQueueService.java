package com.example.bookstore.service;

import com.example.bookstore.entity.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
@Slf4j
public class BookQueueService {

    private final Queue<Book> queue = new ConcurrentLinkedQueue<>();

    public void addToQueue(Book book) {
        log.info("✅ Queued book: {}", book.getTitle());
        queue.add(book);
    }

    public void processQueue() {
        while (!queue.isEmpty()) {
            Book book = queue.poll();
            log.info("📚 Processing book from queue: {}", book.getTitle());

            // Simulate notification or indexing
            System.out.println("📨 Notifying admin: New book added - " + book.getTitle());
        }
    }
}
