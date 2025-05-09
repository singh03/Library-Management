# Library Management System (Spring Boot)
A simple REST-based Library Management Mini-System built with Spring Boot as an interview task. It supports basic operations for managing books and members, including borrowing and returning books. The system uses MySQL for persistence.

## Features
Add, update, delete books and members

Borrow and return books

Prevents borrowing of already issued books

Limits a member to borrow max 3 books


## MySQL Setup
Make sure MySQL is installed and running. Create a database:

CREATE DATABASE library_db;

## API Endpoints
# Books
# Add Book
POST /api/books

{
  "title": "The Alchemist",
  "author": "Paulo Coelho"
}
Get All Books
GET /api/books

Get Book by ID
GET /api/books/{bookId}

Update Book
PUT /api/books/{bookId}

Delete Book
DELETE /api/books/{bookId}

# Members
Add Member
POST /api/members

{
  "name": "Charan"
}
Get All Members
GET /api/members

Get Member by ID
GET /api/members/{memberId}


Update Member
PUT /api/members/{memberId}

Delete Member
DELETE /api/members/{memberId}

# Borrow & Return
# Borrow Book
POST /api/borrow

{
  "memberId": "member-uuid-here",
  "bookId": "book-uuid-here"
}

# Return Book
POST /api/return

{
  "memberId": "member-uuid-here",
  "bookId": "book-uuid-here"
}


## Validation
Title, author, and name cannot be blank

A book can’t be borrowed if it’s already issued

A member can borrow up to 3 books only


## Testing
Basic validation included. You can add unit tests using JUnit for services and controllers.
