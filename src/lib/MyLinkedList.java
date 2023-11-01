package lib;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;
import java.util.function.Predicate;

public class MyLinkedList<T> implements IMyLinkedList<T>, Iterable<T>, Cloneable {
    private Node<T> head;
    private int size;

    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
        }
    }

    // Метод для создания потока из элементов списка
    public Stream<T> stream() {
        MyArrayList<T> list = new MyArrayList<>();
        Node<T> current = head;
        while (current != null) {
            list.add(current.data);
            current = current.next;
        }
        return list.stream();
    }

    @Override
    public void add(T element) {
        Node<T> newNode = new Node<>(element);
        if (head == null) {
            head = newNode;
        } else {
            Node<T> current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    @Override
    public void remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        if (index == 0) {
            head = head.next;
        } else {
            Node<T> current = head;
            for (int i = 0; i < index - 1; i++) {
                current = current.next;
            }
            current.next = current.next.next;
        }
        size--;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    // метод для добавления всех элементов из другого списка
    public void addAll(MyLinkedList<T> other) {
        Node<T> current = other.head;
        while (current != null) {
            add(current.data);
            current = current.next;
        }
    }

    @Override
    // метод для удаления элементов, удовлетворяющих предикату
    public boolean removeIf(Predicate<T> filter) {
        Node<T> current = head;
        Node<T> prev = null;
        boolean isRemoved = false;
        while (current != null) {
            if (filter.test(current.data)) {
                if (prev == null) {
                    head = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                isRemoved = true;
            } else {
                prev = current;
            }
            current = current.next;
        }
        return isRemoved;
    }


    // Метод для клонирования списка
    @Override
    public MyLinkedList<T> clone() {
        try {
            @SuppressWarnings("unchecked")
            MyLinkedList<T> clone = (MyLinkedList<T>) super.clone();
            clone.head = null;
            clone.size = 0;
            Node<T> current = this.head;
            while (current != null) {
                clone.add(current.data);
                current = current.next;
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Не может произойти
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T data = current.data;
                current = current.next;
                return data;
            }
        };
    }

}