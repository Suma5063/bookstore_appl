package com.example.bookstore.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Purchase {

    @Id
    private String id;

    private String userId;
    private String userEmail;
    private String bookId;
    private String bookTitle;
    private double price;
    private int quantityPurchased;
    private LocalDateTime purchaseTime = LocalDateTime.now();
}
