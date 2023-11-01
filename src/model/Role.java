package model;

public enum Role {
    ADMIN("Администратор"),
    LIBRARIAN("Библиотекарь"),
    READER("Читатель");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
