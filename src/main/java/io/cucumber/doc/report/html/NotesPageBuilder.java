package io.cucumber.doc.report.html;

import java.util.EnumSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.cucumber.doc.exception.CukeDocException;
import io.cucumber.doc.model.ApplicationModel;
import io.cucumber.doc.model.NoteModel;
import io.cucumber.doc.util.EnumSets;
import j2html.tags.DomContent;

import static j2html.TagCreator.div;
import static j2html.TagCreator.p;
import static j2html.TagCreator.pre;
import static j2html.TagCreator.rawHtml;

/**
 * Builder for am optional "notes" page
 */
class NotesPageBuilder extends PageBuilder {
    private final NoteModel model;
    private final EnumSet<MenuItem> menuItems;


    NotesPageBuilder(@Nonnull String pageName, @Nonnull ApplicationModel parent, @Nonnull NoteModel model) {
        super(pageName, parent, "../");

        boolean multipleNotes = (parent.getNotes().size() != 1);

        this.model = model;
        this.menuItems = (multipleNotes ? EnumSet.allOf(MenuItem.class) : EnumSets.allExcept(MenuItem.NOTES));
    }


    @Nonnull
    @Override
    public EnumSet<MenuItem> menuOptions() {
        return menuItems;
    }


    @Nullable
    @Override
    public DomContent buildPageContent() {
        DomContent result =
            div(
              p(
                getContent()
              ).withClass("notes")
            ).withId("docBody");

        return result;
    }


    @Nonnull
    private DomContent getContent() {
        DomContent content;

        switch (model.getFormat()) {
            case FEATURE:
            case TEXT:
            case PROPERTIES:
                // The new line is a workaround for a problem with j2htl where the first line of
                // the pre-formatted text becomes indented.
                content = pre("\n" + model.getText());
                break;

            case HTML:
                content = rawHtml(model.getText());
                break;

            default:
                throw new CukeDocException("Unexpected format '%s'", model.getFormat());
        }

        return content;
    }
}
