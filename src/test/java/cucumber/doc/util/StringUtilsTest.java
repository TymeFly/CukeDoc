package cucumber.doc.util;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link StringUtils}
 */
public class StringUtilsTest {
    /**
     * Unit test {@link StringUtils#firstSentence}
     */
    @Test
    public void test_firstSentence() {
        Assert.assertEquals("Empty", "", StringUtils.firstSentence(""));
        Assert.assertEquals("No stop", "Hello World", StringUtils.firstSentence("Hello World"));
        Assert.assertEquals("With stop", "Hello World.", StringUtils.firstSentence("Hello World."));
        Assert.assertEquals("Two words", "Hello.", StringUtils.firstSentence("Hello. World"));
        Assert.assertEquals("Dots In Word", "Hello.World", StringUtils.firstSentence("Hello.World"));
        Assert.assertEquals("Other punctuation", "Hello!", StringUtils.firstSentence("Hello! World."));

        Assert.assertEquals("\\n new line", "Hello!", StringUtils.firstSentence("Hello!\nWorld."));
        Assert.assertEquals("\\r new line", "Hello!", StringUtils.firstSentence("Hello!\rWorld."));
        Assert.assertEquals("\\n\\r new line", "Hello!", StringUtils.firstSentence("Hello!\n\rWorld."));

        Assert.assertEquals("tab at end of sentence", "Hello!", StringUtils.firstSentence("Hello!\tWorld."));
        Assert.assertEquals("tab and space", "Hi, Hello!", StringUtils.firstSentence("Hi, Hello!\tWorld."));
    }


    /**
     * Unit test {@link StringUtils#asList}
     */
    @Test
    public void test_toList() {
        Assert.assertEquals("Empty", Arrays.asList(), StringUtils.asList(""));
        Assert.assertEquals("Spaces", Arrays.asList(), StringUtils.asList("  "));
        Assert.assertEquals("One item", Arrays.asList("One"), StringUtils.asList("One"));
        Assert.assertEquals("Two words", Arrays.asList("One Two"), StringUtils.asList("One Two"));
        Assert.assertEquals("Two items", Arrays.asList("One","Two"), StringUtils.asList("One,Two"));
        Assert.assertEquals("Two trimmed items", Arrays.asList("One","Two"), StringUtils.asList("  One , Two  "));
        Assert.assertEquals("Ignore empty elements", Arrays.asList("One","Two"), StringUtils.asList(",One,,Two,"));
        Assert.assertEquals("Empty inner elements", Arrays.asList("One","Two"), StringUtils.asList("One,,,Two"));
        Assert.assertEquals("Empty start elements", Arrays.asList("One","Two"), StringUtils.asList(",,,One, Two"));
        Assert.assertEquals("Empty end elements", Arrays.asList("One","Two"), StringUtils.asList("One, Two,,,"));
    }


    /**
     * Unit test {@link StringUtils#asList}
     */
    @Test
    public void test_FromList() {
        Assert.assertEquals("Empty List", "", StringUtils.asString(new Object[]{}));
        Assert.assertEquals("One item", "1", StringUtils.asString(new Object[]{ 1 }));
        Assert.assertEquals("Two items", "1, Two", StringUtils.asString(new Object[]{ 1, "Two" }));
        Assert.assertEquals("Three items", "1, Two, 3.0", StringUtils.asString(new Object[]{ 1, "Two", 3.0 }));
    }


    /**
     * Unit test {@link StringUtils#countSubstrings}
     */
    @Test
    public void test_countSubstrings() {
        Assert.assertEquals("Empty", 0, StringUtils.countSubstrings("", "a", 0, 0));
        Assert.assertEquals("single character", 0, StringUtils.countSubstrings("abcabca", "a", 1, 1));
        Assert.assertEquals("multiple characters", 2, StringUtils.countSubstrings("abcabca", "a", 0, 4));
        Assert.assertEquals("Beyond end of String", 3, StringUtils.countSubstrings("abcabca", "a", 0, 9));
        Assert.assertEquals("At end of substring", 1, StringUtils.countSubstrings("abcd", "d", 0, 3));
    }


