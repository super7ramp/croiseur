<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## croiseur-dictionary-xml-codec

`croiseur-dictionary-xml-codec` is a library which allows to read and write dictionaries – or more exactly *word
lists* – in a custom XML format.

It is designed so that the dictionary header – containing dictionary metadata like dictionary name, locale, etc. – can
be read without having to load the entire XML document. This enables fast dictionary information listing. This is made
possible thanks to the JDK's implementation of
the [Streaming API for XML (StAX)](https://docs.oracle.com/javase/tutorial/jaxp/stax/index.html).

### References

* [`dictionary.xsd`](src/main/resources/dictionary.xsd): The XML schema.

### Examples

* [`example.xml`](src/test/resources/example.xml): A simple example.