package test_helper.checker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Nonnull;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Check two text files to see if they contain the same text. EOL markers are not matched, so a *nix encoded file
 * is considered equal to Windows encoded file is the content of the lines is the same.
 * Each of the lines in the actual and expected files are read in and modified according to any of the optional
 * 'line modifiers' that have been set. These allow for basic text manipulation such as trimming white space.
 * Then the lines are then compared according to see if the are the same. By default this is done by the rules of
 * {@link String#equals(Object)} but this may be changed by any of the optional 'match modifiers' that have been set.
 * These allow for more subtle matching such as case insensitive matches.
 */
public class TextFileChecker implements FileChecker {
    private boolean trim = false;
    private boolean ignoreCase = false;
    private Collection<String> ignore = new HashSet<>();
    private Collection<String> regEx = new ArrayList<>();


    /**
     * A line modifier that is used to request white space at the start and end of each line is ignored
     * @return      a flowing interface
     */
    @Nonnull
    public TextFileChecker ignoreSurroundingWhiteSpace() {
        trim = true;

        return this;
    }


    /**
     * A match modifier that is used to request specific, completed, lines are not checked.
     * Typical usage is for lines (such as the current date) that will change on each run.
     * This method may be called multiple times to register multiple lines that need to be ignored.
     * @param line  a complete line of text that will not be checked
     * @return      a flowing interface
     */
    @Nonnull
    public TextFileChecker ignoreLine(@Nonnull String line) {
        ignore.add(line);

        return this;
    }

    /**
     * A match modifier that is used to perform a case insensitive search. Line comparisons will use the rule of
     * {@link String#equalsIgnoreCase(String)} rather then {@link String#equals(Object)}
     * @return      a flowing interface
     */
    @Nonnull
    public TextFileChecker ignoreCase() {
        ignoreCase = true;

        return this;
    }


    /**
     * A matching modifier that is used to check against a regular expression instead of a direct comparison.
     * The {@literal ^} and {@literal $} anchors can be used together to force matching of complete lines.
     * This method can be called multiple time to set define multiple regular expressions that can be applied.
     * @param section       A regular expression that defines a section of text
     * @return              A flowing interface
     */
    @Nonnull
    public TextFileChecker matchRegEx(@Nonnull String section) {
        regEx.add(section);

        return this;
    }



    @Override
    public void check(@Nonnull Path expectedFile, @Nonnull Path actualFile) {
        List<String> expected = readFile(expectedFile);
        List<String> actual = readFile(actualFile);

        boolean sameSize = expected.size() == actual.size();
        int size = Math.min(expected.size(), actual.size());

        for (int line = 0; line < size; line++) {
            String expectedRaw = expected.get(line);
            String actualRaw = actual.get(line);

            String expectedLine = expectedRaw;
            String actualLine = actualRaw;

            // Apply the 'line modifiers'
            if (trim) {
                expectedLine = expectedLine.trim();
                actualLine = actualLine.trim();
            }

            // Run the tests on order of complexity. Low to High
            boolean matched = ignore.contains(expectedLine);
            matched = matched || ignore.contains(actualLine);
            matched = matched || equalStringCheck(expectedLine, actualLine);
            matched = matched || equalRegExCheck(expectedLine, actualLine);

            if (!matched) {
                // AssertEquals will always fail, however it will display a formatted error message
                assertEquals("Difference between " + expectedFile + " and " + actualFile + " on line " + (line + 1) +
                             (sameSize ? "" : " with file size mismatch (" +
                                    "Expected file has " + expected.size() + " lines," +
                                    " but actual file has " + actual.size() + " lines" + ")"),
                             expectedRaw, actualRaw);
            }
        }

        assertTrue("File size mismatch. Expected file " + expectedFile + " has " + expected.size() + " lines," +
                " but actual file " + actualFile + " has " + actual.size() + " lines.", sameSize);
    }


    private boolean equalStringCheck(@Nonnull String expectedTest, @Nonnull String actualTest) {
        return (ignoreCase ? expectedTest.equalsIgnoreCase(actualTest) : expectedTest.equals(actualTest));
    }


    private boolean equalRegExCheck(@Nonnull String expected, @Nonnull String actual) {
        for (String s : regEx) {
            expected = expected.replaceAll(s, "");
            actual = actual.replaceAll(s, "");
        }

        return equalStringCheck(expected, actual);
    }


    @Nonnull
    private List<String> readFile(@Nonnull Path file) {
        List<String> lines;

        try {
            lines = Files.readAllLines(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file " + file.toFile().getAbsolutePath());
        }

        return lines;
    }
}
