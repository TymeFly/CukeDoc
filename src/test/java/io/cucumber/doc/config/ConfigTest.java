package io.cucumber.doc.config;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;

import com.sun.javadoc.DocErrorReporter;
import io.cucumber.doc.report.Format;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.SystemOutRule;
import test_helper.utils.TestFileHelper;

import static org.mockito.Mockito.mock;

/**
 * Unit test for {@link Config}
 */
public class ConfigTest {
    @Rule
    public SystemOutRule standardOut = new SystemOutRule().mute().enableLog();

    @Rule
    public ExpectedSystemExit systemExit = ExpectedSystemExit.none();


    private Config config = Config.newInstance();
    private DocErrorReporter reporter = mock(DocErrorReporter.class);
    private String featureNote;
    private String htmlNote;
    private String textNote;


    @Before
    public void setUp() throws Exception {
        featureNote = TestFileHelper.findFile("sample/sample1.feature");
        htmlNote = TestFileHelper.findFile("note/note.html");
        textNote = TestFileHelper.findFile("note/note.txt");
    }


    /**
     * Unit test {@link Config#requestOption}
     */
    @Test
    public void test_requestOption() {
        Assert.assertEquals("Unexpected option count '-i18n'", 2, config.requestOption("-i18n"));
        Assert.assertEquals("Unexpected option count '-link'", 2, config.requestOption("-link"));
        Assert.assertEquals("Unexpected option count '-windowtitle'", 2, config.requestOption("-windowtitle"));
        Assert.assertEquals("Unexpected option count '-description'", 2, config.requestOption("-description"));
        Assert.assertEquals("Unexpected option count '-footer'", 2, config.requestOption("-footer"));
        Assert.assertEquals("Unexpected option count '-top'", 2, config.requestOption("-top"));
        Assert.assertEquals("Unexpected option count '-bottom'", 2, config.requestOption("-bottom"));
        Assert.assertEquals("Unexpected option count '-d'", 2, config.requestOption("-d"));
        Assert.assertEquals("Unexpected option count '-format'", 2, config.requestOption("-format"));
        Assert.assertEquals("Unexpected option count '-icon'", 2, config.requestOption("-icon"));
        Assert.assertEquals("Unexpected option count '-note'", 3, config.requestOption("-note"));
        Assert.assertEquals("Unexpected option count '-trace'", 1, config.requestOption("-trace"));
        Assert.assertEquals("Unexpected option count '-unknown_option'", 0, config.requestOption("-unknown_option"));
    }


    /**
     * Unit test {@link Config#applyOptions(String[][], DocErrorReporter)}
     */
    @Test
    public void test_requestOption_help() {
        systemExit.expectSystemExitWithStatus(0);

        config.requestOption("-help");
    }


    /**
     * Unit test {@link Config#getLinks}
     */
    @Test
    public void test_GetLinks_Default() {
        boolean valid = config.applyOptions(new String[0][], reporter);

        Assert.assertTrue("Invalid default options", valid);
        Assert.assertEquals("Unexpected links", Collections.emptyList(), new ArrayList<>(config.getLinks()));
    }


    /**
     * Unit test {@link Config#getLinks}
     */
    @Test
    public void test_GetLinks_Valid() throws Exception {
        URL location = getClass().getClassLoader().getResource("config/link.xml");
        String file = new File(location.toURI()).getAbsolutePath();

        boolean valid = config.applyOptions(new String[][]{{"-link", file}}, reporter);

        Assert.assertTrue("Invalid '-link' options", valid);
        Assert.assertEquals("Unexpected links",
                            Arrays.asList(file),
                            new ArrayList<>(config.getLinks()));
    }


    /**
     * Unit test {@link Config#getLinks}
     */
    @Test
    public void test_GetLinks_Invalid() {
        boolean valid = config.applyOptions(new String[][]{{"-link", "a/b/c"}, {"-link", "d/e/f"}}, reporter);

        Assert.assertFalse("Invalid '-link' options", valid);
        Assert.assertEquals("Unexpected links",
                            Arrays.asList("a/b/c", "d/e/f"),
                            new ArrayList<>(config.getLinks()));
    }


    /**
     * Unit test {@link Config#getI18n}
     */
    @Test
    public void test_GetI18n_default() {
        boolean valid = config.applyOptions(new String[0][], reporter);

        Assert.assertTrue("Invalid default options", valid);
        Assert.assertEquals("Unexpected country", I18n.EN, config.getI18n());
    }


