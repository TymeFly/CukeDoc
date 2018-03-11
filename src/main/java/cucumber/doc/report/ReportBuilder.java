package cucumber.doc.report;

/**
 * Create a CukeDoc report.
 */
public interface ReportBuilder {
    /**
     * Write the content of the data model in a format determined by the implementation
     */
    void writeReport();
}
