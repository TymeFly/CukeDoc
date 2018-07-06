package io.cucumber.doc.model;

import javax.annotation.Nonnull;

/**
 * Interface implemented by builders that allow an optional description to be attached to their models
 * @param <B>           Type of the implementing builder
 */
public interface DescriptionModelBuilder<B> {
    /**
     * Add an optional description to the model
     * @param description       Human readable description
     * @return                  A builder for a flowing interface
     */
    B withDescription(@Nonnull String description);


    /**
     * Add an optional version to the model
     * @param version           Human readable version number
     * @return                  A builder for a flowing interface
     */
    B since(@Nonnull String version);
}
