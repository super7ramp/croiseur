/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.puzzle.repository.filesystem.plugin;

import com.gitlab.super7ramp.croiseur.common.puzzle.ChangedPuzzle;
import com.gitlab.super7ramp.croiseur.common.puzzle.Puzzle;
import com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle;
import com.gitlab.super7ramp.croiseur.spi.puzzle.repository.PuzzleRepository;
import com.gitlab.super7ramp.croiseur.spi.puzzle.repository.WriteException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.function.BiPredicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * A {@link PuzzleRepository} implemented by files on disk, written in the xd format.
 */
public final class FileSystemPuzzleRepository implements PuzzleRepository {

    /** The logger. */
    private static final Logger LOGGER =
            Logger.getLogger(FileSystemPuzzleRepository.class.getName());

    /** The system property defining the repository path. */
    private static final String PUZZLE_REPOSITORY_PATH_PROPERTY =
            "com.gitlab.super7ramp.croiseur.puzzle.repository.path";

    /** The names that files should have. Example: "42.xd" (42 is the crossword identifier). */
    private static final Pattern SUPPORTED_FILES_PATTERN = Pattern.compile("\\d+.xd");

    /** A predicate for {@link Files#find}. */
    private static final BiPredicate<Path, BasicFileAttributes> SUPPORTED_FILES =
            (path, attr) -> attr.isRegularFile() &&
                            SUPPORTED_FILES_PATTERN.matcher(path.getFileName().toString())
                                                   .matches();

    /** The repository path. */
    private final Path repositoryPath;

    /** The puzzle file reader. */
    private final PuzzleReader reader;

    /** The puzzle file writer. */
    private final PuzzleWriter writer;

    /**
     * Constructs an instance.
     *
     * @throws IllegalArgumentException if property {@value PUZZLE_REPOSITORY_PATH_PROPERTY} is not
     *                                  set
     * @throws IOException              if directory indicated by the property
     *                                  {@value PUZZLE_REPOSITORY_PATH_PROPERTY} does not exist and
     *                                  cannot be created
     */
    public FileSystemPuzzleRepository() throws IOException {
        final String pathProperty = System.getProperty(PUZZLE_REPOSITORY_PATH_PROPERTY);
        if (pathProperty == null) {
            throw new IllegalStateException(
                    "Failed to instantiate FileSystemPuzzleRepository: The system property '" +
                    PUZZLE_REPOSITORY_PATH_PROPERTY + "' is not set. Check your setup.");
        }
        repositoryPath = Path.of(pathProperty);
        if (Files.notExists(repositoryPath)) {
            Files.createDirectory(repositoryPath);
        }
        reader = new PuzzleReader();
        writer = new PuzzleWriter();
    }

    @Override
    public SavedPuzzle create(final Puzzle puzzle) throws WriteException {
        final long id = nextId().orElseThrow(
                () -> new WriteException("Repository is full, no more id available"));
        final SavedPuzzle savedPuzzle = new SavedPuzzle(id, puzzle, 1);
        writer.write(savedPuzzle, pathOf(id));
        return savedPuzzle;
    }

    @Override
    public SavedPuzzle update(final ChangedPuzzle changedPuzzle) throws WriteException {
        final long id = changedPuzzle.id();

        final SavedPuzzle original = query(id).orElseThrow(() -> new WriteException(
                "Cannot updated puzzle with id " + id + ": Puzzle doesn't exist."));
        if (original.data().equals(changedPuzzle.data())) {
            // No need to write, no change
            return original;
        }

        final SavedPuzzle updated =
                new SavedPuzzle(id, changedPuzzle.data(), original.revision() + 1);
        writer.write(updated, pathOf(id));
        return updated;
    }

    @Override
    public void delete(final long id) throws WriteException {
        try {
            Files.delete(pathOf(id));
        } catch (final IOException e) {
            throw new WriteException("Failed to delete puzzle with id " + id, e);
        }
    }

    @Override
    public void deleteAll() throws WriteException {
        try (final Stream<Path> paths = puzzlePaths()) {
            for (final Iterator<Path> it = paths.iterator(); it.hasNext(); ) {
                final Path path = it.next();
                Files.delete(path);
            }
        } catch (final IOException e) {
            throw new WriteException("Failed to delete puzzles.", e);
        }
    }

    @Override
    public Optional<SavedPuzzle> query(final long id) {
        return reader.read(pathOf(id));
    }

    @Override
    public Collection<SavedPuzzle> list() {
        try (final Stream<Path> paths = puzzlePaths()) {
            return paths.map(reader::read).flatMap(Optional::stream).toList();
        } catch (final IOException e) {
            LOGGER.warning(() -> "Failed to read puzzles: " + e.getMessage());
            LOGGER.log(Level.FINE, "", e);
            return Collections.emptyList();
        }
    }

    /**
     * Returns a stream of the paths of the puzzles.
     *
     * @return a stream of the paths of the puzzles
     * @throws IOException if repository path cannot be read
     */
    private Stream<Path> puzzlePaths() throws IOException {
        return Files.find(repositoryPath, 1 /* not recursive */, SUPPORTED_FILES);
    }

    /**
     * Creates the {@link Path} to puzzle file from given id.
     *
     * @param id the puzzle id
     * @return the path of the puzzle
     */
    private Path pathOf(final long id) {
        return repositoryPath.resolve(id + ".xd");
    }

    /**
     * Returns the next available id.
     *
     * @return the next available id
     */
    private OptionalLong nextId() {
        for (long id = 1; id < Long.MAX_VALUE; id++) {
            if (!Files.exists(pathOf(id))) {
                return OptionalLong.of(id);
            }
        }
        return OptionalLong.empty();
    }
}
