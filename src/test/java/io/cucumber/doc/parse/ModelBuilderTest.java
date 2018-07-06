package io.cucumber.doc.parse;

import java.util.Iterator;

import com.sun.javadoc.DocErrorReporter;
import com.sun.javadoc.RootDoc;
import io.cucumber.doc.config.Config;
import io.cucumber.doc.model.ApplicationModel;
import io.cucumber.doc.model.NoteModel;
import io.cucumber.doc.util.NoteFormat;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import test_helper.utils.Sample;
import test_helper.utils.TestFileHelper;

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

        config.applyOptions(new String[][]{{"-note", "desc", TestFileHelper.findFile("note/note.html")},
                                           {"-note", "desc", TestFileHelper.findFile("note/note.txt")},
                                           {"-note", "name", TestFileHelper.findFile("sample/sample1.feature")},
                                           {"-link", TestFileHelper.findFile("report/xml/sample.xml")}},
                            reporter);

        ApplicationModel actual = new ModelBuilder(app).build();
        Iterator<NoteModel> iterator = actual.getNotes().iterator();

        // Expected 3 direct notes and 2 from the linked description
        Assert.assertEquals("Unexpected Note count", 5, actual.getNotes().size());

        NoteModel note = iterator.next();

        Assert.assertEquals("Unexpected Note 1 name", "Desc", note.getName());
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
        Assert.assertEquals("Unexpected Note 3 name", "Name-1", note.getName());
        Assert.assertTrue("Unexpected Note 3 Content start: " + note.getText(),
                            note.getText().startsWith("Note 1"));
        Assert.assertTrue("Unexpected Note 3 Content end: " + note.getText(),
                            note.getText().endsWith("Note 1"));
        Assert.assertEquals("Unexpected Note 3 Type", NoteFormat.FEATURE, note.getFormat());


        note = iterator.next();
        Assert.assertEquals("Unexpected Note 4 name", "Name-2", note.getName());
        Assert.assertTrue("Unexpected Note 4 Content start: " + note.getText(),
                            note.getText().startsWith("Note 2"));
        Assert.assertTrue("Unexpected Note 4 Content end: " + note.getText(),
                            note.getText().endsWith("Note 2"));
        Assert.assertEquals("Unexpected Note 4 Type", NoteFormat.HTML, note.getFormat());

        note = iterator.next();
        Assert.assertEquals("Unexpected Note 5 name", "To Do", note.getName());
        Assert.assertTrue("Unexpected Note 5 Content start: " + note.getText(),
                            note.getText().startsWith("TODO 1.1"));
        Assert.assertTrue("Unexpected Note 5 Content end: " + note.getText(),
                            note.getText().endsWith("To Do 2.1"));
        Assert.assertEquals("Unexpected Note 5 Type", NoteFormat.TEXT, note.getFormat());

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
}