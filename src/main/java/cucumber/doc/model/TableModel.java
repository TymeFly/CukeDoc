package cucumber.doc.model;

import javax.annotation.Nonnull;

/**
 * The data model for a cucumber table.
 */
public class TableModel extends AbstractParameterModel {
    /**
     * Constructor
     * @param name              the name of this table as seen in the method parameters
     * @param description       Description of the table from the comments
     */
    TableModel(@Nonnull String name, @Nonnull String description) {
        super(name, description);
    }


    @Override
    public String toString() {
        return "TableModel{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                '}';
    }
}
