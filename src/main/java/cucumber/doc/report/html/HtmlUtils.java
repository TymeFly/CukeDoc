package cucumber.doc.report.html;

import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Utility class for generating cleaned up versions of the text for a HTML report
 */
class HtmlUtils {
    private HtmlUtils() {
    }


    /**
     * Remove embedded tags in the form {@code {@____} from descriptions. As the translation may introduce
     * HTML sequences the returned string should be added to the page using {@link j2html.TagCreator#rawHtml}.
     * Note that nested embedded tags are not currently handled
     * @param description       raw form of the description
     * @return                  A cleaned up version of the description which may contain embedded HTML.
     *                              An empty string is returned if {@code description} is {@literal null}
     */
    @Nonnull
    static String cleanDescription(@Nullable String description) {
        if (description == null) {
            description = "";
        } else {
            description = cleanDescription(description, "code", "<code>", "</code>", null);
            description = cleanDescription(description, "link", "<code>", "</code>", HtmlUtils::cleanLink);
            description = cleanDescription(description, "", "", "", null);
        }

        return description;
    }


    /**
     * Similar to {@link #cleanDescription(String)}, but {@code defaultValue} is returned instead of a
     * blank string
     * @param description       raw form of the description
     * @param defaultValue      String to return if description is cleaned to an empty string
     * @return                  A cleaned up version of the description which may contain embedded HTML.
     */
    @Nonnull
    static String cleanDescription(@Nullable String description, @Nonnull String defaultValue) {
        String result = cleanDescription(description);

        return (result.isEmpty() ? defaultValue : result);
    }


    @Nonnull
    private static String cleanLink(@Nonnull String raw) {
        String clean;

        if (raw.startsWith("#")) {
            clean = raw.substring(1);
        } else if (raw.contains("#")) {
            clean = raw.replace('#', '.');
        } else {
            clean = raw;
        }

        return clean;
    }


    @Nonnull
    private static String cleanDescription(@Nonnull String description,
                                           @Nonnull String tag,
                                           @Nonnull String start,
                                           @Nonnull String end,
                                           @Nullable Function<String, String> cleanContent) {
        // String manipulation is a loop isn't good, but in practice this method isn't slow enough to need refactoring
        tag = "{@" + tag;

        int startIndex = description.indexOf(tag);
        while (startIndex != -1) {
            int textIndex = description.replaceAll("\\s", " ").indexOf(" ", startIndex);
            int endIndex = (textIndex == -1 ? -1 : description.indexOf("}", textIndex));

            if (endIndex == -1) {
                break;
            }

            if (description.charAt(textIndex) == ' ') {
                textIndex++;
            }

            String text = description.substring(textIndex, endIndex);
            text = (cleanContent == null ? text : cleanContent.apply(text));

            description = description.substring(0, startIndex) +
                          start +
                          text +
                          end +
                          description.substring(endIndex + 1);

            startIndex = description.indexOf(tag, startIndex);
        }

        return description;
    }
}
