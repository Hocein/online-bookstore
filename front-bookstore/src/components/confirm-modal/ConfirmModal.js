import React from 'react';
import { useNavigate } from 'react-router-dom';
import './ConfirmModal.css';

const ConfirmModal = ({ isOpen, onClose, children }) => {
    const navigate = useNavigate(); // Initialize useNavigate hook

    const handleClose = () => {
        onClose();
        navigate('/books');
    };

    if (!isOpen) return null;

    return (
        <div className="modal-overlay" onClick={handleClose}>
            <div className="modal-content" onClick={e => e.stopPropagation()}>
                {children}
                <button className="modal-close" onClick={handleClose}>Close</button>
            </div>
        </div>
    );
};

export default ConfirmModal;
