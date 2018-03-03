package cucumber.doc.report.html;

import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

class HtmlUtils {
    private HtmlUtils() {
    }

    /**
     * Remove embedded tags in the form {@code {@____} from descriptions. As the translation may introduce
     * HTML sequences the returned string should be added to the page using {@link j2html.TagCreator#rawHtml}.
     * Note that nested emdedded tags are not currently handled
     * @param description       raw form of the description
     * @return                  A cleaned up version of the description which may contain embedded HTML
     */
    @Nullable
    static String cleanDescription(@Nullable String description) {
        if (description != null) {
            description = description.replaceAll(" {2,}", " ");
            description = cleanDescription(description, "code", "<code>", "</code>", null);
            description = cleanDescription(description, "link", "<code>", "</code>", HtmlUtils::cleanLink);
            description = cleanDescription(description, "", "", "", null);
        }

        return description;
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
        // String manipulation is a loop isn't great, but in practice this method isn't slow enough to need refactoring
        boolean allTags = tag.isEmpty();
        tag = "{@" + tag + (allTags ? "" : " ");

        int startIndex = description.indexOf(tag);
        while (startIndex != -1) {
            int textIndex = description.indexOf(" ", startIndex);
            int endIndex = (textIndex == -1 ? -1 : description.indexOf("}", textIndex));

            if (endIndex == -1) {
                break;
            }

            String text = description.substring(textIndex, endIndex).trim();
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
