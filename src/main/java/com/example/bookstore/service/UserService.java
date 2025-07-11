package com.example.bookstore.service;

import com.example.bookstore.entity.User;
import com.example.bookstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User register(User user) {
            Optional<User> existing = userRepository.findByEmail(user.getEmail());
            if (existing.isPresent()) {
                throw new RuntimeException("Email already registered");
            }
            return userRepository.save(user);
    }

    public Optional<User> getByEmail(String email) {
            return userRepository.findByEmail(email);
    }
}
