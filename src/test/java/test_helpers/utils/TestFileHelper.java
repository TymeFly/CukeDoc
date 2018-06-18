/*
 * Copyrighted as an unpublished work 2018 D&B.
 * Proprietary and Confidential.  Use, possession and disclosure subject to license agreement.
 * Unauthorized use, possession or disclosure is a violation of D&B's legal rights and may result
 * in suit or prosecution.
 */

package test_helpers.utils;

import java.io.File;
import java.net.URL;

import javax.annotation.Nonnull;

import static org.junit.Assert.fail;

/**
 * Utility class for test files
 */
public class TestFileHelper {
    private TestFileHelper() {
    }


    @Nonnull
    public static final String findFile(@Nonnull String fileName) throws Exception {
        URL expectedLocation = TestFileHelper.class.getClassLoader().getResource(fileName);

        if (expectedLocation == null) {
            fail("Can not find test file '" + fileName + "'");
        }

        String location = new File(expectedLocation.toURI()).getAbsolutePath();

        return location;
    }
}
