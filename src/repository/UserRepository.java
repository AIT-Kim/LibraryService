package repository;


import interfaces.*;
import model.*;
import lib.*;

import java.util.function.Predicate;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class UserRepository implements IUserRepository {
    private final AtomicInteger nextUserId = new AtomicInteger(1);
    private final MyArrayList<User> users;

    public UserRepository() {
        this.users = new MyArrayList<>();
    }

    @Override
    public synchronized void addUser(User user) {
        int newId = nextUserId.getAndIncrement();
        user.setId(newId);
        users.add(user);
    }

    @Override
    public synchronized User getUserById(int userId) {
        return users.stream()
                .filter(user -> user.getId() == userId)
                .findFirst()
                .orElse(null);
    }

    @Override
    public synchronized User getUserByUsername(String username) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    @Override
    public synchronized IMyArrayList<User> getAllUsers() {
        return users.clone();
    }

    @Override
    public synchronized void updateUser(User updatedUser) {
        IntStream.range(0, users.size())
                .filter(i -> users.get(i).getId() == updatedUser.getId())
                .findFirst()
                .ifPresent(index -> users.set(index, updatedUser));
    }

    @Override
    public synchronized void deleteUser(User user) {
        users.removeIf(u -> u.equals(user));
    }

    @Override
    public synchronized void deleteUser(int userId) {
        users.removeIf(user -> user.getId() == userId);
    }

    @Override
    public synchronized User loginUser(String username, String password) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    @Override
    public synchronized User registerUser(String username, String password) {
        User newUser = new User(username, password, Role.READER);
        addUser(newUser);
        return newUser;
    }

    @Override
    public synchronized IMyArrayList<User> getUsersByRole(String role) {
        return filterUsers(user -> user.getRole().getName().equals(role));
    }

    private synchronized IMyArrayList<User> filterUsers(Predicate<User> predicate) {
        MyArrayList<User> result = new MyArrayList<>();
        users.stream().filter(predicate).forEach(result::add);
        return result;
    }
}


