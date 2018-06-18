package cucumber.doc.report.html;

import java.io.File;
import java.net.URL;

import com.sun.javadoc.DocErrorReporter;
import cucumber.doc.config.Config;
import cucumber.doc.model.ApplicationModel;
import cucumber.doc.report.ReportBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import test_helper.checker.BinaryFileChecker;
import test_helper.checker.DirectoryChecker;
import test_helper.checker.TextFileChecker;
import test_helper.utils.Sample;

import static org.mockito.Mockito.mock;

/**
 * Unit test for {@link HtmlReport}
 */
public class HtmlReportTest {
    @Rule
    public TemporaryFolder temp = new TemporaryFolder();
    private ApplicationModel app;

    private String tempDirectory;


    @Before
    public void setUp() {
        tempDirectory = temp.getRoot().getAbsolutePath();

        DocErrorReporter reporter = mock(DocErrorReporter.class);

        Config.newInstance().applyOptions(new String[][]{{"-d", tempDirectory}}, reporter);

        app = Sample.app();
    }


    /**
     * Unit test {@link HtmlReport#writeReport}
     */
    @Test
    public void test_WriteReport() throws Exception {
        URL expectedFile = getClass().getClassLoader().getResource("report/html-full/index.html");
        String expected = new File(expectedFile.toURI()).getAbsoluteFile().getParent();

        ReportBuilder report = new HtmlReport(app);

        report.writeReport();

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