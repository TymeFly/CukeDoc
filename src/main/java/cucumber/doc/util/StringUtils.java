package cucumber.doc.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import cucumber.doc.annotation.VisibleForTesting;

/**
 * String utility functions.
 */
public class StringUtils {
    private static final Set<String> END_OF_SENTENCE = SetUtils.toUnmodifiableSet(". ", "! ", "? ");


    /** Hide utility class constructor */
    private StringUtils() {
    }


    /**
     * Returns the first sentence in the {@code text}
     * @param text              Some text
     * @return                  The first sentence in the text
     */
    @Nonnull
    public static String firstSentence(@Nonnull String text) {
        int index = text.length() - 1;

        for (String marker : END_OF_SENTENCE) {
            int found = text.indexOf(marker);

            index = (found == -1 ? index : Math.min(found, index));
        }

        return text.substring(0, index + 1);
    }


    /**
     * Returns the index of the first character in the next sentence, or -1 of there are no more sentences.
     * @param text          Some text
     * @param start         index of first character to search
     * @return              index of the start of the next sentence.
     */
    public static int startOfSentence(@Nonnull String text, int start) {
        int found = -1;

        for (String marker : END_OF_SENTENCE) {
            int index = text.indexOf(marker, start);

            if (index != -1) {
                index += marker.length();
                found = (found == -1 ? index : Math.min(found, index));
            }
        }

        return (found == -1 ? found : skipSpaces(text, found));
    }


    /**
     * Ensure description has caps at the start of each sentence.
     * @param text      text to capitalise
     * @return          A version of {@code description} but with correct caps.
     */
    @Nonnull
    public static String capitalise(@Nonnull String text) {
        int index = skipSpaces(text, 0);

        while (index >= 0) {
            char target = text.charAt(index);
            if (Character.isLowerCase(target)) {
                text = text.substring(0, index) +
                              Character.toUpperCase(target) +
                              text.substring(index + 1);
            }

            index = startOfSentence(text, index);
        }

        return text;
    }


    @VisibleForTesting
    static int skipSpaces(@Nonnull String text, int index) {
        if (text.isEmpty()) {
            index = -1;
        } else {
            while (text.charAt(index) == ' ') {
                if (++index == text.length()) {
                    index = -1;
                    break;
                }
            }
        }

        return index;
    }



    /**
     * Returns the number of time the {@code sub}String appears in the {@code str} between the
     * {@code start} and {@code end} indexes
     * @param str           String to examine
     * @param sub           substring to search for
     * @param start         index in string to start the search
     * @param end           index in string to stop the search
     * @return              the number of time the {@code sub}String occurs in the {@code str}
     */
    public static int countSubstrings(@Nonnull String str, @Nonnull String sub, int start, int end) {
        int count = 0;
        int index = -1;

        end = Math.min(end + 1, str.length());
        str = str.substring(start, end);

        while (true) {
            index = str.indexOf(sub, ++index);

            if (index == -1) {
                break;
            } else {
                count++;
            }
        }

        return count;
    }


    /**
     * Split a comma delimited list into it's component parts, removing any white space
     * @param str       string to split
     * @return          the component parts
     */
    public static List<String> toList(@Nonnull String str) {
        List<String> result;

        str = str.trim()
                 .replaceAll("(, *)+", ",")
                 .replaceAll("^,", "")
                 .replaceAll(",$", "");

        if (str.isEmpty()) {
            result = Collections.emptyList();
        } else {
            String[] parts = str.trim().split(" *, *");
            result = Arrays.asList(parts);
        }

        return result;
    }


    /**
     * If {@code surround} text is before and/or after the {@code str} content, then remove it
     * @param str           string to trim
     * @param surround      optional surrounding text
     * @return              the trimmed string
     */
    @Nonnull
    public static String trim(@Nonnull String str, @Nonnull String surround) {
        return trim(str, surround, surround);
    }


    /**
     * Clean a string by trimming and removing {@code prefix} and {@code suffix} if they are present
     * @param str           string to clean
     * @param prefix        prefix to remove
     * @param suffix        suffix to remove
     * @return              cleaned string
     */
    @Nonnull
    public static String trim(@Nonnull String str, @Nonnull String prefix, @Nonnull String suffix) {
        str = str.trim();

        if (str.startsWith(prefix)) {
            str = str.substring(prefix.length(), str.length() - prefix.length());
        }

        str = trimTail(str, suffix);

        return str;
    }


    /**
     * Clean a string by trimming and removing {@code suffix} if it is present
     * @param str           string to clean
     * @param suffix        suffix to remove
     * @return              cleaned string
     */
    @Nonnull
    public static String trimTail(@Nonnull String str, @Nonnull String suffix) {
        if (str.endsWith(suffix)) {
            str = str.substring(0, str.length() - suffix.length());
        }

        return str;
    }


    /**
     * Returns the tail part of a string. This is defined to be the text following the last occurrence of
     * the {@code delimiter}. If the {@code delimiter} is not found the the original string is returned.
     * @param str               string to extract tail from
     * @param delimiter         delimiter that mark the start of the tail
     * @return                  tail part of the string
     */
    @Nonnull
    public static String getTail(@Nonnull String str, @Nonnull String delimiter) {
        int index = str.lastIndexOf(delimiter);

        return (index == -1 ? str : str.substring(index + delimiter.length()));
    }
}
