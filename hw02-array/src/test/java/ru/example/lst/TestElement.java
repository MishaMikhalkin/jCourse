package ru.example.lst;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
class TestElement implements Comparable<TestElement> {
    private String value;

    @Override
    public int compareTo(TestElement o) {
        if (o == null) {
            return -1;
        }
        return this.getValue().compareTo(o.getValue());
    }
}