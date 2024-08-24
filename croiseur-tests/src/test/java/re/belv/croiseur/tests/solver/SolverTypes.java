/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.tests.solver;

import io.cucumber.java.DataTableType;
import java.util.Map;
import re.belv.croiseur.spi.presenter.solver.SolverDescription;

/** Datatable and parameter types pertaining to solver service. */
public final class SolverTypes {

    /** Constructs an instance. */
    public SolverTypes() {
        // Nothing to do.
    }

    @DataTableType
    public SolverDescription solverDescription(final Map<String, String> entry) {
        return new SolverDescription(entry.get("Name"), entry.get("Description"));
    }
}
