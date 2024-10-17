# online-bookstore
This is a simple code kata that involves creating a basic front-end in React and a back-end using a RESTful API. We will create a Simple Online Bookstore. We will display a list of books and users will have the possibility to add books to their cart, display the cart and modify the quantity of items and remove items from the cart. 

# Simple Online Bookstore

A simple online bookstore application built with React (frontend) and Spring Boot (backend), following Test-Driven Development (TDD) principles.

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
    - [Backend](#backend)
    - [Frontend](#frontend)
- [Running the Application](#running-the-application)
    - [Backend](#running-the-backend)
    - [Frontend](#running-the-frontend)
- [Testing](#testing)
    - [Backend Tests](#backend-tests)
    - [Frontend Tests](#frontend-tests)
- [API Documentation](#api-documentation)
- [Project Structure](#project-structure)
- [Contributing](#contributing)
- [License](#license)

## Features

- Display a list of books with titles, authors, and prices.
- Add books to a shopping cart.
- View and modify cart contents (change quantities, remove items).
- User authentication (registration and login).
- Simple checkout process with order summary.

## Technologies Used

- **Frontend**:
    - React
    - Axios
    - React Router DOM
    - Context API
    - Jest & React Testing Library

- **Backend**:
    - Spring Boot
    - Spring Data JPA
    - MySQL
    - Spring Security
    - JUnit & Mockito

## Prerequisites

- Java 21
- Maven
- Node.js and npm
- MySQL Server

## Installation

### Backend

1. **Clone the Repository**:

   ```bash
   git clone https://github.com/yourusername/online-bookstore.git
   cd online-bookstore/backend
