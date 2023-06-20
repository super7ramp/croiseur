<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## croiseur-puzzle-repository-filesystem-plugin

`croiseur-puzzle-repository-filesystem-plugin` is
a [Croiseur puzzle repository](../../croiseur-spi/croiseur-spi-puzzle-repository) whose puzzles are
stored as files on disk.

The path to the repository is defined by the `com.gitlab.super7ramp.croiseur.puzzle.repository.path`
system property.

Files are written in the [xd format](../croiseur-puzzle-codec-xd).
