/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.api.puzzle;

import com.gitlab.super7ramp.croiseur.api.puzzle.exporter.PuzzleExportService;
import com.gitlab.super7ramp.croiseur.api.puzzle.importer.PuzzleImportService;
import com.gitlab.super7ramp.croiseur.api.puzzle.persistence.PuzzlePersistenceService;

/**
 * Services pertaining to puzzle management.
 */
public interface PuzzleService {

    /**
     * The puzzle persistence service.
     *
     * @return puzzle persistence service
     */
    PuzzlePersistenceService persistence();

    /**
     * The puzzle import service.
     *
     * @return puzzle import service
     */
    PuzzleImportService importer();

    /**
     * The puzzle export service.
     *
     * @return puzzle export service
     */
    PuzzleExportService exporter();
}