    /**
     * Unit test {@link Config#getI18n}
     */
    @Test
    public void test_GetI18n() {
        boolean valid = config.applyOptions(new String[][]{{"-i18n", "fr"}}, reporter);

        Assert.assertTrue("Invalid '-i18n' options", valid);
        Assert.assertEquals("Unexpected country", I18n.FR, config.getI18n());
    }


    /**
     * Unit test {@link Config#getTitle}
     */
    @Test
    public void test_GetTitle_default() {
        boolean valid = config.applyOptions(new String[0][], reporter);

        Assert.assertTrue("Invalid default options", valid);
        Assert.assertEquals("Unexpected title", "CukeDoc", config.getTitle());
    }


    /**
     * Unit test {@link Config#getTitle}
     */
    @Test
    public void test_GetTitle() {
        boolean valid = config.applyOptions(new String[][]{{"-windowtitle", "My Title"}}, reporter);

        Assert.assertTrue("Invalid '-windowtitle' options", valid);
        Assert.assertEquals("Unexpected title", "My Title", config.getTitle());
    }


    /**
     * Unit test {@link Config#getFooter}
     */
    @Test
    public void test_GetFooter_default() {
        boolean valid = config.applyOptions(new String[0][], reporter);

        Assert.assertTrue("Invalid default options", valid);
        Assert.assertEquals("Unexpected footer", "", config.getFooter());
    }


    /**
     * Unit test {@link Config#getFooter}
     */
    @Test
    public void test_GetFooter() {
        boolean valid = config.applyOptions(new String[][]{{"-footer", "My Footer"}}, reporter);

        Assert.assertTrue("Invalid '-footer' options", valid);
        Assert.assertEquals("Unexpected footer", "My Footer", config.getFooter());
    }


    /**
     * Unit test {@link Config#getTop}
     */
    @Test
    public void test_GetTop_default() {
        boolean valid = config.applyOptions(new String[0][], reporter);

        Assert.assertTrue("Invalid default options", valid);
        Assert.assertEquals("Unexpected top", "", config.getTop());
    }


    /**
     * Unit test {@link Config#getTop}
     */
    @Test
    public void test_GetTop() {
        boolean valid = config.applyOptions(new String[][]{{"-top", "My Top Text"}}, reporter);

        Assert.assertTrue("Invalid '-top' options", valid);
        Assert.assertEquals("Unexpected top", "My Top Text", config.getTop());
    }


    /**
     * Unit test {@link Config#getBottom}
     */
    @Test
    public void test_GetBottom_default() {
        boolean valid = config.applyOptions(new String[0][], reporter);

        Assert.assertTrue("Invalid default options", valid);
        Assert.assertEquals("Unexpected bottom", "", config.getBottom());
    }


    /**
     * Unit test {@link Config#getBottom}
     */
    @Test
    public void test_GetBottom() {
        boolean valid = config.applyOptions(new String[][]{{"-bottom", "My Bottom Text"}}, reporter);

        Assert.assertTrue("Invalid '-bottom' options", valid);
        Assert.assertEquals("Unexpected bottom", "My Bottom Text", config.getBottom());
    }


    /**
     * Unit test {@link Config#getDirectory}
     */
    @Test
    public void test_GetDirectory_default() {
        boolean valid = config.applyOptions(new String[0][], reporter);

        Assert.assertTrue("Invalid default options", valid);
        Assert.assertEquals("Unexpected target directory", "target/site/cuke-doc", config.getDirectory());
    }


    /**
     * Unit test {@link Config#getDirectory}
     */
    @Test
    public void test_GetDirectory() {
        boolean valid = config.applyOptions(new String[][]{{"-d", "target/custom/out"}}, reporter);

        Assert.assertTrue("Invalid '-d' options", valid);
        Assert.assertEquals("Unexpected target directory", "target/custom/out", config.getDirectory());
    }


    /**
     * Unit test {@link Config#getIconPath}
     */
    @Test
    public void test_GetIconPath_Default() {
        boolean valid = config.applyOptions(new String[0][], reporter);

        Assert.assertTrue("Invalid default options", valid);
        Assert.assertEquals("Unexpected icon path", "html/cuke-doc.png", config.getIconPath());
    }


    /**
     * Unit test {@link Config#getIconPath}
     */
    @Test
    public void test_GetIconPath_Valid() throws Exception {
        URL location = getClass().getClassLoader().getResource("config/icon.png");
        String file = new File(location.toURI()).getAbsolutePath();

        boolean valid = config.applyOptions(new String[][]{{"-icon", file}}, reporter);

        Assert.assertTrue("Invalid '-icon' options", valid);
        Assert.assertEquals("Unexpected icon path", file, config.getIconPath());
    }


