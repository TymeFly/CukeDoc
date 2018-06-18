package test_helper.checker;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.Nonnull;

/**
 * Checks two files to see if they are the same.
 */
public interface FileChecker {
    /**
     * Check two files
     * @param expectedFile      The reference file
     * @param actualFile        The generated file
     */
    default void check(@Nonnull File expectedFile, @Nonnull File actualFile) {
        check(expectedFile.toPath(), actualFile.toPath());
    }


    /**
     * Check two files
     * @param expectedFile      The reference file
     * @param actualFile        The generated file
     */
    default void check(@Nonnull String expectedFile, @Nonnull String actualFile) {
        check(Paths.get(expectedFile), Paths.get(actualFile));
    }


    /**
     * Check two files
     * @param expectedFile      The reference file
     * @param actualFile        The generated file
     */
    void check(@Nonnull Path expectedFile, @Nonnull Path actualFile);
}
