package com.project.Library.Management.library.controller;

import com.project.Library.Management.book.entity.Book;
import com.project.Library.Management.bookReturn.dto.BookBorrowDto;
import jakarta.validation.Valid;
import com.project.Library.Management.library.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LibraryController {

    private final LibraryService libraryService;

    @Autowired
    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @PostMapping("/borrow")
    public ResponseEntity<Book> borrowBook(@Valid @RequestBody BookBorrowDto dto) {
        Book book = libraryService.borrowBook(dto.getMemberId(), dto.getBookId());
        return ResponseEntity.ok(book);
    }

    @PostMapping("/return")
    public ResponseEntity<Book> returnBook(@Valid @RequestBody BookBorrowDto dto) {
        Book book = libraryService.returnBook(dto.getMemberId(), dto.getBookId());
        return ResponseEntity.ok(book);
    }
}
