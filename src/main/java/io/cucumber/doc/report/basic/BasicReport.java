package io.cucumber.doc.report.basic;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Nonnull;

import io.cucumber.doc.config.Config;
import io.cucumber.doc.config.LanguageKey;
import io.cucumber.doc.config.Translate;
import io.cucumber.doc.model.ApplicationModel;
import io.cucumber.doc.model.ImplementationModel;
import io.cucumber.doc.model.MappingModel;
import io.cucumber.doc.model.NoteModel;
import io.cucumber.doc.model.TypeModel;
import io.cucumber.doc.report.ReportBuilder;
import io.cucumber.doc.util.DateUtils;
import io.cucumber.doc.util.FileUtils;
import io.cucumber.doc.util.StringUtils;

/**
 * Generate a simple text based report consisting of just the mappings and their locations. The results are
 * written to a single text file.
 */
public class BasicReport implements ReportBuilder {
    private static final String EOL = System.lineSeparator();
    private static final String INDENT = "    ";
    private static final String HORIZONTAL_LINE = "--------";

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
        String title = Config.getInstance().getTitle();

        builder.append(INDENT).append(INDENT)
               .append("--==| ").append(title).append(" |==--")
               .append(EOL).append(EOL);
        describeMappings(builder);
        addNotes(builder);
        addDate(builder);

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
        String title = Translate.message(LanguageKey.NOTES_TITLE);
        Collection<NoteModel> notes = model.getNotes();
        boolean first = true;

        if (!notes.isEmpty()) {
            builder.append(INDENT).append(INDENT)
                   .append("--==| ").append(title).append(" |==--")
                   .append(EOL);

            for (NoteModel note : notes) {
                if (first) {
                    first = false;
                } else {
                    builder.append(INDENT).append(INDENT)
                           .append(HORIZONTAL_LINE);
                }

                builder.append(EOL);

                if (notes.size() != 1) {
                    String name = note.getFriendlyName();

                    builder.append(name)
                           .append(EOL)
                           .append(StringUtils.sequence('~', name.length()))
                           .append(EOL)
                           .append(EOL);
                }

                builder.append(note.getText())
                       .append(EOL)
                       .append(EOL);
            }
        }
    }


    private void addDate(@Nonnull StringBuilder builder) {
        Date buildDate = DateUtils.localDate();
        String message = Translate.message(LanguageKey.FOOTER_BUILD_STAMP, buildDate, buildDate);

        builder.append(INDENT).append(INDENT)
               .append(HORIZONTAL_LINE)
               .append(EOL)
               .append(message)
               .append(EOL);
    }
}
