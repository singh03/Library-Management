package com.project.Library.Management.book;

import com.project.Library.Management.book.dto.BookDto;
import com.project.Library.Management.book.entity.Book;
import com.project.Library.Management.book.repository.BookRepository;
import com.project.Library.Management.book.service.BookService;
import com.project.Library.Management.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book book1;
    private Book book2;
    private UUID bookId;
    private BookDto bookDTO;

    @BeforeEach
    void setUp() {
        bookId = UUID.randomUUID();

        book1 = new Book("Test Book 1", "Author 1");
        book1.setBookId(bookId);
        book1.setAvailable(true);

        book2 = new Book("Test Book 2", "Author 2");
        book2.setBookId(UUID.randomUUID());
        book2.setAvailable(false);

        bookDTO = new BookDto();
        bookDTO.setTitle("New Book");
        bookDTO.setAuthor("New Author");
    }

    @Test
    void getAllBooks_ShouldReturnAllBooks() {

        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

        List<Book> books = bookService.getAllBooks();

        assertEquals(2, books.size());
        assertEquals("Test Book 1", books.get(0).getTitle());
        assertEquals("Test Book 2", books.get(1).getTitle());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void getBookById_WithValidId_ShouldReturnBook() {

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book1));

        Book book = bookService.getBookById(bookId);

        assertNotNull(book);
        assertEquals(bookId, book.getBookId());
        assertEquals("Test Book 1", book.getTitle());
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    void getBookById_WithInvalidId_ShouldThrowException() {
        UUID invalidId = UUID.randomUUID();
        when(bookRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            bookService.getBookById(invalidId);
        });
        verify(bookRepository, times(1)).findById(invalidId);
    }

    @Test
    void addBook_ShouldSaveAndReturnBook() {
        Book newBook = new Book(bookDTO.getTitle(), bookDTO.getAuthor());
        when(bookRepository.save(any(Book.class))).thenReturn(newBook);

        Book savedBook = bookService.addBook(bookDTO);

        assertNotNull(savedBook);
        assertEquals("New Book", savedBook.getTitle());
        assertEquals("New Author", savedBook.getAuthor());
        assertTrue(savedBook.isAvailable());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void updateBook_WithValidId_ShouldUpdateAndReturnBook() {
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book1));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Book updatedBook = bookService.updateBook(bookId, bookDTO);

        assertNotNull(updatedBook);
        assertEquals(bookId, updatedBook.getBookId());
        assertEquals("New Book", updatedBook.getTitle());
        assertEquals("New Author", updatedBook.getAuthor());
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void deleteBook_WithValidId_ShouldDeleteBook() {

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book1));
        doNothing().when(bookRepository).delete(book1);

        bookService.deleteBook(bookId);

        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).delete(book1);
    }
}