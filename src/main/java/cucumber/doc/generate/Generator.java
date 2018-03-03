package cucumber.doc.generate;

import javax.annotation.Nonnull;

import com.sun.javadoc.RootDoc;
import cucumber.doc.config.Config;
import cucumber.doc.model.ApplicationModel;
import cucumber.doc.util.Trace;

/**
 * Generate the Model
 */
public class Generator {
    private final RootDoc root;

    /**
     * Create a generator
     * @param root          root of the parse tree
     */
    public Generator(@Nonnull RootDoc root) {
        this.root = root;
    }


    /**
     * Generate a report model from the root document and any linked resources
     * @return a report model
     */
    @Nonnull
    public ApplicationModel generate() {
        ApplicationModel.Builder builder = new Scanner(root).scan();

        for (String xmlFile : Config.getInstance().getXmlList()) {
            Trace.message("Linking to xml report %s", xmlFile);

            new ImportXml().importXml(builder, xmlFile);
        }

        return builder.build();
    }
}
