package ru.example.testlib;

import java.lang.annotation.Annotation;

enum TestAnnotations {
    BEFORE(Before.class),
    AFTER(After.class);

    private final Class<? extends Annotation> klassName;

    TestAnnotations(Class<? extends Annotation> cls) {
        klassName = cls;
    }

    public Class<? extends Annotation> getClassName() {
        return klassName;
    }

    @Override
    public String toString() {
        return "TestAnnotations{" +
                "klassName=" + klassName +
                '}';
    }
}