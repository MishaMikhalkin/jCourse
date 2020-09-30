package ru.example.testlib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.concurrent.Callable;


class TestWrapperImpl implements Callable<Integer> {
    Logger logger = LoggerFactory.getLogger(TestWrapperImpl.class);

    private final Class<?> testClass;
    private final Object testObject;
    private final Method before;
    private final Method testMethod;
    private final Method after;
    private final Path out;

    public TestWrapperImpl(Class<?> testClass, Method testMethod, Path outputFile) throws Exception{
        if (testClass == null) {
            throw new Exception("test class is not set");
        }
        this.testClass = testClass;
        testObject = getTestObject();
        if (testObject == null) {
            throw new Exception("Cannot instantiate test Object");
        }

        this.before = getMethodByAnnotation(TestAnnotations.BEFORE);
        this.testMethod = testMethod;
        this.after = getMethodByAnnotation(TestAnnotations.AFTER);
        this.out = outputFile;

        if (testMethod == null) {
            throw new Exception("no method to test");
        }
    }

    private Constructor<?> getConstructor() {
        Constructor<?>[] constructors = testClass.getDeclaredConstructors();
        Constructor<?> constructor = null;
        for (Constructor<?> value : constructors) {
            constructor = value;
            if (constructor.getGenericParameterTypes().length == 0) {
                break;
            }
        }
        return constructor;
    }

    private Object getTestObject() {
       Constructor<?> constructor = getConstructor();
       Object testObject = null;
       try {
           try {
               testObject = constructor.newInstance();
           } catch (IllegalAccessException e) {
               Files.writeString(out,
                       String.format("--> method cannot be called: %s \n", e.getMessage()),
                       StandardOpenOption.APPEND);
           } catch (InstantiationException e) {
               Files.writeString(out,
                       String.format("--> method cannot create object: %s \n", e.getMessage()),
                       StandardOpenOption.APPEND);
           } catch (InvocationTargetException e) {
               Files.writeString(out,
                       String.format("--> method cannot invoke: %s \n", e.getMessage()),
                       StandardOpenOption.APPEND);
           }
       } catch (IOException e) {
           logger.error("Cannot write to file {}", out, e);
       }
       return testObject;
    }

    private Method getMethodByAnnotation(TestAnnotations testAnnotation) {
        return List.of(testClass.getDeclaredMethods())
                .stream()
                .filter(method -> method.isAnnotationPresent(testAnnotation.getClassName())).findFirst().orElse(null);
    }

    private void callMethod(Method method) {
        try {
            try {
                if (method != null) {
                    Files.writeString(
                            out,
                            String.format("--> preparing method invoke: %s in class %s \n", method.getName(), testClass),
                            StandardOpenOption.APPEND);

                    method.setAccessible(true);
                    method.invoke(testObject);

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