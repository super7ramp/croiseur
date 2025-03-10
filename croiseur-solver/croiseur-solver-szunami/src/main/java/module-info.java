/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/** Crossword solver library based on szunami's xwords-rs. */
module re.belv.croiseur.solver.szunami {
    requires com.fasterxml.jackson.databind;
    requires chicory.sdk;

    exports re.belv.croiseur.solver.szunami;
}
