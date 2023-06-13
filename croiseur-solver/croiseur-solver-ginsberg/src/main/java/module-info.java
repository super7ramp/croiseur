/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * Crossword solver library based on Matt Ginsberg's papers.
 */
module com.gitlab.super7ramp.croiseur.solver.ginsberg {
    requires java.logging;
    requires transitive com.gitlab.super7ramp.croiseur.common;
    exports com.gitlab.super7ramp.croiseur.solver.ginsberg;
}