    /**
     * Unit test {@link Config#getIconPath}
     */
    @Test
    public void test_GetIconPath_Invalid() {
        boolean valid = config.applyOptions(new String[][]{{"-icon", "/this/is/an/invalid.png"}}, reporter);

        Assert.assertFalse("Invalid '-icon' options", valid);
        Assert.assertEquals("Unexpected icon path", "/this/is/an/invalid.png", config.getIconPath());
    }


    /**
     * Unit test {@link Config#getNotes}
     */
    @Test
    public void test_GetNotesPath_default() {
        boolean valid = config.applyOptions(new String[0][], reporter);

        Assert.assertTrue("Invalid default options", valid);
        Assert.assertEquals("Unexpected notes", Collections.emptyList(), new ArrayList<>(config.getNotes()));
    }


    /**
     * Unit test {@link Config#getNotes}
     */
    @Test
    public void test_GetNotesPath_valid() throws Exception {
        URL location = getClass().getClassLoader().getResource("config/notes.txt");
        String file = new File(location.toURI()).getAbsolutePath();

        boolean valid = config.applyOptions(new String[][]{{"-note", "valid", file}}, reporter);
        Collection<NoteDescription> actual = config.getNotes();

        Assert.assertTrue("Invalid '-notes' options", valid);
        Assert.assertEquals("Unexpected note count", 1, actual.size());

        NoteDescription desc = actual.iterator().next();

        Assert.assertEquals("Unexpected notes name", "valid", desc.getName());
        Assert.assertEquals("Unexpected notes path", file, desc.getPath());
    }


    /**
     * Unit test {@link Config#getNotes}
     */
    @Test
    public void test_GetNotesPath_Invalid() {
        boolean valid = config.applyOptions(new String[][]{{"-note", "name-1", htmlNote},
                                                           {"-note", "name-2", textNote},
                                                           {"-note", "name-3", featureNote}}, reporter);

        Collection<NoteDescription> actual = config.getNotes();

        Assert.assertTrue("Invalid '-notes' options", valid);
        Assert.assertEquals("Unexpected note count", 3, actual.size());

        Iterator<NoteDescription> iterator = actual.iterator();
        NoteDescription desc = iterator.next();

        Assert.assertEquals("Unexpected notes name", "name-1", desc.getName());
        Assert.assertEquals("Unexpected notes path", htmlNote, desc.getPath());

        desc = iterator.next();
        Assert.assertEquals("Unexpected notes name", "name-2", desc.getName());
        Assert.assertEquals("Unexpected notes path", textNote, desc.getPath());

        desc = iterator.next();
        Assert.assertEquals("Unexpected notes name", "name-3", desc.getName());
        Assert.assertEquals("Unexpected notes path", featureNote, desc.getPath());
    }


    /**
     * Unit test {@link Config#getNotes}
     */
    @Test
    public void test_GetNotesPath_CheckNames()  {
        Assert.assertFalse("Empty Name",
            Config.newInstance().applyOptions(new String[][]{{"-note", "", htmlNote}}, reporter));
        Assert.assertTrue("Single character",
            Config.newInstance().applyOptions(new String[][]{{"-note", "X", htmlNote}}, reporter));
        Assert.assertTrue("Multiple characters",
            Config.newInstance().applyOptions(new String[][]{{"-note", "XYZ", htmlNote}}, reporter));
        Assert.assertTrue("Digits",
            Config.newInstance().applyOptions(new String[][]{{"-note", "123", htmlNote}}, reporter));

        Assert.assertFalse("Starts with space",
            Config.newInstance().applyOptions(new String[][]{{"-note", " index", htmlNote}}, reporter));
        Assert.assertFalse("Ends with space",
            Config.newInstance().applyOptions(new String[][]{{"-note", "index ", htmlNote}}, reporter));
        Assert.assertTrue("Contains space",
            Config.newInstance().applyOptions(new String[][]{{"-note", "index page", htmlNote}}, reporter));

        Assert.assertFalse("Starts with dot",
            Config.newInstance().applyOptions(new String[][]{{"-note", ".index", htmlNote}}, reporter));
        Assert.assertFalse("Ends with dot",
            Config.newInstance().applyOptions(new String[][]{{"-note", "index.", htmlNote}}, reporter));
        Assert.assertTrue("Contains _",
            Config.newInstance().applyOptions(new String[][]{{"-note", "index.page", htmlNote}}, reporter));

        Assert.assertFalse("Starts with _",
            Config.newInstance().applyOptions(new String[][]{{"-note", "_index", htmlNote}}, reporter));
        Assert.assertFalse("Ends with _",
            Config.newInstance().applyOptions(new String[][]{{"-note", "index_", htmlNote}}, reporter));
        Assert.assertTrue("Contains _",
            Config.newInstance().applyOptions(new String[][]{{"-note", "index_page", htmlNote}}, reporter));

        Assert.assertFalse("Starts with -",
            Config.newInstance().applyOptions(new String[][]{{"-note", "-index", htmlNote}}, reporter));
        Assert.assertFalse("Ends with -",
            Config.newInstance().applyOptions(new String[][]{{"-note", "index-", htmlNote}}, reporter));
        Assert.assertTrue("Contains -",
            Config.newInstance().applyOptions(new String[][]{{"-note", "index-page", htmlNote}}, reporter));

        Assert.assertFalse("Starts with *",
            Config.newInstance().applyOptions(new String[][]{{"-note", "*index", htmlNote}}, reporter));
        Assert.assertFalse("Ends with *",
            Config.newInstance().applyOptions(new String[][]{{"-note", "index*", htmlNote}}, reporter));
        Assert.assertFalse("Contains *",
            Config.newInstance().applyOptions(new String[][]{{"-note", "index*page", htmlNote}}, reporter));

        Assert.assertFalse("Starts with ?",
            Config.newInstance().applyOptions(new String[][]{{"-note", "*index", htmlNote}}, reporter));
        Assert.assertFalse("Ends with ?",
            Config.newInstance().applyOptions(new String[][]{{"-note", "index*", htmlNote}}, reporter));
        Assert.assertFalse("Contains ?",
            Config.newInstance().applyOptions(new String[][]{{"-note", "index*page", htmlNote}}, reporter));
    }


