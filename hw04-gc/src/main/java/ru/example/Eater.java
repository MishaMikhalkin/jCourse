package ru.example;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Eater implements Runnable{
    public static int NUM_THREADS = 5;

    private static final Map<MemoryEater, String> map = new HashMap<>();

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);

        for (int i =0 ; i < NUM_THREADS; i++) {
            executorService.submit(new Eater());
        }

    }


    public void run() {
        while (true) {
            map.put(new MemoryEater(UUID.randomUUID().toString()), UUID.randomUUID().toString());
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
