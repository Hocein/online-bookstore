// src/components/__tests__/BookList.test.js
import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import BookList from 'BookList';
import { CartContext } from '../../contexts/CartContext';
import axios from 'axios';
import userEvent from '@testing-library/user-event';

jest.mock('axios');

const mockBooks = [
    { id: 1, title: 'Book One', author: 'Author A', price: 29.99 },
    { id: 2, title: 'Book Two', author: 'Author B', price: 39.99 },
];

test('renders books fetched from API', async () => {
    axios.get.mockResolvedValue({ data: mockBooks });

    render(
        <CartContext.Provider value={{ addToCart: jest.fn() }}>
            <BookList />
        </CartContext.Provider>
    );

    expect(screen.getByText(/Books/i)).toBeInTheDocument();

    await waitFor(() => {
        expect(screen.getByText('Book One')).toBeInTheDocument();
        expect(screen.getByText('Book Two')).toBeInTheDocument();
    });
});

test('calls addToCart when "Add to Cart" button is clicked', async () => {
    const addToCart = jest.fn();
    axios.get.mockResolvedValue({ data: mockBooks });

    render(
        <CartContext.Provider value={{ addToCart }}>
            <BookList />
        </CartContext.Provider>
    );

    await waitFor(() => {
        expect(screen.getByText('Book One')).toBeInTheDocument();
    });

    const addButton = screen.getAllByText(/Add to Cart/i)[0];
    userEvent.click(addButton);

    expect(addToCart).toHaveBeenCalledWith(mockBooks[0]);
});
