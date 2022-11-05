## crossword-solver

This folder gathers two types of modules:

- Standalone crossword or constraint solver libraries
- Crossword solver plugins

Crossword solver plugins are composed of libraries implementing the crosswords solver provider
interfaces defined in `crosswords-solver-spi` and thus usable by the `crosswords` library.

Their names are suffixed with `-plugin`.

Typically, these modules are mere adapters of the interfaces of standalone libraries to the
interfaces defined in `crosswords-solver-spi`.