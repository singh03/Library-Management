package com.project.Library.Management.libraryService;

import com.project.Library.Management.book.entity.Book;
import com.project.Library.Management.book.service.BookService;
import com.project.Library.Management.exception.LibraryException;
import com.project.Library.Management.library.service.LibraryService;
import com.project.Library.Management.member.entity.Member;
import com.project.Library.Management.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LibraryServiceTest {

    @Mock
    private BookService bookService;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private LibraryService libraryService;

    private Member member;
    private Book book;
    private UUID memberId;
    private UUID bookId;

    @BeforeEach
    void setUp() {
        memberId = UUID.randomUUID();
        bookId = UUID.randomUUID();

        member = new Member("John Doe");
        member.setMemberId(memberId);

        book = new Book("Test Book", "Test Author");
        book.setBookId(bookId);
        book.setAvailable(true);
    }

    @Test
    void borrowBook_WithValidIdAndAvailableBook_ShouldBorrowBook() {
        when(memberService.getMemberById(memberId)).thenReturn(member);
        when(bookService.getBookById(bookId)).thenReturn(book);

        Book borrowedBook = libraryService.borrowBook(memberId, bookId);

        assertNotNull(borrowedBook);
        assertFalse(borrowedBook.isAvailable());
        assertEquals(1, member.getBorrowedBooks().size());
        assertEquals(bookId, member.getBorrowedBooks().get(0).getBookId());
        verify(memberService, times(1)).getMemberById(memberId);
        verify(bookService, times(1)).getBookById(bookId);
    }

    @Test
    void borrowBook_WhenMemberHasMaxBooks_ShouldThrowException() {

        Book book1 = new Book("Book 1", "Author 1");
        book1.setBookId(UUID.randomUUID());
        Book book2 = new Book("Book 2", "Author 2");
        book2.setBookId(UUID.randomUUID());
        Book book3 = new Book("Book 3", "Author 3");
        book3.setBookId(UUID.randomUUID());

        member.setBorrowedBooks(new ArrayList<>(Arrays.asList(book1, book2, book3)));

        when(memberService.getMemberById(memberId)).thenReturn(member);
        when(bookService.getBookById(bookId)).thenReturn(book);


        assertThrows(LibraryException.class, () -> {
            libraryService.borrowBook(memberId, bookId);
        });
        verify(memberService, times(1)).getMemberById(memberId);
        verify(bookService, times(1)).getBookById(bookId);
    }

    @Test
    void borrowBook_WhenBookNotAvailable_ShouldThrowException() {

        book.setAvailable(false);

        when(memberService.getMemberById(memberId)).thenReturn(member);
        when(bookService.getBookById(bookId)).thenReturn(book);

        assertThrows(LibraryException.class, () -> {
            libraryService.borrowBook(memberId, bookId);
        });
        verify(memberService, times(1)).getMemberById(memberId);
        verify(bookService, times(1)).getBookById(bookId);
    }

    @Test
    void returnBook_WithValidIdsAndBorrowedBook_ShouldReturnBook() {
        member.borrowBook(book);
        book.setAvailable(false);

        when(memberService.getMemberById(memberId)).thenReturn(member);
        when(bookService.getBookById(bookId)).thenReturn(book);

        Book returnedBook = libraryService.returnBook(memberId, bookId);

        assertNotNull(returnedBook);
        assertTrue(returnedBook.isAvailable());
        assertEquals(0, member.getBorrowedBooks().size());
        verify(memberService, times(1)).getMemberById(memberId);
        verify(bookService, times(1)).getBookById(bookId);
    }

    @Test
    void returnBook_WhenMemberHasNotBorrowedBook_ShouldThrowException() {
        when(memberService.getMemberById(memberId)).thenReturn(member);
        when(bookService.getBookById(bookId)).thenReturn(book);

        assertThrows(LibraryException.class, () -> {
            libraryService.returnBook(memberId, bookId);
        });
        verify(memberService, times(1)).getMemberById(memberId);
        verify(bookService, times(1)).getBookById(bookId);
    }
}