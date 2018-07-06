package io.cucumber.doc.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Unit test for {@link EnumUtils}
 */
public class EnumUtilsTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();


    enum TestEnum {
        ONE, TWO, THREE, THREE_B
    }


    /**
     * Unit test {@link EnumUtils#toEnum}
     */
    @Test
    public void test_toEnum() {
        Assert.assertEquals("basic", TestEnum.ONE, EnumUtils.toEnum(TestEnum.class, "ONE"));
        Assert.assertEquals("change case", TestEnum.TWO, EnumUtils.toEnum(TestEnum.class, "two"));
        Assert.assertEquals("Trim", TestEnum.THREE, EnumUtils.toEnum(TestEnum.class, "  THREE  "));
        Assert.assertEquals("Underscores", TestEnum.THREE_B, EnumUtils.toEnum(TestEnum.class, "THREE  B"));
        Assert.assertEquals("All", TestEnum.THREE_B, EnumUtils.toEnum(TestEnum.class, "  Three  B  "));
    }


    /**
     * Unit test {@link EnumUtils#toEnum}
     */
    @Test
    public void test_toEnum_EmptyName() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Invalid TestEnum ''");

        EnumUtils.toEnum(TestEnum.class, "");
    }


    /**
     * Unit test {@link EnumUtils#toEnum}
     */
    @Test
    public void test_toEnum_InvalidName() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Invalid TestEnum 'FOUR'");

        EnumUtils.toEnum(TestEnum.class, "FOUR");
    }


    /**
     * Unit test {@link EnumUtils#toEnums}
     */
    @Test
    public void test_toEnums() {
        Set<TestEnum> expected = new HashSet<TestEnum>() {{
            add(TestEnum.ONE);
            add(TestEnum.TWO);
        }};

        Assert.assertEquals("basic",
            expected,
            EnumUtils.toEnums(TestEnum.class,
                              Arrays.asList("ONE", "TWO", "ONE")));
    }
}