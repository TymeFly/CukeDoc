package io.cucumber.doc.report.html;

import javax.annotation.Nonnull;

import io.cucumber.doc.config.LanguageKey;
import io.cucumber.doc.model.ApplicationModel;

/**
 * Enumeration of links that appear in the menu at the top of each page.
 * The order the constants are declared in this class matches the order they links are displayed
 */
enum MenuItem {
    /** Menu item for a link to the root of this site */
    OVERVIEW(LanguageKey.OVERVIEW_TITLE, "index.html"),

    /** Menu item for a link to the root of this site */
    NOTES(LanguageKey.NOTES_TITLE, "notes/_index.html") {
        @Override
        public boolean isAvailable(@Nonnull ApplicationModel model) {
            return (!model.getNotes().isEmpty());
        }
    },

    /** Menu item for a link to the Cucumber website */
    CUCUMBER(LanguageKey.MENU_CUCUMBER, "http://cucumber.io/");


    private final LanguageKey key;
    private final String href;


    MenuItem(@Nonnull LanguageKey key, @Nonnull String href) {
        this.key = key;
        this.href = href;
    }

    /**
     * Returns a language key for the displayed text
     * @return a language key for the displayed text
     */
    @Nonnull
    public LanguageKey getKey() {
        return key;
    }

    /**
     * Returns the destination link
     * @return the destination link
     */
    @Nonnull
    public String getHref() {
        return href;
    }


    /**
     * Returns {@code true} only if this menu item should be displayed. It is acceptable to
     * display a menu item even if the link from it is disabled
     * @param model         application model
     * @return {@code true} only if this menu item should be displayed; else [@code false}
     */
    public boolean isAvailable(@Nonnull ApplicationModel model) {
        return true;
    }
}
