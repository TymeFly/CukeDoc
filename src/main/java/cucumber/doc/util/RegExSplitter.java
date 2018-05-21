package cucumber.doc.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * Split a regular expression into its component parts. These can be read via calls to
 * {@link #getUncapturedText()} and {@link #getCaptureGroups()}.
 */
public class RegExSplitter {
    private static final char EOS = (char) -1;

    private final String regEx;

    // used to parse the regEx
    private List<String> captureGroups = new ArrayList<>();
    private List<String> uncaptured = new ArrayList<>();
    private int next = 0;
    private int nestCount = 0;
    private int textStart = 0;


    private RegExSplitter(@Nonnull String regEx) {
        this.regEx = regEx.trim();
    }


    /**
     * Static Factory method to compile a regular expression into its component parts
     * @param regEx     Regular expression to parse
     * @return          An instance of {@link RegExSplitter}
     */
    @Nonnull
    public static RegExSplitter compile(@Nonnull String regEx) {
        RegExSplitter parser = new RegExSplitter(regEx);

        parser.parse();

        return parser;
    }


    private void parse() {
        parse(EOS);

        if (textStart != next) {
            String text = regEx.substring(textStart, next);
            uncaptured.add(text);
        }

        captureGroups = Collections.unmodifiableList(captureGroups);
        uncaptured = Collections.unmodifiableList(uncaptured);
    }


    private char parse(char end) {
        char in = nextChar();

        while ((in != EOS) && (in != end)) {
            if (in == '[') {
                in = parseCharacterClass();
            } else if (in == '(') {
                in = parseGroup();
            } else if (in == '\\') {
                nextChar();
                in = nextChar();
            } else {
                in = nextChar();
            }
        }

        return in;
    }


    /**
     * As we've just read a '[', Parse a character class.
     * @return          next character in the {@link #regEx} to process
     */
    private char parseCharacterClass() {
        char in = nextChar();

        while ((in != EOS) && (in != ']')) {
            if (in == '\\') {
                nextChar();        // Skip escaped character
            }

            in = nextChar();
        }

        return in;
    }


    /**
     * As we've just read a '(', parse either a capture or a non-capture group.
     * @return          next character in the {@link #regEx} to process
     */
    private char parseGroup() {
        char in;

        if (peek(0) != '?') {
            in = parseCaptureGroup(0);
        } else if (peek(1) != '<') {
            in = parseNonCaptureGroup();
        } else {
            in = parseNamedCaptureGroup();
        }

        return in;
    }


    /**
     * Parse a non-capturing group
     * @return          next character in the {@link #regEx} to process
     */
    private char parseNonCaptureGroup() {
        parse(')');

        return nextChar();
    }


    /**
     * Parse the capture group updating {@link #captureGroups} and {@link #uncaptured}
     * @param skip      Number of characters between the opening '(' and the first character to store
     *                  in the capture group.
     * @return          next character in the {@link #regEx} to process
     */
    private char parseCaptureGroup(int skip) {
        nestCount++;

        int start = next;
        char in = parse(')');
        int end = next - 1;

        --nestCount;
        if ((nestCount == 0) && (in == ')')) {
            String text = regEx.substring(textStart, start - skip - 1);
            String capture = regEx.substring(start, end);

            captureGroups.add(capture);
            uncaptured.add(text);
            textStart = next;
        } else {
            nextChar();
        }

        return in;
    }


    /**
     * Parse a Named capture group. Next two chars are known to be '?' and '<'
     * @return          next character in the {@link #regEx} to process
     */
    private char parseNamedCaptureGroup() {
        // Skip name - we don't care about it
        char in = nextChar();
        int start = next;

        while ((in != EOS) && (in != '>')) {
            in = nextChar();
        }

        return parseCaptureGroup(next - start + 1);
    }


    /**
     * Returns the next character in the regular expression or {@link #EOS} if there are none. Move the
     * {@link #next} index on as required
     * @return          The next character in the {@link #regEx}
     */
    private char nextChar() {
        return (next >= regEx.length() ? EOS : regEx.charAt(next++));
    }


    /**
     * Returns characters in the regular expression or {@link #EOS} if there are none. The
     * {@link #next} index will not be modified
     * @param offset    0 based offset from {@link #next}
     * @return          next character in the {@link #regEx} to process
     */
    private char peek(int offset) {
        int index = next + offset;

        return (index >= regEx.length() ? EOS : regEx.charAt(index));
    }


    /**
     * Returns the content of the capture groups. This may be an empty list in the case that the
     * original regular expression contained no capture groups.
     * @return the content of the capture groups
     */
    @Nonnull
    public List<String> getCaptureGroups() {
        return captureGroups;
    }


    /**
     * Returns the uncaptured text from the Regular Expression. After each block of uncaptured text in the
     * original regular expression there will be text from a capture group. It is possible for this list to start
     * with an empty string, which indicates that the regular expression started with a capture group
     * @return the uncaptured text from the Regular Expression.
     */
    @Nonnull
    public List<String> getUncapturedText() {
        return uncaptured;
    }


    @Override
    public String toString() {
        return "RegExParser{regEx='" + regEx + '\'' + '}';
    }
}