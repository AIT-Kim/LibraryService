package lib;

import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface IMyArrayList<T> extends Iterable<T> {
    void add(T value);
    T get(int index);
    void set(int index, T value);
    int size();
    void clear();
    void remove(int index);
    T[] toArray();
    List<T> toList();
    void removeIf(Predicate<T> filter);
    Iterator<T> iterator();
    Stream<T> stream();
    MyArrayList<T> clone();
}



