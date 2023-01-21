<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## crosswords-spi

This folder gathers the definitions of the SPIs – _Service Provider Interfaces_, i.e. the plugin
interfaces – used by the core [`crosswords`](../crosswords/README.md) library. These are:

* [`crosswords-spi-dictionary`](crosswords-spi-dictionary): The definition of a dictionary
  provider.
* [`crosswords-spi-solver`](crosswords-spi-solver): The definition of crossword solver
  provider.
* [`crosswords-spi-presenter`](crosswords-spi-presenter): The definition of a _presenter_
  provider – e.g. a Graphical User Interface.

These interfaces are designed to be implemented by any module willing to extend the capabilities of
the `crosswords` library.

In order to allow the sustainable development of such extensions, SPIs are designed to be as
stable as possible and respect the following rules:

* SPIs should not depend on other libraries; In particular:
    * SPIs shall not depend on the `crosswords` library API: This ensures that extension
      implementations remain compatible with newer versions of the `crosswords` library;
    * SPIs may depend on `crosswords-common`, as `crosswords-common` strives for backward
      compatibility;
* SPIs shall have their own versioning scheme.
