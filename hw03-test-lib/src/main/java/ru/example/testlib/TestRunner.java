package ru.example.testlib;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "dyi tester", mixinStandardHelpOptions = true,
        description = "set class name in parameter")
public class TestRunner implements Callable<Long> {
    Logger log = LoggerFactory.getLogger(TestRunner.class);

    @CommandLine.Parameters(index = "0", description = "test class name")
    private String className;

    @CommandLine.Parameters(index = "1", description = "output file")
    private String output;

    public static void main(String[] args)  {
        int exitCode = new CommandLine(new TestRunner()).execute(args);
        System.exit(exitCode);
    }


    @Override
    public Long call() throws Exception { // your business logic goes here...
            log.info("started {}", className);
            log.info("logging test into {}", output);
            return new TestService().test(className, output);
    }
}
