package cucumber.doc.util;

import javax.annotation.Nonnull;

import cucumber.doc.config.Config;

/**
 * If trace mode is configured, write messages to standard output. We can't use the
 * {@link com.sun.javadoc.DocErrorReporter} as it might not hve been passed to the Doclet yet.
 */
public class Trace {
    /** Hide utility class constructor. */
    private Trace() {
    }


    /**
     * If trace is enabled, write a message.
     * @param message       hman readable message
     */
    public static void message(@Nonnull String message) {
        if (Config.getInstance().traceMode()) {
            System.out.println(message);
        }
    }


    /**
     * If trace is enabled, write a formatted message.
     * @param format        format string
     * @param args          Formatting arguments
     * @see java.util.Formatter
     */
    public static void message(@Nonnull String format, @Nonnull Object... args) {
        if (Config.getInstance().traceMode()) {
            String message = String.format(format, args);

            System.out.println(message);
        }
    }
}
