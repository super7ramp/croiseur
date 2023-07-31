/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * Crossword solver library based on Matt Ginsberg's papers.
 */
module com.gitlab.super7ramp.croiseur.solver.ginsberg {
    requires transitive com.gitlab.super7ramp.croiseur.common;
    requires java.logging;
    exports com.gitlab.super7ramp.croiseur.solver.ginsberg;
}