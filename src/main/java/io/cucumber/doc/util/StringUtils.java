package io.cucumber.doc.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import io.cucumber.doc.annotation.VisibleForTesting;

/**
 * String utility functions.
 */
public class StringUtils {
    private static final Set<String> END_OF_SENTENCE =
        SetUtils.toUnmodifiableSet(". ", "! ", "? ",
                                   ".\n", "!\n", "?\n");


    /** Hide utility class constructor */
    private StringUtils() {
    }


    /**
     * Remove all Leading and trailing lines that only contain white space characters
     * @param text      Text to trim
     * @return          The original string with the leading and trailing blank lines removed
     */
    @Nonnull
    public static String trimLines(@Nonnull String text) {
        return text.replaceAll("^(\\h*\\v)+", "")
                   .replaceAll("(\\v\\h*)+$", "");
    }


    /**
     * Returns the first sentence in the {@code text}
     * @param text              Some text
     * @return                  The first sentence in the text
     */
    @Nonnull
    public static String firstSentence(@Nonnull String text) {
        text = text.replace('\t', ' ')
                   .replace('\r', '\n');

        int index = text.length() - 1;

        for (String marker : END_OF_SENTENCE) {
            int found = text.indexOf(marker);

            index = (found == -1 ? index : Math.min(found, index));
        }

        text = text.substring(0, index + 1);

        return text;
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
     * @see #asString(Collection)
     * @see #asString(Object[])
     */
    public static List<String> asList(@Nonnull String str) {
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
     * Return a representation of the array as a comma separated list of values. If the array
     * is empty then an empty list is returned.
     * @param values        values to be represented in the string
     * @return              a coma separated list of values.
     * @see #asList(String)
     */
    public static String asString(@Nonnull Object[] values) {
        return asString(Arrays.asList(values));
    }


    /**
     * Return a representation of the collection as a comma separated list of values. If the collection
     * is empty then an empty list is returned.
     * @param values        values to be represented in the string
     * @return              a coma separated list of values.
     * @see #asList(String)
     */
    @Nonnull
    public static String asString(@Nonnull Collection<?> values) {
        StringBuilder builder = new StringBuilder();
        String separator = "";

        for (Object value : values) {
            String str = (value == null ? "<null>" : value.toString());
            builder.append(separator).append(str);

            separator = ", ";
        }

        return builder.toString();
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
            str = str.substring(prefix.length());
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


    /**
     * Return a string that contains the single {@code character} repeated {@code length} times
     * @param character         The single character found in the generated string
     * @param length            Number of times the character is repeated
     * @return                  {@code character} repeated {@code length} times.
     */
    public static String sequence(char character, int length) {
        char[] buffer = new char[length];

        Arrays.fill(buffer, character);

        return new String(buffer);
    }
}
