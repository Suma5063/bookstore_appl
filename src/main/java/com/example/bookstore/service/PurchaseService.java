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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepo;
    private final BookRepository bookRepo;
    private final UserRepository userRepo;
    private final EmailService emailService;

    public Purchase purchaseBook(String email, String bookId, int quantity) {
        userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        User user = new User();
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
        emailService.sendPurchaseConfirmation(email, book, quantity, remainingQuantity); // to buyer
        emailService.sendPurchaseConfirmation("suma@mailinator.com", book, quantity, remainingQuantity); // to admin
        emailService.sendSimpleMail(
                "suma@mailinator.com",
                "📦 Book Sold",
                book.getTitle() + " x" + quantity + " bought by " + user.getEmail()
        );
        return purchase;
    }

}