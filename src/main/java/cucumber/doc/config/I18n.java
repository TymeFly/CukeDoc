package cucumber.doc.config;

import java.util.Locale;

import javax.annotation.Nonnull;

enum I18n {
    EN(Locale.ENGLISH),

    EN_US(Locale.US),

    FR(Locale.FRENCH);


    private final Locale locale;


    I18n(@Nonnull Locale locale) {
        this.locale = locale;
    }


    @Nonnull
    public Locale getLocale() {
        return locale;
    }
}
