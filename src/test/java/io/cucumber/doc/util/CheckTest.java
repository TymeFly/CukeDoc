package io.cucumber.doc.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link Check}
 */
public class CheckTest {

    /**
     * Unit test {@link Check#hasText}
     */
    @Test
    public void test_hasText_withText() {
        Assert.assertFalse("Null", Check.hasText(null));
        Assert.assertFalse("Empty String", Check.hasText(""));
        Assert.assertFalse("String of spaces", Check.hasText("   "));
        Assert.assertTrue("With text", Check.hasText("Hello"));
        Assert.assertTrue("With text and spaces", Check.hasText("  Hello  "));
    }


    /**
     * Unit test {@link Check#hasElement}
     */
    @Test
    public void test_hasElement() {
        Assert.assertFalse("null array", Check.hasElement(null, 2));
        Assert.assertFalse("short array", Check.hasElement(new String[]{ "0", "1" }, 2));
        Assert.assertTrue("exact length", Check.hasElement(new String[]{ "0", "1", "2" }, 2));
        Assert.assertTrue("long array", Check.hasElement(new String[]{ "0", "1", "2", "3" }, 2));
        Assert.assertTrue("nulls in array", Check.hasElement(new String[]{ null, null, null, null }, 2));
    }
}