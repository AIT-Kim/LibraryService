package lib;

import java.util.function.Predicate;

public interface IMyLinkedList<T> extends Iterable<T> {
    void add(T element);
    T get(int index);
    void remove(int index);
    int size();
    void addAll(MyLinkedList<T> other);
    boolean removeIf(Predicate<T> filter);

}