<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## Croiseur CLI

**Croiseur CLI** is a command-line application allowing to generate crossword puzzles. It is a 
frontend to the `croiseur` library.

[![asciicast](https://asciinema.org/a/8DVXDIm7U87RzSeC8lnBQFD3J.svg)](https://asciinema.org/a/8DVXDIm7U87RzSeC8lnBQFD3J)

### Usage

```
Usage: croiseur-cli COMMAND

Commands:
  help        Display help information about the specified command
  dictionary  List and print available dictionaries
  solver      Solve crosswords and list available solvers
  clue        Get crossword clues and list available clue providers
  puzzle      Manage saved puzzles

First time with Croiseur? Try this out:

        croiseur-cli solver run --size 4x4

This command will generate your first square grid! Next step: Discover the
options and the examples of the 'solver run' subcommand with:

        croiseur-cli solver run --help
```

### Installation

Refer to [INSTALL.md](INSTALL.md).
