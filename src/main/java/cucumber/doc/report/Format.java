package cucumber.doc.report;

import java.util.function.Function;

import javax.annotation.Nonnull;

import cucumber.doc.model.ApplicationModel;
import cucumber.doc.report.basic.BasicReport;
import cucumber.doc.report.html.HtmlReport;
import cucumber.doc.report.xml.XmlReport;

/**
 * Defines the different types of report that can be generated by CukeDoc.
 */
public enum Format {
    /** Report as a simple text file */
    BASIC(BasicReport::new),

    /** Report as a XML document */
    XML(XmlReport::new),

    /** Report as a set of HTML pages */
    HTML(HtmlReport::new);


    private final Function<ApplicationModel, ReportBuilder> constructor;


    Format(@Nonnull Function<ApplicationModel, ReportBuilder> constructor) {
        this.constructor = constructor;
    }


    /**
     * Returns a report builder for this format
     * @param model         application model
     * @return a report builder for this format
     */
    @Nonnull
    public ReportBuilder getReportBuilder(@Nonnull ApplicationModel model) {
        return constructor.apply(model);
    }
}
