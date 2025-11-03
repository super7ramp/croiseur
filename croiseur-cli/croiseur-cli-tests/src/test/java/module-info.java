/*
 * SPDX-FileCopyrightText: 2025 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/** End-to-end tests of Croiseur CLI. */
module re.belv.croiseur.cli.tests {
    requires org.junit.jupiter.api;
    requires re.belv.croiseur.cli;

    opens re.belv.croiseur.cli.tests to
            org.junit.platform.commons;
}
