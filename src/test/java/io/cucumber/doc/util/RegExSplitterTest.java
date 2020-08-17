package io.cucumber.doc.util;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link RegExSplitter}
 */
public class RegExSplitterTest {

    /**
     * Unit test {@link RegExSplitter#getCaptureGroups()}
     */
    @Test
    public void test_getCaptureGroups_EmptyExpression() {
        RegExSplitter splitter = RegExSplitter.compile("");

        Assert.assertEquals("Bad captures", Collections.emptyList(), splitter.getCaptureGroups());
        Assert.assertEquals("Bad Text", Collections.emptyList(), splitter.getUncapturedText());
    }


    /**
     * Unit test {@link RegExSplitter#getCaptureGroups()}
     */
    @Test
    public void test_getCaptureGroups_NoGroup() {
        RegExSplitter splitter = RegExSplitter.compile("No groups here");

        Assert.assertEquals("Bad captures", Collections.emptyList(), splitter.getCaptureGroups());
        Assert.assertEquals("Bad Text", Arrays.asList("No groups here"), splitter.getUncapturedText());
    }


    /**
     * Unit test {@link RegExSplitter#getCaptureGroups()}
     */
    @Test
    public void test_getCaptureGroups_NonCaptureGroup() {
        RegExSplitter splitter = RegExSplitter.compile("No (?:groups) here");

        Assert.assertEquals("Bad captures", Collections.emptyList(), splitter.getCaptureGroups());
        Assert.assertEquals("Bad Text", Arrays.asList("No (?:groups) here"), splitter.getUncapturedText());
    }


    /**
     * Unit test {@link RegExSplitter#getCaptureGroups()}
     */
    @Test
    public void test_getCaptureGroups_MatchingGroup_1() {
        RegExSplitter splitter = RegExSplitter.compile("Just (\\d) group");

        Assert.assertEquals("Bad captures", Collections.singletonList("\\d"), splitter.getCaptureGroups());
        Assert.assertEquals("Bad Text", Arrays.asList("Just ", " group"), splitter.getUncapturedText());
    }


    /**
     * Unit test {@link RegExSplitter#getCaptureGroups()}
     */
    @Test
    public void test_getCaptureGroups_MatchingGroup_2() {
        RegExSplitter splitter = RegExSplitter.compile("Just (\\d) group and (.*) here");

        Assert.assertEquals("Bad captures", Arrays.asList("\\d", ".*"), splitter.getCaptureGroups());
        Assert.assertEquals("Bad Text", Arrays.asList("Just ", " group and ", " here"), splitter.getUncapturedText());
    }


    /**
     * Unit test {@link RegExSplitter#getCaptureGroups()}
     */
    @Test
    public void test_getCaptureGroups_MatchingGroup_3() {
        RegExSplitter splitter = RegExSplitter.compile("Just (\\d) group and (.*) here and \"([a-zA-Z0-9])?\"");

        Assert.assertEquals("Bad captures", Arrays.asList("\\d", ".*", "[a-zA-Z0-9]"), splitter.getCaptureGroups());
        Assert.assertEquals("Bad Text", Arrays.asList("Just ", " group and ", " here and \"", "?\""), splitter.getUncapturedText());
    }



    /**
     * Unit test {@link RegExSplitter#getCaptureGroups()}
     */
    @Test
    public void test_getCaptureGroups_MatchingGroup_Named() {
        RegExSplitter splitter = RegExSplitter.compile("Hello (?<name>\\d+) World");

        Assert.assertEquals("Bad captures", Arrays.asList("\\d+"), splitter.getCaptureGroups());
        Assert.assertEquals("Bad Text", Arrays.asList("Hello ", " World"), splitter.getUncapturedText());
    }


    /**
     * Unit test {@link RegExSplitter#getCaptureGroups()}
     */
    @Test
    public void test_getCaptureGroups_MatchingGroup_BackToBack() {
        RegExSplitter splitter = RegExSplitter.compile("xxx (a?)(b?)(c?)(d?) xxx");

        Assert.assertEquals("Bad captures", Arrays.asList("a?", "b?", "c?", "d?"), splitter.getCaptureGroups());
        Assert.assertEquals("Bad Text", Arrays.asList("xxx ", "", "", "", " xxx"), splitter.getUncapturedText());
    }


    /**
     * Unit test {@link RegExSplitter#getCaptureGroups()}
     */
    @Test
    public void test_getCaptureGroups_MatchingGroup_AtStart() {
        RegExSplitter splitter = RegExSplitter.compile("(a?) xxx");

        Assert.assertEquals("Bad captures", Arrays.asList("a?"), splitter.getCaptureGroups());
        Assert.assertEquals("Bad Text", Arrays.asList("", " xxx"), splitter.getUncapturedText());
    }


