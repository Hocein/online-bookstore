import './BookItem.css';
import React, {useContext} from 'react';
import {CartContext} from "../../contexts/CartContext";
import {useNavigate} from "react-router-dom";

const BookItem = ({ book }) => {
    const { addToCart } = useContext(CartContext);
    const navigate = useNavigate();
    const handleAddToCart = () => {
        addToCart(book);
        navigate('/cart');

    };

    return (
        <div className="book-item">
            <h3>{book?.title}</h3>
            <p>Author: {book?.author}</p>
            <p>Price: ${book?.price.toFixed(2)}</p>
            <button onClick={handleAddToCart}>Add to Cart</button>
        </div>
    );
};

export default BookItem;
