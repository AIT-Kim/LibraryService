package view;

import interfaces.*;
import lib.IMyArrayList;
import model.*;
import service.*;
import testdata.BookGenerator;
import testdata.UserGenerator;
import java.util.*;

public class LibraryConsoleApplication {
    private ILibraryService libraryService;
    private User currentUser;

    public LibraryConsoleApplication(IBookRepository bookRepository, IUserRepository userRepository) {
        this.libraryService = new LibraryService(bookRepository, userRepository);
    }

    //TODO For debug only - need delete
    public void GenerateData() {
        var userGenerator = new UserGenerator();
        var users = userGenerator.createStaticUsers();
        libraryService.addUsers(users);

        var bookGenerator = new BookGenerator();
        var books = bookGenerator.createLibraryFromCsvFile("books.csv");
        libraryService.addBooks(books);
    }


    public void start() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                if (currentUser == null) {
                    showUnauthenticatedMenu(scanner);
                } else {
                    showAuthenticatedMenu(scanner);
                }
            }
        }
    }

    private void showUnauthenticatedMenu(Scanner scanner) {
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                login(scanner);
                break;
            case 2:
                register(scanner);
                break;
            case 0:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice");
        }
    }

    private void login(Scanner scanner) {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        User user = libraryService.loginUser(username, password);
        if (user != null) {
            currentUser = user;
            System.out.println("Login successful!");
        } else {
            System.out.println("Invalid username or password");
        }
    }

    private void register(Scanner scanner) {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        var user = libraryService.registerUser(username, password, Role.READER);
        System.out.println("Registration successful!");
    }

    private void showAuthenticatedMenu(Scanner scanner) {
        switch (currentUser.getRole()) {
            case ADMIN:
                showAdminMenu(scanner);
                break;
            case LIBRARIAN:
                showLibrarianMenu(scanner);
                break;
            case READER:
                showReaderMenu(scanner);
                break;
        }
    }

    private void showAdminMenu(Scanner scanner) {
        while (true) {
            System.out.println("1. Список всех пользователей");
            System.out.println("2. Добавить пользователя");
            System.out.println("3. Редактировать пользователя");
            System.out.println("4. Удалить пользователя");
            System.out.println("0. Выйти");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    listAllUsers();
                    break;
                case 2:
                    addUser(scanner);
                    break;
                case 3:
                    editUser(scanner);
                    break;
                case 4:
                    deleteUser(scanner);
                    break;
                case 0:
                    currentUser = null;
                    System.out.println("Вы вышли из учетной записи");
                    return;
                default:
                    System.out.println("Неверный ввод. Пожалуйста, попробуйте еще раз.");
            }
        }
    }

    private void showLibrarianMenu(Scanner scanner) {
        while (true) {
            System.out.println("1. Добавить книгу");
            System.out.println("2. Редактировать книгу");
            System.out.println("3. Удалить книгу");
            System.out.println("4. Список всех книг");
            System.out.println("5. Список всех свободных книг");
            System.out.println("6. Список всех занятых книг");
            System.out.println("7. Список книг по автору");
            System.out.println("8. Поиск книг по названию");
            System.out.println("9. Взять книгу");
            System.out.println("10. Вернуть книгу");
            System.out.println("0. Выйти");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addBook(scanner);
                    break;
                case 2:
                    editBookInfo(scanner);
                    break;
                case 3:
                    deleteBook(scanner);
                    break;
                case 4:
                    listAllBooks();
                    break;
                case 5:
                    listAvailableBooks();
                    break;
                case 6:
                    //listBorrowedBooks();
                    break;
                case 7:
                    listBooksByAuthor(scanner);
                    break;
                case 8:
                    searchBooksByTitle(scanner);
                    break;
                case 9:
                    borrowBook(scanner);
                    break;
                case 10:
                    returnBook(scanner);
                    break;
                case 0:
                    currentUser = null;
                    System.out.println("Вы вышли из учетной записи");
                    return;
                default:
                    System.out.println("Неверный ввод. Пожалуйста, попробуйте еще раз.");
            }
        }
    }


    private void showReaderMenu(Scanner scanner) {
        System.out.println("1. Просмотреть все книги");
        System.out.println("2. Просмотреть доступные книги");
        System.out.println("3. Взять книгу");
        System.out.println("4. Вернуть книгу");
        System.out.println("5. Поиск книг по названию");
        System.out.println("6. Просмотреть книги по автору");
        System.out.println("7. Просмотреть книги, взятые мной");
        System.out.println("0. Выйти");
        System.out.print("Выберите действие: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        switch (choice) {
            case 1:
                listAllBooks();
                break;
            case 2:
                listAvailableBooks();
                break;
            case 3:
                borrowBook(scanner);
                break;
            case 4:
                returnBook(scanner);
                break;
            case 5:
                searchBooksByTitle(scanner);
                break;
            case 6:
                listBooksByAuthor(scanner);
                break;
            case 7:
                listMyBorrowedBooks();
                break;
            case 0:
                currentUser = null;
                System.out.println("Вы вышли из учетной записи");
                break;
            default:
                System.out.println("Неверный выбор, попробуйте еще раз");
        }
    }

    private void listAllUsers() {
        IMyArrayList<User> users = libraryService.getAllUsers();
        if (users.size() == 0) {
            System.out.println("Пользователей в системе нет.");
            return;
        }
        System.out.println("Список всех пользователей в системе:");
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            System.out.println("ID: " + user.getId() + ", Имя пользователя: " + user.getUsername() + ", Роль: " + user.getRole().getName());
        }
    }

    private void addUser(Scanner scanner) {
        System.out.println("Добавление нового пользователя");
        System.out.print("Введите имя пользователя: ");
        String username = scanner.nextLine();

        // Проверка на существование пользователя с таким именем
        if (libraryService.getUserByUsername(username) != null) {
            System.out.println("Пользователь с таким именем уже существует.");
            return;
        }

        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        System.out.println("Выберите роль пользователя:");
        for (Role role : Role.values()) {
            System.out.println(role.ordinal() + 1 + ". " + role.getName());
        }

        Role userRole = null;
        while (userRole == null) {
            System.out.print("Введите номер роли: ");
            int roleChoice = scanner.nextInt();
            scanner.nextLine(); // Очищаем буфер
            if (roleChoice >= 1 && roleChoice <= Role.values().length) {
                userRole = Role.values()[roleChoice - 1];
            } else {
                System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }

        User newUser = new User(username, password, userRole);
        libraryService.addUser(newUser);
        System.out.println("Пользователь успешно добавлен.");
        System.out.println("ID: " + newUser.getId() + ", Имя пользователя: " + newUser.getUsername() + ", Роль: " + newUser.getRole().getName());
    }

    private void editUser(Scanner scanner) {
        System.out.println("Редактирование пользователя");
        System.out.print("Введите ID или имя пользователя: ");
        String input = scanner.nextLine();

        User user;
        try {
            int userId = Integer.parseInt(input);
            user = libraryService.getUserById(userId);
        } catch (NumberFormatException e) {
            user = libraryService.getUserByUsername(input);
        }

        if (user == null) {
            System.out.println("Пользователь не найден.");
            return;
        }

        System.out.println("ID: " + user.getId() + ", Имя пользователя: " + user.getUsername() + ", Роль: " + user.getRole().getName());
        System.out.println("Выберите параметр для изменения:");
        System.out.println("1. Имя пользователя");
        System.out.println("2. Пароль");
        System.out.println("3. Роль");
        System.out.println("0. Выйти");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Очищаем буфер

        switch (choice) {
            case 1:
                System.out.print("Введите новое имя пользователя: ");
                String newUsername = scanner.nextLine();
                user.setUsername(newUsername);
                break;
            case 2:
                System.out.print("Введите новый пароль: ");
                String newPassword = scanner.nextLine();
                user.setPassword(newPassword);
                break;
            case 3:
                System.out.println("Выберите новую роль:");
                for (Role role : Role.values()) {
                    System.out.println(role.ordinal() + 1 + ". " + role.getName());
                }
                Role newUserRole = null;
                while (newUserRole == null) {
                    System.out.print("Введите номер роли: ");
                    int roleChoice = scanner.nextInt();
                    scanner.nextLine(); // Очищаем буфер
                    if (roleChoice >= 1 && roleChoice <= Role.values().length) {
                        newUserRole = Role.values()[roleChoice - 1];
                    } else {
                        System.out.println("Неверный выбор. Попробуйте снова.");
                    }
                }
                libraryService.roleChanged(user, newUserRole);
                break;
            case 0:
                currentUser = null;
                System.out.println("Вы вышли из учетной записи");
                return;
            default:
                System.out.println("Неверный выбор. Попробуйте снова.");
        }

        libraryService.updateUser(user);
        System.out.println("Пользователь успешно обновлен.");
    }

    private void deleteUser(Scanner scanner) {
        System.out.println("Удаление пользователя");
        System.out.print("Введите ID или имя пользователя: ");
        String input = scanner.nextLine();

        User user;
        try {
            int userId = Integer.parseInt(input);
            user = libraryService.getUserById(userId);
        } catch (NumberFormatException e) {
            user = libraryService.getUserByUsername(input);
        }

        if (user == null) {
            System.out.println("Пользователь не найден.");
            return;
        }

        IMyArrayList<Book> borrowedBooks = libraryService.getBooksOfUser(user.getId());
        if (borrowedBooks.size() > 0) {
            System.out.println("У пользователя есть взятые книги:");
            for (Book book : borrowedBooks) {
                System.out.println("ID: " + book.getId() + ", Название: " + book.getTitle() + ", Автор: " + book.getAuthor());
            }
            System.out.println("Вы все равно хотите удалить пользователя " + user.getUsername() + " и вернуть все его книги? (да/нет): ");
        } else {
            System.out.println("Вы уверены, что хотите удалить пользователя " + user.getUsername() + "? (да/нет): ");
        }

        String confirmation = scanner.nextLine();
        if (confirmation.equalsIgnoreCase("да")) {
            // Возвращаем все книги, которые находятся у пользователя
            for (Book book : borrowedBooks) {
                libraryService.returnBook(user.getId(), book.getId());
            }
            libraryService.deleteUser(user.getId());
            System.out.println("Пользователь успешно удален.");
        } else {
            System.out.println("Удаление отменено.");
        }
    }



    private void listAllBooks() {
        IMyArrayList<Book> books = libraryService.getAllBooks();
        if (books.size() == 0) {
            System.out.println("В библиотеке нет книг.");
            return;
        }
        System.out.println("Список всех книг в библиотеке:");
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            System.out.println((i + 1) + ". " + book.getTitle() + " - " + book.getAuthor() + " (" + book.getPublicationYear() + ")");
        }
    }

    private void listAvailableBooks() {
        IMyArrayList<Book> availableBooks = libraryService.getAllAvailableBooks();
        if (availableBooks.size() == 0) {
            System.out.println("Нет доступных книг.");
            return;
        }
        System.out.println("Доступные книги:");
        for (int i = 0; i < availableBooks.size(); i++) {
            Book book = availableBooks.get(i);
            System.out.println((i + 1) + ". " + book.getTitle() + " - " + book.getAuthor() + " (" + book.getPublicationYear() + ")");
        }
    }

    private void borrowBook(Scanner scanner) {
        System.out.print("Введите ID книги, которую вы хотите взять: ");
        int bookId = scanner.nextInt();
        scanner.nextLine();
        int result = libraryService.borrowBook(bookId, currentUser.getId());
        if (result == 0) {
            System.out.println("Книга успешно взята!");
        } else {
            System.out.println("Не удалось взять книгу. Проверьте ID и доступность книги.");
        }
    }

    private void returnBook(Scanner scanner) {
        System.out.print("Введите ID книги, которую вы хотите вернуть: ");
        int bookId = scanner.nextInt();
        scanner.nextLine();
        int result = libraryService.returnBook(bookId, currentUser.getId());
        if (result == 0) {
            System.out.println("Книга успешно возвращена!");
        } else {
            System.out.println("Не удалось вернуть книгу. Проверьте ID и убедитесь, что вы взяли эту книгу.");
        }
    }

    private void searchBooksByTitle(Scanner scanner) {
        System.out.print("Введите название книги: ");
        String title = scanner.nextLine();
        IMyArrayList<Book> foundBooks = libraryService.searchBooksByTitle(title);
        if (foundBooks.size() == 0) {
            System.out.println("Книги с таким названием не найдены.");
            return;
        }
        System.out.println("Найденные книги:");
        for (int i = 0; i < foundBooks.size(); i++) {
            Book book = foundBooks.get(i);
            System.out.println((i + 1) + ". " + book.getTitle() + " - " + book.getAuthor() + " (" + book.getPublicationYear() + ")");
        }
    }

    private void listBooksByAuthor(Scanner scanner) {
        System.out.print("Введите имя автора: ");
        String author = scanner.nextLine();
        IMyArrayList<Book> books = libraryService.getBooksByAuthor(author);
        if (books.size() == 0) {
            System.out.println("Книги этого автора не найдены.");
            return;
        }
        System.out.println("Книги автора " + author + ":");
        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            System.out.println((i + 1) + ". " + book.getTitle() + " (" + book.getPublicationYear() + ")");
        }
    }

    private void listMyBorrowedBooks() {
        IMyArrayList<Book> borrowedBooks = libraryService.getBooksOfUser(currentUser.getId());
        if (borrowedBooks.size() == 0) {
            System.out.println("Вы не взяли ни одной книги.");
            return;
        }
        System.out.println("Книги, взятые вами:");
        for (int i = 0; i < borrowedBooks.size(); i++) {
            Book book = borrowedBooks.get(i);
            System.out.println((i + 1) + ". " + book.getTitle() + " - " + book.getAuthor() + " (" + book.getPublicationYear() + ")");
        }
    }


    /**
     *  работа с книгами для библиотекаря - доп методы
     */
    private void addBook(Scanner scanner) {
        System.out.println("Введите ID книги:");
        int id = scanner.nextInt();
        scanner.nextLine(); // Пропуск оставшейся части строки после числа
        System.out.println("Введите название книги:");
        String title = scanner.nextLine();
        System.out.println("Введите автора книги:");
        String author = scanner.nextLine();
        System.out.println("Введите жанр книги:");
        String genre = scanner.nextLine();
        System.out.println("Введите год публикации книги:");
        int publicationYear = scanner.nextInt();
        scanner.nextLine(); // Пропуск оставшейся части строки после числа

        Book book = new Book(id, title, author, genre, publicationYear);
        libraryService.addBook(book);
        System.out.println("Книга успешно добавлена!");
    }

    private void editBookInfo(Scanner scanner) {
        System.out.println("Введите ID книги, информацию о которой вы хотите изменить:");
        int id = scanner.nextInt();
        scanner.nextLine(); // Пропуск оставшейся части строки после числа

        // Попытка найти книгу в системе
        Book book = libraryService.getBookById(id);
        if (book == null) {
            System.out.println("Книга с таким ID не найдена.");
            return;
        }

        // Показать текущую информацию о книге
        System.out.println("Текущая информация о книге:");
        System.out.println("ID: " + book.getId());
        System.out.println("Название: " + book.getTitle());
        System.out.println("Автор: " + book.getAuthor());
        System.out.println("Жанр: " + book.getGenre());
        System.out.println("Год публикации: " + book.getPublicationYear());
        System.out.println();

        // Запрашиваем у пользователя новую информацию о книге
        System.out.println("Введите новое название книги (или нажмите Enter, чтобы оставить текущее):");
        String title = scanner.nextLine();
        if (!title.isEmpty()) {
            book.setTitle(title);
        }

        System.out.println("Введите нового автора книги (или нажмите Enter, чтобы оставить текущего):");
        String author = scanner.nextLine();
        if (!author.isEmpty()) {
            book.setAuthor(author);
        }

        System.out.println("Введите новый жанр книги (или нажмите Enter, чтобы оставить текущий):");
        String genre = scanner.nextLine();
        if (!genre.isEmpty()) {
            book.setGenre(genre);
        }

        System.out.println("Введите новый год публикации книги (или 0, чтобы оставить текущий):");
        int publicationYear = scanner.nextInt();
        if (publicationYear != 0) {
            book.setPublicationYear(publicationYear);
        }

        // Обновление информации о книге в системе
        libraryService.editBookInfo(book);
        System.out.println("Информация о книге успешно обновлена!");
    }

    private void deleteBook(Scanner scanner) {
        System.out.println("Введите ID книги, которую вы хотите удалить:");
        int id = scanner.nextInt();
        scanner.nextLine(); // Пропуск оставшейся части строки после числа

        // Попытка найти книгу в системе
        Book book = libraryService.getBookById(id);
        if (book == null) {
            System.out.println("Книга с таким ID не найдена.");
            return;
        }

        // Показать информацию о книге
        System.out.println("Информация о книге:");
        System.out.println("ID: " + book.getId());
        System.out.println("Название: " + book.getTitle());
        System.out.println("Автор: " + book.getAuthor());
        System.out.println("Жанр: " + book.getGenre());
        System.out.println("Год публикации: " + book.getPublicationYear());
        System.out.println();

        // Запрос подтверждения удаления
        System.out.println("Вы действительно хотите удалить эту книгу? (да/нет)");
        String confirmation = scanner.nextLine().toLowerCase();

        if (confirmation.equals("да")) {
            libraryService.deleteBook(book.getId());
            System.out.println("Книга успешно удалена!");
        } else {
            System.out.println("Удаление отменено.");
        }
    }





}
