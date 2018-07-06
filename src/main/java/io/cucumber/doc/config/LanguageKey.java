package io.cucumber.doc.config;

import javax.annotation.Nonnull;

/**
 * Keys into the resource bundle for i18n.
 */
public enum LanguageKey {
    // Used all over the application
    GENERAL_DESCRIPTION,
    GENERAL_SINCE,
    GENERAL_MAPPINGS,
    GENERAL_MAPPING,
    GENERAL_NAME,
    GENERAL_PAGE,

    GENERAL_PANEL_FEATURE,
    GENERAL_PANEL_JAVA,

    FOOTER_BUILD_STAMP,

    MENU_CUCUMBER,

    // Used on the overview page
    OVERVIEW_TITLE,
    OVERVIEW_TYPES,
    OVERVIEW_TYPE,

    // Used on the type pages
    TYPE_TABLE,
    TYPE_ARGUMENT,
    TYPE_WHERE,
    TYPE_HOVER_METHOD,
    TYPE_HOVER_ANNOTATION,
    TYPE_HOVER_TYPE,
    TYPE_HOVER_FORMAT,

    // Used on the notes pages
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
