## crossword-solver

This folder gathers two types of modules:

- Solver libraries
- Solver plugins

### Solver libraries

These are standalone crosswords or constraint solver libraries.

### Solver plugins

Solver plugins are libraries implementing the crosswords solver provider interfaces defined in
`crosswords-solver-spi` and thus usable by the `crosswords` library. Their names are suffixed
with `-plugin`.

Typically, plugins contain no logic: They simply adapt the complex standalone solver libraries to
the interfaces defined in `crosswords-solver-spi`.