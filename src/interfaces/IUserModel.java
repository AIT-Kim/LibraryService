package interfaces;


import model.Role;

public interface IUserModel {

    int getId();
    String getUsername();
    void setUsername(String username);
    String getPassword();
    void setPassword(String password);
    Role getRole();
    void setRole(Role role);
}

