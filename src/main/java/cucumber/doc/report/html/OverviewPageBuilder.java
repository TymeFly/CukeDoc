package cucumber.doc.report.html;

import java.util.EnumSet;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cucumber.doc.config.Config;
import cucumber.doc.config.LanguageKey;
import cucumber.doc.config.Translate;
import cucumber.doc.model.ApplicationModel;
import cucumber.doc.model.MappingModel;
import cucumber.doc.util.FileUtils;
import j2html.tags.DomContent;
import j2html.tags.Tag;
import j2html.tags.Text;

import static j2html.TagCreator.a;
import static j2html.TagCreator.b;
import static j2html.TagCreator.caption;
import static j2html.TagCreator.div;
import static j2html.TagCreator.each;
import static j2html.TagCreator.h2;
import static j2html.TagCreator.i;
import static j2html.TagCreator.iff;
import static j2html.TagCreator.rawHtml;
import static j2html.TagCreator.table;
import static j2html.TagCreator.tbody;
import static j2html.TagCreator.td;
import static j2html.TagCreator.text;
import static j2html.TagCreator.th;
import static j2html.TagCreator.tr;

/**
 * Page builder for the Overview (index.html) page
 */
class OverviewPageBuilder extends PageBuilder {
    OverviewPageBuilder(@Nonnull String pageName, @Nonnull ApplicationModel model) {
        super(pageName, model, "");
    }


    @Nonnull
    @Override
    public EnumSet<MenuItem> menuOptions() {
        return EnumSet.complementOf(EnumSet.of(MenuItem.OVERVIEW));
    }


    @Nullable
    @Override
    public String loadAction() {
        return "showPanel('mappingTable', 'mappingPanel', 'featurePanel');";
    }


    @Nonnull
    @Override
    public Tag buildPageContent() {
        return
          div(
            h2(Translate.message(LanguageKey.OVERVIEW_TITLE)).withClass("title"),
            implementationDescription(),
            typeTable(),
            mappingTable()
          ).withId("docBody");
    }


    @Nullable
    private Tag implementationDescription() {
        String descriptionPath = Config.getInstance().getDescriptionPath();
        String html = (descriptionPath == null ? null : FileUtils.read(descriptionPath));

        return
            div(
              iff((descriptionPath != null),
                div(
                  rawHtml(html)
                ).withClass("implementationElement")
              )
            ).withClass("descriptionContainer");
    }


    @Nonnull
    private Tag typeTable() {
        return
            div(
              table(
                caption(
                  a(
                    Translate.message(LanguageKey.OVERVIEW_TYPES)
                  ).withHref("javascript:showPanel('classTable', 'classPanel', 'classPanel');")
                ).withClasses("classTable", "classPanel", "panelTab", "activeTab"),
                tbody(
                  tr(
                    th(Translate.message(LanguageKey.OVERVIEW_TYPE)).withClass("colTypes"),
                    th(Translate.message(LanguageKey.GENERAL_DESCRIPTION)).withClass("colDescription")
                  ),
                  each(getModel().getTypes(), mappingClass ->
                    tr(
                      td(
                        a(
                          mappingClass.getFriendlyName()
                        ).withHref(mappingClass.getQualifiedName() + ".html").withClass("contentLink")
                      ),
                      td(
                        rawHtml(
                          HtmlUtils.cleanDescription(mappingClass.getSummary()))
                        )
                      ).withClass("colTypes")
                    )
                  ).withClass("contentBody")
                ).withClass("summaryTypes")
            ).withClass("contentContainer");
    }


    @Nonnull
    private Tag mappingTable() {
        return
          div(
            table(
              caption(
                a(
                  Translate.message(LanguageKey.GENERAL_PANEL_FEATURE)
                ).withHref("javascript:showPanel('mappingTable', 'mappingPanel', 'featurePanel');")
              ).withClasses("mappingTable", "featurePanel"),
              caption(
                a(
                  Translate.message(LanguageKey.GENERAL_PANEL_JAVA)
                ).withHref("javascript:showPanel('mappingTable', 'mappingPanel', 'regExPanel');")
              ).withClasses("mappingTable", "regExPanel"),
              tbody(
                tr(
                  th(Translate.message(LanguageKey.GENERAL_MAPPING)).withClass("colMappings"),
                  th(Translate.message(LanguageKey.OVERVIEW_TYPE)).withClass("colTypes")
                ),
                mappingPanel("featurePanel", text(""), text(" "), MappingModel::getFriendlyMapping),
                mappingPanel("regExPanel", text("@"), text(""), MappingModel::getAnnotationText)
              ).withClass("contentBody")
            ).withClass("summaryMapping")
          ).withClass("contentContainer");
    }


    @Nonnull
    private DomContent mappingPanel(@Nonnull String panelName,
                                    @Nonnull Text prefix,
                                    @Nonnull Text separator,
                                    @Nonnull Function<MappingModel, String> mappingName) {
        return
            each(getModel().getMappings(), mapping ->
              tr(
                td(
                  a(
                    i(prefix, text(mapping.getVerb())),
                    separator,
                    b(mappingName.apply(mapping))
                  ).withHref(mapping.getMappingType().getQualifiedName() + ".html#"
                                    + mapping.getImplementation().getUniqueId())
                   .withClasses("contentLink", "mappingLink")
                ).withClass("colMappings"),
                td(
                  a(mapping.getMappingType().getFriendlyName()
                ).withHref(mapping.getMappingType().getQualifiedName() + ".html")
                 .withClass("contentLink")
                ).withClass("colTypes")
              ).withClasses(panelName, "mappingPanel")
            );
    }
}
