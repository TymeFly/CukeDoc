package cucumber.doc.model;

import javax.annotation.Nonnull;

/**
 * Interface implemented by builders that allow an optional description to be attached to their models
 */
public interface DescriptionModelBuilder {
    /**
     * Add an optional description to the model
     * @param description       Human readable description
     */
    void withDescription(@Nonnull String description);


    /**
     * Add an optional version to the model
     * @param version           Human readable version number
     */
    void since(@Nonnull String version);
}
