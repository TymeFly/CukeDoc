package cucumber.doc.config;

import javax.annotation.Nonnull;

/**
 * Keys into the resource bundle for i18n.
 */
public enum LanguageKey {
    GENERAL_DESCRIPTION,
    GENERAL_SINCE,
    GENERAL_MAPPINGS,
    GENERAL_MAPPING,

    MENU_CUCUMBER,
    FOOTER_BUILD_STAMP,

    OVERVIEW_TITLE,
    OVERVIEW_TYPES,
    OVERVIEW_TYPE,
    GENERAL_PANEL_FEATURE,
    GENERAL_PANEL_JAVA,

    CLASS_TABLE,
    CLASS_WHERE,
    CLASS_HOVER_METHOD,
    CLASS_HOVER_ANNOTATION,
    CLASS_HOVER_TYPE,
    CLASS_HOVER_FORMAT,

    NOTES_TITLE;

    private final String key;

    LanguageKey() {
        key = name().toLowerCase();
    }


    @Nonnull
    String getKey() {
        return key;
    }
}
