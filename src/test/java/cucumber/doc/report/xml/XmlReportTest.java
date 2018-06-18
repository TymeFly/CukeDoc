package cucumber.doc.report.xml;

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
import test_helpers.checker.TextFileChecker;
import test_helpers.utils.Sample;

import static org.mockito.Mockito.mock;

/**
 * Unit test for {@link XmlReport}
 */
public class XmlReportTest {
    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    private ApplicationModel app;

    private String tempDirectory;

    @Before
    public void setUp() {
        app = Sample.app();

        tempDirectory = temp.getRoot().getAbsolutePath();
        DocErrorReporter reporter = mock(DocErrorReporter.class);

        Config.newInstance().applyOptions(new String[][]{{"-d", tempDirectory}}, reporter);
    }


    /**
     * Unit test {@link XmlReport#writeReport}
     */
    @Test
    public void test_WriteReport() throws Exception {
        URL expectedLocation = getClass().getClassLoader().getResource("report/xml/sample.xml");
        String expected = new File(expectedLocation.toURI()).getAbsolutePath();

        ReportBuilder report = new XmlReport(app);

        report.writeReport();

        new TextFileChecker()
            .ignoreSurroundingWhiteSpace()
            .ignoreLine("<date>dd MMM yyyy at hh:mm</date>")
            .check(expected, tempDirectory + "/cuke-doc.xml");
    }
}