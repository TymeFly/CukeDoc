package cucumber.doc.report.basic;

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
import test_helpers.Sample;
import test_helpers.TextFileChecker;

import static org.mockito.Mockito.mock;

/**
 * Unit test for {@link BasicReport}
 */
public class BasicReportTest {
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
     * Unit test {@link BasicReport#writeReport}
     */
    @Test
    public void test_WriteReport() throws Exception {
        URL expectedLocation = getClass().getClassLoader().getResource("report/basic/basic.txt");
        String expected = new File(expectedLocation.toURI()).getAbsolutePath();

        ReportBuilder report = new BasicReport(app);

        report.writeReport();

        new TextFileChecker()
            .ignoreSurroundingWhiteSpace()
            .ignoreLine("Built on dd MMM yyyy at hh:mm")
            .check(expected, tempDirectory + "/mappings.txt");
    }
}