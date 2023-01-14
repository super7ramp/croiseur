<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## crosswords-dictionary

This folder gathers the following kinds of modules:

- Dictionary data
- Dictionary codecs
- Dictionary plugins
- Dictionary tools

### Dictionary data

These modules provide the dictionary entries.

They are named using the following scheme: `crosswords-dictionary-<dictionary_or_format_name>-data`.

Example: `crosswords-dictionary-xml-data`.

Only textual representation should be committed under version control. (If binary formats were 
to be supported in the future, they would have to be generated from a textual representation and 
generated binary would have to be excluded from version control.)

### Dictionary codecs

These modules are standalone libraries providing encoding/decoding functions for a peculiar 
dictionary format.

They are named using the following scheme: 
`crosswords-dictionary-<dictionary_or_format_name>-codec`.

Example: `crosswords-dictionary-xml-codec`.

### Dictionary plugins

These modules are special libraries implementing the crosswords dictionary provider interfaces 
defined in `crosswords-dictionary-spi` and thus usable by the `crosswords` library.

They are named using the following scheme: 
`crosswords-dictionary-provider-<dictionary_or_format_name>-plugin`.

Example: `crosswords-dictionary-xml-plugin`.

Typically, these modules:

* Retrieve the dictionary data - location information may be hard-coded in the plugin or read from 
  a configuration file;
* Decode these data using codecs;
* Expose the decoded data via the `crosswords-dictionary-spi` interface.

## Dictionary tools

These are tools to manipulate dictionaries.

They are grouped in the single `crosswords-dictionary-tools` module.