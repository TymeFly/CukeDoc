package test_helpers;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import org.junit.Assert;

/**
 * Utility to check the content of two directories. Typical usage is:
 * <pre>{@code
 *     new DirectoryChecker(expectedDirectory, actualDirectory).recursive()
 *             .check("*.txt", new TextFileChecker())               // Check the 'txt' files have the same content
 *             .check("*.dat", new TextFileChecker().ignoreCase())  // Case insensitive check of 'dat' files
 *             .check("*.*", new BinaryFileChecker())               // Check remaining files as binary
 *             .assertAllChecked();                                 // Check all files have been checked
 * }</pre>
 */
public class DirectoryChecker {
    private final String expectedDirectory;
    private final String actualDirectory;
    private boolean recursive = false;
    private List<String> expectedFiles = null;
    private List<String> actualFiles = null;


    /**
     * Create a new Directory checker
     * @param expected          Location of the expected directory
     * @param actual            Location of the actual directory
     */
    public DirectoryChecker(@Nonnull String expected, @Nonnull String actual) {
        this.expectedDirectory = expected;
        this.actualDirectory = actual;
    }


    /**
     * If called then the directory paths given in the constructor will be scanned recursively.
     * This must be called before any checks
     * @return      A flowing interface
     */
    @Nonnull
    public DirectoryChecker recursive() {
        if (expectedFiles != null) {
            throw new IllegalStateException("Can not change criteria after checking has started");
        }

        recursive = true;

        return this;
    }


    /**
     * Check all the files in the directory that match the specified {@code glob} pattern using the
     * supplied {@code checker}
     * @param glob              Glob to match files against
     * @param checker           Check to perform the comparisons
     * @return                  A flowing interface
     */
    @Nonnull
    public DirectoryChecker check(@Nonnull String glob, @Nonnull FileChecker checker) {
        if (expectedFiles == null) {
            expectedFiles = readFileList(expectedDirectory);
            actualFiles = readFileList(actualDirectory);
        }

        List<String> targetFiles = getTargets(expectedFiles, glob);

        for (String target : targetFiles) {
            Assert.assertTrue("Directory " + actualDirectory + " is missing file " + target,
                              actualFiles.contains(target));

            checker.check(expectedDirectory + "/" + target, actualDirectory + "/" + target);
        }

        expectedFiles.removeAll(targetFiles);
        actualFiles.removeAll(targetFiles);

        List<String> unmatched = getTargets(actualFiles, glob);

        Assert.assertTrue("Directory " + actualDirectory + " has unexpected file(s) " + unmatched, unmatched.isEmpty());

        return this;
    }


    @Nonnull
    private List<String> getTargets(@Nonnull List<String> Files, @Nonnull String glob) {
        String prefix = (recursive ? "**/" : "/");
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + prefix + glob);
        List<String> targetFiles = new ArrayList<>();

        for (String name : Files) {
            Path path = Paths.get(name);

            if (matcher.matches(path)) {
                targetFiles.add(name);
            }
        }

        return targetFiles;
    }


    /**
     * Assert that all files have been checked. This should always be the last call in the flowing interface.
     */
    public void assertAllChecked() {
        Assert.assertTrue("No checks have been run", (expectedFiles != null));
        Assert.assertTrue("Expected files have not all been checked: " + expectedFiles, expectedFiles.isEmpty());
        Assert.assertTrue("Actual files have not all been checked: " + actualFiles, actualFiles.isEmpty());
    }


    /**
     * Read all the files under the specified {@code directory}. If, and only if, the {@link #recursive} flag
     * has been set then sub directories are also included.
     * @param directory     directory to check.
     * @return              List of all regular files in the directory. Each file is prefixed by a slash.
     */
    @Nonnull
    private List<String> readFileList(@Nonnull String directory) {
        List<String> result;
        int directoryLength = directory.length();
        Path start = Paths.get(directory);
        int depth = recursive ? Integer.MAX_VALUE : 1;

        try {
            result = Files.find(start, depth, (path, attribute) -> attribute.isRegularFile())
                .map(p -> p.toString().substring(directoryLength))
                .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read directory " + directory, e);
        }

        return result;
    }
}
