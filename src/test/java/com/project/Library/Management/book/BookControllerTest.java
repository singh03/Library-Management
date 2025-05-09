package com.project.Library.Management.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.Library.Management.book.controller.BookController;
import com.project.Library.Management.book.dto.BookDto;
import com.project.Library.Management.book.entity.Book;
import com.project.Library.Management.book.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    private Book book1;
    private Book book2;
    private UUID bookId;
    private BookDto bookDTO;
    private String bookDtoJson;

    @BeforeEach
    void setUp() throws Exception {
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

        bookDtoJson = objectMapper.writeValueAsString(bookDTO);
    }

    @Test
    void getAllBooks_ShouldReturnBooks() throws Exception {

        List<Book> books = Arrays.asList(book1, book2);
        when(bookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Test Book 1")))
                .andExpect(jsonPath("$[1].title", is("Test Book 2")));

        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void getBookById_WithValidId_ShouldReturnBook() throws Exception {

        when(bookService.getBookById(bookId)).thenReturn(book1);

        mockMvc.perform(get("/api/books/{id}", bookId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is("Test Book 1")))
                .andExpect(jsonPath("$.author", is("Author 1")))
                .andExpect(jsonPath("$.available", is(true)));

        verify(bookService, times(1)).getBookById(bookId);
    }

    @Test
    void addBook_WithValidData_ShouldCreateBook() throws Exception {

        Book newBook = new Book(bookDTO.getTitle(), bookDTO.getAuthor());
        newBook.setBookId(UUID.randomUUID());

        when(bookService.addBook(any(BookDto.class))).thenReturn(newBook);

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookDtoJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is("New Book")))
                .andExpect(jsonPath("$.author", is("New Author")))
                .andExpect(jsonPath("$.available", is(true)));

        verify(bookService, times(1)).addBook(any(BookDto.class));
    }

    @Test
    void updateBook_WithValidIdAndData_ShouldUpdateBook() throws Exception {

        Book updatedBook = new Book(bookDTO.getTitle(), bookDTO.getAuthor());
        updatedBook.setBookId(bookId);

        when(bookService.updateBook(eq(bookId), any(BookDto.class))).thenReturn(updatedBook);

        mockMvc.perform(put("/api/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookDtoJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.bookId", is(bookId.toString())))
                .andExpect(jsonPath("$.title", is("New Book")))
                .andExpect(jsonPath("$.author", is("New Author")));

        verify(bookService, times(1)).updateBook(eq(bookId), any(BookDto.class));
    }

    @Test
    void deleteBook_WithValidId_ShouldDeleteBook() throws Exception {

        doNothing().when(bookService).deleteBook(bookId);

        mockMvc.perform(delete("/api/books/{id}", bookId))
                .andExpect(status().isNoContent());

        verify(bookService, times(1)).deleteBook(bookId);
    }
}