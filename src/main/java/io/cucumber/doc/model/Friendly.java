package io.cucumber.doc.model;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.cucumber.doc.annotation.VisibleForTesting;
import io.cucumber.doc.util.Check;
import io.cucumber.doc.util.RegExSplitter;
import io.cucumber.doc.util.SetUtils;
import io.cucumber.doc.util.StringUtils;

/**
 * Class to make "friendly" versions of elements found in the source code.
 * As these are all static methods the client code will see the calls as {@code Friendly.name(data);}
 */
class Friendly {
    private static final Set<Character> QUANTIFIERS = SetUtils.toUnmodifiableSet('?', '*', '+');

    /** Hide utility class constructor */
    private Friendly() {
    }


    /**
     * Convert a Java method or class name into something in the form {@code Word1 Word2 Word3}.
     * Note the caps and spaces.
     * @param name      A Java identifier
     * @return          A friendly name
     */
    @Nonnull
    static String name(@Nonnull String name) {
        String friendly;

        name = name.trim();

        if (name.isEmpty()) {
            friendly = name;
        } else {
            int length = name.length();
            boolean needUpper = true;
            boolean wasUpper = false;
            boolean wasSpace = false;
            StringBuilder buffer = new StringBuilder(length);

            int index = 0;
            do {
                char element = name.charAt(index);

                if (!wasUpper && (index != 0) && Character.isUpperCase(element)) {
                    if (!wasSpace) {
                        buffer.append(" ");
                        wasSpace = true;
                    }
                }

                if (needUpper) {
                    element = Character.toUpperCase(element);
                }

                wasUpper = Character.isUpperCase(element);
                needUpper = (element == '_');

                if (needUpper) {
                    element = ' ';
                }

                if (!(wasSpace && (element == ' '))) {
                    buffer.append(element);
                }

                wasSpace = (element == ' ');
            } while (++index != length);

            friendly = buffer.toString().trim();
        }

        return friendly;
    }


    /**
     * Convert a Java identifier for a parameter into something in the form {@code word1-word2-word3}.
     * Note the lower case letters and the dashes
     * @param parameter     A Java identifier
     * @return              A friendly name
     */
    @Nonnull
    static String parameter(@Nonnull String parameter) {
        // Rather more string manipulation that I'd like, but an easy implementation
        return name(parameter).toLowerCase().replace(' ', '-');
    }


    /**
     * Convert a capture group from a Java regular expression into something more readable.
     * For example, {@code \\d+} will become {@code \d+}
     * @param captureGroup  A Java Regular Expression capture group
     * @return              A friendly name
     */
    @Nonnull
    static String captureGroup(@Nonnull String captureGroup) {
        return removeSlashes(captureGroup);
    }



    /**
     * Convert a class, method (aka implementation) or argument description into something more readable.
     * Since the output isn't guaranteed to be HTML at this point, embedded tags in the form {@code {@____} }
     * are retained.
     * @param description   A JavaDoc description
     * @return              A friendly description
     */
    @Nullable
    static String description(@Nullable String description) {
        if (Check.hasText(description)) {
            description = description.trim();
            description = StringUtils.capitalise(description);
        }

        return description;
    }


    /**
     * Clean up on mappings
     * @param mapping       dirty mapping definition
     * @param parameters    parameter definitions for this mapping
     * @return              clean version of mapping
     */
    @Nonnull
    static String mapping(@Nonnull String mapping, @Nonnull List<ParameterModel> parameters) {
        // There is a lot of string manipulation here. If the performance becomes unacceptable
        // then consider rewriting as a parser. Until then this is probably easier to maintain
        String friendly;

        friendly = StringUtils.trim(mapping, "^", "$");
        friendly = StringUtils.trimTail(friendly, "\\\\.?");
        friendly = substituteParameterNames(friendly, parameters);
        friendly = cleanNonCaptureGroups(friendly);
        friendly = removeSpecialConstructs(friendly);

        /*
         * Not currently supported (would they ever be used?):
         *    X??, X*? X+?, X?+, X*+, X++, {n} {n,}, {n,m}, {n}? {n,}?, {n,m}?, {n}+ {n,}+, {n,m}+,
         *    backReferences
         */

        friendly = cleanAllQuantifiers(friendly);
        friendly = removeBrackets(friendly);
        friendly = removeEscapedCharacters(friendly);
        friendly = removeSlashes(friendly);

        return friendly;
    }


    @Nonnull
    private static String substituteParameterNames(@Nonnull String mapping, @Nonnull List<ParameterModel> parameters) {
        RegExSplitter splitter = RegExSplitter.compile(mapping);
        StringBuilder builder = new StringBuilder();
        Iterator<ParameterModel> iterator = parameters.iterator();
        boolean first = true;

        for (String section : splitter.getUncapturedText()) {
            if (first) {
                first = false;
            } else {
                builder.append('<')
                       .append(iterator.hasNext() ? iterator.next().getFriendlyName() : "<missing-parameter>")
                       .append('>');
            }

            builder.append(section);
        }

        if (iterator.hasNext()) {
            builder.append('<').append(iterator.next().getFriendlyName()).append('>');
        }

        return builder.toString();
    }



    // (?:xxxx) => (xxxx)
    // I expect few (if any) of these, so I'm not overly worried about string manipulation in a loop
    @Nonnull
    @VisibleForTesting
    static String cleanNonCaptureGroups(@Nonnull String mapping) {
        int start = indexOf(mapping, "(?:");

        while (start != -1) {
            int end = indexOf(mapping, ")", start);
            if (end != -1) {                    // If this happens then the mapping is malformed
                mapping = mapping.substring(0, start) + '(' + mapping.substring(start + 3);
            }

            start = indexOf(mapping, "(?:", start);
        }

        return mapping;
    }


