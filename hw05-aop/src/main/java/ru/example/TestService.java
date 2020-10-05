package ru.example;

public class TestService {


    @CustomLog
    public int calculate(int val1, int val2) {
        return val1 * val2;
    }
}
