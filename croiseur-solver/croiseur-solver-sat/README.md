<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## croiseur-solver-sat

`croiseur-solver-sat` is a crossword solver based on the default
pseudo-boolean [SAT](https://en.wikipedia.org/wiki/Boolean_satisfiability_problem) solver provided
by the [Sat4j](http://sat4j.org/) library.

It does not embed heuristics specific to the crossword problem, and it is quite slow and memory
intensive on large grid and/or word lists.

### Explanations

* [Martin Hořeňovský's introduction to SAT solvers](https://codingnest.com/modern-sat-solvers-fast-neat-underused-part-1-of-n/):
  An introduction to practical SAT solving, using the sudoku problem as an example.
* [A Deep Dive into CDCL Pseudo-Boolean Solvers (focusing on the implementation in Sat4j)](https://invidious.fdn.fr/watch?v=BophysYDUZ8):
  A presentation on pseudo-boolean solvers. It is not necessary to watch the video to
  understand `croiseur-solver-sat` source code.