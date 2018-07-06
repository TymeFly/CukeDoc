package io.cucumber.doc.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link SetUtils}
 */
public class SetUtilsTest {

    /**
     * Unit test {@link SetUtils#toUnmodifiableSet(Object[])}
     */
    @Test
    public void test_toUnmodifiableSet_Array() {
        Assert.assertEquals("Empty Set", Collections.emptySet(), SetUtils.toUnmodifiableSet());
        Assert.assertEquals("Letter", Collections.singleton("A"), SetUtils.toUnmodifiableSet("A"));

        assertUnmodifiable(SetUtils.toUnmodifiableSet("A", "B", "C"));
    }


    /**
     * Unit test {@link SetUtils#toUnmodifiableSet(List)}
     */
    @Test
    public void test_toUnmodifiableSet_List() {
        Assert.assertEquals("Empty Set", Collections.emptySet(), SetUtils.toUnmodifiableSet(Arrays.asList()));
        Assert.assertEquals("Letter", Collections.singleton("A"), SetUtils.toUnmodifiableSet(Arrays.asList("A")));

        assertUnmodifiable(SetUtils.toUnmodifiableSet("A", "B", "C"));
    }


    /**
     * Helper method to ensure that the set really is unmodifiable
     */
    private void assertUnmodifiable(@Nonnull Set<String> testSet) {
        try {
            testSet.add("?");
            Assert.fail("Modified set");
        } catch (UnsupportedOperationException e) {
            // Do nothing - this was the expected action
        }
    }
}