    /**
     * Unit test {@link Config#getDescriptionPath}
     */
    @Test
    public void test_GetDescriptionPath_default() {
        boolean valid = config.applyOptions(new String[0][], reporter);

        Assert.assertTrue("Invalid default options", valid);
        Assert.assertEquals("Unexpected description path", null, config.getDescriptionPath());
    }


    /**
     * Unit test {@link Config#getDescriptionPath}
     */
    @Test
    public void test_GetDescriptionPath_Valid() throws Exception {
        URL location = getClass().getClassLoader().getResource("config/description.txt");
        String file = new File(location.toURI()).getAbsolutePath();

        boolean valid = config.applyOptions(new String[][]{{"-description", file}}, reporter);

        Assert.assertTrue("Invalid '-description' options", valid);
        Assert.assertEquals("Unexpected description path", file, config.getDescriptionPath());
    }


    /**
     * Unit test {@link Config#getDescriptionPath}
     */
    @Test
    public void test_GetDescriptionPath_Invalid() {
        boolean valid = config.applyOptions(new String[][]{{"-description", "/this/does/not/exist.txt"}}, reporter);

        Assert.assertFalse("Invalid '-description' options", valid);
        Assert.assertEquals("Unexpected description path", "/this/does/not/exist.txt", config.getDescriptionPath());
    }


    /**
     * Unit test {@link Config#getFormats}
     */
    @Test
    public void test_GetFormats_default() {
        boolean valid = config.applyOptions(new String[0][], reporter);

        Assert.assertTrue("Invalid default options", valid);
        Assert.assertEquals("Unexpected formats", EnumSet.of(Format.HTML, Format.BASIC), config.getFormats());
    }


    /**
     * Unit test {@link Config#getFormats}
     */
    @Test
    public void test_GetFormats() {
        boolean valid = config.applyOptions(new String[][]{{"-format", "basic,xml"}, {"-format", "xml" }}, reporter);

        Assert.assertTrue("Invalid '-format' options", valid);
        Assert.assertEquals("Unexpected formats",
                            Arrays.asList(Format.BASIC, Format.XML),
                            new ArrayList<>(config.getFormats()));
   }


    /**
     * Unit test {@link Config#applyOptions(String[][], DocErrorReporter)}
     */
    @Test(expected = IllegalStateException.class)
    public void test_applyOptions_notApplied() {
        config.getFormats();
    }



    /**
     * Unit test {@link Config#applyOptions(String[][], DocErrorReporter)}
     */
    @Test(expected = IllegalStateException.class)
    public void test_applyOptions_multipleTimes() {
        boolean valid = config.applyOptions(new String[0][], reporter);

        Assert.assertTrue("Invalid default options", valid);

        config.applyOptions(new String[0][], reporter);
    }


    /**
     * Unit test {@link Config#applyOptions(String[][], DocErrorReporter)}
     */
    @Test
    public void test_applyOptions_invalid() {
        boolean valid = config.applyOptions(new String[][]{{"-unknown_option"}}, reporter);

        Assert.assertTrue("Option '-unknown_option' should be ignored", valid);
   }
}