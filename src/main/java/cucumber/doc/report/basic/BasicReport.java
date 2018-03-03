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


    @Override
    public void writeReport(@Nonnull ApplicationModel results) {
        String target = Config.getInstance().getDirectory() + "/mappings.txt";
        String report = generatedReport(results.getTypes());

        FileUtils.delete(target);
        FileUtils.write(target, report);
    }

    @Nonnull
    private String generatedReport(@Nonnull List<TypeModel> results) {
        StringBuilder builder = new StringBuilder();

        builder.append(INDENT).append(INDENT)
               .append("--==| ").append(Config.getInstance().getTitle()).append(" |==--")
               .append(EOL).append(EOL);
        describeMappings(builder, results);
        addNotes(builder);

        return builder.toString();
    }


    private void describeMappings(@Nonnull StringBuilder builder, @Nonnull List<TypeModel> results) {
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
        String notesPath = Config.getInstance().getNotesPath();

        if (notesPath != null) {
            String notes = FileUtils.read(notesPath);

            builder.append("--==| Notes |==--")
                   .append(EOL).append(EOL)
                   .append(notes)
                   .append(EOL);
        }
    }
}
