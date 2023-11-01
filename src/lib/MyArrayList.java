package lib;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;


import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayList<T> implements IMyArrayList<T>, Cloneable, Iterable<T> {
    private static final int DEFAULT_CAPACITY = 10;
    private int size;
    private Object[] elements;

    public MyArrayList() {
        elements = new Object[DEFAULT_CAPACITY];
    }

    public Stream<T> stream() {
        return IntStream.range(0, size).mapToObj(this::get);
    }

    @Override
    public void add(T value) {
        ensureCapacity();
        elements[size++] = value;
    }

    public void addAll(MyArrayList<T> other) {
        for (int i = 0; i < other.size(); i++) {
            this.add(other.get(i));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(int index) {
        checkIndex(index);
        return (T) elements[index];
    }

    @Override
    public void set(int index, T value) {
        checkIndex(index);
        elements[index] = value;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null; // Clear to let GC do its work
        }
        size = 0;
    }

    @Override
    public void remove(int index) {
        checkIndex(index);
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        elements[--size] = null; // Clear to let GC do its work
    }

    @Override
    @SuppressWarnings("unchecked")
    public T[] toArray() {
        return (T[]) Arrays.copyOf(elements, size);
    }

    @Override
    public List<T> toList() {
        List<T> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            @SuppressWarnings("unchecked")
            T item = (T) elements[i];
            list.add(item);
        }
        return list;
    }

    private void ensureCapacity() {
        if (size == elements.length) {
            int newCapacity = elements.length * 3 / 2 + 1;
            elements = Arrays.copyOf(elements, newCapacity);
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    public void removeIf(Predicate<T> filter) {
        int writeIndex = 0;
        for (int readIndex = 0; readIndex < size; readIndex++) {
            if (!filter.test((T) elements[readIndex])) {
                elements[writeIndex++] = elements[readIndex];
            }
        }
        for (int i = writeIndex; i < size; i++) {
            elements[i] = null; // помогаем сборщику мусора
        }
        size = writeIndex;
    }


    @Override
    public MyArrayList<T> clone() {
        try {
            @SuppressWarnings("unchecked")
            MyArrayList<T> clone = (MyArrayList<T>) super.clone();
            clone.elements = Arrays.copyOf(this.elements, this.size);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Не может произойти
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @SuppressWarnings("unchecked")
            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return (T) elements[currentIndex++];
            }
        };
    }
}



