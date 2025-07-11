package com.example.bookstore.service;

import com.example.bookstore.EmailService;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Purchase;
import com.example.bookstore.entity.User;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.PurchaseRepository;
import com.example.bookstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepo;
    private final PurchaseRepository purchaseRepo;
    private final UserRepository userRepo;
    private final BookQueueService bookQueueService;

    @Autowired
    private EmailService emailService;

    public List<Book> getAll() {
        return bookRepo.findAll();
    }

    public Optional<Book> getById(String id) {
        return bookRepo.findById(id);
    }

    public Optional<Book> update(String id, Book book) {
        return bookRepo.findById(id).map(existing -> {
            book.setId(existing.getId());
            return bookRepo.save(book);
        });
    }

    public void delete(String id) {
        bookRepo.deleteById(id);
    }

    public List<Book> getBooksByTitle(String title) {
        return bookRepo.findByTitleIgnoreCase(title);
    }

    public List<Book> getBooksByAuthor(String author) {
        return bookRepo.findByAuthorIgnoreCase(author);
    }

    public List<Book> getBooksByPriceRange(Double min, Double max) {
        return bookRepo.findByPriceBetween(min, max);
    }

    public List<Book> getBooksSortedBy(String field) {
        return bookRepo.findAll(Sort.by(Sort.Direction.ASC, field));
    }

    public Book create(Book book) {
        Book savedBook = bookRepo.save(book);
        emailService.sendBookCreationEmail(savedBook);

        // ✅ Add to processing queue
        bookQueueService.addToQueue(savedBook);

        return savedBook;
    }


    public Purchase purchaseBook(String email, String bookId, int quantity) {
        User user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bookRepo.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.getQuantity() < quantity) {
            throw new RuntimeException("Not enough stock");
        }

        book.setQuantity(book.getQuantity() - quantity);
        bookRepo.save(book);

        Purchase purchase = new Purchase();
        purchase.setUserId(user.getId());
        purchase.setUserEmail(user.getEmail());
        purchase.setBookId(book.getId());
        purchase.setBookTitle(book.getTitle());
        purchase.setPrice(book.getPrice() * quantity);
        purchase.setQuantityPurchased(quantity);
        purchase.setPurchaseTime(LocalDateTime.now());
        purchaseRepo.save(purchase);

        emailService.sendPurchaseConfirmation(user.getEmail(), book, book.getQuantity());
        emailService.sendSimpleMail("admin@gmail.com", "📦 Book Sold",
                book.getTitle() + " x" + quantity + " bought by " + user.getEmail());

        return purchase;
    }

}

