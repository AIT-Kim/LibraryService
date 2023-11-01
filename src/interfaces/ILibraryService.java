package interfaces;


import model.Book;
import model.BorrowedBookInfo;
import model.Role;
import model.User;
import lib.*;

public interface ILibraryService {

    // Добавление книги в библиотеку
    int addBook(Book book);
    int deleteBook(int bookId);
    void addUser(User user);
    IMyArrayList<User> getAllUsers();

    User getUserByUsername(String username);
    User getUserById(int userId);
    int addUsers(IMyArrayList<User> users);
    int addBooks(IMyArrayList<Book> books);
    void updateUser(User updatedUser);
    void deleteUser(int userId);
    void roleChanged(User user, Role newUserRole);

    // Получение списка всех книг в библиотеке
    IMyArrayList<Book> getAllBooks();

    // Взятие книги из библиотеки пользователем
    int borrowBook(int userId, int bookId);

    // Возврат книги в библиотеку
    int returnBook(int userId, int bookId);

    MyLinkedList<BorrowedBookInfo> getBorrowedBooksByUser(int userId);

    // Получение списка всех свободных книг в библиотеке
    IMyArrayList<Book> getAllAvailableBooks();

    // Получение списка всех книг, находящихся у пользователей
    IMyArrayList<Book> getAllBorrowedBooks();

    // Получение списка книг по автору
    IMyArrayList<Book> getBooksByAuthor(String author);

    // Поиск книг по названию
    IMyArrayList<Book> searchBooksByTitle(String title);

    // Регистрация нового пользователя в библиотеке
    User registerUser(String username,  String password, Role role);

    // Авторизация пользователя в библиотеке
    User loginUser(String username, String password);

    // Получение списка книг, которые на данный момент у пользователя
    IMyArrayList<Book> getBooksOfUser(int userId);
    Book getBookById(int bookId);

    // Редактирование информации о книге
    void editBookInfo(Book book);

    // Просмотр, у кого находится книга, если она занята
    User getUserOfBorrowedBook(Book book);

    // Получение даты, когда книга была взята на прокат
    String getBorrowDateOfBook(Book book);

    // Установка новой даты для книги, взятой на прокат
    void setBorrowDateOfBook(Book book, String newBorrowDate);

    // Получение информации о том, сколько дней книга находится у пользователя
    int getDaysBookWithUser(Book book);
}