    // remove anything that goes {@code (?.......)}, so flags, lookahead, lookbehind
    @Nonnull
    @VisibleForTesting
    static String removeSpecialConstructs(@Nonnull String mapping) {
        int start = indexOf(mapping, "(?");

        while (start != -1) {
            int end = indexOf(mapping, ")", start);
            if (end != -1) {                    // If this happens then the mapping is malformed
                mapping = mapping.substring(0, start) + mapping.substring(end + 1);
            }

            start = indexOf(mapping, "(?", start);
        }

        return mapping;
    }


    @Nonnull
    private static String cleanAllQuantifiers(@Nonnull String mapping) {
        for (char quantifier : QUANTIFIERS) {
            mapping = cleanQuantifiers(mapping, quantifier);
        }

        return mapping;
    }


    // handle '?', '+' and '*' as in {@code "an?"}, {@code (abc)?} or {@code ab*}
    @Nonnull
    @VisibleForTesting
    static String cleanQuantifiers(@Nonnull String mapping, char quantifier) {
        String search = "" + quantifier;
        int index = indexOf(mapping, search);

        while (index != -1) {
            int previousPosition = index - 1;
            char previous = (index == 0 ? 0 : mapping.charAt(previousPosition));

            if (index == 0) {
                index++;
            } else if ((index > 1) && previous == ')' && (mapping.charAt(index - 2) == '\\')) {
                mapping = mapping.substring(0, index - 2) +
                        "(\\)" + quantifier + ')' + mapping.substring(index + 1);
                index += 3;
            } else if (previous == ')') {
                mapping = mapping.substring(0, previousPosition) + quantifier + ')' + mapping.substring(index + 1);
                index++;
            } else if (previous == '>') {
                int startIndex = Math.max(mapping.lastIndexOf('<', previousPosition), 0);

                mapping = mapping.substring(0, startIndex) +
                          '(' + mapping.substring(startIndex, index) + quantifier + ')' +
                          mapping.substring(index + 1);
                index += 3;
            } else {
                mapping = mapping.substring(0, previousPosition) +
                        '(' + previous + quantifier + ')' + mapping.substring(index + 1);
                index += 3;
            }

            index = indexOf(mapping, search, index);
        }

        return mapping;
    }


    @Nonnull
    @VisibleForTesting
    static String removeBrackets(@Nonnull String mapping) {
        int index = indexOf(mapping, ")");

        while (index != -1) {
            char previous = (index == 0 ? 0 : mapping.charAt(index - 1));
            boolean required = QUANTIFIERS.contains(previous);
            int begin = (required ? -1 : lastIndexOf(mapping, "(", index));

            if (begin == -1) {
                index++;
            } else {
                mapping = mapping.substring(0, begin) +
                          mapping.substring(begin + 1, index) +
                          mapping.substring(index + 1);
            }

            index = indexOf(mapping, ")", index);
        }

        return mapping;
    }


    // We should also do octal and hex characters
    @Nonnull
    private static String removeEscapedCharacters(@Nonnull String mapping) {
        mapping = mapping.replace("\\\\.", ".");
        mapping = mapping.replace("\\\\'", "'");
        mapping = mapping.replace("\\\\\"", "\"");

        return mapping;
    }


    @Nonnull
    private static String removeSlashes(@Nonnull String mapping) {
        String friendly;
        int index = mapping.indexOf('\\');

        if (index == -1) {
            friendly = mapping;
        } else {
            StringBuilder builder = new StringBuilder(mapping.substring(0, index));
            char[] data = mapping.toCharArray();
            boolean skipSlash = true;

            while (++index != data.length) {
                if (!skipSlash && data[index] == '\\') {
                    skipSlash = true;
                } else {
                    builder.append(data[index]);
                    skipSlash = false;
                }
            }

            friendly = builder.toString();
        }

        return friendly;
    }


    /**
     * Similar to {@code target.indexOf(search)}, but handles the case of escaping characters
     * @param target        string to search
     * @param index         index into {@code target} to search from
     * @return              index in {@code target} of {@code search}, or -1 if it was not found
     */
    private static int indexOf(@Nonnull String target, @Nonnull String index) {
        return indexOf(target, index, 0);
    }


    /**
     * Similar to {@code target.indexOf(search, index)}, but handles the case of escaping characters
     * @param target        string to search
     * @param search        unescaped string to search for
     * @param index         index into {@code target} to search from
     * @return              index in {@code target} of {@code search}, or -1 if it was not found
     */
    private static int indexOf(@Nonnull String target, @Nonnull String search, int index) {
        boolean done;

        index--;
        do {
            index = target.indexOf(search, ++index);

            if (index == -1) {
                done = true;
            } else if (index == 0) {
                done = true;
            } else {
                done = (target.charAt(index - 1) != '\\');
            }
        } while (!done);


        return index;
    }


    /**
     * Similar to {@code target.lastIndexOf(search, index)}, but handles the case of escaping characters
     * @param target        string to search
     * @param search        unescaped string to search for
     * @param index         index into {@code target} to search from
     * @return              index in {@code target} of {@code search}, or -1 if it was not found
     */
    private static int lastIndexOf(@Nonnull String target, @Nonnull String search, int index) {
        boolean done;

        index++;
        do {
            index = target.lastIndexOf(search, --index);

            if (index == -1) {
                done = true;
            } else if (index == 0) {
                done = true;
            } else {
                done = (target.charAt(index - 1) != '\\');
            }
        } while (!done);


        return index;
    }
}
