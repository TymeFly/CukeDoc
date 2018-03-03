package cucumber.doc.config;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link LanguageKey}
 */
public class LanguageKeyTest {
    /**
     * Unit test {@link LanguageKey#getKey()}
     */
    @Test
    public void testName() {
        Assert.assertEquals("Unexpected key", "overview_title", LanguageKey.OVERVIEW_TITLE.getKey());
    }
}