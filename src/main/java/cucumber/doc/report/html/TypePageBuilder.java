package cucumber.doc.report.html;

import java.util.EnumSet;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cucumber.doc.config.LanguageKey;
import cucumber.doc.config.Translate;
import cucumber.doc.model.ApplicationModel;
import cucumber.doc.model.ImplementationModel;
import cucumber.doc.model.MappingModel;
import cucumber.doc.model.TypeModel;
import j2html.tags.ContainerTag;
import j2html.tags.DomContent;
import j2html.tags.Tag;
import j2html.tags.Text;

import static j2html.TagCreator.a;
import static j2html.TagCreator.b;
import static j2html.TagCreator.caption;
import static j2html.TagCreator.div;
import static j2html.TagCreator.each;
import static j2html.TagCreator.h2;
import static j2html.TagCreator.h4;
import static j2html.TagCreator.h5;
import static j2html.TagCreator.i;
import static j2html.TagCreator.iff;
import static j2html.TagCreator.join;
import static j2html.TagCreator.li;
import static j2html.TagCreator.rawHtml;
import static j2html.TagCreator.span;
import static j2html.TagCreator.table;
import static j2html.TagCreator.tbody;
import static j2html.TagCreator.td;
import static j2html.TagCreator.text;
import static j2html.TagCreator.tr;
import static j2html.TagCreator.ul;

/**
 * Builder for a type
 */
class TypePageBuilder extends PageBuilder {
    private static final String DEFAULT_TABLE_DESCRIPTION =
        i(
          text('<' + Translate.message(LanguageKey.TYPE_TABLE) + '>')
        ).renderFormatted();

    private static final String DEFAULT_ARGUMENT_DESCRIPTION =
        i(
          text('<' + Translate.message(LanguageKey.TYPE_ARGUMENT) + '>')
        ).renderFormatted();


    private final TypeModel typeModel;


    TypePageBuilder(@Nonnull String pageName, @Nonnull ApplicationModel parent, @Nonnull TypeModel model) {
        super(pageName, parent, "");

        this.typeModel = model;
    }


    @Nonnull
    public EnumSet<MenuItem> menuOptions() {
        return EnumSet.allOf(MenuItem.class);
    }


    @Nullable
    @Override
    public String loadAction() {
        return "showPanel('mappingTable', 'mappingPanel', 'featurePanel');";
    }


    @Nonnull
    @Override
    public ContainerTag buildPageContent() {
        return
            div(
              h2(typeModel.getFriendlyName()),
              typeDescription(),
              typeSince(),
              mappingTable(),
              each(typeModel.getImplementations(), this::implementationDetail)
            ).withId("docBody");
    }


    @Nullable
    private Tag typeDescription() {
        return
            iff((typeModel.getDescription() != null),
              div(
                div(
                  h5(Translate.message(LanguageKey.GENERAL_DESCRIPTION)).withClass("elementSubtitle"),
                  span(
                    rawHtml(HtmlUtils.cleanDescription(typeModel.getSummary()))
                  ).withClass("implementationDescription")
                ).withClass("implementationElement")
              ).withClass("descriptionContainer")
            );
    }


