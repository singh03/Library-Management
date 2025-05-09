package com.project.Library.Management.library.service;

import com.project.Library.Management.book.entity.Book;
import com.project.Library.Management.book.service.BookService;
import com.project.Library.Management.exception.LibraryException;
import jakarta.transaction.Transactional;
import com.project.Library.Management.member.entity.Member;
import com.project.Library.Management.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LibraryService {

    private static final int MAX_BOOKS_PER_MEMBER = 3;

    private final BookService bookService;
    private final MemberService memberService;

    @Autowired
    public LibraryService(BookService bookService, MemberService memberService) {
        this.bookService = bookService;
        this.memberService = memberService;
    }

    @Transactional
    public Book borrowBook(UUID memberId, UUID bookId) {
        Member member = memberService.getMemberById(memberId);
        Book book = bookService.getBookById(bookId);

        // Check if member has reached borrowing limit
        if (member.getBorrowedCount() >= MAX_BOOKS_PER_MEMBER) {
            throw new LibraryException("Member has reached the maximum limit of " + MAX_BOOKS_PER_MEMBER + " borrowed books");
        }

        // Check if book is available
        if (!book.isAvailable()) {
            throw new LibraryException("Book is not available for borrowing");
        }

        // Update book status
        book.setAvailable(false);

        // Add book to member's borrowed books
        member.borrowBook(book);

        return book;
    }

    @Transactional
    public Book returnBook(UUID memberId, UUID bookId) {
        Member member = memberService.getMemberById(memberId);
        Book book = bookService.getBookById(bookId);

        // Check if member has borrowed this book
        if (!member.hasBorrowedBook(bookId)) {
            throw new LibraryException("Member has not borrowed this book");
        }

        // Update book status
        book.setAvailable(true);

        // Remove book from member's borrowed books
        member.returnBook(book);

        return book;
    }
}
