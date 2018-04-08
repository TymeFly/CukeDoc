package cucumber.doc.util;

import cucumber.doc.config.Config;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

/**
 * Unit test for {@link Trace}
 */
public class TraceTest {
    @Rule
    public SystemOutRule standardOut = new SystemOutRule().mute().enableLog();        // turn off logging to console


    @Before
    public void setUp() {
        standardOut.clearLog();
    }


    /**
     * Unit test {@link Trace#message(String)}
     */
    @Test
    public void test_message_Simple_disabled() {
        Config.getInstance().setTrace(false);

        Trace.message("Hello World");

        Assert.assertEquals("Unexpected message written", "", standardOut.getLog());
    }


    /**
     * Unit test {@link Trace#message(String)}
     */
    @Test
    public void test_message_Simple_enabled() {
        Config.getInstance().setTrace(true);

        Trace.message("Hello World");

        Assert.assertEquals("Unexpected message written", "Hello World" + System.lineSeparator(), standardOut.getLog());
    }


    /**
     * Unit test {@link Trace#message(String, Object...)}
     */
    @Test
    public void test_message_formatted_disabled() {
        Config.getInstance().setTrace(false);

        Trace.message("%s = %s", "value", 2);

        Assert.assertEquals("Unexpected message written", "", standardOut.getLog());
    }


    /**
     * Unit test {@link Trace#message(String, Object...)}
     */
    @Test
    public void test_message_formatted_enabled() {
        Config.getInstance().setTrace(true);

        Trace.message("%s = %s", "value", 2);

        Assert.assertEquals("Unexpected message written", "value = 2" + System.lineSeparator(), standardOut.getLog());
    }
}