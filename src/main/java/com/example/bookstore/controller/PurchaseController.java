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

@RestController
@RequestMapping("/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    @Autowired
    private  BookService bookService;

    @Autowired
    private  PurchaseService purchaseService;

    @Autowired
    private PurchaseRepository purchaseRepository;

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
        return purchaseService.getAllPurchases();
    }

    @GetMapping("/user")
    public ResponseEntity<?> getPurchasesByEmail(@RequestParam String email) {
        return purchaseService.getPurchasesByEmail(email);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePurchase(@PathVariable String id) {
        return purchaseService.deletePurchaseById(id);
    }
    @GetMapping("/between")
    public ResponseEntity<?> getPurchasesBetweenDates(
            @RequestParam String from,
            @RequestParam String to
    ) {        return purchaseService.getPurchasesBetweenDates(from, to);
    }

    @GetMapping("/top-sold")
    public ResponseEntity<?> getTopSoldBooks(@RequestParam(defaultValue = "5") int limit) {
        return purchaseService.getTopSoldBooks(limit);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Purchase> getPurchaseById(@PathVariable String id) {
        return ResponseEntity.ok(purchaseService.getPurchaseOrThrow(id));
    }

}