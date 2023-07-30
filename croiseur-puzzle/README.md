<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## croiseur-puzzle

This folder gathers three types of modules:

- Puzzle codec
- Puzzle codec plugins
- Puzzle repository plugins

### Puzzle codecs

These are standalone libraries providing encoders/decoders of crossword puzzle in various formats.

They are named using the following scheme: `croiseur-puzzle-codec-<format_name>`.

### Puzzle codec plugins

These are libraries implementing the `croiseur` puzzle codec provider interface defined
in `croiseur-spi-puzzle-codec` and thus usable by the `croiseur` library.

They are named using the following scheme: `croiseur-puzzle-codec-<format_name>-plugin`.

### Puzzle repository plugins

Puzzle repository plugins are libraries implementing the `croiseur` puzzle repository provider
interface defined in `croiseur-spi-puzzle-repository` and thus usable by the `croiseur` library.

They are named using the following scheme: `croiseur-puzzle-repository-<storage_kind_name>-plugin`.