    @Nullable
    private Tag typeSince() {
        return
            iff((typeModel.getSince() != null),
              div(
                div(
                  h5(Translate.message(LanguageKey.GENERAL_SINCE)).withClass("elementSubtitle"),
                  span(typeModel.getSince()).withClass("implementationDescription")
                ).withClass("implementationElement")
              ).withClass("descriptionContainer")
            );
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
                  mappingPanel("featurePanel", text(""), text(" "), MappingModel::getFriendlyMapping),
                  mappingPanel("regExPanel", text("@"), text(""), MappingModel::getAnnotationText)
                ).withClass("contentBody")
              ).withClass("summaryTypes")
            ).withClass("contentContainer");
    }


    @Nonnull
    private DomContent mappingPanel(@Nonnull String panelName,
                                    @Nonnull Text prefix,
                                    @Nonnull Text separator,
                                    @Nonnull Function<MappingModel, String> mappingName) {
        return each(typeModel.getImplementations(), method ->
            each(method.getMappings(), mapping ->
              tr(
                td(
                  a(
                    i(prefix, text(mapping.getVerb())),
                    separator,
                    b(mappingName.apply(mapping))

                  ).withHref("#" + method.getUniqueId())
                ).withClass("colTypes")
              ).withClasses(panelName, "mappingPanel")
            )
          );
    }


    @Nonnull
    private Tag implementationDetail(@Nonnull ImplementationModel implementationModel) {
        return
          div(
            a().withName(implementationModel.getUniqueId()),
            h4(implementationModel.getFriendlyName())
              .withClass("implementationTitle")
              .attr("title",
                    Translate.message(LanguageKey.TYPE_HOVER_METHOD) + ":\n  " +
                                      implementationModel.getMappingType().getSimpleName() + "." +
                                      implementationModel.getName()),
            div(
              implementationDescription(implementationModel),
              implementationSince(implementationModel),
              mappings(implementationModel),
              implementationParameters(implementationModel),
              implementationTable(implementationModel)
            ).withClass("descriptionContainer")
          ).withClass("implementationDetail");
    }


    @Nullable
    private Tag implementationDescription(@Nonnull ImplementationModel implementationModel) {
        return
            iff((implementationModel.getDescription() != null),
              div(
                h5(Translate.message(LanguageKey.GENERAL_DESCRIPTION)).withClass("elementSubtitle"),
                span(
                  rawHtml(HtmlUtils.cleanDescription(implementationModel.getDescription()))
                ).withClass("implementationDescription")
              ).withClass("implementationElement")
            );
    }


    @Nullable
    private Tag implementationSince(@Nonnull ImplementationModel implementationModel) {
        return
            iff((implementationModel.getSince() != null),
              div(
                h5(Translate.message(LanguageKey.GENERAL_SINCE)).withClass("elementSubtitle"),
                span(
                  text(implementationModel.getSince())
                ).withClass("implementationDescription")
              ).withClass("implementationElement")
            );
    }


    @Nullable
    private Tag mappings(@Nonnull ImplementationModel implementationModel) {
        return
            div(
              h5(Translate.message(LanguageKey.GENERAL_MAPPINGS)).withClass("elementSubtitle"),
                ul(
                  each(implementationModel.getMappings(), mapping ->
                    li(
                      i(mapping.getVerb()),
                      text(" "),
                      b(mapping.getFriendlyMapping())
                    ).attr("title",
                           Translate.message(LanguageKey.TYPE_HOVER_ANNOTATION) + ":\n  @" +
                                             mapping.getVerb() + mapping.getAnnotationText())
                  )
              )
            ).withClass("implementationElement");
    }


    @Nullable
    private Tag implementationParameters(@Nonnull ImplementationModel implementationModel) {
        return
            iff((!implementationModel.getParameters().isEmpty()),
              div(
                h5(Translate.message(LanguageKey.TYPE_WHERE)).withClass("elementSubtitle"),
                ul(
                  each(implementationModel.getParameters(), parameters ->
                    li(
                      join(
                        b("<" + parameters.getFriendlyName() + ">")
                          .attr("title",
                                 Translate.message(LanguageKey.TYPE_HOVER_TYPE) + ":\n  " +
                                    parameters.getType() + "\n" +
                                    Translate.message(LanguageKey.TYPE_HOVER_FORMAT) + ":\n  " +
                                    parameters.getFriendlyFormat()),
                        text(":"),
                        rawHtml(HtmlUtils.cleanDescription(parameters.getDescription(), DEFAULT_ARGUMENT_DESCRIPTION))
                      )
                    )
                  )
                )
              ).withClass("implementationElement")
            );
    }


    @Nullable
    private Tag implementationTable(@Nonnull ImplementationModel implementation) {
        return
            iff((implementation.getTable() != null),
              div(
                h5(Translate.message(LanguageKey.TYPE_TABLE)).withClass("elementSubtitle"),
                ul(
                  li(
                    rawHtml(
                      (implementation.getTable() != null ?
                        HtmlUtils.cleanDescription(implementation.getTable().getDescription(),
                                                   DEFAULT_TABLE_DESCRIPTION) :
                        DEFAULT_TABLE_DESCRIPTION
                      )
                    )
                  )
                )
              ).withClass("implementationElement")
            );
    }
}
