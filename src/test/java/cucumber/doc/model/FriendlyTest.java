package cucumber.doc.model;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link Friendly}
 */
public class FriendlyTest {
    /**
     * Unit test {@link Friendly#name}
     */
    @Test
    public void test_friendlyName() {
        Assert.assertEquals("Empty String", "", Friendly.name(""));
        Assert.assertEquals("No Change required", "Hello", Friendly.name("Hello"));
        Assert.assertEquals("Upper First", "Hello", Friendly.name("hello"));
        Assert.assertEquals("CammelCase", "Hello World", Friendly.name("HelloWorld"));
        Assert.assertEquals("UnderscoreCase#1", "Hello World", Friendly.name("Hello_world"));
        Assert.assertEquals("UnderscoreCase#2", "Hello World", Friendly.name("Hello_World"));
        Assert.assertEquals("UnderscoreCase#3", "Hello World", Friendly.name("Hello__World"));
        Assert.assertEquals("With TLA", "My TLA", Friendly.name("MyTLA"));
        Assert.assertEquals("All changes", "My TLA Is Best!", Friendly.name("myTLA__is_best!"));
    }


    /**
     * Unit test {@link Friendly#description(String)}
     */
    @Test
    public void test_friendlyDescription() {
        Assert.assertEquals("Null Description", null, Friendly.description(null));
        Assert.assertEquals("Empty Description", "", Friendly.description(""));
        Assert.assertEquals("No Change required", "Hello world.", Friendly.description("Hello world."));
        Assert.assertEquals("Trim", "Hello world.", Friendly.description("  Hello world.  "));
        Assert.assertEquals("Remove additional white space", "Hello world.", Friendly.description("Hello  world."));
        Assert.assertEquals("Start with Caps", "Hello world.", Friendly.description("hello world."));
        Assert.assertEquals("Inner Caps", "Hello. World.", Friendly.description("Hello. world."));
        Assert.assertEquals("Using !", "Hello! World.", Friendly.description("Hello! world."));
        Assert.assertEquals("Starts with Stop", ". Hello world.", Friendly.description(". Hello world."));
        Assert.assertEquals("Stop in word", "He.llo world", Friendly.description("He.llo world"));
        Assert.assertEquals("Multiple changes", ". He.llo! World", Friendly.description(".  he.llo!   world   "));
    }


    /**
     * Unit test {@link Friendly#captureGroup(String)}
     */
    @Test
    public void test_friendlyCaptureGroup() {
        Assert.assertEquals("Empty String", "", Friendly.captureGroup(""));
        Assert.assertEquals("Number", "\\d+", Friendly.captureGroup("\\\\d+"));
        Assert.assertEquals("2 Numbers", "\\d\\d", Friendly.captureGroup("\\\\d\\\\d"));
        Assert.assertEquals("Char Set", "ab\\_~", Friendly.captureGroup("ab\\\\_~"));
        Assert.assertEquals("Quoted String", "([^\"]*)", Friendly.captureGroup("([^\\\"]*)"));
        Assert.assertEquals("Invalid", "", Friendly.captureGroup("\\"));
    }


    /**
     * Unit test {@link Friendly#cleanNoncaptureGroups(String)}
     */
    @Test
    public void test_cleanNoncaptureGroups() {
        Assert.assertEquals("Empty String", "", Friendly.cleanNoncaptureGroups(""));
        Assert.assertEquals("No Group", "My Mapping", Friendly.cleanNoncaptureGroups("My Mapping"));
        Assert.assertEquals("One Group", "My (first) Mapping", Friendly.cleanNoncaptureGroups("My (?:first) Mapping"));
        Assert.assertEquals("Two Groups", "My (first) Mapping (second)", Friendly.cleanNoncaptureGroups("My (?:first) Mapping (?:second)"));
        Assert.assertEquals("Nested Groups", "xxx (out (in)) xxx", Friendly.cleanNoncaptureGroups("xxx (?:out (?:in)) xxx"));
        Assert.assertEquals("Escaped", "My \\(?:first) Mapping", Friendly.cleanNoncaptureGroups("My \\(?:first) Mapping"));
    }


    /**
     * Unit test {@link Friendly#removeSpecialConstructs(String)}
     */
    @Test
    public void test_removeSpecialConstructs() {
        Assert.assertEquals("Empty String", "", Friendly.removeSpecialConstructs(""));
        Assert.assertEquals("No Constructs", "No Constructs", Friendly.removeSpecialConstructs("No Constructs"));
        Assert.assertEquals("Single flag", "My Flag", Friendly.removeSpecialConstructs("My (?i)Flag"));
        Assert.assertEquals("Lookahead", "My Lookahead", Friendly.removeSpecialConstructs("My (?=9)Lookahead"));
        Assert.assertEquals("Multiple constructs", "my Lookahead ", Friendly.removeSpecialConstructs("(?i)my Lookahead (?=9)"));
    }


    /**
     * Unit test {@link Friendly#cleanQuantifiers(String, char)}.
     * Optional words could be retro fitted to make a mapping more obvious with out breaking
     * backwards compatibility. An optional letter 's' could be used to allow an english language word
     * to made plural
     */
    @Test
    public void test_cleanQuantifiers_optional() {
        Assert.assertEquals("Empty String", "", Friendly.cleanQuantifiers("", '?'));
        Assert.assertEquals("No Constructs", "No Constructs", Friendly.cleanQuantifiers("No Constructs", '?'));
        Assert.assertEquals("Single letter", "document(s?) here", Friendly.cleanQuantifiers("documents? here", '?'));
        Assert.assertEquals("Two letters", "(a?) (b?)", Friendly.cleanQuantifiers("a? b?", '?'));
        Assert.assertEquals("Single word", "(Hello?)", Friendly.cleanQuantifiers("(Hello)?", '?'));
        Assert.assertEquals("As First Char", "?abc", Friendly.cleanQuantifiers("?abc", '?'));
        Assert.assertEquals("Escaped", "a\\? b", Friendly.cleanQuantifiers("a\\? b", '?'));
    }


