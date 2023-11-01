import view.*;
import repository.*;

public class Main {

    public static void main(String[] args) {
        var bookRepository = new BookRepository();
        var userRepository = new UserRepository();

        var program = new LibraryConsoleApplication(bookRepository, userRepository);
        program.GenerateData();
        program.start();
    }
}
