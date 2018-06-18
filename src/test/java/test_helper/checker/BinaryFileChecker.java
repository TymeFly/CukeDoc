package test_helper.checker;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import javax.annotation.Nonnull;

import org.junit.Assert;

/**
 * Perform a byte for byte comparison of two files.
 */
public class BinaryFileChecker implements FileChecker {
    @Override
    public void check(@Nonnull Path expectedFile, @Nonnull Path actualFile) {
        int index = 0;
        long length = actualFile.toFile().length();

        Assert.assertEquals(expectedFile + " and " + actualFile + " have different lengths",
                            expectedFile.toFile().length(),
                            length);

        try (
            InputStream expectedStream = new BufferedInputStream(new FileInputStream(expectedFile.toFile()));
            InputStream actualStream = new BufferedInputStream(new FileInputStream(actualFile.toFile()))
        ) {
            byte[] expected = new byte[1];
            byte[] actual = new byte[1];

            while (index++ != length) {
                read(expectedFile, expectedStream, expected);
                read(actualFile, actualStream, actual);

                if (expected[0] != actual[0]) {         // This extra check to save lots of string manipulation
                    Assert.assertEquals("Files differ at index " + index, expected[0], actual[0]);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file");
        }
    }


    private void read(@Nonnull Path source,
                      @Nonnull InputStream expectedStream,
                      @Nonnull byte[] buffer) throws IOException {
        int size = expectedStream.read(buffer, 0, 1);

        if (size != 1) {            // Don't bother to convert source to a string for every byte read
            Assert.fail("Unable to read " + source);
        }
    }
}
