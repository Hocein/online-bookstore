package com.bnp.bookstore.config;

import com.bnp.bookstore.entity.Book;
import com.bnp.bookstore.entity.User;
import com.bnp.bookstore.repository.BookRepository;
import com.bnp.bookstore.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@Configuration
public class DataInitializer {

    private final PasswordEncoder passwordEncoder;

    // Inject PasswordEncoder through constructor
    public DataInitializer(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public ApplicationRunner loadData(BookRepository bookRepository, UserRepository userRepository) {
        return args -> {
            // Initialize books if not already present
            if (bookRepository.count() == 0) {
                bookRepository.saveAll(Arrays.asList(
                        new Book(null, "Book 1", "author1", 10.50),
                        new Book(null, "Book 2", "author2", 8.30),
                        new Book(null, "Book 3", "author3", 14.50),
                        new Book(null, "Book 4", "author4", 11.00),
                        new Book(null, "Book 5", "author5", 13.60),
                        new Book(null, "Book 6", "author6", 7.50),
                        new Book(null, "Book 7", "author7", 22.58),
                        new Book(null, "Book 8", "author8", 8.99),
                        new Book(null, "Book 9", "author9", 15.55),
                        new Book(null, "Book 10", "author10", 10.99)
                ));
            }

            // Hash the password before saving the user
            if (userRepository.count() == 0) {
                String encodedPassword = passwordEncoder.encode("admin");
                userRepository.save(new User(null, "admin", encodedPassword));
            }
        };
    }
}
