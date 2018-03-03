package cucumber.doc.report;

import javax.annotation.Nonnull;

import cucumber.doc.model.ApplicationModel;

/**
 * Create a CukeDoc report.
 */
public interface ReportBuilder {
    /**
     * Write the content of the data model in a format determined by the implementation
     * @param results       The data model
     */
    void writeReport(@Nonnull ApplicationModel results);
}
