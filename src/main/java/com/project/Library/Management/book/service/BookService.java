package com.project.Library.Management.book.service;

import com.project.Library.Management.book.dto.BookDto;
import com.project.Library.Management.book.entity.Book;
import com.project.Library.Management.book.repository.BookRepository;
import com.project.Library.Management.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(UUID id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", id));
    }

    public Book addBook(BookDto bookDTO) {
        Book book = new Book(bookDTO.getTitle(), bookDTO.getAuthor());
        return bookRepository.save(book);
    }

    public Book updateBook(UUID id, BookDto bookDTO) {
        Book book = getBookById(id);
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        return bookRepository.save(book);
    }

    public void deleteBook(UUID id) {
        Book book = getBookById(id);
        bookRepository.delete(book);
    }
}