    /**
     * Unit test {@link Friendly#cleanQuantifiers(String, char)}
     * Not sure who would ever use this, outside of a capture group
     */
    @Test
    public void test_cleanQuantifiers_repeated() {
        Assert.assertEquals("Empty String", "", Friendly.cleanQuantifiers("", '+'));
        Assert.assertEquals("No Constructs", "No Constructs", Friendly.cleanQuantifiers("No Constructs", '+'));
        Assert.assertEquals("Single letter", "document(s+) here", Friendly.cleanQuantifiers("documents+ here", '+'));
        Assert.assertEquals("Two letters", "(a+) (b+)", Friendly.cleanQuantifiers("a+ b+", '+'));
        Assert.assertEquals("Single word", "(Hello+)", Friendly.cleanQuantifiers("(Hello)+", '+'));
        Assert.assertEquals("As First Char", "+abc", Friendly.cleanQuantifiers("+abc", '+'));
        Assert.assertEquals("Escaped", "a\\+ b", Friendly.cleanQuantifiers("a\\+ b", '+'));
    }

    /**
     * Unit test {@link Friendly#cleanQuantifiers(String, char)}
     * Not sure who would ever use this, outside of a capture group
     */
    @Test
    public void test_cleanQuantifiers_repeated_or_optional() {
        Assert.assertEquals("Empty String", "", Friendly.cleanQuantifiers("", '*'));
        Assert.assertEquals("No Constructs", "No Constructs", Friendly.cleanQuantifiers("No Constructs", '*'));
        Assert.assertEquals("Single letter", "document(s*) here", Friendly.cleanQuantifiers("documents* here", '*'));
        Assert.assertEquals("Two letters", "(a*) (b*)", Friendly.cleanQuantifiers("a* b*", '*'));
        Assert.assertEquals("Two letters - no break", "(a*)(b*)c", Friendly.cleanQuantifiers("a*b*c", '*'));
        Assert.assertEquals("Single word", "(Hello*)", Friendly.cleanQuantifiers("(Hello)*", '*'));
        Assert.assertEquals("As First Char", "*abc", Friendly.cleanQuantifiers("*abc", '*'));
        Assert.assertEquals("Escaped", "a\\* b", Friendly.cleanQuantifiers("a\\* b", '*'));
        Assert.assertEquals("Single letter - escaped", "Hello(\\)*)", Friendly.cleanQuantifiers("Hello\\)*", '*'));
    }


    /**
     * Unit test {@link Friendly#removeBrackets(String)}
     */
    @Test
    public void test_removeBrackets() {
        Assert.assertEquals("Empty String", "", Friendly.removeBrackets(""));
        Assert.assertEquals("No Constructs", "No Constructs", Friendly.removeBrackets("No Constructs"));
        Assert.assertEquals("One group", "abc def ghi", Friendly.removeBrackets("abc (def) ghi"));
        Assert.assertEquals("Two groups", "abc def ghi", Friendly.removeBrackets("(abc) def (ghi)"));
        Assert.assertEquals("Escaped opening bracket", "abc \\(def ghi", Friendly.removeBrackets("(abc \\(def) ghi"));
        Assert.assertEquals("Quantified?", "(abc?) def ghi", Friendly.removeBrackets("(abc?) def ghi"));
        Assert.assertEquals("Quantified*", "abc (def*) ghi", Friendly.removeBrackets("abc (def*) ghi"));
        Assert.assertEquals("Quantified+", "abc def (ghi+)", Friendly.removeBrackets("abc def (ghi+)"));
        Assert.assertEquals("No opening bracket", "abc def)", Friendly.removeBrackets("abc def)"));
        Assert.assertEquals("Closing bracket at start", ")abc def", Friendly.removeBrackets(")abc def"));
    }


    /**
     * Unit test {@link Friendly#mapping}
     */
    @Test
    public void test_mapping() {
        Assert.assertEquals("Empty Mapping", "", Friendly.mapping("", Collections.emptyList()));
        Assert.assertEquals("Pass through", "Hello World", Friendly.mapping("Hello World", Collections.emptyList()));
        Assert.assertEquals("Quoted string", "\"Hello World\"", Friendly.mapping("\"Hello World\"", Collections.emptyList()));
        Assert.assertEquals("With Anchors", "Hello Worlds", Friendly.mapping("^Hello Worlds$", Collections.emptyList()));
        Assert.assertEquals("Optional Char", "Hello World(s?)", Friendly.mapping("Hello Worlds?", Collections.emptyList()));
        Assert.assertEquals("Literal ?", "Hello World?", Friendly.mapping("Hello World\\?", Collections.emptyList()));
        Assert.assertEquals("Literal .", "Hello World.", Friendly.mapping("Hello World\\.", Collections.emptyList()));
        Assert.assertEquals("Optional terminator", "Hello World", Friendly.mapping("^Hello World\\\\.?$", Collections.emptyList()));
        Assert.assertEquals("IPv4 Address", "IP <o1>.<o2>.<o3>.<o4>", Friendly.mapping("IP (\\d+)\\.(\\d+)\\.(\\d+)\\.(\\d+)",
                    Arrays.asList(new ParameterModel("o1", "byte", "\\d+", "Octet1"),
                                  new ParameterModel("o2", "byte", "\\d+", "Octet2"),
                                  new ParameterModel("o3", "byte", "\\d+", "Octet3"),
                                  new ParameterModel("o4", "byte", "\\d+", "Octet4"))));
    }
}