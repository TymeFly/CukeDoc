package cucumber.doc.report.html;

import java.util.EnumSet;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cucumber.doc.model.ApplicationModel;
import j2html.tags.DomContent;

import static j2html.TagCreator.body;
import static j2html.TagCreator.each;
import static j2html.TagCreator.p;
import static j2html.TagCreator.rawHtml;

/**
 * Builder for the optional "notes" page
 */
class NotesPageBuilder implements PageBuilder {
    private final ApplicationModel model;

    NotesPageBuilder(@Nonnull ApplicationModel model) {
        this.model = model;
    }


    @Nonnull
    @Override
    public EnumSet<MenuItem> menuOptions() {
        return EnumSet.complementOf(EnumSet.of(MenuItem.NOTES));
    }

    @Nullable
    @Override
    public DomContent buildPage() {
        List<String> notes = model.getNotes();
        DomContent result;

        if (notes.isEmpty()) {
            result = null;
        } else {
            result =
              body(
                each(notes, note ->
                  p(
                    rawHtml(note)
                  ).withClass("notes")
                )
              );
        }

        return result;
    }
}
