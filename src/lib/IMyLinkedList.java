package lib;

public interface IMyLinkedList<T> extends Iterable<T> {
    void add(T element);
    T get(int index);
    void remove(int index);
    int size();

}