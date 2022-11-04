## dictionary-tools

`dictionary-tools` is a collection of tools related to dictionary conversion.

The tools are:

* `HunspellToText`: An application which allows to convert a Hunspell dictionary into a simpler
  text file format (one word per line).
* `TextToInternal`: An application which allows to convert a simple text file dictionary into a
  binary dictionary readable by the `dictionary-internal` library.
* `HunspellToInternal`: An application which allows to convert a Hunspell dictionary into a binary
  dictionary readable by the `dictionary-internal` library. It is basically the composition of
  `HunspellToText` and `TextToInternal`.