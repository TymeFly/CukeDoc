package cucumber.doc.parse;

import java.io.File;
import java.net.URL;
import java.util.Iterator;

import javax.annotation.Nonnull;

import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.RootDoc;
import cucumber.doc.config.Config;
import cucumber.doc.model.ApplicationModel;
import cucumber.doc.model.NoteModel;
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

        config.applyOptions(new String[][]{{"-notes", "desc", findFile("notes/note.html")},
                                           {"-notes", "desc", findFile("notes/note.txt")},
                                           {"-notes", "name", findFile("sample/sample1.feature")},
                                           {"-link", findFile("report/xml/sample.xml")}},
                            reporter);

        ApplicationModel actual = new ModelBuilder(app).build();
        Iterator<NoteModel> iterator = actual.getNotes().iterator();

        // Expected 3 direct notes and 2 from the linked description
        Assert.assertEquals("Unexpected Note count", 4, actual.getNotes().size());

        NoteModel note = iterator.next();

        Assert.assertEquals("Unexpected Note 1 name", "desc", note.getName());
        Assert.assertTrue("Unexpected Note 1 Content start: " + note.getText(),
                            note.getText().startsWith("This is a <b>html</b> note"));
        Assert.assertTrue("Unexpected Note 1 Content end: " + note.getText(),
                            note.getText().endsWith("Hello World"));
        Assert.assertEquals("Unexpected Note 1 Type", NoteFormat.TEXT, note.getFormat());

        note = iterator.next();
        Assert.assertEquals("Unexpected Note 2 name", "name", note.getName());
        Assert.assertTrue("Unexpected Note 2 Content start: " + note.getText(),
                            note.getText().startsWith("Feature: Sample Feature"));
        Assert.assertTrue("Unexpected Note 2 Content end: " + note.getText(),
                            note.getText().endsWith("Then I should have 2 cukes"));
        Assert.assertEquals("Unexpected Note 2 Type", NoteFormat.FEATURE, note.getFormat());

        note = iterator.next();
        Assert.assertEquals("Unexpected Note 3 name", "name-1", note.getName());
        Assert.assertTrue("Unexpected Note 3 Content start: " + note.getText(),
                            note.getText().startsWith("Note 1.1"));
        Assert.assertTrue("Unexpected Note 3 Content end: " + note.getText(),
                            note.getText().endsWith("Note 3"));
        Assert.assertEquals("Unexpected Note 3 Type", NoteFormat.TEXT, note.getFormat());

        note = iterator.next();
        Assert.assertEquals("Unexpected Note 4 name", "name-2", note.getName());
        Assert.assertTrue("Unexpected Note 4 Content start: " + note.getText(),
                            note.getText().startsWith("Note 2"));
        Assert.assertTrue("Unexpected Note 4 Content end: " + note.getText(),
                            note.getText().endsWith("Note 2"));
        Assert.assertEquals("Unexpected Note 4 Type", NoteFormat.HTML, note.getFormat());

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