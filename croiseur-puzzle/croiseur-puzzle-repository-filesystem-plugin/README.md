<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## croiseur-puzzle-repository-filesystem-plugin

`croiseur-puzzle-repository-filesystem-plugin` is
a [Croiseur puzzle repository](../../croiseur-spi/croiseur-spi-puzzle-repository) whose puzzles are
stored as files on disk.

Files are written in the [xd format](../croiseur-puzzle-codec-xd).

The path to the repository is defined by the `re.belv.croiseur.puzzle.path` system property. If the
system property is not defined, the plugin will use `${user.home}/croiseur/puzzles` as fallback.

If the directory pointed by the given (or fallback path) does not exist, the plugin will attempt to
create it.
