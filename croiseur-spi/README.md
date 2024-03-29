<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## croiseur-spi

This folder gathers the definitions of the SPIs – _Service Provider Interfaces_, i.e. the plugin
interfaces – used by the core [`croiseur`](../croiseur/README.md) library. These are:

* [`croiseur-spi-clue`](croiseur-spi-clue): The definition of a clue provider.
* [`croiseur-spi-dictionary`](croiseur-spi-dictionary): The definition of a dictionary
  provider.
* [`croiseur-spi-presenter`](croiseur-spi-presenter): The definition of a _presenter_ – e.g. a
  Graphical User Interface.
* [`croiseur-spi-puzzle-codec`](croiseur-spi-puzzle-codec): The definition of a puzzle format
  encoder/decoder.
* [`croiseur-spi-puzzle-repository`](croiseur-spi-puzzle-repository): The definition of a puzzle
  repository.
* [`croiseur-spi-solver`](croiseur-spi-solver): The definition of a crossword solver.

These interfaces are designed to be implemented by any module willing to extend the capabilities of
the `croiseur` library.

In order to allow the sustainable development of such extensions, SPIs are designed to be as
stable as possible and respect the following rules:

* SPIs should not depend on other libraries; In particular:
    * SPIs shall not depend on the `croiseur` library API: This ensures that extension
      implementations remain compatible with newer versions of the `croiseur` library;
    * SPIs may depend on `croiseur-common`, as `croiseur-common` strives for backward
      compatibility;
* SPIs shall have their own versioning scheme.

> Note: Until a first stable version of Croiseur is made, the above rules are not strictly enforced.
> SPIs may break, and the SPI versions follow the Croiseur API version.