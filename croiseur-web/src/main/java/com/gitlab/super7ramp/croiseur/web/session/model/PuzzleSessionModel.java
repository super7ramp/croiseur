/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.web.session.model;

import com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;

/**
 * The puzzle session model.
 */
@Component
@SessionScope
public class PuzzleSessionModel {

    /** The saved puzzles. */
    private final List<SavedPuzzle> puzzles;

    /** The errors. */
    private final Queue<String> errors;

    /** The loaded puzzle. May be {@code null}. */
    private SavedPuzzle loadedPuzzle;

    /**
     * Constructs an instance.
     */
    public PuzzleSessionModel() {
        puzzles = new ArrayList<>();
        errors = new LinkedList<>();
        // loadedPuzzle intentionally kept null
    }

    /**
     * Retrieves the saved puzzles for this session.
     *
     * @return the saved puzzles
     */
    public List<SavedPuzzle> puzzles() {
        return puzzles;
    }

    /**
     * Sets the list of saved puzzles for this session.
     *
     * @param puzzlesArg the puzzles to add
     * @throws IllegalStateException if this session already contains some puzzles
     */
    public void puzzles(final List<SavedPuzzle> puzzlesArg) {
        puzzles.clear();
        puzzles.addAll(puzzlesArg);
    }

    /**
     * Queues the given error.
     *
     * @param error the error to queue
     */
    public void error(final String error) {
        Objects.requireNonNull(error, "Software error: Puzzle error may not be null");
        errors.add(error);
    }

    /**
     * Gets the next queued error.
     *
     * @return the next queued error or {@link Optional#empty()} if no error is queued
     */
    public Optional<String> error() {
        final String error = errors.poll();
        return Optional.ofNullable(error);
    }

    /**
     * Clears all queued errors.
     */
    public void clearErrors() {
        errors.clear();
    }

    /**
     * Resets the loaded puzzle.
     */
    public void resetLoadedPuzzle() {
        loadedPuzzle(null);
    }

    /**
     * Sets the loaded puzzle.
     *
     * @param puzzle the loaded puzzle to set
     */
    public void loadedPuzzle(final SavedPuzzle puzzle) {
        loadedPuzzle = puzzle;
    }

    /**
     * Returns the currently loaded puzzle.
     *
     * @return the currently loaded puzzle
     */
    public Optional<SavedPuzzle> loadedPuzzle() {
        return Optional.ofNullable(loadedPuzzle);
    }
}