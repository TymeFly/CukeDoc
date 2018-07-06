package io.cucumber.doc.util;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Unit test for {@link NoteFormat}
 */
public class NoteFormatTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    /**
     * Unit test {@link NoteFormat#forFile}
     */
    @Test
    public void test_ForFile_Valid() {
        Assert.assertEquals("Unexpected type for feature file", NoteFormat.FEATURE, NoteFormat.forFile("/home/cuke/xyz.feature"));
        Assert.assertEquals("Unexpected type for text file", NoteFormat.TEXT, NoteFormat.forFile("/home/app/readme.txt"));
        Assert.assertEquals("Unexpected type for properties file", NoteFormat.PROPERTIES, NoteFormat.forFile("C:\\app\\cuke.PROPERTIES"));
        Assert.assertEquals("Unexpected type for html file", NoteFormat.HTML, NoteFormat.forFile("C:\\web\\Index.Html"));
    }


    /**
     * Unit test {@link NoteFormat#forFile}
     */
    @Test
    public void test_ForFile_Invalid() throws Exception {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Invalid file type for notes '/home/unknown.extension'");

        NoteFormat.forFile("/home/unknown.extension");
    }
}