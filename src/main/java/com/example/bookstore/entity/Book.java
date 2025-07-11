package com.example.bookstore.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    private String id;
    private String title;
    private String author;
    private double price;
    private int quantity;
    private LocalDate createdDate = LocalDate.now();
}
