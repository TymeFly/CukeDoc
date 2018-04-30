package cucumber.doc.report.html;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link HtmlUtils}
 */
public class HtmlUtilsTest {

    /**
     * Unit test {@link HtmlUtils#cleanDescription(String)}
     */
    @Test
    public void test_cleanDescription() {
        Assert.assertEquals("null", "", HtmlUtils.cleanDescription(null));
        Assert.assertEquals("Empty Description", "", HtmlUtils.cleanDescription(""));
        Assert.assertEquals("No Change required", "Hello world.", HtmlUtils.cleanDescription("Hello world."));
        Assert.assertEquals("just code", "<code>alf</code>", HtmlUtils.cleanDescription("{@code alf}"));
        Assert.assertEquals("code and text", "xxx <code>alf</code> yyy", HtmlUtils.cleanDescription("xxx {@code alf} yyy"));
        Assert.assertEquals("code x 2", "<code>alf</code> <code>bert</code>", HtmlUtils.cleanDescription("{@code alf} {@code bert}"));
        Assert.assertEquals("missing end", "{@code alf", HtmlUtils.cleanDescription("{@code alf"));
        Assert.assertEquals("only start", "{@code", HtmlUtils.cleanDescription("{@code"));

        Assert.assertEquals("value", "alf", HtmlUtils.cleanDescription("{@value alf}"));
        Assert.assertEquals("local link", "<code>alf</code>", HtmlUtils.cleanDescription("{@link #alf}"));
        Assert.assertEquals("class link", "<code>a.b.c</code>", HtmlUtils.cleanDescription("{@link a.b.c}"));
        Assert.assertEquals("link into class", "<code>a.b.c.d</code>", HtmlUtils.cleanDescription("{@link a.b.c#d}"));
    }


    /**
     * Unit test {@link HtmlUtils#cleanDescription(String, String)}
     */
    @Test
    public void test_CleanDescription_WithDefault() {
        Assert.assertEquals("null", "defaultValue", HtmlUtils.cleanDescription(null, "defaultValue"));
        Assert.assertEquals("Empty Description", "defaultValue", HtmlUtils.cleanDescription("", "defaultValue"));
        Assert.assertEquals("With value", "Hello world.", HtmlUtils.cleanDescription("Hello world.", "defaultValue"));
    }
}