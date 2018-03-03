package cucumber.doc.model;

import javax.annotation.Nonnull;

/**
 * The data model for a cucumber table.
 */
public class TableModel extends AbstractParameterModel {
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
