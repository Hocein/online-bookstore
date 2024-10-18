import React, { createContext, useState } from 'react';

export const CartContext = createContext();

export const CartProvider = ({ children }) => {
    const [cartItems, setCart] = useState([]);

    const addToCart = (book) => {
        setCart(prevCart => {
            const existing = prevCart.find(item => item.book.id === book.id);
            if (existing) {
                return prevCart.map(item =>
                    item.book.id === book.id ? { ...item, quantity: item.quantity + 1 } : item
                );
            }
            return [...prevCart, { book, quantity: 1 }];
        });
    };

    const removeFromCart = (bookId) => {
        setCart(prevCart => prevCart.filter(item => item.book.id !== bookId));
    };

    const updateQuantity = (bookId, quantity) => {
        setCart(prevCart =>
            prevCart.map(item =>
                item.book.id === bookId ? { ...item, quantity } : item
            )
        );
    };

    return (
        <CartContext.Provider value={{ cartItems, addToCart, removeFromCart, updateQuantity }}>
            {children}
        </CartContext.Provider>
    );
};
