package repository;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.concurrent.atomic.AtomicInteger;

import interfaces.*;
import model.*;
import lib.*;
import model.Book;

public class BookRepository implements IBookRepository {
    private final AtomicInteger nextBookId = new AtomicInteger(1); // счетчик ID
//    private final ReentrantLock lock = new ReentrantLock();
    public static final int BOOK_NOT_BORROWED = -1;
    private final MyArrayList<Book> books;
    private final MyLinkedList<BorrowedBookInfo> borrowedBooks = new MyLinkedList<>();



    public BookRepository() {
        this.books = new MyArrayList<>();
    }

    @Override
    public synchronized int addBook(Book book) {
        int newId = nextBookId.getAndIncrement();
        book.setId(newId); // присваиваем уникальный ID книге
        books.add(book);
        return 0;
    }


    @Override
    public synchronized Book getBookById(int bookId) {
        return books.stream().filter(book -> book.getId() == bookId).findFirst().orElse(null);
    }

    @Override
    public synchronized IMyArrayList<Book> getAllBooks() {
        return books.clone();
    }

    @Override
    public synchronized void updateBook(Book book) {
        IntStream.range(0, books.size())
                .filter(i -> books.get(i).getId() == book.getId())
                .findFirst()
                .ifPresent(index -> books.set(index, book));
    }

    @Override
    public synchronized void deleteBook(Book book) {
        books.removeIf(b -> b.equals(book));
        borrowedBooks.removeIf(bbi -> bbi.getBookId() == book.getId());
    }

    @Override
    public synchronized int deleteBook(int bookId) {
        if (getBookById(bookId)!=null) {
            books.removeIf(book -> book.getId() == bookId);
            borrowedBooks.removeIf(bbi -> bbi.getBookId() == bookId);
            return 0;
        }
        return -1;
    }


    @Override
    public synchronized IMyArrayList<Book> searchBooksByTitle(String title) {
        return filterBooks(book -> book.getTitle().toLowerCase().contains(title.toLowerCase()));
    }

    @Override
    public synchronized IMyArrayList<Book> getAvailableBooks() {
        MyArrayList<Book> result = new MyArrayList<>();
        for (Book book : books) {
            if (!isBookBorrowed(book.getId())) {
                result.add(book);
            }
        }
        return result;
    }

    private boolean isBookBorrowed(int bookId) {
        for (BorrowedBookInfo info : borrowedBooks) {
            if (info.getBookId() == bookId) {
                return true;
            }
        }
        return false;
    }



    @Override
    public synchronized IMyArrayList<Book> getBorrowedBooks() {
        MyArrayList<Book> result = new MyArrayList<>();
        for (BorrowedBookInfo info : borrowedBooks) {
            Book book = getBookById(info.getBookId());
            if (book != null) {
                result.add(book);
            }
        }
        return result;
    }


    @Override
    public synchronized IMyArrayList<Book> getBooksByAuthor(String author) {
        return filterBooks(book -> book.getAuthor().equalsIgnoreCase(author));
    }


    @Override
    public synchronized int borrowBook(int bookId, int userId) {
        // Проверяем, взята ли уже книга
        for (BorrowedBookInfo bbi : borrowedBooks) {
            if (bbi.getBookId() == bookId) {
                return -1; // Книга уже взята
            }
        }

        // Проверяем, существует ли книга
        Book book = getBookById(bookId);
        if (book == null) {
            return -1; // Книга не найдена
        }

        // Все проверки пройдены, книгу можно взять
        LocalDate now = LocalDate.now();
        borrowedBooks.add(new BorrowedBookInfo(userId, bookId, now));
        return 0;
    }



    @Override
    public synchronized int returnBook(int bookId, int userId) {
        boolean removed = borrowedBooks.removeIf(b -> b.getBookId() == bookId && b.getUserId() == userId);
        return removed ? 0 : -1;
    }



    public synchronized MyLinkedList<BorrowedBookInfo> getBorrowedBooksByUser(int userId) {
        MyLinkedList<BorrowedBookInfo> result = new MyLinkedList<>();
        for (BorrowedBookInfo info : borrowedBooks) {
            if (info.getUserId() == userId) {
                result.add(info);
            }
        }
        return result;
    }

    public synchronized IMyArrayList<Book> getBooksOfUser(int userId) {
        MyArrayList<Book> result = new MyArrayList<>();
        for (BorrowedBookInfo info : borrowedBooks) {
            if (info.getUserId() == userId) {
                Book book = getBookById(info.getBookId());
                if (book != null) {
                    result.add(book);
                }
            }
        }
        return result;
    }


    public synchronized BorrowedBookInfo getBorrowedInfoByBookId(int bookId) {
        for (BorrowedBookInfo info : borrowedBooks) {
            if (info.getBookId() == bookId) {
                return info;
            }
        }
        return null;
    }

    public synchronized void removeBorrowedBooksByUser(int userId) {
        borrowedBooks.removeIf(bbi -> bbi.getUserId() == userId);
    }




    private synchronized IMyArrayList<Book> filterBooks(Predicate<Book> predicate) {
        MyArrayList<Book> result = new MyArrayList<>();
        books.stream().filter(predicate).forEach(result::add);
        return result;
    }
}


