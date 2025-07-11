package com.example.bookstore.controller;

import com.example.bookstore.EmailService;
import com.example.bookstore.entity.Book;
import com.example.bookstore.service.BookQueueService;
import com.example.bookstore.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @Autowired
    private BookQueueService bookQueueService;

    @Autowired private EmailService emailService;

    @PostMapping
    public Book create(@RequestBody Book book) {
        return bookService.create(book);
    }

    @GetMapping
    public List<Book> getAll() {
        return bookService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getById(@PathVariable String id) {
        return bookService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> update(@PathVariable String id, @RequestBody Book book) {
        return bookService.update(id, book)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/")
    public String home() {
        return "📚 BookStore API is running! Use /books for endpoints.";
    }

    @GetMapping("/books/title/{title}")
    public ResponseEntity<List<Book>> getBooksByTitle(@PathVariable String title) {
        return ResponseEntity.ok(bookService.getBooksByTitle(title));
    }

    @GetMapping("/books/author/{author}")
    public ResponseEntity<List<Book>> getBooksByAuthor(@PathVariable String author) {
        return ResponseEntity.ok(bookService.getBooksByAuthor(author));
    }

    @GetMapping("/books/price")
    public ResponseEntity<List<Book>> getBooksByPriceRange(
            @RequestParam Double min,
            @RequestParam Double max) {
        return ResponseEntity.ok(bookService.getBooksByPriceRange(min, max));
    }

    @GetMapping("/books")
    public ResponseEntity<List<Book>> getBooksSorted(@RequestParam(required = false) String sort) {
        if (sort != null && (sort.equals("title") || sort.equals("author"))) {
            return ResponseEntity.ok(bookService.getBooksSortedBy(sort));
        }
        return ResponseEntity.ok(bookService.getAll());
    }

}
