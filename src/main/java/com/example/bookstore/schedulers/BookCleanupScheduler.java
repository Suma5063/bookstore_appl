package com.example.bookstore.schedulers;

import com.example.bookstore.entity.Book;
import com.example.bookstore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class BookCleanupScheduler {

    @Autowired
    private BookRepository bookRepository;

    // Run daily at 2 AM
    @Scheduled(cron = "0 0 2 * * *")
    public void deleteOldBooks() {
        LocalDate cutoff = LocalDate.now().minusDays(30);
        List<Book> oldBooks = bookRepository.findByCreatedDateBefore(cutoff);

        oldBooks.forEach(book -> {
            System.out.println("🗑️ Deleting old book: " + book.getTitle());
            bookRepository.delete(book);
        });
    }
}
