## crosswords-dictionary

This folder gathers two types of modules:

- Standalone dictionary libraries
- Crosswords dictionary plugins

Crossword dictionary plugins are composed of libraries implementing the crosswords dictionary
provider interfaces defined in `crosswords-dictionary-spi` and thus usable by the `crosswords`
library.

Their names are suffixed with `-plugin`.

Typically, these modules are mere adapters of the interfaces of standalone libraries to the
interfaces defined in `crosswords-dictionary-spi`.