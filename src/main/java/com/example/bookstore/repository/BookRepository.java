package com.example.bookstore.repository;

import com.example.bookstore.entity.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookRepository extends MongoRepository<Book, String> {

    List<Book> findByTitleIgnoreCase(String title);

    List<Book> findByAuthorIgnoreCase(String author);

    List<Book> findByPriceBetween(Double min, Double max);

    List<Book> findByCreatedDateBefore(LocalDate date);

}
