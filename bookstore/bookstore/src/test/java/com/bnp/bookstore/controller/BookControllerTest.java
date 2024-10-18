package com.bnp.bookstore.controller;

import com.bnp.bookstore.entity.Book;
import com.bnp.bookstore.exception.BookNotFoundException;
import com.bnp.bookstore.services.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllBooks_shouldReturnListOfBooks() {
        // Arrange
        List<Book> expectedBooks = Arrays.asList(
                new Book(1L, "Book 1", "Author 1", 9.99),
                new Book(2L, "Book 2", "Author 2", 14.99)
        );
        when(bookService.getAllBooks()).thenReturn(expectedBooks);

        // Act
        List<Book> result = bookController.getAllBooks();

        // Assert
        assertEquals(expectedBooks, result);
        verify(bookService).getAllBooks();
    }

    @Test
    void createBook_shouldReturnCreatedBook() {
        // Arrange
        Book bookToCreate = new Book(null, "New Book", "New Author", 19.99);
        Book createdBook = new Book(1L, "New Book", "New Author", 19.99);
        when(bookService.createBook(bookToCreate)).thenReturn(createdBook);

        // Act
        Book result = bookController.createBook(bookToCreate);

        // Assert
        assertEquals(createdBook, result);
        verify(bookService).createBook(bookToCreate);
    }

    @Test
    void getBookById_shouldReturnBook_whenBookExists() {
        // Arrange
        Long bookId = 1L;
        Book expectedBook = new Book(bookId, "Book 1", "Author 1", 9.99);
        when(bookService.getBookById(bookId)).thenReturn(expectedBook);

        // Act
        Book result = bookController.getBookById(bookId);

        // Assert
        assertEquals(expectedBook, result);
        verify(bookService).getBookById(bookId);
    }

    @Test
    void getBookById_shouldThrowException_whenBookDoesNotExist() {
        // Arrange
        Long bookId = 1L;
        when(bookService.getBookById(bookId)).thenThrow(new BookNotFoundException("Book not found"));

        // Act & Assert
        assertThrows(BookNotFoundException.class, () -> bookController.getBookById(bookId));
        verify(bookService).getBookById(bookId);
    }

    @Test
    void updateBook_shouldReturnUpdatedBook_whenBookExists() {
        // Arrange
        Long bookId = 1L;
        Book bookDetails = new Book(null, "Updated Book", "Updated Author", 24.99);
        Book updatedBook = new Book(bookId, "Updated Book", "Updated Author", 24.99);
        when(bookService.updateBook(bookId, bookDetails)).thenReturn(updatedBook);

        // Act
        Book result = bookController.updateBook(bookId, bookDetails);

        // Assert
        assertEquals(updatedBook, result);
        verify(bookService).updateBook(bookId, bookDetails);
    }

    @Test
    void updateBook_shouldThrowException_whenBookDoesNotExist() {
        // Arrange
        Long bookId = 1L;
        Book bookDetails = new Book(null, "Updated Book", "Updated Author", 24.99);
        when(bookService.updateBook(bookId, bookDetails)).thenThrow(new BookNotFoundException("Book not found"));

        // Act & Assert
        assertThrows(BookNotFoundException.class, () -> bookController.updateBook(bookId, bookDetails));
        verify(bookService).updateBook(bookId, bookDetails);
    }

    @Test
    void deleteBook_shouldNotThrowException_whenBookExists() {
        // Arrange
        Long bookId = 1L;
        doNothing().when(bookService).deleteBook(bookId);

        // Act & Assert
        assertDoesNotThrow(() -> bookController.deleteBook(bookId));
        verify(bookService).deleteBook(bookId);
    }

    @Test
    void deleteBook_shouldThrowException_whenBookDoesNotExist() {
        // Arrange
        Long bookId = 1L;
        doThrow(new BookNotFoundException("Book not found")).when(bookService).deleteBook(bookId);

        // Act & Assert
        assertThrows(BookNotFoundException.class, () -> bookController.deleteBook(bookId));
        verify(bookService).deleteBook(bookId);
    }

    @Test
    void handleBookNotFoundException_shouldReturnNotFoundStatus() {
        // Arrange
        BookNotFoundException ex = new BookNotFoundException("Book not found");

        // Act
        ResponseEntity<String> response = bookController.handleBookNotFoundException(ex);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Book not found", response.getBody());
    }
}