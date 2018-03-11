package cucumber.doc.report.basic;

import java.util.List;

import javax.annotation.Nonnull;

import cucumber.doc.config.Config;
import cucumber.doc.model.ApplicationModel;
import cucumber.doc.model.ImplementationModel;
import cucumber.doc.model.MappingModel;
import cucumber.doc.model.TypeModel;
import cucumber.doc.report.ReportBuilder;
import cucumber.doc.util.FileUtils;

/**
 * Generate a simple text based report consisting of just the mappings and their locations. The results are
 * written to a single text file.
 */
public class BasicReport implements ReportBuilder {
    private static final String EOL = System.lineSeparator();
    private static final String INDENT = "    ";

    private final ApplicationModel model;

    /**
     * Create a basic text report
     * @param model         application model
     */
    public BasicReport(@Nonnull ApplicationModel model) {
        this.model = model;
    }


    @Override
    public void writeReport() {
        String target = Config.getInstance().getDirectory() + "/mappings.txt";
        String report = generatedReport();

        FileUtils.delete(target);
        FileUtils.write(target, report);
    }


    @Nonnull
    private String generatedReport() {
        StringBuilder builder = new StringBuilder();

        builder.append(INDENT).append(INDENT)
               .append("--==| ").append(Config.getInstance().getTitle()).append(" |==--")
               .append(EOL).append(EOL);
        describeMappings(builder);
        addNotes(builder);

        return builder.toString();
    }


    private void describeMappings(@Nonnull StringBuilder builder) {
        List<TypeModel> results = model.getTypes();

        for (TypeModel type : results) {
            builder.append(type.getSimpleName()).append(':').append(EOL);

            for (ImplementationModel implementation : type.getImplementations()) {
                for (MappingModel mapping : implementation.getMappings()) {
                    builder.append(INDENT)
                            .append(mapping.getVerb())
                            .append(" ")
                            .append("\t")
                            .append(mapping.getRegEx())
                            .append(EOL);
                }
            }

            builder.append(EOL);
        }
    }


    private void addNotes(@Nonnull StringBuilder builder) {
        List<String> notes = model.getNotes();

        if (!notes.isEmpty()) {
            builder.append("--==| Notes |==--")
                   .append(EOL);

            for (String note : notes) {
                builder.append(EOL)
                       .append(note)
                       .append(EOL);
            }
        }
    }
}
