package ru.example.lst;



import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Consumer;

public class DYIArrayList2<E> implements List<E> {

    private int insertedElements;
    private E[] array;



    public DYIArrayList2(Class<E> c, int listSize){
        @SuppressWarnings("unchecked")
        E[] a = (E[]) Array.newInstance(c, listSize);
        array = a;
        insertedElements = 0;
    }

    @Override
    public int size() {
        return insertedElements;
    }

    @Override
    public boolean isEmpty() {
        return insertedElements == 0;
    }

    @Override
    public boolean contains(Object o) {
        for(int i = 0; i < insertedElements;i++) {
            if (array[i].equals(o)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iter();
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(array, array.length);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean add(E e) {
        if (insertedElements >= array.length) {
            @SuppressWarnings("unchecked")
            E[] extendedArray = (E[]) Array.newInstance(this.array.getClass().getComponentType(), array.length * 2);
            System.arraycopy(array, 0, extendedArray, 0, array.length);
            array = extendedArray;
        }
        array[insertedElements] = e;
        insertedElements++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int i = 0;
        while (i < insertedElements) {
            if (array[i].equals(o)) {
                break;
            }
            i++;
        }

        while (i < insertedElements) {
            array[i] = array[i++];
        }
        insertedElements--;
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        c.forEach(element -> this.remove(element));
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        this.array = (E[]) Array.newInstance(this.array.getClass().getComponentType(), array.length * 2);
        insertedElements = 0;
    }

    @Override
    public E get(int index) {
        if (index < insertedElements) {
            return array[index];
        } else {
            throw new ArrayIndexOutOfBoundsException(String.format("%d in is not inserted yet", index));
        }
    }

    @Override
    public E set(int index, E element) {
        E value = array[index];
        array[index] = element;
        return value;
    }

    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException();

    }

    @Override
    public E remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<E> listIterator() {
        return new Iter();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DYIArrayList2<?> that = (DYIArrayList2<?>) o;
        return insertedElements == that.insertedElements &&
                Arrays.equals(array, that.array);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(insertedElements);
        result = 31 * result + Arrays.hashCode(array);
        return result;
    }

    private class Iter implements Iterator<E>, ListIterator<E> {
        private int cursor;

        Iter() {
            cursor = 0;
        }

        @Override
        public boolean hasNext() {
            return cursor < insertedElements;
        }

        @Override
        public E next() {
            E value = array[cursor];
            if (cursor < array.length) {
                cursor++;
            }
            return value;
        }

        @Override
        public boolean hasPrevious() {
            throw new UnsupportedOperationException();
        }

        @Override
        public E previous() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int nextIndex() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int previousIndex() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(E e) {
            array[cursor-1] = e;
        }

        @Override
        public void add(E e) {
            throw new UnsupportedOperationException();
        }
    }
}
