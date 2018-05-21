package cucumber.doc.report.html;

import java.util.Arrays;
import java.util.Date;
import java.util.EnumSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cucumber.doc.config.Config;
import cucumber.doc.config.LanguageKey;
import cucumber.doc.config.Translate;
import cucumber.doc.model.ApplicationModel;
import cucumber.doc.model.TypeModel;
import cucumber.doc.report.ReportBuilder;
import cucumber.doc.util.Check;
import cucumber.doc.util.DateUtils;
import cucumber.doc.util.FileUtils;
import cucumber.doc.util.Trace;
import j2html.tags.DomContent;

import static cucumber.doc.config.Translate.message;
import static j2html.TagCreator.a;
import static j2html.TagCreator.body;
import static j2html.TagCreator.div;
import static j2html.TagCreator.document;
import static j2html.TagCreator.each;
import static j2html.TagCreator.footer;
import static j2html.TagCreator.head;
import static j2html.TagCreator.header;
import static j2html.TagCreator.html;
import static j2html.TagCreator.iff;
import static j2html.TagCreator.iffElse;
import static j2html.TagCreator.link;
import static j2html.TagCreator.meta;
import static j2html.TagCreator.rawHtml;
import static j2html.TagCreator.script;
import static j2html.TagCreator.span;
import static j2html.TagCreator.text;
import static j2html.TagCreator.title;


/**
 * A report builder for pages of HTML
 * <b>Note:</b> This class and it's children use <a href="https://j2html.com/">j2html</a>
 */
public class HtmlReport implements ReportBuilder {
    private final Date buildDate;
    private final ApplicationModel model;

    /**
     * Create a Report for pages of HTML
     * @param model         application model
     */
    public HtmlReport(@Nonnull ApplicationModel model) {
        this.model = model;
        this.buildDate = DateUtils.localDate();
    }


    @Override
    public void writeReport() {
        writeTypes();
        writePage(new OverviewPageBuilder(model), Translate.message(LanguageKey.OVERVIEW_TITLE), "index.html");
        writePage(new NotesPageBuilder(model), Translate.message(LanguageKey.NOTES_TITLE), "notes.html");

        copyResources();
    }


    private void writeTypes() {
        for (TypeModel type : model.getTypes()) {
            writePage(new TypePageBuilder(type),
                      type.getFriendlyName(),
                      type.getQualifiedName() + ".html");
        }
    }


    private void writePage(@Nonnull PageBuilder pageBuilder,
                           @Nonnull String pageName,
                           @Nonnull String targetFile) {
        DomContent content = pageBuilder.buildPage();

        if (content != null) {
            String loadAction = pageBuilder.loadAction();
            loadAction = (loadAction == null ? "" : loadAction);

            Trace.message("Creating page %s", pageName);

            String html =
                document().render() +
                html(
                  head(
                    title(Config.getInstance().getTitle() + " - " + pageName),
                    link().withRel("stylesheet").withHref("css/main.css"),
                    link().withRel("icon").withHref("icon.png"),
                    meta().attr("charset", "utf-8"),
                    script().attr("src", "cuke-doc.js").attr("type", "text/javascript")
                  ),
                  body(
                    top(),
                    a().withName("_top"),
                    cukeDocHeader(pageName, pageBuilder.menuOptions()),
                    content,
                    scrollUp(),
                    cukeDocFooter(),
                    bottom()
                  ).attr("onload", loadAction)
                ).renderFormatted();

            FileUtils.write(Config.getInstance().getDirectory() + "/" + targetFile, html);
        }
    }


    @Nullable
    private DomContent top() {
        String top = Config.getInstance().getTop();

        return
            iff((Check.hasText(top)),
              div(
                 rawHtml(top)
              ).withClass("pageTop")
            );
    }


    @Nullable
    private DomContent bottom() {
        String bottom = Config.getInstance().getBottom();

        return
            iff((Check.hasText(bottom)),
              div(
                 rawHtml(bottom)
              ).withClass("pageBottom")
            );
    }


    @Nullable
    private DomContent scrollUp() {
        return
            a(
              text("")
            ).withHref("#_top")
             .withId("scrollUp");
    }


    @Nonnull
    private DomContent cukeDocHeader(@Nonnull String title,
                                     @Nonnull EnumSet<MenuItem> menu) {
        return
            header(
              span(title).withClass("headerTitle"),
              div(
                each(Arrays.asList(MenuItem.values()), item ->
                  iffElse(item.isAvailable(model),
                    span(
                      iffElse((menu.contains(item)),
                        a(Translate.message(item.getKey())).withHref(item.getHref()).withClass("menuItemText"),
                        span(Translate.message(item.getKey())).withClass("menuItemText")
                      )
                    ).withClass("menuItem"),
                    text("")
                  )
                )
              ).withId("menu")
           ).withId("header");
    }


    @Nonnull
    private DomContent cukeDocFooter() {
        return
            footer(
              span(Config.getInstance().getFooter()).withClass("customFooter"),
              span(
                message(LanguageKey.FOOTER_BUILD_STAMP, buildDate, buildDate)
              ).withId("buildDate")
            ).withId("footer");
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


