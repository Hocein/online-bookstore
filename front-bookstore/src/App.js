import React, { useState } from 'react';
import {BrowserRouter as Router, Routes, Route, Link, Navigate} from 'react-router-dom';
import BookList from './components/booklist/BookList';
import Cart from './components/cart/Cart';
import Checkout from './components/checkout/Checkout';
import Login from './components/login/Login';
import BookItem from './components/book/BookItem';
import { CartProvider } from './contexts/CartContext';
import { Menu, X, ShoppingCart, Book, LogIn } from 'lucide-react';

const Navigation = () => {
  const [isOpen, setIsOpen] = useState(false);

  return (
      <nav className="bg-gray-800 text-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-16">
            <div className="flex items-center">
              <span className="font-bold text-xl">BookStore</span>
            </div>
            <div className="hidden md:block">
              <div className="ml-10 flex items-baseline space-x-4">
                <Link to="/books" className="px-3 py-2 rounded-md text-sm font-medium hover:bg-gray-700 flex items-center">
                  <Book className="w-4 h-4 mr-2" />
                  Books
                </Link>
                <Link to="/cart" className="px-3 py-2 rounded-md text-sm font-medium hover:bg-gray-700 flex items-center">
                  <ShoppingCart className="w-4 h-4 mr-2" />
                  Cart
                </Link>
                <Link to="/login" className="px-3 py-2 rounded-md text-sm font-medium hover:bg-gray-700 flex items-center">
                  <LogIn className="w-4 h-4 mr-2" />
                  Login
                </Link>
              </div>
            </div>
            <div className="md:hidden">
              <button
                  onClick={() => setIsOpen(!isOpen)}
                  className="inline-flex items-center justify-center p-2 rounded-md text-gray-400 hover:text-white hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-offset-gray-800 focus:ring-white"
              >
                <span className="sr-only">Open main menu</span>
                {isOpen ? <X className="block h-6 w-6" /> : <Menu className="block h-6 w-6" />}
              </button>
            </div>
          </div>
        </div>

        {isOpen && (
            <div className="md:hidden">
              <div className="px-2 pt-2 pb-3 space-y-1 sm:px-3">
                <Link to="/" className="block px-3 py-2 rounded-md text-base font-medium text-white hover:bg-gray-700">Books</Link>
                <Link to="/cart" className="block px-3 py-2 rounded-md text-base font-medium text-white hover:bg-gray-700">Cart</Link>
                <Link to="/login" className="block px-3 py-2 rounded-md text-base font-medium text-white hover:bg-gray-700">Login</Link>
              </div>
            </div>
        )}
      </nav>
  );
};

function App() {
  return (
      <CartProvider>
        <Router>
          <div className="min-h-screen bg-gray-100">
            <Navigation />
            <main className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
              <Routes>
                <Route path="/login" element={<Login />} />
                <Route path="/books" element={<BookList />} />
                <Route path="/cart" element={<Cart />} />
                <Route path="/checkout" element={<Checkout />} />
                <Route path="/item" element={<BookItem />} />
                <Route path="/" element={<Navigate replace to="/login" />} />
              </Routes>
            </main>
          </div>
        </Router>
      </CartProvider>
  );
}

export default App;