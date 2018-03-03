package cucumber.doc.report.html;

import java.util.EnumSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cucumber.doc.config.Config;
import j2html.tags.DomContent;

import static j2html.TagCreator.body;
import static j2html.TagCreator.fileAsString;
import static j2html.TagCreator.p;

/**
 * Builder for the optional "notes" page
 */
public class NotesPageBuilder implements PageBuilder {
    @Nonnull
    @Override
    public EnumSet<MenuItem> menuOptions() {
        return EnumSet.complementOf(EnumSet.of(MenuItem.NOTES));
    }

    @Nullable
    @Override
    public DomContent buildPage() {
        String notesPath = Config.getInstance().getNotesPath();
        DomContent result;

        if (notesPath == null) {
            result = null;
        } else {
            result =
              body(
                p(
                  fileAsString(notesPath)
                ).withClass("notes")
              );
        }

        return result;
    }
}
