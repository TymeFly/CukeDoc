package io.cucumber.doc.model;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link NameUtils}
 */
public class NameUtilsTest {

    /**
     * Unit test {@link NameUtils#canonise}
     */
    @Test
    public void test_Canonise() {
        Assert.assertEquals("Empty String", "", NameUtils.canonise(""));
        Assert.assertEquals("Just spaces", "", NameUtils.canonise("   "));
        Assert.assertEquals("Just Underscores", "", NameUtils.canonise("____"));
        Assert.assertEquals("Just Dashes", "", NameUtils.canonise("----"));

        Assert.assertEquals("'Page One'", "page one", NameUtils.canonise("Page One"));
        Assert.assertEquals("'Page-One'", "page one", NameUtils.canonise("Page-One"));
        Assert.assertEquals("'_Page-One_'", "page one", NameUtils.canonise("_Page-One_"));

        Assert.assertEquals("'Page-1'", "page 1", NameUtils.canonise("Page-1"));
        Assert.assertEquals("'Page 1'", "page 1", NameUtils.canonise("Page 1"));

        Assert.assertEquals("'To Do'", "todo", NameUtils.canonise("todo"));
        Assert.assertEquals("'ToDo'", "todo", NameUtils.canonise("ToDo"));
        Assert.assertEquals("'_To-Do_'", "todo", NameUtils.canonise("_To-Do_"));

        Assert.assertEquals("'READ ME'", "read me", NameUtils.canonise("READ ME"));
        Assert.assertEquals("'ReadMe'", "read me", NameUtils.canonise("ReadMe"));
        Assert.assertEquals("'Read_Me'", "read me", NameUtils.canonise("Read_Me"));
    }
}