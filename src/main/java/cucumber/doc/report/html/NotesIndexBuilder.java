package cucumber.doc.report.html;

import java.util.EnumSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cucumber.doc.config.LanguageKey;
import cucumber.doc.config.Translate;
import cucumber.doc.model.ApplicationModel;
import cucumber.doc.util.EnumSets;
import j2html.tags.DomContent;
import j2html.tags.Tag;

import static j2html.TagCreator.a;
import static j2html.TagCreator.caption;
import static j2html.TagCreator.div;
import static j2html.TagCreator.each;
import static j2html.TagCreator.p;
import static j2html.TagCreator.table;
import static j2html.TagCreator.tbody;
import static j2html.TagCreator.td;
import static j2html.TagCreator.th;
import static j2html.TagCreator.tr;

/**
 * Builder for the "notes" index page
 */
class NotesIndexBuilder extends PageBuilder {
    NotesIndexBuilder(@Nonnull ApplicationModel parent) {
        super(Translate.message(LanguageKey.NOTES_TITLE), parent, "../");
    }


    @Nonnull
    @Override
    public EnumSet<MenuItem> menuOptions() {
        return EnumSets.allExcept(MenuItem.NOTES);
    }


    @Nullable
    @Override
    public DomContent buildPageContent() {
        DomContent result =
            div(
              p(
                noteTable()
              ).withClass("notes")
            ).withId("docBody");

        return result;
    }


    @Nonnull
    private Tag noteTable() {
        return
            div(
              table(
                caption(
                  a(
                    Translate.message(LanguageKey.GENERAL_PAGE)
                  )
                ).withClasses("noteTable", "notePanel", "panelTab", "activeTab"),
                tbody(
                  tr(
                    th(Translate.message(LanguageKey.GENERAL_NAME)).withClass("colTypes")
                  ),
                  each(getModel().getNotes(), note ->
                    tr(
                      td(
                        a(
                          note.getFriendlyName()
                        ).withHref(note.getName() + ".html").withClass("contentLink")
                      )
                    ).withClass("colTypes")
                  )
                ).withClass("contentBody")
              ).withClass("summaryTypes")
            ).withClass("contentContainer");
    }
}
