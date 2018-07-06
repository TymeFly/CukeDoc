package io.cucumber.doc.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Unit test for {@link Preconditions}
 */
public class PreconditionsTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    /**
     * Unit test {@link Preconditions#checkNotNull(Object, String, Object...)}
     */
    @Test
    public void test_checkNotNull() {
        Preconditions.checkNotNull(new Object(), "Hello %d %s", 2, "you");
    }

    /**
     * Unit test {@link Preconditions#checkNotNull(Object, String, Object...)}
     */
    @Test
    public void test_checkNotNull_Null() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Hello 2 you");

        Preconditions.checkNotNull(null, "Hello %d %s", 2, "you");
    }


    /**
     * Unit test {@link Preconditions#checkHasText(String, String, Object...)}
     */
    @Test
    public void test_checkHasText_withText() {
        Preconditions.checkHasText("Test String", "Hello %d %s", 2, "you");
    }


    /**
     * Unit test {@link Preconditions#checkHasText(String, String, Object...)}
     */
    @Test
    public void test_checkHasText_null() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Hello 2 you");

        Preconditions.checkHasText(null, "Hello %d %s", 2, "you");
    }


    /**
     * Unit test {@link Preconditions#checkHasText(String, String, Object...)}
     */
    @Test
    public void test_checkHasText_empty() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Hello 2 you");

        Preconditions.checkHasText("", "Hello %d %s", 2, "you");
    }


    /**
     * Unit test {@link Preconditions#checkState(boolean, String, Object...)}
     */
    @Test
    public void test_checkState_valid() {
        Preconditions.checkState(true, "Hello %d %s", 2, "you");
    }


    /**
     * Unit test {@link Preconditions#checkState(boolean, String, Object...)}
     */
    @Test
    public void test_checkState_invalid() {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Hello 2 you");

        Preconditions.checkState(false, "Hello %d %s", 2, "you");
    }


    /**
     * Unit test {@link Preconditions#checkArgument(boolean, String, Object...)}
     */
    @Test
    public void test_checkArgument_valid() {
        Preconditions.checkArgument(true, "Hello %d %s", 2, "you");
    }


    /**
     * Unit test {@link Preconditions#checkArgument(boolean, String, Object...)}     */
    @Test
    public void test_checkArgument_invalid() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Hello 2 you");

        Preconditions.checkArgument(false, "Hello %d %s", 2, "you");
    }
}