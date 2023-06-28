<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

# 2. Split core from solver and dictionary details

Date: 2023-06-28 (*a posteriori*)

## Status

Accepted

## Context

Project is a crossword filler/editor.

First use-cases are:

- Automatically fill a grid given one (or several) word lists
- Support multiple word list formats
- Support multiple solver algorithms

## Decision

1. Split program in the following modules:
    - Core: Implements the use-cases.
    - Solver providers: Abstract the solving algorithms.
    - Dictionary providers : Abstract the word list formats/sources.
2. Name the modules consistently:
    - Core: `croiseur`
    - Solver providers:
        - Interface: `croiseur-spi-solver`
        - Implementations: `croiseur-solver-*-plugin`
    - Dictionary providers:
        - Interface: `croiseur-spi-dictionary`
        - Implementation: `croiseur-dictionary-*-plugin`

## Consequences

### Program will be easier to reason with

The separation of concerns allows:

- Not to bother with the dictionary formats while implementing the solver algorithms.
- Not to bother with the solver algorithms nor the dictionary formats while implementing the
  core use-cases.
- To focus on dictionary formats while implementing dictionary providers.

### Project needs a consistent way to manage solver/dictionary providers

Splitting the project in modules introduces overhead to create them: Design their API, define how
to load them, define how to build them. A consistent way to create/manage modules is needed to limit
the cognitive overhead, otherwise the split will do more harm than good.
