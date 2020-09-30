package ru.example.testlib;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class TestService {
    Logger log = LoggerFactory.getLogger(TestService.class);

    public TestService() { }

    private Path getOutputFile(String output) {
        return Paths.get(output);
    }

    public long test(String filename, String output) throws ClassNotFoundException {
        Class<?> testClass = getClass(filename);
        List<Method> methods = getTestMethods(filename);
        Path out = getOutputFile(output);

        List<TestWrapperImpl> testWrappers = methods
                .stream()
                .map(method -> generateTestWrapper(testClass, method, out))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        try {
            List<Future<Integer>> results = executorService.invokeAll(testWrappers);
            return (int) results
                    .stream()
                    .map(element -> { return element.isCancelled() || !element.isDone();})
                    .count();
        } catch (InterruptedException e) {
            log.error("Thread interrupted: " + e);
            return 1;
        }
    }

    private Class<?> getClass(String filename) throws ClassNotFoundException {
        return this.getClass().getClassLoader().loadClass(filename);
    }

    private List<Method> getMethods(Class<?> testClass) {
        return List.of(testClass.getDeclaredMethods());
    }

    private List<Method> getTestMethods(String filename) throws ClassNotFoundException {
        return getMethods(getClass(filename))
                .stream()
                .filter(method -> method.isAnnotationPresent(Test.class))
                .collect(Collectors.toList());
    }

    private ExecutorService pool = Executors.newFixedThreadPool(2);

    private TestWrapperImpl generateTestWrapper(Class<?> testClass, Method testMethod, Path outputFile) {
        try {
            return new TestWrapperImpl(testClass, testMethod, outputFile);
        } catch (Exception e) {
            log.info("Skipping test: " + testMethod.getName());
        };
        return null;
    }


}
