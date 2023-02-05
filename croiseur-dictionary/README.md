<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## croiseur-dictionary

This folder gathers the following kinds of modules:

- Dictionary data
- Dictionary codecs
- Dictionary plugins
- Dictionary tools

### Dictionary data

These modules provide the dictionary entries.

They are named using the following scheme: `croiseur-dictionary-<dictionary_or_format_name>-data`.

Example: `croiseur-dictionary-xml-data`.

Only textual representation should be committed under version control. (If binary formats were
to be supported in the future, they would have to be generated from a textual representation and
generated binary would have to be excluded from version control.)

### Dictionary codecs

These modules are standalone libraries providing encoding/decoding functions for a peculiar
dictionary format.

They are named using the following scheme:
`croiseur-dictionary-<dictionary_or_format_name>-codec`.

Example: `croiseur-dictionary-xml-codec`.

### Dictionary plugins

These modules are libraries implementing the crosswords dictionary provider interfaces defined
in [`croiseur-dictionary-spi`](../croiseur-spi/croiseur-spi-dictionary) and thus usable by
the [`croiseur`](../croiseur/README.md) library.

They are named using the following scheme:
`croiseur-dictionary-provider-<dictionary_or_format_name>-plugin`.

Example: `croiseur-dictionary-xml-plugin`.

Typically, these modules:

* Retrieve the dictionary data - location information may be hard-coded, read from
  configuration or system property like `com.gitlab.super7ramp.croiseur.dictionary.path`;
* Decode these data using codecs;
* Expose the decoded data via the `croiseur-spi-dictionary` interface.

### Dictionary tools

These are tools to manipulate dictionaries.

They are grouped in the single [`croiseur-dictionary-tools`](croiseur-dictionary-tools) module.
