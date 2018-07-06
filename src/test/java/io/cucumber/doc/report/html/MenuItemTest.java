package io.cucumber.doc.report.html;

import io.cucumber.doc.config.LanguageKey;
import org.junit.Assert;
import org.junit.Test;
import test_helper.utils.Sample;

/**
 * Unit test for {@link MenuItem}
 */
public class MenuItemTest {

    /**
     * Unit test {@link MenuItem#getKey}
     */
    @Test
    public void test_GetKey() {
        Assert.assertEquals("OVERVIEW has wrong key", LanguageKey.OVERVIEW_TITLE, MenuItem.OVERVIEW.getKey());
        Assert.assertEquals("NOTES has wrong key", LanguageKey.NOTES_TITLE, MenuItem.NOTES.getKey());
        Assert.assertEquals("CUCUMBER has wrong key", LanguageKey.MENU_CUCUMBER, MenuItem.CUCUMBER.getKey());
    }


    /**
     * Unit test {@link MenuItem#getHref}
     */
    @Test
    public void test_GetHref() {
        Assert.assertEquals("OVERVIEW has wrong link", "index.html", MenuItem.OVERVIEW.getHref());
        Assert.assertEquals("NOTES has wrong link", "notes/_index.html", MenuItem.NOTES.getHref());
        Assert.assertEquals("CUCUMBER has wrong link", "http://cucumber.io/", MenuItem.CUCUMBER.getHref());
    }


    /**
     * Unit test {@link MenuItem#isAvailable}
     */
    @Test
    public void test_IsAvailable() {
        Assert.assertTrue("OVERVIEW has wrong availability", MenuItem.OVERVIEW.isAvailable(Sample.app()));
        Assert.assertTrue("CUCUMBER has wrong availability", MenuItem.CUCUMBER.isAvailable(Sample.app()));
        Assert.assertTrue("NOTES has wrong availability - with notes", MenuItem.NOTES.isAvailable(Sample.app()));
    }


    /**
     * Unit test {@link MenuItem#isAvailable}
     */
    @Test
    public void test_IsAvailable_EmptyApp() {
        Assert.assertTrue("OVERVIEW has wrong availability", MenuItem.OVERVIEW.isAvailable(Sample.emptyApp()));
        Assert.assertTrue("CUCUMBER has wrong availability", MenuItem.CUCUMBER.isAvailable(Sample.emptyApp()));
        Assert.assertFalse("NOTES has wrong availability", MenuItem.NOTES.isAvailable(Sample.emptyApp()));
    }
}