package io.cucumber.doc.config;

import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link Translate}
 */
public class TranslateTest {
    @After
    public void tearDown() {
        Translate.setI18n(I18n.EN);
    }


    /**
     * Unit test {@link Translate#message(LanguageKey)}
     */
    @Test
    public void test_Message_en() {
        Translate.setI18n(I18n.EN);

        String actual = Translate.message(LanguageKey.OVERVIEW_TITLE);

        Assert.assertEquals("Unexpected message", "Overview", actual);
    }


    /**
     * Unit test {@link Translate#message(LanguageKey, Object...)}
     */
    @Test
    public void test_Message_en_withArguments() {
        Translate.setI18n(I18n.EN);

        Date testDate = new Date(1265980200000L);
        String actual = Translate.message(LanguageKey.FOOTER_BUILD_STAMP, testDate, testDate);

        Assert.assertEquals("Unexpected message", "Built on 12 February 2010 at 13:10", actual);
    }


    /**
     * Unit test {@link Translate#message(LanguageKey, Object...)}
     */
    @Test
    public void test_Message_us_withArguments() {
        Translate.setI18n(I18n.EN_US);

        Date testDate = new Date(1265980200000L);
        String actual = Translate.message(LanguageKey.FOOTER_BUILD_STAMP, testDate, testDate);

        Assert.assertEquals("Unexpected message", "Built on 12 February 2010 at 13:10", actual);
    }


    /**
     * Unit test {@link Translate#message(LanguageKey)}
     */
    @Test
    public void test_Message_fr() {
        Translate.setI18n(I18n.FR);

        String actual = Translate.message(LanguageKey.OVERVIEW_TITLE);

        Assert.assertEquals("Unexpected message", "Aper\u00e7u", actual);
    }


    /**
     * Unit test {@link Translate#message(LanguageKey, Object...)}
     */
    @Test
    public void test_Message_fr_withArguments() {
        Translate.setI18n(I18n.FR);

        Date testDate = new Date(1265980200000L);
        String actual = Translate.message(LanguageKey.FOOTER_BUILD_STAMP, testDate, testDate);

        Assert.assertEquals("Unexpected message", "Construit sur 12 February 2010 \u00e0 13:10", actual);
    }
}