<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## croiseur-dictionary-hunspell-codec

`croiseur-dictionary-hunspell-codec` is a library which allows to read Hunspell dictionaries and
generate all word forms from them.

### Motivation

> Why not using the C++ Hunspell library/tools directly?

Three reasons:

- Hunspell tool `unmunch` is [buggy](https://github.com/hunspell/hunspell/issues/404);
- Calling an external process is doable but since the called tool is buggy it would require
  some post-processing, which seems very fragile;
- Same remark for JNI (which is even more tedious to set up).

Creating a new library in Java from scratch will probably be buggier and slower but at least:

- It will not need some external process/foreign interface/post-processing;
- Library doesn't need to be full-fledged, it just needs to generate enough acceptable words for
  crosswords usage (which I agree sounds presumptuous);
- Hopefully it will give a better understanding of how Hunspell works (and that's great!).

### Status

Follows a non-comprehensive list of supported options, where:

* _Recognized_ means the .aff parser is able to parse the option; Not recognized means the
  parser rejects the file;
* _Implemented_ means that not only the option is parsed but is correctly interpreted.

See the [documentation](doc/reference/0001-hunspell-format.md) for details about these options.

#### General Options

| Option                             | Description                                                    | Recognized | Implemented | Note                                       |
|------------------------------------|----------------------------------------------------------------|------------|-------------|--------------------------------------------|
| SET encoding                       | Character encoding                                             | ✔️         | ⚠️          | All encodings supported except ISO-8859-10 |
| FLAG value                         | Flag type                                                      | ✔️         | ⚠️          | Only default (ASCII) and "long" supported  |
| COMPLEXPREFIXES                    | Two-fold prefix stripping                                      | ✔️         | ❌           |                                            |
| LANG langcode                      | Language specific functions (for Azeri and Turkish)            | ✔️         | ❌           |                                            |
| IGNORE characters                  | Characters to ignore dictionary words, affixes and input words | ❌          | ❌           |                                            |
| AF number_of_flag_vector_aliases   | Stuff for compression                                          | ❌          | ❌           |                                            |
| AF flag_vector                     | Stuff for compression                                          | ❌          | ❌           |                                            |
| AM number_of_morphological_aliases | Stuff for compression                                          | ❌          | ❌           |                                            |
| AM morphological_fields            | Stuff for compression                                          | ❌          | ❌           |                                            |

#### Affix Creation Options

| Option                                                              | Description             | Recognized | Implemented | Note                                 |
|---------------------------------------------------------------------|-------------------------|------------|-------------|--------------------------------------|
| PFX flag cross_product number                                       | Prefix class definition | ✔️         | ✔️          |                                      |
| PFX flag stripping prefix \[condition \[morphological_fields...\]\] | Prefix rule definition  | ✔️         | ⚠️          | Probably lots of bugs; Missing tests |
| SFX flag cross_product number                                       | Suffix class definition | ✔️         | ✔️          |                                      |
| SFX flag stripping prefix \[condition \[morphological_fields...\]\] | Suffix rule definition  | ✔️         | ⚠️          | Probably lots of bugs; Missing tests |

#### Compounding Options

| Option                                                          | Description                                                                                             | Recognized | Implemented | Note                                                |
|-----------------------------------------------------------------|---------------------------------------------------------------------------------------------------------|------------|-------------|-----------------------------------------------------|
| BREAK number_of_break_definitions                               | Break definition header                                                                                 | ✔️         | ❌           |                                                     |
| BREAK character_or_character_sequence                           | Definition of the break-point characters                                                                | ✔️         | ❌           |                                                     |
| COMPOUNDRULE number_of_compound_definitions                     | Compound definitions header                                                                             | ✔️         | ❌           |                                                     |
| COMPOUNDRULE compound_pattern                                   | Custom compound pattern with a regex-like syntax                                                        | ✔️         | ❌           |                                                     |
| COMPOUNDMIN num                                                 | Minimum length of words used for compounding                                                            | ✔️         | ❌           |                                                     |
| COMPOUNDFLAG flag                                               | Flag to apply on words that can be in a compound (anywhere)                                             | ✔️         | ⚠️          | Very limited, no continuation from affixed form (1) |
| COMPOUNDBEGIN flag                                              | Flag to apply on words that can be in a compound (as first element only)                                | ✔️         | ⚠️          | Very limited, no continuation from affixed form (1) |
| COMPOUNDLAST flag                                               | Flag to apply on words that can be in a compound (as last element only)                                 | ✔️         | ⚠️          | Very limited, no continuation from affixed form (1) |
| COMPOUNDMIDDLE flag                                             | Flag to apply on words that can be in a compound (as middle element only)                               | ✔️         | ⚠️          | Very limited, no continuation from affixed form (1) |
| ONLYINCOMPOUND flag                                             | Flag to apply on words that can only be in a compound                                                   | ✔️         | ❌           |                                                     | 
| COMPOUNDPERMITFLAG flag                                         | Flag for affixes that can be applied inside compound (in addition to start and end of compound)         | ✔️         | ❌           |                                                     |
| COMPOUNDFORBIDFLAG flag                                         | Flag for affixes that cannot be applied on a compound                                                   | ❌          | ❌           |                                                     |
| COMPOUNDMORESUFFIXES                                            | Allow twofold suffixes within compounds                                                                 | ❌          | ❌           |                                                     |
| COMPOUNDROOT flag                                               | ? (only used for Hungarian dictionary)                                                                  | ❌          | ❌           |                                                     |
| COMPOUNDWORDMAX number                                          | Maximum number of word in a compound (default is unlimited)                                             | ❌          | ❌           |                                                     |
| CHECKCOMPOUNDDUP                                                | No word duplication in compound (e.g. no "foofoo")                                                      | ❌          | ❌           |                                                     |
| CHECKCOMPOUNDREP                                                | ?                                                                                                       | ❌          | ❌           |                                                     |
| CHECKCOMPOUNDCASE                                               | No upper case characters at word boundaries in compounds                                                | ❌          | ❌           |                                                     |
| CHECKCOMPOUNDTRIPLE                                             | No compounding, if compound word contains triple repeating letters (e.g. foo-ox or xo-oof)              | ❌          | ❌           |                                                     |
| SIMPLIFIEDTRIPLE                                                | Allow simplified 2-letter forms of the compounds forbidden by CHECKCOMPOUNDTRIPLE                       | ❌          | ❌           |                                                     |
| CHECKCOMPOUNDPATTERN number_of_checkcompoundpattern_definitions | Header of CHECKCOMPOUNDPATTERN rules                                                                    | ❌          | ❌           |                                                     |
| FORCEUCASE flag                                                 | Last word part of a compound <br/>with flag FORCEUCASE forces capitalization of the whole compound word | ❌          | ❌           |                                                     |
| COMPOUNDSYLLABLE max_syllable vowels                            | ? (special compounding rules in Hungarian)                                                              | ❌          | ❌           |                                                     |
| SYLLABLENUM flags                                               | ? (Hungarian specific)                                                                                  | ❌          | ❌           |                                                     |

> (1): Compound flags and continuation limitation:
>
> For now, only root words (i.e. forms present in .dic file) can be used as compound parts
> (provided they're flagged with the right compound flags).
>
> Compounds parts could also be created via application of an affix rule which has a compound flag
> as continuation class: This does not work for now.
>
> Currently, the generation works like this:
>
> 1. For each dic entry, create affixed forms (incl. affixed forms from continuation classes +
     > cross-product)
> 2. For each dic entry, create compound forms defined by compound flags (+ generate affixed forms
     > from continuation classes + cross-product)
>
> This needs to be reworked _so that continuation can also handle compound flags_. Not sure how to
> do it. Currently, continuation is managed directly by Affixer function only. Compounder functions
> reference Affixer function.
>
> Maybe a Continuation (or even AffixContinuation + CompoundContinuation) should be created as
> output of Affixer and Compounder. Until the Continuation is not "empty", the processing should
> loop on its content.

#### Suggestion Options

None of the suggestion options are actually used since the library does not offer any suggestion
service.

| Option                                                           | Recognized |
|------------------------------------------------------------------|------------|
| KEY characters_separated_by_vertical_line_optionally             | ✔️️        |
| TRY characters                                                   | ✔️         |
| NOSUGGEST flag                                                   | ✔️         |
| MAXCPDSUGS num                                                   | ❌          |
| MAXNGRAMSUGS num                                                 | ✔️         |
| MAXDIFF \[0-10\]                                                 | ❌          |
| ONLYMAXDIFF                                                      | ❌          |
| NOSPLITSUGS                                                      | ✔️         |
| SUGSWITHDOTS                                                     | ❌          |
| REP number_of_replacement_definitions                            | ✔️         |
| REP what replacement                                             | ✔️         |
| MAP number_of_map_definitions                                    | ✔️         |
| MAP string_of_related_chars_or_parenthesized_character_sequences | ✔️         |
| PHONE number_of_phone_definitions                                | ❌          |
| PHONE what replacement                                           | ❌          |
| WARN flag                                                        | ❌          |

#### Other Options

| Option                            | Recognized | Implemented | Notes                                            |
|-----------------------------------|------------|-------------|--------------------------------------------------|
| CIRCUMFIX flag                    | ✔️         | ❌           | Does it impact word form generation?             |
| FORBIDDENWORD flag                | ✔️         | ❌           | Does it impact word form generation?             |
| FULLSTRIP                         | ✔️         | ❌           | Does it impact word form generation?             |
| KEEPCASE flag                     | ✔️         | ❌           | Does it impact word form generation?             |
| ICONV number_of_ICONV_definitions | ✔️         | ❌           | Does it impact word form generation?             |
| ICONV pattern pattern2            | ✔️         | ❌           | Does it impact word form generation?             |
| OCONV number_of_ICONV_definitions | ✔️         | ❌           | Does it impact word form generation?             |
| OCONV pattern pattern2            | ✔️         | ❌           | Does it impact word form generation?             |
| LEMMA_PRESENT flag                | ❌          | ❌           | Deprecated; Does it impact word form generation? |
| NEEDAFFIX flag                    | ✔️         | ❌           | Does it impact word form generation?             |
| PSEUDOROOT flag                   | ❌          | ❌           | Does it impact word form generation?             |
| SUBSTANDARD flag                  | ❌          | ❌           | Does it impact word form generation?             |
| WORDCHARS characters              | ✔️         | ❌           | Does it impact word form generation?             |
| CHECKSHARPS                       | ✔️         | ❌           | Does it impact word form generation?             |

#### Undocumented Options

These options have been spotted in dictionaries but no documentation about them have been found.
These options are recognized by the parser and ignored. Any options not mentioned above nor in the
following table will be rejected by the parser.

| Option          | Notes                                     |
|-----------------|-------------------------------------------|
| HOME url        | Found in LibreOffice's Italian dictionary |
| NAME name       | Found in LibreOffice's Italian dictionary |
| VERSION version | Found in LibreOffice's Italian dictionary |
