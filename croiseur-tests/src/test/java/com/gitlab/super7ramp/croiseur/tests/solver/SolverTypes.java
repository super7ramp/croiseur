/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.tests.solver;

import com.gitlab.super7ramp.croiseur.common.GridPosition;
import com.gitlab.super7ramp.croiseur.common.PuzzleDefinition;
import com.gitlab.super7ramp.croiseur.spi.presenter.solver.SolverDescription;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.DataTableType;

import java.util.List;
import java.util.Map;

/**
 * Datatable and parameter types pertaining to solver service.
 */
public final class SolverTypes {

    /**
     * Constructs an instance.
     */
    public SolverTypes() {
        // Nothing to do.
    }

    @DataTableType
    public SolverDescription solverDescription(final Map<String, String> entry) {
        return new SolverDescription(entry.get("Name"), entry.get("Description"));
    }

    @DataTableType
    public PuzzleDefinition puzzleDefinition(final DataTable table) {
        final PuzzleDefinition.PuzzleDefinitionBuilder pdb =
                new PuzzleDefinition.PuzzleDefinitionBuilder();
        pdb.height(table.height());
        pdb.width(table.width());
        for (int rowIndex = 0; rowIndex < table.height(); rowIndex++) {
            final List<String> row = table.row(rowIndex);
            for (int columnIndex = 0; columnIndex < row.size(); columnIndex++) {
                final String cellContent = row.get(columnIndex);
                if (cellContent == null) {
                    // blank
                    continue;
                }
                if (cellContent.length() > 1) {
                    throw new IllegalArgumentException("Cells should only contain " +
                            "single character, but got " + cellContent);
                }
                if (cellContent.equals("#")) {
                    pdb.shade(new GridPosition(columnIndex, rowIndex));
                } else {
                    pdb.fill(new GridPosition(columnIndex, rowIndex), cellContent.charAt(0));
                }
            }
        }
        return pdb.build();
    }

}
