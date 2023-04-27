<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## croiseur-dictionary-tools

`croiseur-dictionary-tools` is a collection of tools related to dictionary conversion.

The tools are:

* `HunspellToText`: A program which allows to convert a Hunspell dictionary into a simpler text file format (one word
  per line).
* `TextToXml`: A program which allows to convert a simple text file dictionary into an XML dictionary readable by
  the `dictionary-xml` library.
* `HunspellToXml`: A program which allows to convert a Hunspell dictionary into an XML dictionary readable by
  the `dictionary-xml` library. It is basically the composition of `HunspellToText` and `TextToInternal`.
* `Scorer`: A program which gives a score to a given dictionary corresponding to the capability of its
  words to cross with each other. Experimental, still looking for a good way to define the score.