import React, { useContext } from 'react';
import { CartContext } from '../../contexts/CartContext'; // Assuming you are using Context API for cart management
import './Cart.css';
import { useNavigate } from 'react-router-dom'; // for navigation to checkout page

const Cart = () => {
    const { cartItems, updateQuantity, removeFromCart } = useContext(CartContext);
    const navigate = useNavigate();

    const calculateTotal = () => {
        return cartItems.reduce((total, item) => {
            const itemPrice = parseFloat(item.book.price) || 0;
            const itemQuantity = parseInt(item.quantity) || 0;
            return total + (itemPrice * itemQuantity);
        }, 0).toFixed(2);
    };

    const handleCheckout = () => {
        if (cartItems.length === 0) {
            alert("Your cart is empty. Please add items to proceed.");
        } else {
            // Navigate to the checkout page
            navigate('/checkout');
        }
    };

    const handleRemoveFromCart = (itemId) => {
        removeFromCart(itemId);
    };

    return (
        <div className="cart">
            <h2>Your Cart</h2>
            {cartItems.length === 0 ? (
                <p>Your cart is empty.</p>
            ) : (
                <ul>
                    {cartItems.map(item => (
                        <li key={item.book.id}>
                            <div className="cart-item-details">
                                <span className="cart-item-title">{item.book.title}</span>
                                <span className="cart-item-price">${item.book.price.toFixed(2)}</span>
                                <div className="cart-item-quantity">
                                    <span>Quantity:</span>
                                    <input
                                        type="number"
                                        value={item.quantity}
                                        min="1"
                                        onChange={(e) =>
                                            updateQuantity(item.book.id, Number(e.target.value))
                                        }
                                    />
                                </div>
                            </div>
                            <button className="cart-item-remove" onClick={() => handleRemoveFromCart(item.book.id)}>
                                Remove
                            </button>
                        </li>
                    ))}
                </ul>
            )}
            {cartItems.length > 0 && (
                <div className="cart-total">
                    <span>Total:</span>
                    <span>${calculateTotal()}</span>
                    <button className="cart-checkout-button" onClick={handleCheckout}>Checkout</button>
                </div>
            )}
        </div>
    );
};

export default Cart;
