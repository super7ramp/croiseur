<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## croiseur-puzzle

This folder gathers two types of modules:

- Puzzle codec
- Puzzle repository plugins

### Puzzle codec

These are standalone libraries providing encoders/decoders of crossword puzzle in various formats.

They are named using the following scheme: `croiseur-puzzle-codec-<format_name>-plugin`.

### Puzzle repository plugins

Puzzle repository plugins are libraries implementing the `croiseur` puzzle repository provider
interface defined in `croiseur-spi-puzzle-repository` and thus usable by the `croiseur` library.

They are named using the following scheme: `croiseur-puzzle-repository-<storage_kind_name>-plugin`.
