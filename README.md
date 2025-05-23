<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## Croiseur

![Logo](croiseur-gui/croiseur-gui/src/main/resources/re/belv/croiseur/gui/application-icon.png)

![](https://gitlab.com/super7ramp/croiseur/-/badges/release.svg?key_text=Latest+Release+(Unstable)&key_width=150)

**Croiseur**[^1] is a software for creating crossword puzzles. It has the following features:

* It embeds [dictionaries][] for multiple languages;
* It provides several crossword [solvers][] capable of finding various grid fillings;
* It can import and export puzzles in the [xd][] format;
* It is able to suggest [clues][];
* It can be used through a simple [desktop application][] for manual authoring, as well as a
  [command-line interface][] for automation;
* It is designed to be [extensible][]: Solvers, dictionaries, clue providers, puzzle formats and
  even user interfaces can be plugged to a core component.

### Getting Started 🚀

As a crossword enthusiast, you may be interested in creating your crossword puzzle right away with
**[Croiseur GUI][]**, a desktop application based on Croiseur.

As a command-line fan, you may be interested in **[Croiseur CLI][]**, a command-line interface to
Croiseur.

Need more? Check the [project documentation][]!

### Licence ⚖️

This work is a free software licenced under multiple licences:

* The original code is licenced under [GPL-3.0-or-later][];
* Code and data reused from other projects are licenced under either GPL-3.0-or-later or
  compatible free software licences listed in the [LICENCES][] folder.

You may use the [reuse tool][] to generate a
comprehensive listing of all the files with their associated licences.

<!-- Reference Links -->

[clues]: croiseur-clue/croiseur-clue-openai-plugin

[command-line interface]: croiseur-cli

[Croiseur CLI]: croiseur-cli

[desktop application]: croiseur-gui

[Croiseur GUI]: croiseur-gui

[dictionaries]: doc/reference/Available-service-providers.md#dictionary-list

[extensible]: croiseur-spi

[GPL-3.0-or-later]: LICENSES/GPL-3.0-or-later.txt

[LICENCES]: LICENSES

[platforms]: https://wiki.openjdk.org/display/Build/Supported+Build+Platforms

[project documentation]: doc

[reuse tool]: https://github.com/fsfe/reuse-tool

[solvers]: doc/reference/Available-service-providers.md#solvers

[xd]: croiseur-puzzle/croiseur-puzzle-codec-xd

---

[^1]: [_croiseur_](https://en.wiktionary.org/wiki/croiseur) is the French name for _cruiser_
(battleship). It can also be literally understood as _the one who does crosses_ (_croi\[x\]_ is
_cross_, and _-eur_ is equivalent to the English suffix _-er_).
