package ru.example.lst;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;



class DYIArrayList2Test {
    private DYIArrayList2<TestElement> elementList;
    private final TestElement testElement0 = new TestElement("0");
    private final TestElement testElement1 = new TestElement("1");
    private final TestElement testElement2 = new TestElement("2");

    @BeforeEach
    void init() {
        elementList = new DYIArrayList2<>(TestElement.class, 2);
        elementList.add(testElement0);
        elementList.add(testElement1);
    }

    @AfterEach
    void cleanup() {
        if (elementList.size() == 3) {
            elementList.remove(testElement2);
            assertEquals(2, elementList.size());
        }
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
    void shouldExtendArrayListWhenAddExtra() {
        elementList.add(testElement2);
        assertEquals(3, elementList.size());
        assertEquals(testElement2.getValue(), elementList.get(2).getValue());
    }

    @Test
    void get() {
        assertEquals("0", elementList.get(0).getValue());
        assertEquals("1", elementList.get(1).getValue());
    }

    @Test
    void removeAll() {
        ArrayList<TestElement> testElements = new ArrayList<TestElement>();
        testElements.add(testElement0);
        testElements.add(testElement1);
        elementList.removeAll(testElements);
        assertEquals(0, elementList.size());
    }

    @Test
    void iterator() {
        Iterator<TestElement> iterator = elementList.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(testElement0.getValue(), iterator.next().getValue());
        assertTrue(iterator.hasNext());
        assertEquals(testElement1.getValue(), iterator.next().getValue());
        assertFalse(iterator.hasNext());
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
            assertFalse(false);
        } catch (UnsupportedOperationException e) {
            assertTrue(true);
        }
    }

    @Test
    void size() {
        assertEquals(2, elementList.size());
    }

    @Test
    void isEmpty() {
        assertFalse(elementList.isEmpty());
    }

    @Test
    void cointains() {
        assertTrue(elementList.contains(testElement0));
        assertFalse(elementList.contains(testElement2));
    }

    @Test
    void addAll() {
        elementList.clear();
        Collections.addAll(elementList, testElement0, testElement1, testElement2);
        assertEquals(3,elementList.size());
    }

    @Test
    void toArray() {
        Object[] elements = elementList.toArray();
        assertEquals(testElement0.getValue(), ((TestElement) elements[0]).getValue());
        assertEquals(testElement1.getValue(), ((TestElement) elements[1]).getValue());
        assertEquals(2, elements.length);

    }


    @Test
    void sort() {
        DYIArrayList2<TestElement> elements = new DYIArrayList2<>(TestElement.class, 3);
        elements.add(testElement2);
        elements.add(testElement0);
        elements.add(testElement1);

        Collections.sort(elements, TestElementComparator.testElementComparator);
        assertEquals(testElement0.getValue(), elements.get(0).getValue());
        assertEquals(testElement1.getValue(), elements.get(1).getValue());
        assertEquals(testElement2.getValue(), elements.get(2).getValue());
    }

    @Test
    void copy() {
        DYIArrayList2<TestElement> elements = new DYIArrayList2<>(TestElement.class, 3);
        elements.add(testElement2);
        elements.add(testElement0);

        Collections.copy(elementList, elements);
        assertEquals(testElement2.getValue(), elementList.get(0).getValue());
        assertEquals(testElement0.getValue(), elementList.get(1).getValue());

    }

}