package service;

import interfaces.*;
import model.*;
import lib.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;



public class LibraryService implements ILibraryService {
    private final IBookRepository bookRepository;
    private final IUserRepository userRepository;

    public LibraryService(IBookRepository bookRepository, IUserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    public int addBook(Book book) {
        return bookRepository.addBook(book);
    }

    @Override
    public int deleteBook(int bookId) {
        return bookRepository.deleteBook(bookId);
    }

    @Override
    public void addUser(User user) {
        userRepository.addUser(user);
    }

    @Override
    public int addUsers(IMyArrayList<User> users) {
        if(users == null)
            return -1;

        users.forEach(user -> {
            userRepository.addUser(user);
        });
        return 0;
    }

    @Override
    public int addBooks(IMyArrayList<Book> books) {
        if(books == null)
            return -1;

        books.forEach(book -> {
            bookRepository.addBook(book);
        });
        return 0;
    }

    @Override
    public IMyArrayList<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }

    @Override
    public  User getUserById(int userId) {
        return userRepository.getUserById(userId);
    }

    @Override
    public void updateUser(User updatedUser) {
        userRepository.updateUser(updatedUser);
    }

    @Override
    public void deleteUser(int userId){
        userRepository.deleteUser(userId);
    }

    @Override
    public void roleChanged(User user, Role newUserRole) {
        user.setRole(newUserRole);
    }

    @Override
    public IMyArrayList<Book> getAllBooks() {
        return bookRepository.getAllBooks();
    }

    @Override
    public int borrowBook(int userId, int bookId) {
        int result = bookRepository.borrowBook(userId, bookId);
        if (result == -1) {
            throw new RuntimeException("Книга уже взята");
        }
        return result;
    }


    @Override
    public int returnBook(int userId, int bookId) {
        int result = bookRepository.returnBook(userId, bookId);
        if (result == -1) {
            throw new RuntimeException("Эту книгу нельзя вернуть");
        }
        return result;
    }

    public MyLinkedList<BorrowedBookInfo> getBorrowedBooksByUser(int userId) {
        return bookRepository.getBorrowedBooksByUser(userId);
    }

    @Override
    public IMyArrayList<Book> getAllAvailableBooks() {
        return bookRepository.getAvailableBooks();
    }

    @Override
    public IMyArrayList<Book> getAllBorrowedBooks() {
        return bookRepository.getBorrowedBooks();
    }

    @Override
    public IMyArrayList<Book> getBooksByAuthor(String author) {
        return bookRepository.getBooksByAuthor(author);
    }

    @Override
    public IMyArrayList<Book> searchBooksByTitle(String title) {
        return bookRepository.searchBooksByTitle(title);
    }

    @Override
    public User registerUser(String username,  String password, Role role) {
        return userRepository.registerUser(username, password);
    }

    @Override
    public User loginUser(String username, String password) {
        return userRepository.loginUser(username, password);
    }

    @Override
    public IMyArrayList<Book> getBooksOfUser(int userId) {
        return bookRepository.getBooksOfUser(userId);
    }

    @Override
    public Book getBookById(int bookId)   {
        return bookRepository.getBookById(bookId);
    }

    @Override
    public void editBookInfo(Book book) {
        bookRepository.updateBook(book);
    }

    @Override
    public User getUserOfBorrowedBook(Book book) {
        BorrowedBookInfo borrowedInfo = bookRepository.getBorrowedInfoByBookId(book.getId());
        if (borrowedInfo == null) {
            throw new RuntimeException("Книга не взята");
        }
        return userRepository.getUserById(borrowedInfo.getUserId());
    }

    @Override
    public String getBorrowDateOfBook(Book book) {
        BorrowedBookInfo borrowedInfo = bookRepository.getBorrowedInfoByBookId(book.getId());
        if (borrowedInfo == null) {
            throw new RuntimeException("Книга не взята");
        }
        return borrowedInfo.getBorrowedDate().toString();
    }

    @Override
    public void setBorrowDateOfBook(Book book, String newBorrowDate) {
        BorrowedBookInfo borrowedInfo = bookRepository.getBorrowedInfoByBookId(book.getId());
        if (borrowedInfo == null) {
            throw new RuntimeException("Книга не взята");
        }
        borrowedInfo.setBorrowedDate(LocalDate.parse(newBorrowDate));
    }


    @Override
    public int getDaysBookWithUser(Book book) {
        BorrowedBookInfo borrowedInfo = bookRepository.getBorrowedInfoByBookId(book.getId());
        if (borrowedInfo == null) {
            throw new RuntimeException("Книга не взята");
        }
        return (int) ChronoUnit.DAYS.between(borrowedInfo.getBorrowedDate(), LocalDate.now());
    }
}



