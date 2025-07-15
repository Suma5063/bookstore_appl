package com.example.bookstore.controller;

import com.example.bookstore.entity.Purchase;
import com.example.bookstore.repository.PurchaseRepository;
import com.example.bookstore.service.BookService;
import com.example.bookstore.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final BookService bookService;
    private final PurchaseService purchaseService;

    @Autowired
    private final PurchaseRepository purchaseRepository;

    @PostMapping
    public ResponseEntity<?> purchaseBook(
            @RequestParam String email,
            @RequestParam String bookId,
            @RequestParam(defaultValue = "1") int quantity
    ) {
        try {
            Purchase purchase = bookService.purchaseBook(email, bookId, quantity);
            return ResponseEntity.ok(purchase);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ " + e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllPurchases() {
        try {
            List<Purchase> purchases = purchaseRepository.findAll();

            if (purchases.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No purchases found.");
            }

            return ResponseEntity.ok(purchases);

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while fetching purchases: " + ex.getMessage());
        }
    }

}