    /**
     * Unit test {@link StringUtils#startOfSentence}
     */
    @Test
    public void test_startOfSentence() {
        Assert.assertEquals("Empty", -1, StringUtils.startOfSentence("", 0));
        Assert.assertEquals("One Sentence", -1, StringUtils.startOfSentence("Hello World", 0));
        Assert.assertEquals("Two Sentences. Part1", 7, StringUtils.startOfSentence("Hello. World", 0));
        Assert.assertEquals("Two Sentences. Part2", -1, StringUtils.startOfSentence("Hello. World", 7));
        Assert.assertEquals("Two Sentences. Part1", 7, StringUtils.startOfSentence("Hello. World", 0));
        Assert.assertEquals("With Exclamation", 7, StringUtils.startOfSentence("Hello! World", 0));
        Assert.assertEquals("Three Markers", 7, StringUtils.startOfSentence("Hello? World! test.", 0));
    }


    /**
     * Unit test {@link StringUtils#capitalise}
     */
    @Test
    public void test_capitalise() {
        Assert.assertEquals("Empty", "", StringUtils.capitalise(""));
        Assert.assertEquals("Do Nothing", "Hello. World!", StringUtils.capitalise("Hello. World!"));
        Assert.assertEquals("First Sentence", "Hello. World!", StringUtils.capitalise("hello. World!"));
        Assert.assertEquals("Second Sentence", "Hello. World!", StringUtils.capitalise("Hello. world!"));
        Assert.assertEquals("Both Sentences", "Hello. World!", StringUtils.capitalise("hello. world!"));
        Assert.assertEquals("With Spaces", "  Hello.  World!  ", StringUtils.capitalise("  hello.  world!  "));
        Assert.assertEquals("No Spaces", "Hello.world!", StringUtils.capitalise("hello.world!"));
    }


    /**
     * Unit test {@link StringUtils#skipSpaces}
     */
    @Test
    public void test_skipSpaces() {
        Assert.assertEquals("Empty", -1, StringUtils.skipSpaces("", 0));
        Assert.assertEquals("Spaces", -1, StringUtils.skipSpaces("   ", 0));
        Assert.assertEquals("1 word", 3, StringUtils.skipSpaces("   Hello", 0));
        Assert.assertEquals("2 words. Part 1", 3, StringUtils.skipSpaces("   Hello  World  ", 0));
        Assert.assertEquals("2 words. Part 2", 10, StringUtils.skipSpaces("   Hello  World  ", 8));
        Assert.assertEquals("2 words. Part 3", -1, StringUtils.skipSpaces("   Hello  World  ", 15));
    }


    /**
     * Unit test {@link StringUtils#getTail}
     */
    @Test
    public void test_getTail() {
        Assert.assertEquals("Empty", "", StringUtils.getTail("", "~~"));
        Assert.assertEquals("No delimiter", "abc def ghi", StringUtils.getTail("abc def ghi", "~~"));
        Assert.assertEquals("1 delimiter", "def ghi", StringUtils.getTail("abc~~def ghi", "~~"));
        Assert.assertEquals("multiple delimiters", "ghi", StringUtils.getTail("abc~~def~~ghi", "~~"));
    }


    /**
     * Unit test {@link StringUtils#trim(String, String)}
     */
    @Test
    public void test_trim() {
        Assert.assertEquals("Empty", "", StringUtils.trim("", "~~"));
        Assert.assertEquals("pre-trimmed", "abc", StringUtils.trim("abc", "~~"));
        Assert.assertEquals("prefix", "abc", StringUtils.trim("~~abc", "~~"));
        Assert.assertEquals("suffix", "abc", StringUtils.trim("abc~~", "~~"));
        Assert.assertEquals("both", "abc", StringUtils.trim("~~abc~~", "~~"));
    }


    /**
     * Unit test {@link StringUtils#asString}
     */
    @Test
    public void test_asString() {
        Assert.assertEquals("Empty", "", StringUtils.asString(Collections.emptySet()));
        Assert.assertEquals("Single", "A", StringUtils.asString(Collections.singleton("A")));
        Assert.assertEquals("Multiple", "1, 2, 3", StringUtils.asString(Arrays.asList(1, 2, 3)));
    }
}