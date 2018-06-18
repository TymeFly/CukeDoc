package cucumber.doc.report.html;

import java.io.File;
import java.net.URL;

import javax.annotation.Nonnull;

import com.sun.javadoc.DocErrorReporter;
import cucumber.doc.config.Config;
import cucumber.doc.model.ApplicationModel;
import cucumber.doc.model.NoteModel;
import cucumber.doc.util.NoteFormat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import test_helper.checker.BinaryFileChecker;
import test_helper.checker.DirectoryChecker;
import test_helper.checker.TextFileChecker;
import test_helper.utils.Sample;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

/**
 * Unit test for {@link HtmlReport}.
 * <br/>
 * The "Unit" we are testing is the complete package, as each production class creates exactly one
 * file it should be easy enough to determine where any faults may be.
 */
public class HtmlReportTest {
    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    private String tempDirectory;


    @Before
    public void setUp() {
        tempDirectory = temp.getRoot().getAbsolutePath();

        DocErrorReporter reporter = mock(DocErrorReporter.class);

        Config.newInstance().applyOptions(new String[][]{{"-d", tempDirectory}}, reporter);
    }


    /**
     * Unit test {@link HtmlReport#writeReport}
     */
    @Test
    public void test_WriteReport_FullApp() throws Exception {
        new HtmlReport(Sample.app()).writeReport();

        validate("html-full");
    }


    /**
     * Unit test {@link HtmlReport#writeReport}
     */
    @Test
    public void test_WriteReport_EmptyApp() throws Exception {
        new HtmlReport(Sample.emptyApp()).writeReport();

        validate("html-empty");
    }


    /**
     * Unit test {@link HtmlReport#writeReport}
     */
    @Test
    public void test_WriteReport_SingleNote() throws Exception {
        ApplicationModel app = new ApplicationModel.Builder()
                        .withNote(new NoteModel("MyNote", "Note 3", NoteFormat.HTML))
                        .build();

        new HtmlReport(app).writeReport();

        validate("html-single-note");
    }


    private void validate(@Nonnull String testName) throws Exception {
        String pathName = "report/" + testName + "/index.html";
        URL expectedFile = getClass().getClassLoader().getResource(pathName);

        if (expectedFile == null) {
            fail("Could not find expected files for " + testName);
        }

        String expected = new File(expectedFile.toURI()).getAbsoluteFile().getParent();

        new DirectoryChecker(expected, tempDirectory).recursive()
            .check("*.html", new TextFileChecker().ignoreSurroundingWhiteSpace()
                                .ignoreLine("Built on dd MMM yyyy at hh:mm")
                                .matchRegEx("href=\"[a-zA-Z0-9_.]+\\.html#_id\\d+_\"")
                                .matchRegEx("<a href=\"#_id\\d+_\">")
                                .matchRegEx("<a name=\"_id\\d+_\">"))
            .check("*.css", new TextFileChecker())
            .check("*.js", new TextFileChecker())
            .check("*.png", new BinaryFileChecker())
            .assertAllChecked();
    }
}