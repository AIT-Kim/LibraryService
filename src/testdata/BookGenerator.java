package testdata;


import lib.IMyArrayList;
import lib.MyArrayList;
import model.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BookGenerator {

    public BookGenerator() {

    }


    public IMyArrayList<Book> createLibraryFromCsvFile(String filePath) {
        var books = new MyArrayList<Book>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            if (line == null) {
                System.out.println("Файл пуст");
                return books;
            }
            Map<String, Integer> headerMap = parseHeader(line);
            while ((line = br.readLine()) != null) {
                Map<String, String> bookData = parseLine(line, headerMap);

                String title = bookData.getOrDefault("title", "");
                String author = bookData.getOrDefault("author", "");
                String genre = bookData.getOrDefault("genre", "");
                int publicationYear = parseYear(bookData.getOrDefault("publicationYear", "0"));

                Book book = new Book(0, title, author, genre, publicationYear);
                books.add(book);
            }
            System.out.println("Библиотека успешно заполнена из файла " + filePath);
        } catch (IOException e) {
            System.out.println("Произошла ошибка при чтении файла: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Произошла ошибка при обработке данных: " + e.getMessage());
        }
        return books;
    }

    private Map<String, Integer> parseHeader(String line) {
        Map<String, Integer> headerMap = new HashMap<>();
        List<String> headers = parseLine(line);
        for (int i = 0; i < headers.size(); i++) {
            headerMap.put(headers.get(i), i);
        }
        return headerMap;
    }

    private Map<String, String> parseLine(String line, Map<String, Integer> headerMap) {
        Map<String, String> bookData = new HashMap<>();
        List<String> values = parseLine(line);
        for (Map.Entry<String, Integer> entry : headerMap.entrySet()) {
            String key = entry.getKey();
            int index = entry.getValue();
            if (index < values.size()) {
                bookData.put(key, values.get(index));
            }
        }
        return bookData;
    }

    private List<String> parseLine(String line) {
        List<String> values = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder buffer = new StringBuilder();
        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes; // переключаем состояние
            } else if (c == ',' && !inQuotes) {
                values.add(buffer.toString());
                buffer.setLength(0); // очищаем буфер
            } else {
                buffer.append(c);
            }
        }
        values.add(buffer.toString()); // добавляем последнее значение
        return values;
    }

    private int parseYear(String yearString) {
        try {
            return Integer.parseInt(yearString);
        } catch (NumberFormatException e) {
            System.out.println("Не удалось преобразовать строку в год: " + yearString);
            return 0;
        }
    }
}
