/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.tests.puzzle;

import io.cucumber.java.DataTableType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import re.belv.croiseur.common.puzzle.PuzzleCodecDetails;

/** Datatable and parameter types pertaining to puzzle codec service. */
public final class PuzzleCodecTypes {

    /** Constructs an instance. */
    public PuzzleCodecTypes() {
        // Nothing to do.
    }

    @DataTableType
    public PuzzleCodecDetails puzzleCodecDetails(final Map<String, String> table) {
        final String name = table.get("Name");
        final String description = table.get("Description");
        final List<String> supportedFormats =
                Arrays.asList(table.get("Supported Formats").split(","));
        return new PuzzleCodecDetails(name, description, supportedFormats);
    }
}
