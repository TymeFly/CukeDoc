package cucumber.doc.report.html;

import java.util.EnumSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import j2html.tags.DomContent;

/**
 * Implemented by classes that produce pages of html
 */
interface PageBuilder {
    /**
     * Returns the items that will be active on the top menu
     * @return the items that will be active on the top menu
     */
    @Nonnull
    EnumSet<MenuItem> menuOptions();

    /**
     * Generates the content specific part of the page body, or {@code null} if the page has no content.
     * The standard header and footers will be added later and it will all be wrapped inside a
     * standard html 'body' tag.
     * @return the content specific part of the page body; or {@code null} of the page does not exist
     */
    @Nullable
    DomContent buildPage();


    /**
     * Returns the optional action that is taken when the page is loaded
     * @return the optional action that is taken when the page is loaded
     */
    @Nullable
    default String loadAction() {
        return null;
    }
}
