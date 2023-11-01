package interfaces;


import model.Book;
import lib.*;
import model.BorrowedBookInfo;


public interface IBookRepository {
    int addBook(Book book);
    Book getBookById(int bookId);
    IMyArrayList<Book> getAllBooks();
    void updateBook(Book book);
    void deleteBook(Book book);
    int deleteBook(int bookId);
    IMyArrayList<Book> searchBooksByTitle(String title);
    IMyArrayList<Book> getAvailableBooks();
    IMyArrayList<Book> getBorrowedBooks();
    IMyArrayList<Book> getBooksByAuthor(String author);
    int borrowBook(int bookId, int userId);
    int returnBook(int bookId, int userId);
    MyLinkedList<BorrowedBookInfo> getBorrowedBooksByUser(int userId);
    BorrowedBookInfo getBorrowedInfoByBookId(int bookId);
    void removeBorrowedBooksByUser(int userId);
    IMyArrayList<Book> getBooksOfUser(int userId);
}
