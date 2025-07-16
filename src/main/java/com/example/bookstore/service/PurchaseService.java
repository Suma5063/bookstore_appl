package com.example.bookstore.service;

import com.example.bookstore.EmailService;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Purchase;
import com.example.bookstore.entity.User;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.PurchaseRepository;
import com.example.bookstore.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepo;
    private final BookRepository bookRepo;
    private final UserRepository userRepo;
    private final EmailService emailService;

    @Autowired
    private BookService bookService;

    public Purchase purchaseBook(String email, String bookId, int quantity) {
        userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        user.setEmail(user.getEmail());
        user.setName(user.getName());
        user.setRole(user.getRole());
        user.setPassword(user.getPassword());


        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        int remainingQuantity = book.getQuantity();
        // 💡 Validate stock
        if (book.getQuantity() < quantity) {
            throw new RuntimeException("Not enough stock! Only " + book.getQuantity() + " left.");
        }

        // 📉 Decrement stock
        book.setQuantity(book.getQuantity() - quantity);
        bookRepo.save(book);

        // 💾 Create Purchase
        Purchase purchase = new Purchase();
        purchase.setUserId(user.getId());
        purchase.setUserEmail(email);
        purchase.setBookId(book.getId());
        purchase.setBookTitle(book.getTitle());
        purchase.setPrice(book.getPrice() * quantity); // total price
        purchase.setQuantityPurchased(quantity); // 👈 new field
        purchase.setPurchaseTime(LocalDateTime.now());

        purchaseRepo.save(purchase);

        // 📧 Send confirmation email
        emailService.sendPurchaseConfirmation(email, purchase, remainingQuantity);
        emailService.sendPurchaseConfirmation("suma@mailinator.com", purchase, remainingQuantity);
        emailService.sendSimpleMail(
                "suma@mailinator.com",
                "📦 Book Sold",
                book.getTitle() + " x" + quantity + " bought by " + user.getEmail()
        );
        return purchase;
    }

    public ResponseEntity<?> getAllPurchases() {
        List<Purchase> purchases = purchaseRepo.findAll();
        if (purchases.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No purchases found.");
        }
        return ResponseEntity.ok(purchases);
    }

    public ResponseEntity<?> getPurchasesByEmail(String email) {
        List<Purchase> purchases = purchaseRepo.findByEmail(email);
        if (purchases.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No purchases found for email: " + email);
        }
        return ResponseEntity.ok(purchases);
    }

    public ResponseEntity<Purchase> getPurchaseById(String id) {
        return purchaseRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<?> deletePurchaseById(String id) {
        if (purchaseRepo.existsById(id)) {
            purchaseRepo.deleteById(id);
            return ResponseEntity.ok("Purchase deleted successfully.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Purchase not found.");
    }

    public ResponseEntity<?> getPurchasesBetweenDates(String from, String to) {
        try {
            LocalDate startDate = LocalDate.parse(from);
            LocalDate endDate = LocalDate.parse(to);
            List<Purchase> purchases = purchaseRepo.findByPurchaseDateBetween(startDate, endDate);

            if (purchases.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No purchases in this date range.");
            }
            return ResponseEntity.ok(purchases);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid date format. Use YYYY-MM-DD.");
        }
    }

    public ResponseEntity<?> getTopSoldBooks(int limit) {
        List<Purchase> all = purchaseRepo.findAll();

        Map<String, Integer> salesMap = all.stream()
                .collect(Collectors.groupingBy(Purchase::getBookId,
                        Collectors.summingInt(Purchase::getQuantityPurchased)));

        List<Map<String, Object>> topBooks = salesMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(limit)
                .map(entry -> {
                    Map<String, Object> bookData = new HashMap<>();
                    bookData.put("bookId", entry.getKey());
                    bookData.put("title", bookService.getTitleById(entry.getKey()));
                    bookData.put("totalSold", entry.getValue());
                    return bookData;
                })
                .toList();

        return ResponseEntity.ok(topBooks);
    }
    public Purchase getPurchaseOrThrow(String id) {
        return purchaseRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Purchase not found with id: " + id));
    }

}