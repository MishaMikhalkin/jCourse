package ru.example.testlib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Callable;


class TestWrapperImpl implements Callable<Integer> {
    Logger logger = LoggerFactory.getLogger(TestWrapperImpl.class);

    public TestWrapperImpl(Class<?> testClass, Method before, Method testMethod, Method after, Path outputFile) {
        this.testClass = testClass;
        this.before = before;
        this.testMethod = testMethod;
        this.after = after;
        this.out = outputFile;
    }

    private final Class<?> testClass;

    private final Method before;
    private final Method testMethod;
    private final Method after;

    private final Path out;

    private void callMethod(Method method) {
        try {
            try {
                if (method != null) {
                    Files.writeString(
                            out,
                            String.format("--> preparing method invoke: %s in class %s \n", method.getName(), testClass),
                            StandardOpenOption.APPEND);

                    method.setAccessible(true);
                    method.invoke(testClass.newInstance());

                    Files.writeString(
                            out,
                            String.format("--> stopped method invoke: %s in class %s \n", method.getName(), testClass),
                            StandardOpenOption.APPEND);
                }
            } catch (InvocationTargetException e) {
                Files.writeString(
                        out,
                        String.format("--> method got exception: %s \n", e.getMessage()),
                        StandardOpenOption.APPEND);

            } catch (InstantiationException e) {
                logger.error("Error: cannot create test object ", e);
            } catch (IllegalAccessException e) {
                logger.error("Error: cannot invoke method ", e);
            }
        } catch (IOException e) {
            logger.error("Cannot write to file {}", out, e);
        }
    }

    @Override
    public Integer call() {
        try {
            Files.writeString(out, String.format("-> Starting test: %s in class %s \n", testMethod, testClass), StandardOpenOption.APPEND);
            callMethod(before);
            callMethod(testMethod);
            callMethod(after);
            Files.writeString(out, String.format("-> Finished test: %s in class %s \n", testMethod, testClass), StandardOpenOption.APPEND);
        } catch (IOException e) {
            logger.error("Cannot write to file {}", out, e);
            return 1;
        }
        return 0;
    }
}