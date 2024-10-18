-- Delete all existing data from the book table
DELETE FROM book;

-- Reset the ID sequence (specific to H2)
ALTER TABLE book ALTER COLUMN id RESTART WITH 1;

-- Insert initial data
INSERT INTO book (title, author, price) VALUES
('To Kill a Mockingbird', 'Harper Lee', 12.99),
('1984', 'George Orwell', 10.99),
('Pride and Prejudice', 'Jane Austen', 9.99),
('The Great Gatsby', 'F. Scott Fitzgerald', 11.99),
('Moby Dick', 'Herman Melville', 14.99),
('The Catcher in the Rye', 'J.D. Salinger', 10.50),
('Jane Eyre', 'Charlotte Brontë', 12.50),
('The Hobbit', 'J.R.R. Tolkien', 13.99),
('Fahrenheit 451', 'Ray Bradbury', 11.50),
('Brave New World', 'Aldous Huxley', 12.75);