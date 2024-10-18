package com.bnp.bookstore.services.impl;

import com.bnp.bookstore.entity.Book;
import com.bnp.bookstore.exception.BookNotFoundException;
import com.bnp.bookstore.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    private BookServiceImpl bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookService = new BookServiceImpl(bookRepository);
    }

    @Test
    void getAllBooks_shouldReturnAllBooks() {
        // Arrange
        List<Book> expectedBooks = Arrays.asList(
                new Book(1L, "Book 1", "Author 1", 9.99),
                new Book(2L, "Book 2", "Author 2", 14.99)
        );
        when(bookRepository.findAll()).thenReturn(expectedBooks);

        // Act
        List<Book> actualBooks = bookService.getAllBooks();

        // Assert
        assertEquals(expectedBooks, actualBooks);
        verify(bookRepository).findAll();
    }

    @Test
    void createBook_shouldSaveAndReturnBook() {
        // Arrange
        Book bookToCreate = new Book(null, "New Book", "New Author", 19.99);
        Book savedBook = new Book(1L, "New Book", "New Author", 19.99);
        when(bookRepository.save(bookToCreate)).thenReturn(savedBook);

        // Act
        Book createdBook = bookService.createBook(bookToCreate);

        // Assert
        assertEquals(savedBook, createdBook);
        verify(bookRepository).save(bookToCreate);
    }

    @Test
    void getBookById_shouldReturnBook_whenBookExists() {
        // Arrange
        Long bookId = 1L;
        Book expectedBook = new Book(bookId, "Book 1", "Author 1", 9.99);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(expectedBook));

        // Act
        Book actualBook = bookService.getBookById(bookId);

        // Assert
        assertEquals(expectedBook, actualBook);
        verify(bookRepository).findById(bookId);
    }

    @Test
    void getBookById_shouldThrowException_whenBookDoesNotExist() {
        // Arrange
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(bookId));
        verify(bookRepository).findById(bookId);
    }

    @Test
    void updateBook_shouldUpdateAndReturnBook_whenBookExists() {
        // Arrange
        Long bookId = 1L;
        Book existingBook = new Book(bookId, "Old Title", "Old Author", 9.99);
        Book bookDetails = new Book(null, "Updated Title", "Updated Author", 14.99);
        Book updatedBook = new Book(bookId, "Updated Title", "Updated Author", 14.99);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

        // Act
        Book result = bookService.updateBook(bookId, bookDetails);

        // Assert
        assertEquals(updatedBook, result);
        verify(bookRepository).findById(bookId);
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void deleteBook_shouldDeleteBook_whenBookExists() {
        // Arrange
        Long bookId = 1L;
        Book bookToDelete = new Book(bookId, "Book to Delete", "Author", 9.99);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookToDelete));

        // Act
        bookService.deleteBook(bookId);

        // Assert
        verify(bookRepository).findById(bookId);
        verify(bookRepository).delete(bookToDelete);
    }

    @Test
    void deleteBook_shouldThrowException_whenBookDoesNotExist() {
        // Arrange
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(bookId));
        verify(bookRepository).findById(bookId);
        verify(bookRepository, never()).delete(any(Book.class));
    }
}