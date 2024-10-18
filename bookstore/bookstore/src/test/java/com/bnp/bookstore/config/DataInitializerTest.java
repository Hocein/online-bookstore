package com.bnp.bookstore.config;

import com.bnp.bookstore.entity.Book;
import com.bnp.bookstore.entity.User;
import com.bnp.bookstore.repository.BookRepository;
import com.bnp.bookstore.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DataInitializerTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DataInitializer dataInitializer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadData_ShouldInitializeBooksAndUser() throws Exception {
        // Mock behavior for book and user repository
        when(bookRepository.count()).thenReturn(0L); // Empty book repository
        when(userRepository.count()).thenReturn(0L); // Empty user repository
        when(passwordEncoder.encode("admin")).thenReturn("hashedPassword");

        // Execute the data initialization
        ApplicationRunner runner = dataInitializer.loadData(bookRepository, userRepository);
        runner.run(null);

        // Verify that books are saved
        ArgumentCaptor<List<Book>> bookCaptor = ArgumentCaptor.forClass(List.class);
        verify(bookRepository, times(1)).saveAll(bookCaptor.capture());
        List<Book> savedBooks = bookCaptor.getValue();
        assertEquals(10, savedBooks.size()); // Expecting 10 books to be saved

        // Verify that user is saved with a hashed password
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        assertEquals("admin", savedUser.getUsername());
        assertEquals("hashedPassword", savedUser.getPassword()); // Verify the password is hashed
    }

    @Test
    void loadData_ShouldNotInitializeDataWhenAlreadyPresent() throws Exception {
        // Mock behavior for non-empty repositories
        when(bookRepository.count()).thenReturn(10L); // Already has books
        when(userRepository.count()).thenReturn(1L); // Already has a user

        // Execute the data initialization
        ApplicationRunner runner = dataInitializer.loadData(bookRepository, userRepository);
        runner.run(null);

        // Verify that no books or users are saved
        verify(bookRepository, never()).saveAll(any());
        verify(userRepository, never()).save(any());
    }
}
