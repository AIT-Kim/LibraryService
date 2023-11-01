package interfaces;

import model.User;
import lib.*;

public interface IUserRepository {

    // Добавление пользователя в хранилище
    void addUser(User user);

    // Получение пользователя по идентификатору
    User getUserById(int userId);

    // Получение пользователя по имени пользователя
    User getUserByUsername(String username);

    // Получение списка всех пользователей
    IMyArrayList<User> getAllUsers();

    // Обновление информации о пользователе
    void updateUser(User user);

    // Удаление пользователя из хранилища
    void deleteUser(User user);

    void deleteUser(int userId);

    // Авторизация пользователя
    User loginUser(String username, String password);

    // Регистрация нового пользователя
    User registerUser(String username, String password);

    // Получение списка пользователей по роли
    IMyArrayList<User> getUsersByRole(String role);
}
