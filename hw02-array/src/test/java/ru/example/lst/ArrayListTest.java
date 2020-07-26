package ru.example.lst;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


class ArrayListTest {
    private ArrayList<TestElement> elementList;

    @BeforeEach
    void init() {
        elementList = new ArrayList<>(16);
        elementList.add(0,new TestElement("0"));
        elementList.add(1,new TestElement("1"));
    }

    @Test
    void checkOutOfBound() {
        try {
            elementList.get(2);
            assertFalse(false);
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        } catch (Exception e) {
           assertFalse(false);
        }
    }

    @Test
    void get() {
        assertEquals("0", elementList.get(0).getValue());
        assertEquals("1", elementList.get(1).getValue());
    }

    @Test
    void forEach() {
        elementList.forEach(testElement -> testElement.setValue(testElement.getValue() + "+"));
        assertEquals("0+", elementList.get(0).getValue());
        assertEquals("1+", elementList.get(1).getValue());
        elementList.forEach(testElement -> testElement.setValue(testElement.getValue().replace("+", "")));
    }

    @Test
    void spliterator() {
    }

    @Test
    void stream() {
        assertEquals(2, elementList.stream().count());
    }

    @Test
    void parallelStream() {
        try {
            elementList.parallelStream().findFirst();
            assertTrue(true);
        } catch (UnsupportedOperationException e) {
            assertTrue(false);
        }
    }

    @Test
    void size() {
        assertEquals(2, elementList.size());
    }

    @Test
    void toArray() {
        TestElement[] check = new TestElement[1];
        check[0] = new TestElement("0");
        TestElement[] result = elementList.stream().filter(e -> e.getValue().equals("0")).toArray(TestElement[]::new);
        assertArrayEquals(check, result);
    }

    @Test
    void removeIf() {
        elementList.removeIf(element -> element.getValue().equals("0"));
        assertEquals("1", elementList.get(0).getValue());
        assertEquals(1, elementList.size());
    }
}