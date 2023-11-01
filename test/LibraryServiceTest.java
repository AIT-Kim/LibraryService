import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;

import testdata.BookGenerator;
import interfaces.*;
import lib.IMyArrayList;
import service.*;
import model.*;



class LibraryServiceTest {
    private static IBookRepository bookRepository;
    private static IUserRepository userRepository;
    private static LibraryService libraryService;

    static {
        bookRepository = mock(IBookRepository.class);
        userRepository = mock(IUserRepository.class);
        libraryService = new LibraryService(bookRepository, userRepository);
    }

    static Stream<Book> validBooks() {
        var bookGenerator = new BookGenerator();
        var books = bookGenerator.createLibraryFromCsvFile("books.csv");
        System.out.println("загружено книг:" + books.size());
        return books.stream();

//        return Stream.of(
//                new Book(1, "Test Book 1", "Test Author 1", "Test Genre 1",2021),
//                new Book(2, "Test Book 2", "Test Author 2", "Test Genre 1",2022)
//        );
    }



    @ParameterizedTest
    @MethodSource("validBooks")
    void addBookShouldCallAddBookOnRepository(Book book) {
        // Настройка
        when(bookRepository.addBook(book)).thenReturn(0);

        // Действие
        int result = libraryService.addBook(book);

        // Проверка
        assertEquals(0, result, "The addBook method should return 0 on success");
        verify(bookRepository).addBook(book);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2})
    void deleteBookShouldReturnZeroWhenBookExists(int bookId) {
        IBookRepository bookRepository = mock(IBookRepository.class);
        IUserRepository userRepository = mock(IUserRepository.class);
        LibraryService libraryService = new LibraryService(bookRepository, userRepository);

        when(bookRepository.deleteBook(bookId)).thenReturn(0);

        int result = libraryService.deleteBook(bookId);

        assertEquals(0, result);
        verify(bookRepository).deleteBook(bookId);
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 4})
    void deleteBookShouldReturnMinusOneWhenBookDoesNotExist(int bookId) {
        IBookRepository bookRepository = mock(IBookRepository.class);
        IUserRepository userRepository = mock(IUserRepository.class);
        LibraryService libraryService = new LibraryService(bookRepository, userRepository);

        when(bookRepository.deleteBook(bookId)).thenReturn(-1);

        int result = libraryService.deleteBook(bookId);

        assertEquals(-1, result);
        verify(bookRepository).deleteBook(bookId);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void deleteBookShouldHandleInvalidBookIdGracefully(int bookId) {
        IBookRepository bookRepository = mock(IBookRepository.class);
        IUserRepository userRepository = mock(IUserRepository.class);
        LibraryService libraryService = new LibraryService(bookRepository, userRepository);

        when(bookRepository.deleteBook(bookId)).thenReturn(-2);

        int result = libraryService.deleteBook(bookId);

        assertEquals(-2, result);
        verify(bookRepository).deleteBook(bookId);
    }
}
