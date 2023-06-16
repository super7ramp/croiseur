/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.spi.puzzle.repository;

import com.gitlab.super7ramp.croiseur.common.puzzle.ChangedPuzzle;
import com.gitlab.super7ramp.croiseur.common.puzzle.Puzzle;
import com.gitlab.super7ramp.croiseur.common.puzzle.SavedPuzzle;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

/**
 * A dummy repository that always rejects writes. Can be used when repository services are not
 * used.
 */
public final class DummyPuzzleRepository implements PuzzleRepository {

    /**
     * Constructs an instance.
     */
    public DummyPuzzleRepository() {
        // Nothing to do.
    }

    @Override
    public SavedPuzzle create(final Puzzle puzzle) throws WriteException {
        throw new WriteException("Cannot create puzzle: No repository defined.");
    }

    @Override
    public SavedPuzzle update(final ChangedPuzzle changedPuzzle)
            throws WriteException {
        throw new WriteException("Cannot update puzzle: No repository defined.");
    }

    @Override
    public void delete(final long id) throws WriteException {
        throw new WriteException("Cannot delete puzzle: No repository defined.");
    }

    @Override
    public Optional<SavedPuzzle> query(final long id) {
        return Optional.empty();
    }

    @Override
    public Collection<SavedPuzzle> list() {
        return Collections.emptyList();
    }
}
