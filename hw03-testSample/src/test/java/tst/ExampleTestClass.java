package tst;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.example.testlib.Before;
import ru.example.testlib.Test;
import ru.example.testlib.TestRunner;

public class ExampleTestClass {
    Logger log = LoggerFactory.getLogger(TestRunner.class);
    private String state = "";

    @Before
    void init() {
         log.info("init method");
         assert !state.isEmpty();
         state = "init";
    }

    @Test
    void runTest() throws Exception{
        Thread.sleep(10000);
        log.info("test method");
        assert !state.equals("init");
    }

}
