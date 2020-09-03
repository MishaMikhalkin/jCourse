package check;

import ru.example.testlib.TestRunner;

import java.io.File;
import java.io.IOException;

public class Tester {

    private static File TMP_FILE;

    public static void init() {
        try {
            TMP_FILE = File.createTempFile("test_", "_test.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        init();
        TestRunner.main(new String[]{"tst.ExampleTestClass", TMP_FILE.getAbsolutePath()});
    }

}
