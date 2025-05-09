package com.project.Library.Management.bookReturn.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class BookBorrowDto {

    @NotNull(message = "Member ID cannot be null")
    private UUID memberId;

    @NotNull(message = "Book ID cannot be null")
    private UUID bookId;

    public UUID getMemberId() {
        return memberId;
    }

    public void setMemberId(UUID memberId) {
        this.memberId = memberId;
    }

    public UUID getBookId() {
        return bookId;
    }

    public void setBookId(UUID bookId) {
        this.bookId = bookId;
    }
}