    /**
     * Unit test {@link RegExSplitter#getCaptureGroups()}
     */
    @Test
    public void test_getCaptureGroups_MatchingGroup_AtEnd() {
        RegExSplitter splitter = RegExSplitter.compile("xxx (a?)");

        Assert.assertEquals("Bad captures", Arrays.asList("a?"), splitter.getCaptureGroups());
        Assert.assertEquals("Bad Text", Arrays.asList("xxx "), splitter.getUncapturedText());
    }



    /**
     * Unit test {@link RegExSplitter#getCaptureGroups()}
     */
    @Test
    public void test_getCaptureGroups_RecursiveMatchingGroup() {
        RegExSplitter splitter = RegExSplitter.compile("Just (a(b(c))) group");

        Assert.assertEquals("Bad captures", Collections.singletonList("a(b(c))"), splitter.getCaptureGroups());
        Assert.assertEquals("Bad Text", Arrays.asList("Just ", " group"), splitter.getUncapturedText());
    }


    /**
     * Unit test {@link RegExSplitter#getCaptureGroups()}
     */
    @Test
    public void test_getCaptureGroups_EscapedBrackets_Single() {
        RegExSplitter splitter = RegExSplitter.compile("No \\(groups\\) here");

        Assert.assertEquals("Bad captures", Collections.emptyList(), splitter.getCaptureGroups());
        Assert.assertEquals("Bad Text", Arrays.asList("No \\(groups\\) here"), splitter.getUncapturedText());
    }


    /**
     * Unit test {@link RegExSplitter#getCaptureGroups()}
     */
    @Test
    public void test_getCaptureGroups_EscapedBrackets_Double() {
        RegExSplitter splitter = RegExSplitter.compile("No \\(groups\\) here or \\(here\\)!");

        Assert.assertEquals("Bad captures", Collections.emptyList(), splitter.getCaptureGroups());
        Assert.assertEquals("Bad Text", Arrays.asList("No \\(groups\\) here or \\(here\\)!"), splitter.getUncapturedText());
    }


    /**
     * Unit test {@link RegExSplitter#getCaptureGroups()}
     */
    @Test
    public void test_getCaptureGroups_CharacterClassBrackets() {
        RegExSplitter splitter = RegExSplitter.compile("No [(groups)] here");

        Assert.assertEquals("Bad captures", Collections.emptyList(), splitter.getCaptureGroups());
        Assert.assertEquals("Bad Text", Arrays.asList("No [(groups)] here"), splitter.getUncapturedText());
    }


    /**
     * Unit test {@link RegExSplitter#getCaptureGroups()}
     */
    @Test
    public void test_getCaptureGroups_CharacterClassBracketsAndSquareBracket() {
        RegExSplitter splitter = RegExSplitter.compile("No [\\[(groups)\\]] here");

        Assert.assertEquals("Bad captures", Collections.emptyList(), splitter.getCaptureGroups());
        Assert.assertEquals("Bad Text", Arrays.asList("No [\\[(groups)\\]] here"), splitter.getUncapturedText());
    }


    /**
     * Unit test {@link RegExSplitter#getCaptureGroups()}
     */
    @Test
    public void test_getCaptureGroups_MissingBracket_Opening() {
        RegExSplitter splitter = RegExSplitter.compile("No groups) here");

        Assert.assertEquals("Bad captures", Collections.emptyList(), splitter.getCaptureGroups());
        Assert.assertEquals("Bad Text", Arrays.asList("No groups) here"), splitter.getUncapturedText());
    }


    /**
     * Unit test {@link RegExSplitter#getCaptureGroups()}
     */
    @Test
    public void test_getCaptureGroups_MissingBracket_Closing() {
        RegExSplitter splitter = RegExSplitter.compile("No (groups here");

        Assert.assertEquals("Bad captures", Collections.emptyList(), splitter.getCaptureGroups());
        Assert.assertEquals("Bad Text", Arrays.asList("No (groups here"), splitter.getUncapturedText());
    }


    /**
     * Unit test {@link RegExSplitter#getCaptureGroups()}
     */
    @Test
    public void test_getCaptureGroups_EmptyCaptureGroup() {
        RegExSplitter splitter = RegExSplitter.compile("Empty () here");

        Assert.assertEquals("Bad captures", Collections.singletonList(""), splitter.getCaptureGroups());
        Assert.assertEquals("Bad Text", Arrays.asList("Empty ", " here"), splitter.getUncapturedText());
    }


    /**
     * Unit test {@link RegExSplitter#getCaptureGroups()}
     */
    @Test
    public void test_getCaptureGroups_CapturesInsideNonCapture() {
        RegExSplitter splitter = RegExSplitter.compile("abc (?:foo(a*) (b*)) def");

        Assert.assertEquals("Bad captures", Arrays.asList("a*", "b*"), splitter.getCaptureGroups());
        Assert.assertEquals("Bad Text", Arrays.asList("abc (?:foo", " ", ") def"), splitter.getUncapturedText());
    }
}
