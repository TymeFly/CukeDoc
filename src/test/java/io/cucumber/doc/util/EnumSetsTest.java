package io.cucumber.doc.util;

import java.util.EnumSet;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link EnumSets}
 */
public class EnumSetsTest {
    private enum Enum{ A, B, C, D }


    /**
     * Unit test {@link EnumSets#allExcept}
     */
    @Test
    public void test_AllExcept() {
        Assert.assertEquals("Missing A", EnumSet.of(Enum.B, Enum.C, Enum.D), EnumSets.allExcept(Enum.A));
        Assert.assertEquals("Missing A, B", EnumSet.of(Enum.C, Enum.D), EnumSets.allExcept(Enum.A, Enum.B));
        Assert.assertEquals("Missing A, B, C", EnumSet.of(Enum.D), EnumSets.allExcept(Enum.A, Enum.B, Enum.C));
        Assert.assertEquals("Missing A, B, C, D", EnumSet.noneOf(Enum.class), EnumSets.allExcept(Enum.A, Enum.B, Enum.C, Enum.D));
    }
}