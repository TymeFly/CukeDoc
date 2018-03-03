package cucumber.doc.report.html;

import javax.annotation.Nonnull;

import cucumber.doc.config.Config;
import cucumber.doc.config.LanguageKey;

/**
 * Enumeration of links that appear in the menu at the top of each page.
 * The order the constants are declared in this class matches the order they links are displayed
 */
enum MenuItem {
    /** Menu item for a link to the root of this site */
    OVERVIEW(LanguageKey.OVERVIEW_TITLE, "index.html"),

    /** Menu item for a link to the root of this site */
    NOTES(LanguageKey.NOTES_TITLE, "notes.html") {
        @Override
        public boolean isAvailable() {
            return (Config.getInstance().getNotesPath() != null);
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
     * Returns {@code true} only if this menu item should be displayed
     * @return {@code true} only if this menu item should be displayed; else [@code false}
     */
    public boolean isAvailable() {
        return true;
    }
}
