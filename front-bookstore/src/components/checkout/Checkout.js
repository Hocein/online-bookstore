import './Checkout.css';
import React, {useContext, useState} from 'react';
import { CartContext } from '../../contexts/CartContext';
import ConfirmModal from "../confirm-modal/ConfirmModal";

const Checkout = () => {
    const { cartItems } = useContext(CartContext);
    const [isModalOpen, setIsModalOpen] = useState(false);

    const calculateTotal = () => {
        return cartItems.reduce((total, item) => total + item.book.price * item.quantity, 0).toFixed(2);
    };

    const handleOrderConfirmation = () => {
        setIsModalOpen(true);
    };

    return (
        <div className="checkout">
            <h2>Order Summary</h2>
            {!cartItems || cartItems.length === 0 ? (
                <p>Your cart is empty.</p>
            ) : (
                <>
                    <ul>
                        {cartItems.map(item => (
                            <li key={item.id}>
                                {item.book.title} - ${item.book.price.toFixed(2)} x {item.quantity}
                            </li>
                        ))}
                    </ul>
                    <h3>Total: ${calculateTotal()}</h3>
                    <button className="checkout-confirm-button" onClick={handleOrderConfirmation}>
                        Confirm Order
                    </button>
                </>
            )}
            <ConfirmModal isOpen={isModalOpen} onClose={() => setIsModalOpen(false)}>
                <h2>Order Confirmed!</h2>
                <p>Your order has been placed successfully.</p>
            </ConfirmModal>
        </div>
    );
};

export default Checkout;
