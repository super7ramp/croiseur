/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/** Crossword solver library based on Matt Ginsberg's papers. */
module com.gitlab.super7ramp.croiseur.solver.ginsberg {
    requires transitive re.belv.croiseur.common;
    requires java.logging;

    exports re.belv.croiseur.solver.ginsberg;
}
