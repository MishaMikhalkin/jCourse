package ru.example;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelloOtus {

    public static int countArgs(String[] args) {
        return Lists.newArrayList(args).size();
    }

    public static void main(String[] args) {
        log.info("Args size is:  {} ", countArgs(args));
    }
}
