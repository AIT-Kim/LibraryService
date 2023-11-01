package testdata;

import lib.IMyArrayList;
import lib.MyArrayList;
import model.Role;
import model.User;

import java.security.SecureRandom;

public class UserGenerator {
    public UserGenerator() {

    }

    private static final SecureRandom random = new SecureRandom();
    private static final String[] FIRST_NAMES = {"Alex", "Jordan", "Taylor", "Morgan", "Casey", "Jamie", "Dakota"};
    private static final String[] LAST_NAMES = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Miller", "Davis"};
    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+[]{}|;:'\",.<>?";
    private static final String ALL_CHARACTERS = LETTERS + LETTERS.toUpperCase() + NUMBERS + SPECIAL_CHARACTERS;

    public static String generateUsername() {
        String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
        String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
        String number = String.valueOf(random.nextInt(100));
        return (firstName + lastName + number).toLowerCase();
    }

    public static String generatePassword(int length) {
        return generateRandomString(LETTERS, 1) +
                generateRandomString(LETTERS.toUpperCase(), 1) +
                generateRandomString(NUMBERS, 1) +
                generateRandomString(SPECIAL_CHARACTERS, 1) +
                generateRandomString(ALL_CHARACTERS, length - 4);
    }

    private static String generateRandomString(String characters, int length) {
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            result.append(characters.charAt(index));
        }
        return result.toString();
    }

    public IMyArrayList<User> createStaticUsers() {
        var users = new MyArrayList<User>();
        createStaticSystemUsers(users);
        createStaticReaderUsers(users);
        return users;
    }
    private void createStaticSystemUsers(IMyArrayList<User> users) {
        User admin = new User("admin", "admin", Role.ADMIN);
        User librarian = new User("lib", "lib", Role.LIBRARIAN);
        users.add(admin);
        users.add(librarian);
    }

    private void createStaticReaderUsers(IMyArrayList<User> users){
        for (int i = 0; i < 30; i++) {
            String username = generateUsername();
            String password = generatePassword(8);
            User user = new User(username, password, Role.READER);
            users.add(user);
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 30; i++) {
            String username = generateUsername();
            String password = generatePassword(8);
            System.out.println("Username: " + username + ", Password: " + password);
        }
    }
}
