package com.bnp.bookstore.services;

import com.bnp.bookstore.entity.Book;
import java.util.List;

public interface BookService {
    List<Book> getAllBooks();
    Book createBook(Book book);
    Book getBookById(Long id);
    Book updateBook(Long id, Book bookDetails);
    void deleteBook(Long id);
}