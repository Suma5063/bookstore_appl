package com.example.bookstore.controller;

import com.example.bookstore.entity.Purchase;
import com.example.bookstore.service.BookService;
import com.example.bookstore.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final BookService bookService;
    private final PurchaseService purchaseService;
    
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
}