/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.web.model;

import com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * The puzzle request response model: All data relative to puzzle management and relevant only for
 * the lifetime of a request.
 */
@Component
@RequestScope
public class PuzzleRequestResponseModel {

    /** The list of puzzles created, updated or queried by the current request. */
    private final List<SavedPuzzle> puzzles;

    /** The list of puzzle ids deleted by the current request. */
    private final List<Long> deletedPuzzleIds;

    /** Whether all puzzle has been deleted following by the current request. */
    private boolean allPuzzlesDeleted;

    /**
     * Constructs an instance.
     */
    public PuzzleRequestResponseModel() {
        puzzles = new ArrayList<>();
        deletedPuzzleIds = new ArrayList<>();
    }

    /**
     * Sets the value of the flag indicating that all puzzles have been deleted by the request.
     *
     * @param value the value to set
     */
    public void allPuzzlesDeleted(final boolean value) {
        allPuzzlesDeleted = value;
    }

    /**
     * Whether all the puzzles have been deleted by the current request.
     *
     * @return {@code true} if all puzzles have been deleted by the current request
     */
    public boolean allPuzzlesDeleted() {
        return allPuzzlesDeleted;
    }

    /**
     * Returns the unmodifiable list of puzzles created, updated or queried by the current request.
     *
     * @return the unmodifiable list of puzzles created, updated or queried by the current request.
     */
    public List<SavedPuzzle> puzzles() {
        return Collections.unmodifiableList(puzzles);
    }

    /**
     * Returns the first puzzle created, updated or queried by the current request.
     *
     * @return the first puzzle created, updated or queried by the current request
     */
    public Optional<SavedPuzzle> puzzle() {
        return puzzles.stream().findFirst();
    }

    /**
     * Adds the given puzzle list to the list of created, updated or queried puzzles.
     *
     * @param puzzlesArg the puzzles to add
     */
    public void puzzles(final List<SavedPuzzle> puzzlesArg) {
        puzzles.addAll(puzzlesArg);
    }

    /**
     * Adds the given puzzle to the list of created, updated or queried puzzles.
     *
     * @param puzzle the puzzle to add
     */
    public void puzzle(final SavedPuzzle puzzle) {
        puzzles.add(puzzle);
    }

    /**
     * Returns the unmodifiable list of ids of puzzles deleted by the request.
     *
     * @return the unmodifiable list of ids of puzzles deleted by the request
     */
    public List<Long> deletedPuzzleIds() {
        return Collections.unmodifiableList(deletedPuzzleIds);
    }

    /**
     * Adds an id to the list of puzzles deleted by the request.
     *
     * @param id the deleted puzzle id
     */
    public void deletedPuzzleId(final long id) {
        deletedPuzzleIds.add(id);
    }
}
