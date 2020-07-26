package ru.example.lst;


import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class DYIArrayList<E> extends AbstractList<E> {

    private int insertedElements;
    private E[] array;

    private static <E> E[] createArrayWithSize(Class<E> c, int listSize) {
        return (E[]) Array.newInstance(c,listSize);
    }

    public DYIArrayList(Class<E> c, int listSize){
        @SuppressWarnings("unchecked")
        final E[] a = (E[]) Array.newInstance(c,listSize);
        this.array = a;
        insertedElements = 0;
    }

    @Override
    public boolean add(E element) {
        int update = insertedElements + 1;
        if (update < array.length) {
            array[update] = element;
            insertedElements = insertedElements + 1;
            return true;
        }
        //TODO: extend array
        return false;
    }

    @Override
    public E get(int index) {
        return array[index];
    }

    @Override
    public E set(int index, E element) {
        E old = array[index];
        array[index] = element;
        insertedElements++;
        return old;
    }

    //TODO: check for concurrent modifications
    @Override
    public void forEach(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        for (E e : array) {
            if (e != null) {
                action.accept(e);
            }
        }
    }

    @Override
    public Spliterator<E> spliterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Stream<E> stream() {
        return Arrays.stream(array).filter(x -> x != null);
    }

    @Override
    public Stream<E> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }

    @Override
    public int size() {
        return insertedElements;
    }

    @Override
    public <E> E[] toArray(IntFunction<E[]> generator) {
        return toArray(generator.apply(0));
    }


    /*
      1 2 3 4 5 6 7
      2 1 1 1 2 1 2
      2 \ \ \
      2 2
     */
    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        E[] clonedArray = array.clone();
        int newInsertedIndex = 0;

        for (int i = 0 ; i < insertedElements; i++) {
            if (!filter.test(array[i])) {
                clonedArray[newInsertedIndex] = array[i];
                newInsertedIndex++;
            }
        }
        array = clonedArray;
        insertedElements = newInsertedIndex;
        return removed;
    }

    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        Objects.requireNonNull(operator);
        for (int i = 0 ; i < array.length; i++) {
            operator.apply(array[i++]);
        }
    }

    @Override
    public void sort(Comparator<? super E> c) {
        Arrays.sort(array, c);
    }
}
