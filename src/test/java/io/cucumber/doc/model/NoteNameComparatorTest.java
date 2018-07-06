package io.cucumber.doc.model;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link NoteNameComparator}
 */
public class NoteNameComparatorTest {
    private NoteNameComparator comparator = new NoteNameComparator();


    /**
     * Unit test {@link NoteNameComparator#compare}
     */
    @Test
    public void test_Compare() {
        // Basic string comparisons
        Assert.assertTrue("Compare 'A' and 'A'", comparator.compare("A", "A") == 0);
        Assert.assertTrue("Compare 'A' and 'B'", comparator.compare("A", "B") < 0);
        Assert.assertTrue("Compare 'B' and 'A'", comparator.compare("B", "A") > 0);

        // Check it's case insensitive
        Assert.assertTrue("Compare 'a' and 'A'", comparator.compare("a", "A") == 0);
        Assert.assertTrue("Compare 'a' and 'B'", comparator.compare("a", "B") < 0);
        Assert.assertTrue("Compare 'b' and 'A'", comparator.compare("b", "A") > 0);

        // Typical page names
        Assert.assertTrue("Compare 'Page 1' and 'Page 2'", comparator.compare("Page 1", "Page 2") < 0);
        Assert.assertTrue("Compare 'Hello' and 'World'", comparator.compare("Hello", "World") < 0);
        Assert.assertTrue("Compare 'Start' and 'End'", comparator.compare("Start", "End") > 0);

        // Compare a page name against predefined names
        Assert.assertTrue("Compare 'Overview' and 'Page 1'", comparator.compare("Overview", "Page 1") < 0);
        Assert.assertTrue("Compare 'Contents' and 'Page 1'", comparator.compare("Contents", "Page 1") < 0);
        Assert.assertTrue("Compare 'Read Me' and 'Page 1'", comparator.compare("Read Me", "Page 1") < 0);
        Assert.assertTrue("Compare 'TODO' and 'Page 1'", comparator.compare("TODO", "Page 1") > 0);
        Assert.assertTrue("Compare 'Credits' and 'Page 1'", comparator.compare("Credits", "Page 1") > 0);
        Assert.assertTrue("Compare 'Index' and 'Page 1'", comparator.compare("Index", "Page 1") > 0);

        // Compare predefined names order
        Assert.assertTrue("Compare 'Overview' and 'Contents'", comparator.compare("Overview", "Contents") < 0);
        Assert.assertTrue("Compare 'Contents' and 'Read Me'", comparator.compare("Contents", "Read Me") < 0);
        Assert.assertTrue("Compare 'Read Me' and 'TODO'", comparator.compare("Read Me", "TODO") < 0);
        Assert.assertTrue("Compare 'TODO' and 'Credits'", comparator.compare("TODO", "Credits") < 0);
        Assert.assertTrue("Compare 'Credits' and 'Index'", comparator.compare("Credits", "Index") < 0);
    }
}