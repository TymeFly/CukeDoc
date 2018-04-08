package cucumber.doc.exception;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link CukeDocException}
 */
public class CukeDocExceptionTest {
    /**
     * Unit test {@link CukeDocException#CukeDocException(String)}
     */
    @Test
    public void test_SimpleConstructor() {
        CukeDocException actual = new CukeDocException("Hello");

        Assert.assertEquals("Unexpected Message", "Hello", actual.getMessage());
        Assert.assertEquals("Unexpected cause", null, actual.getCause());
    }


    /**
     * Unit test {@link CukeDocException#CukeDocException(String)}
     */
    @Test
    public void test_FormattedConstructor() {
        CukeDocException actual = new CukeDocException("Hello %s : %d", "World", 2);

        Assert.assertEquals("Unexpected Message", "Hello World : 2", actual.getMessage());
        Assert.assertEquals("Unexpected cause", null, actual.getCause());
    }


    /**
     * Unit test {@link CukeDocException#CukeDocException(String)}
     */
    @Test
    public void test_WrappedConstructor() {
        IOException cause = new IOException();
        CukeDocException actual = new CukeDocException("Hello", cause);

        Assert.assertEquals("Unexpected Message", "Hello", actual.getMessage());
        Assert.assertEquals("Unexpected cause", cause, actual.getCause());
    }
}