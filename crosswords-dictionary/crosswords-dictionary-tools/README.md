## crosswords-dictionary-tools

`crosswords-dictionary-tools` is a collection of tools related to dictionary conversion.

The tools are:

* `HunspellToText`: An application which allows to convert a Hunspell dictionary into a simpler
  text file format (one word per line).
* `TextToXml`: An application which allows to convert a simple text file dictionary into an
  XML dictionary readable by the `dictionary-xml` library.
* `HunspellToXml`: An application which allows to convert a Hunspell dictionary into an XML
  dictionary readable by the `dictionary-xml` library. It is basically the composition of
  `HunspellToText` and `TextToInternal`.