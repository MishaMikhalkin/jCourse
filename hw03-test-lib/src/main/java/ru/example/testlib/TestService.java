package ru.example.testlib;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class TestService {
    Logger log = LoggerFactory.getLogger(TestService.class);

    public TestService() { }

    public Class<?> getClass(String filename) throws ClassNotFoundException {
        return this.getClass().getClassLoader().loadClass(filename);
    }

    public List<Method> getMethods(Class<?> testClass) {
        return List.of(testClass.getDeclaredMethods());
    }

    public List<Method> getTestMethods(String filename) throws ClassNotFoundException {
        return getMethods(getClass(filename))
                .stream()
                .filter(method -> method.isAnnotationPresent(Test.class))
                .collect(Collectors.toList());
    }

    public Method getBefore(String filename) throws ClassNotFoundException {
        return List.of(getClass(filename).getDeclaredMethods())
                .stream()
                .filter(method -> method.isAnnotationPresent(Before.class)).findFirst().orElse(null);
    }

    public Method getAfter(String filename) throws ClassNotFoundException {
        return List.of(getClass(filename).getMethods())
                .stream()
                .filter(method -> method.isAnnotationPresent(After.class)).findFirst().orElse(null);
    }

    public Path getOutputFile(String output) {
        return Paths.get(output);
    }


    private ExecutorService pool = Executors.newFixedThreadPool(2);
    private CompletableFuture<TestWrapperImpl> getTestWrapper(Class<?> testClass, Method b, Method t, Method a, Path outputFile) {
        return CompletableFuture.supplyAsync(() -> new TestWrapperImpl(testClass, b, t, a, outputFile), pool);
    }



    public long test(String filename, String output) throws ClassNotFoundException {
        Class<?> testClass = getClass(filename);
        Method beforeMethod = getBefore(filename);
        Method afterMethod = getAfter(filename);
        List<Method> methods = getTestMethods(filename);
        Path out = getOutputFile(output);



        List<TestWrapperImpl> testWrappers = methods
                .stream()
                .map(method -> new TestWrapperImpl(testClass, beforeMethod, method, afterMethod, out))
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


}
