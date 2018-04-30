package cucumber.doc.parse;

import java.io.File;
import java.net.URL;

import javax.annotation.Nonnull;

import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.RootDoc;
import cucumber.doc.config.Config;
import cucumber.doc.model.ApplicationModel;
import cucumber.doc.util.NoteFormat;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import test_helpers.Sample;

import static org.mockito.Mockito.mock;

/**
 * Unit test for {@link ModelBuilder}
 */
public class ModelBuilderTest {
    private Config config;
    private DocErrorReporter reporter = mock(DocErrorReporter.class);


    @Before
    public void setUp() {
        config = Config.newInstance();
    }


    /**
     * Unit test {@link ModelBuilder#build}
     */
    @Test
    public void test_Build() throws Exception {
        RootDoc app = Sample.rootDoc();

        config.applyOptions(new String[][]{{"-notes", findFile("notes/note.html")},
                                           {"-notes", findFile("notes/note.txt")},
                                           {"-notes", findFile("sample/sample1.feature")},
                                           {"-link", findFile("report/xml/sample.xml")}},
                            reporter);

        ApplicationModel actual = new ModelBuilder(app).build();

        // Expected 3 direct notes and 2 from the linked description
        Assert.assertEquals("Unexpected Note count", 5, actual.getNotes().size());
        Assert.assertTrue("Unexpected Note 1 Content: " + actual.getNotes().get(0).getText(),
                            actual.getNotes().get(0).getText().startsWith("This is a <b>html</b> note"));
        Assert.assertEquals("Unexpected Note 1 Type", NoteFormat.HTML, actual.getNotes().get(0).getFormat());
        Assert.assertTrue("Unexpected Note 2 Content: " + actual.getNotes().get(1).getText(),
                            actual.getNotes().get(1).getText().startsWith("This is a text note"));
        Assert.assertEquals("Unexpected Note 2 Type", NoteFormat.TEXT, actual.getNotes().get(1).getFormat());
        Assert.assertTrue("Unexpected Note 3 Content: " + actual.getNotes().get(2).getText(),
                            actual.getNotes().get(2).getText().startsWith("Feature: Sample Feature"));
        Assert.assertEquals("Unexpected Note 3 Type", NoteFormat.FEATURE, actual.getNotes().get(2).getFormat());
        Assert.assertTrue("Unexpected Note 4 Content: " + actual.getNotes().get(3).getText(),
                            actual.getNotes().get(3).getText().startsWith("Line1"));
        Assert.assertEquals("Unexpected Note 4 Type", NoteFormat.TEXT, actual.getNotes().get(3).getFormat());
        Assert.assertTrue("Unexpected Note 5 Content: " + actual.getNotes().get(4).getText(),
                            actual.getNotes().get(4).getText().startsWith("note2"));
        Assert.assertEquals("Unexpected Note 5 Type", NoteFormat.HTML, actual.getNotes().get(4).getFormat());

        // Expected 2 classes from the app and 2 that were linked, sorted by name
        Assert.assertEquals("Unexpected Type count", 4, actual.getTypes().size());
        Assert.assertEquals("Unexpected Type 1 name", "a.b.Class1", actual.getTypes().get(0).getQualifiedName());
        Assert.assertEquals("Unexpected Type 2 name", "a.b.c.Class1", actual.getTypes().get(1).getQualifiedName());
        Assert.assertEquals("Unexpected Type 3 name", "a.b.c.Class2", actual.getTypes().get(2).getQualifiedName());
        Assert.assertEquals("Unexpected Type 4 name", "d.e.f.Class2", actual.getTypes().get(3).getQualifiedName());

        // Expected 2 mappings from the app and 2 that were linked, sorted by regEx
        Assert.assertEquals("Unexpected Type count", 4, actual.getTypes().size());
        Assert.assertEquals("Unexpected mapping1", "Foo Bar", actual.getMappings().get(0).getRegEx());
        Assert.assertEquals("Unexpected mapping2", "Hello (\\d+)", actual.getMappings().get(1).getRegEx());
        Assert.assertEquals("Unexpected mapping3", "World (\\d+)", actual.getMappings().get(2).getRegEx());
        Assert.assertEquals("Unexpected mapping4", "mapping1", actual.getMappings().get(3).getRegEx());
    }


    @Nonnull
    private String findFile(@Nonnull String fileName) throws Exception {
        URL expectedLocation = getClass().getClassLoader().getResource(fileName);
        String location = new File(expectedLocation.toURI()).getAbsolutePath();

        return location;
    }
}