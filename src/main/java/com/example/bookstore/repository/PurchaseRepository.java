package com.example.bookstore.repository;

import com.example.bookstore.entity.Purchase;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PurchaseRepository extends MongoRepository<Purchase,String> {
}
