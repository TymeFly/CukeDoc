package cucumber.doc.parse;

import javax.annotation.Nonnull;

import com.sun.javadoc.RootDoc;
import cucumber.doc.config.Config;
import cucumber.doc.config.NoteDescription;
import cucumber.doc.model.ApplicationModel;
import cucumber.doc.model.NoteModel;
import cucumber.doc.util.FileUtils;
import cucumber.doc.util.NoteFormat;
import cucumber.doc.util.Trace;

/**
 * Generate the Model
 */
public class ModelBuilder {
    private final RootDoc root;

    /**
     * Create a model builder
     * @param root          root of the parse tree
     */
    public ModelBuilder(@Nonnull RootDoc root) {
        this.root = root;
    }


    /**
     * Generate a report model from the root document and all linked XML reports
     * @return a report model
     */
    @Nonnull
    public ApplicationModel build() {
        ApplicationModel.Builder builder = new Scanner(root).scan();

        addNotes(builder);
        addLinks(builder);

        return builder.build();
    }



    /**
     * Add the notes indicated from the command line options. Notes from referenced projects will be managed
     * through their XML reports
     * @param builder       application builder.
     */
    private void addNotes(@Nonnull ApplicationModel.Builder builder) {
        for (NoteDescription note : Config.getInstance().getNotes()) {
            String name = note.getName();
            String notesPath = note.getPath();
            String text = FileUtils.read(notesPath);
            NoteFormat format = NoteFormat.forFile(notesPath);
            NoteModel model = new NoteModel(name, text, format);

            builder.withNote(model);
        }
    }


    private void addLinks(@Nonnull ApplicationModel.Builder builder) {
        for (String xmlFile : Config.getInstance().getLinks()) {
            Trace.message("Linking to xml report %s", xmlFile);

            new ImportXml().importXml(builder, xmlFile);
        }
    }
}
