## crosswords-dictionary

This folder gathers the following kinds of modules:

- Dictionary data
- Dictionary libraries
- Dictionary plugins

### Dictionary data

TODO.

### Dictionary libraries

Currently, this folder does not contain any standalone dictionary library.

`crosswords-dictionary-hunspell-plugin` – which implements a non-trivial reading of Hunspell
dictionary and wordforms generation – is candidate to have its logic split in a standalone
library to be put here.

### Dictionary plugins

Dictionary plugins are libraries implementing the crosswords dictionary provider
interfaces defined in `crosswords-dictionary-spi` and thus usable by the `crosswords` library.

Their names are suffixed with `-plugin`.

Typically, these modules simply adapts the interfaces of standalone dictionary libraries to the
interfaces defined in `crosswords-dictionary-spi`.

Alternatively, the whole dictionary reading logic can be put in a plugin module. This is
acceptable when the dictionary format is not meant to be re-used in any other projects (e.g.
`crosswords-dictionary-internal-plugin`).