package ru.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HelloOtusTest {

    @Test
    public void countArgs() {
        String[] testArgs = {"1"};
        assertEquals(1, HelloOtus.countArgs(testArgs));
    }
}