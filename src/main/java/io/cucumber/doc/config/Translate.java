package io.cucumber.doc.config;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.annotation.Nonnull;

import io.cucumber.doc.annotation.VisibleForTesting;
import io.cucumber.doc.exception.CukeDocException;

/**
 * i18n implementation
 */
public class Translate {
    private static Translate instance;

    private final ResourceBundle labels;


    private Translate(@Nonnull I18n type) {
        this.labels = ResourceBundle.getBundle("i18n.cuke-doc", type.getLocale());
    }


    /** Used by Unit tests only */
    @VisibleForTesting
    static void setI18n(@Nonnull I18n i18n) {
        instance = new Translate(i18n);
    }


    @Nonnull
    private static Translate getInstance() {
        if (instance == null) {
            instance = new Translate(Config.getInstance().getI18n());
        }

        return instance;
    }


    /**
     * Translate a message
     * @param key       Language key
     * @return          the translated message
     */
    @Nonnull
    public static String message(@Nonnull LanguageKey key) {
        return getInstance().labels.getString(key.name().toLowerCase());
    }


    /**
     * Translate a message with parameters
     * @param key       Language key
     * @param args      parameters in the message
     * @return          the translated message
     */
    @Nonnull
    public static String message(@Nonnull LanguageKey key, @Nonnull Object... args) {
        String result;

        try {
            result = MessageFormat.format(message(key), args);
        } catch (MissingResourceException e) {
            throw new CukeDocException("Unable to message '" + key + "'", e);
        }

        return result;
    }
}
