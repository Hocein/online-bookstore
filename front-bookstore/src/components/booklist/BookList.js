import './BookList.css';
import React, { useEffect, useState } from 'react';
import BookItem from '../book/BookItem';
import {useApi} from "../../services/Interceptors";

const BookList = () => {
    const api = useApi();
    const [books, setBooks] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchBooks = async () => {
            try {
                const response = await api.get('/books');
                setBooks(response.data);
            } catch (error) {
                if(error.response) {
                    // Server responded with a status other than 200 range
                    setError("Failed to load books. Please try again later.");
                } else if(error.request) {
                    // Request made but no response received
                    setError("No response from server");
                } else {
                    // Something else happened
                    setError("Unknown Error");
                }
            }
        };

        fetchBooks();
    }, [api]);

    return (
        <div className="book-list">
            <h3>Available Books</h3>
            {error && <p className="error">{error}</p>}
            <div className="book-items">
                {books.map(book => (
                    <BookItem key={book.id} book={book} />
                ))}
            </div>
        </div>
    );
};

export default BookList;
