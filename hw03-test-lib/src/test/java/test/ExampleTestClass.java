package test;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.example.testlib.Before;
import ru.example.testlib.Test;


public class ExampleTestClass {
    Logger log = LoggerFactory.getLogger(ExampleTestClass.class);


    @Before
    public void init() {
         log.info("init");
    }

    @Test
    public void runTest() throws Exception {
        log.info("test");
    }

}
