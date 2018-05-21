package cucumber.doc.report.html;

import java.util.EnumSet;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cucumber.doc.exception.CukeDocException;
import cucumber.doc.model.ApplicationModel;
import cucumber.doc.model.NoteModel;
import j2html.tags.DomContent;

import static j2html.TagCreator.div;
import static j2html.TagCreator.each;
import static j2html.TagCreator.p;
import static j2html.TagCreator.pre;
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
        List<NoteModel> notes = model.getNotes();
        DomContent result;

        if (notes.isEmpty()) {
            result = null;
        } else {
            result =
              div(
                each(notes, note ->
                  p(
                    getContent(note)
                  ).withClass("notes")
                )
              ).withId("docBody");
        }

        return result;
    }


    @Nonnull
    private DomContent getContent(@Nonnull NoteModel note) {
        DomContent content;

        switch (note.getFormat()) {
            case FEATURE:
            case TEXT:
            case PROPERTIES:
                content = pre(note.getText());
                break;

            case HTML:
                content = rawHtml(note.getText());
                break;

            default:
                throw new CukeDocException("Unexpected format '%s'", note.getFormat());
        }

        return content;
    }
}
