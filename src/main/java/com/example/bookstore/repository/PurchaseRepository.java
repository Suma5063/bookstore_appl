package com.example.bookstore.repository;

import com.example.bookstore.entity.Purchase;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface PurchaseRepository extends MongoRepository<Purchase,String> {

    List<Purchase> findByEmail(String email);
    List<Purchase> findByPurchaseDateBetween(LocalDate from, LocalDate to);

}
