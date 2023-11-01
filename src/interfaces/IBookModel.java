package interfaces;


import java.time.LocalDate;

public interface IBookModel {
    int getId();
    void setId(int id);
    String getTitle();
    void setTitle(String title);
    String getAuthor();
    void setAuthor(String author);
    String getGenre();
    void setGenre(String genre);
    int getPublicationYear();
    void setPublicationYear(int publicationYear);

}

