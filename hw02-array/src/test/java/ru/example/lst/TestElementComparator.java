package ru.example.lst;

import java.util.Comparator;

public class TestElementComparator implements Comparator<TestElement> {

    public static TestElementComparator testElementComparator = new TestElementComparator();

    @Override
    public int compare(TestElement o1, TestElement o2) {
        if ((o1 == null) || (o2 == null)) {
            return -1;
        }
        return  o1.getValue().compareTo(o2.getValue());
    }
}
