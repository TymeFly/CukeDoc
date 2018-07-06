package io.cucumber.doc.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nonnull;

import io.cucumber.doc.exception.CukeDocException;

/**
 * Utility class containing functions to handle files
 */
public class FileUtils {
    private static final ClassLoader CLASS_LOADER = FileUtils.class.getClassLoader();
    private static final String SEPARATOR = File.separator;
    private static final Charset FILE_ENCODING = StandardCharsets.UTF_8;


    /** Hide utility class constructor */
    private FileUtils() {
    }


    /**
     * Delete a file or directory (including its contents). If the {@code target} does not exist then do nothing
     * @param target            Path of the file or directory to delete
     * @throws CukeDocException if the target could not be deleted
     */
    public static void delete(@Nonnull String target) throws CukeDocException {
        Path rootPath = Paths.get(target);

        if (rootPath.toFile().exists()) {
            try {
                Files.walk(rootPath, FileVisitOption.FOLLOW_LINKS)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
            } catch (IOException e) {
                throw new CukeDocException("Failed to delete " + target + "'", e);
            }
        }
    }


    /**
     * Returns {@code true} only if {@code fileName} refers to a valid file that can be read
     * @param fileName      file name to test
     * @return {@code true} only if {@code fileName} refers to a valid file that can be read
     */
    public static boolean canRead(@Nonnull String fileName) {
        Path path = Paths.get(fileName);

        return Files.isReadable(path);
    }


    /**
     * Locate a file on the classpath
     * @param fileName      file name to read
     * @return              The location of the file
     * @throws CukeDocException if the file could not be found
     */
    @Nonnull
    public static URL findFile(@Nonnull String fileName) throws CukeDocException {
        URL resource = CLASS_LOADER.getResource(fileName);

        if (resource == null) {
            throw new CukeDocException("Failed to locate file '%s'", fileName);
        }

        return resource;
    }


    /**
     * Read the content of a file as a sequence of lines
     * @param fileName      file name to read
     * @return              content of the file
     * @throws CukeDocException if the file could not be read
     */
    @Nonnull
    public static List<String> readLines(@Nonnull String fileName) throws CukeDocException {
        URL url;

        try {
            url = new File(fileName).toURI().toURL();
        } catch (IOException e) {
            throw new CukeDocException("Failed to read " + fileName + "'", e);
        }

        return readLines(url);
    }


    /**
     * Read the content of a file as a sequence of lines
     * @param file          file to read
     * @return              content of the file
     * @throws CukeDocException if the file could not be read
     */
    @Nonnull
    public static List<String> readLines(@Nonnull URL file) throws CukeDocException {
        List<String> content = new ArrayList<>();

        try (
            InputStream inputStream = file.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, FILE_ENCODING))
        ) {
            String line = reader.readLine();

            while (line != null) {
                if (!line.isEmpty()) {
                    content.add(line);
                }

                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new CukeDocException("Failed to read " + file + "'", e);
        }

        return content;
    }


    /**
     * Read the content of a file as a single string
     * @param fileName      file name to read
     * @return              content of the file
     * @throws CukeDocException if the file could not be read
     */
    @Nonnull
    public static String read(@Nonnull String fileName) throws CukeDocException {
        String content;

        try {
            Path path = Paths.get(fileName);
            byte[] raw = Files.readAllBytes(path);

            content = new String(raw, FILE_ENCODING);
        } catch (IOException e) {
            throw new CukeDocException("Failed to read " + fileName + "'", e);
        }

        return content;
    }


    /**
     * Write the {@code content} to a new file. If the file already exists it will be over written; if it does not
     * exist it will be created along with any required parent directories
     * @param fileName      file name to write.
     * @param content       content of the generated file
     * @throws CukeDocException if the file could not be written
     */
    public static void write(@Nonnull String fileName, @Nonnull String content) throws CukeDocException {
        byte[] raw = content.getBytes(FILE_ENCODING);
        Path path = Paths.get(fileName);

        createParentDirectory(path);

        try {
            Files.write(path, raw, StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            throw new CukeDocException("Failed to write file '" + fileName + "'", e);
        }
    }


    /**
     * Copy a file from the classpath to another location. If the target already exists it will be over written
     * @param sourceFileName        file name as seen on the class path. This may include a path
     * @param targetDirectory       destination directory name
     * @param targetFileName        destination file file
     * @throws CukeDocException if the file could not be copied
     */
    public static void copyResource(@Nonnull String sourceFileName,
                                    @Nonnull String targetDirectory,
                                    @Nonnull String targetFileName) throws CukeDocException {
        copyResource(sourceFileName, targetDirectory + SEPARATOR + targetFileName);
    }


    /**
     * Copy a file from the classpath to another location. If the target already exists it will be over written
     * @param sourceFileName        file name as seen on the class path
     * @param targetPathName        destination file name. This may include a path
     * @throws CukeDocException if the file could not be copied
     */
    public static void copyResource(@Nonnull String sourceFileName,
                                    @Nonnull String targetPathName) throws CukeDocException {
        Path target = Paths.get(targetPathName);

        createParentDirectory(target);

        try (
            InputStream in = CLASS_LOADER.getResourceAsStream(sourceFileName)
        ) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new CukeDocException("Failed to copy file '" + sourceFileName + "' to '" + targetPathName + "'", e);
        }
    }


    /**
     * Create any/all parent directories required for {@code path}. Does nothing if the directories already exist
     * @param path      child path
     * @throws CukeDocException if the directories could not be created
     */
    public static void createDirectory(@Nonnull String path) {
        Path target = Paths.get(path);

        createDirectory(target);
    }


    /**
     * Create any/all parent directories required for {@code path}. Does nothing if the directories already exist
     * @param path      child path
     * @throws CukeDocException if the directories could not be created
     */
    private static void createParentDirectory(@Nonnull Path path) {
        Path parent = path.getParent();

        if (parent == null) {
            throw new CukeDocException("Failed to locate parent of '%s", path.toFile().getAbsolutePath());
        }

        createDirectory(parent);
    }


    private static void createDirectory(@Nonnull Path path) {
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new CukeDocException("Failed to create directory '" + path + "'", e);
        }
    }
}
