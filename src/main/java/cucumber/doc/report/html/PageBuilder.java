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
import cucumber.doc.util.Check;
import cucumber.doc.util.DateUtils;
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
 * Implemented by classes that produce pages of html
 */
abstract class PageBuilder {
    private static final Date BUILD_DATE = DateUtils.localDate();

    private final String pageName;
    private final ApplicationModel model;
    private final String rootPath;


    PageBuilder(@Nonnull String pageName, @Nonnull ApplicationModel model, @Nonnull String rootPath) {
        this.pageName = pageName;
        this.model = model;
        this.rootPath = rootPath;
    }


    /**
     * Returns the items that will be active on the top menu
     * @return the items that will be active on the top menu
     */
    @Nonnull
    abstract EnumSet<MenuItem> menuOptions();


    /**
     * Returns the optional action that is taken when the page is loaded
     * @return the optional action that is taken when the page is loaded
     */
    @Nullable
    String loadAction() {
        return null;
    }


    /**
     * Generates the content specific part of the page body, or {@code null} if the page has no content.
     * The standard header and footers will be added later and it will all be wrapped inside a
     * standard html 'body' tag.
     * @return the content specific part of the page body; or {@code null} of the page does not exist
     */
    @Nullable
    protected abstract DomContent buildPageContent();


    @Nonnull
    protected ApplicationModel getModel() {
        return model;
    }


    @Nullable
    String buildPage() {
        DomContent content = buildPageContent();
        String page;

        if (content == null) {
            page = null;
        } else {
            String loadAction = loadAction();
            loadAction = (loadAction == null ? "" : loadAction);

            Trace.message("Creating page %s", pageName);

            page = document().render() +
                html(
                  head(
                    title(Config.getInstance().getTitle() + " - " + pageName),
                    link().withRel("stylesheet").withHref(rootPath + "css/main.css"),
                    link().withRel("icon").withHref("icon.png"),
                    meta().attr("charset", "utf-8"),
                    script().attr("src", rootPath + "cuke-doc.js").attr("type", "text/javascript")
                  ),
                  body(
                    top(),
                    a().withName("_top"),
                    cukeDocHeader(pageName, menuOptions()),
                    content,
                    scrollUp(),
                    cukeDocFooter(),
                    bottom()
                  ).attr("onload", loadAction)
                ).renderFormatted();
        }

        return page;
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
                        a(Translate.message(item.getKey()))
                          .withHref(rootPath + item.getHref())
                          .withClass("menuItemText"),
                        span(Translate.message(item.getKey()))
                          .withClass("menuItemText")
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
                message(LanguageKey.FOOTER_BUILD_STAMP, BUILD_DATE, BUILD_DATE)
              ).withId("buildDate")
            ).withId("footer");
    }
}
