package cucumber.doc.report.html;

import java.util.Collection;

import javax.annotation.Nonnull;

import cucumber.doc.config.Config;
import cucumber.doc.config.LanguageKey;
import cucumber.doc.config.Translate;
import cucumber.doc.model.ApplicationModel;
import cucumber.doc.model.NoteModel;
import cucumber.doc.model.TypeModel;
import cucumber.doc.report.ReportBuilder;
import cucumber.doc.util.FileUtils;


/**
 * A report builder for pages of HTML
 * <b>Note:</b> This class and it's children use <a href="https://j2html.com/">j2html</a>
 */
public class HtmlReport implements ReportBuilder {
    private final ApplicationModel model;

    /**
     * Create a Report for pages of HTML
     * @param model         application model
     */
    public HtmlReport(@Nonnull ApplicationModel model) {
        this.model = model;
    }


    @Override
    public void writeReport() {
        writeOverview();
        writeTypes();
        writeNotes();

        copyResources();
    }


    private void writeOverview() {
        String name = Translate.message(LanguageKey.OVERVIEW_TITLE);
        OverviewPageBuilder builder = new OverviewPageBuilder(name, model);

        writePage(builder, "index.html");       // TODO: page names defined in two places
    }


    private void writeNotes() {
        Collection<NoteModel> notes = model.getNotes();

        if (!notes.isEmpty()) {
            boolean singleNote = notes.size() == 1;

            for (NoteModel note : notes) {
                String targetFile = (singleNote ? "_index" : note.getFriendlyName());
                String name = (singleNote ? Translate.message(LanguageKey.NOTES_TITLE) : targetFile);
                NotesPageBuilder builder = new NotesPageBuilder(name, model, note);

                writePage(builder, "notes/" + targetFile + ".html");
            }

            if (!singleNote) {
                NotesIndexBuilder builder = new NotesIndexBuilder(model);
                writePage(builder, MenuItem.NOTES.getHref());
            }
        }
    }


    private void writeTypes() {
        for (TypeModel type : model.getTypes()) {
            String name = type.getFriendlyName();
            TypePageBuilder builder = new TypePageBuilder(name, model, type);
            String targetFile = type.getQualifiedName() + ".html";

            writePage(builder, targetFile);
        }
    }


    private void writePage(@Nonnull PageBuilder pageBuilder,
                           @Nonnull String targetFile) {
        String html = pageBuilder.buildPage();

        if (html != null) {
            FileUtils.write(Config.getInstance().getDirectory() + "/" + targetFile, html);
        }
    }


    private void copyResources() {
        String directory = Config.getInstance().getDirectory();

        FileUtils.copyResource("html/cuke-doc.css", directory, "css/main.css");
        FileUtils.copyResource("html/cuke-doc.js", directory, "cuke-doc.js");
        FileUtils.copyResource("html/up.png", directory, "up.png");
        FileUtils.copyResource("html/up-hover.png", directory, "up-hover.png");
        FileUtils.copyResource(Config.getInstance().getIconPath(), directory, "icon.png");
    }
}


