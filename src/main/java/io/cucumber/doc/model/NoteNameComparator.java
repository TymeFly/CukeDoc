package io.cucumber.doc.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;


/**
 * Determines the order of the notes within the application. The order is described at
 * {@link ApplicationModel#getNotes()}
 */
class NoteNameComparator implements Comparator<String>, Serializable {
    // Map of Canonised names to priority values - the lowest values are sorted to the head
    private static final Map<String, Integer> RESERVED = new HashMap<String, Integer>() {
        // TODO: In a future version we should consider support for other languages
        {   // Suspend Checkstyle rule MagicNumber for 10 lines: They are the priority values
            // for the predefined names
            put("overview", -3);
            put("contents", -2);
            put("read me", -1);
            // 0 is used for all other pages - they will be sorted alphabetically
            put("todo", 1);
            put("credits", 2);
            put("index", 3);
        }
    };


    @Override
    public int compare(@Nonnull String left, @Nonnull String right) {
        left = NameUtils.canonise(left);
        right = NameUtils.canonise(right);

        int leftValue = keyValue(left);
        int rightValue = keyValue(right);
        int result;

        if ((leftValue == 0) && (rightValue == 0)) {
            result = left.compareTo(right);
        } else {
            result = leftValue - rightValue;
        }

        return result;
    }


    private int keyValue(@Nonnull String key) {
        Integer value = RESERVED.get(key);

        return (value == null ? 0 : value);
    }
}
