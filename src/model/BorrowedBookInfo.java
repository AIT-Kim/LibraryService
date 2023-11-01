package model;

import java.time.LocalDate;

public class BorrowedBookInfo {
    private final int userId;
    private final int bookId;
    private LocalDate borrowedDate;

    public BorrowedBookInfo(int userId, int bookId, LocalDate borrowedDate) {
        this.userId = userId;
        this.bookId = bookId;
        this.borrowedDate = borrowedDate;
    }

    public int getUserId() {
        return userId;
    }

    public int getBookId() {
        return bookId;
    }

    public LocalDate getBorrowedDate() {
        return borrowedDate;
    }

    // Сеттер для изменения даты взятия книги
    public void setBorrowedDate(LocalDate newBorrowedDate) {
        this.borrowedDate = newBorrowedDate;
    }
}
