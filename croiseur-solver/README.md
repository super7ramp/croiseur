<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## croiseur-solver

This folder gathers two types of modules:

- Solver libraries
- Solver plugins

### Solver libraries

These are standalone crossword or constraint solver libraries.

### Solver plugins

Solver plugins are libraries implementing the `croiseur` solver provider interface defined in
`croiseur-solver-spi` and thus usable by the `croiseur` library. Their names are suffixed
with `-plugin`.

Typically, plugins contain no logic: They simply adapt the complex standalone solver libraries to
the interface defined in `croiseur-solver-spi`.
