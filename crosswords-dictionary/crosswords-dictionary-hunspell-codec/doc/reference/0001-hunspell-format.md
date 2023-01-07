## The Hunspell Format

### External resources

* [man 5 hunspell](https://helpmanual.io/man5/hunspell/): The following mostly paraphrases this 
  manual page.

### Overview

A Hunspell dictionary is composed of two text files:

* A "dictionary" file, with extension `.dic`
* An "affix" file, with extension `.aff`

The dictionary file lists the _base words_ (or _root words_) of the dictionary. Each base word 
can be annotated with _flags_ in order to represent various _compounds_ forms - e.g. conjugated 
forms.

The affix file describes what the _flags_ of the dictionary file do.

#### Example

Dictionary file:
```
3
hello
try/B
work/AB
```

3 base words are defined. Word "try" is annotated with a flag named "B". Word "work" is annotated 
with two flags "A" and "B"

The following affix defines the meaning of these flags:

```
SET UTF-8

PFX A Y 1
PFX A 0 re .

SFX B Y 2
SFX B 0 ed [^y]
SFX B y ied y
```

Flag "A" is a prefix flag. It indicates that "re" can be placed before the base word it 
annotates to form a compound word.

Flag "B" is a suffix flag. It indicates that:
* "ed" can be placed after the base word the flag annotates, provided the base word doesn't 
  terminate with the letter y
* "ied" can be placed after the base word the flag annotates when the base word terminates with 
  the letter y

All accepted words with this dictionary and affix combination are: "hello", "try", "tried", "work",
"worked", "rework", "reworked".

### The dictionary file

The first line contains a string representation of an integer, indicating the estimated number of 
base words in the file. (It doesn't need to be precise, it's just an indication that may be of some 
help for optimising programs reading/interpreting the file.)

Each following line has the following format: `<base word>/<flag>`.
A base word has at least one character.
A flag is exactly one character and cannot be "/".

> Well... not exactly. Flags can be actually multi-characters. See FLAG option in affix file.

The dictionary file is the simplest of the two: The complexity resides in the affix file.

### The affix file

Affix file doesn't contain only prefixes and suffixes flag definitions but many other kinds of 
so-called _options_.

General format is `<OPTION> [some option parameter...]`.

The following describes the different available options. It insists on options necessary for 
word form generations. Options related to word suggestion are simply listed, see manual page for 
details.

#### General options

##### SET encoding

Set character encoding of words and morphemes in affix and dictionary files. Possible 
values: UTF-8, ISO8859-1 - ISO8859-10, ISO8859-13 - ISO8859-15, KOI8-R, KOI8-U, cp1251, 
ISCII-DEVANAGARI. 

Example:

    SET UTF-8

##### FLAG value

Set flag type. Default type is the extended ASCII (8-bit) character. `UTF-8` parameter sets
UTF-8 encoded Unicode character flags. The `long` value sets the double extended ASCII character
flag type, the `num` sets the decimal number flag type. Decimal flags numbered from 1 to 65000,
and in flag fields are separated by comma. 

Example:

    FLAG long

##### COMPLEXPREFIXES

Set twofold prefix stripping (but single suffix stripping) e.g. for morphologically complex 
languages with right-to-left writing system.

> Not supported by crosswords-hunspell-plugin.

##### LANG langcode

Set language code for language specific functions of Hunspell. Use it to activate special casing of
Azeri (LANG az) and Turkish (LANG tr).

> Not supported by crosswords-hunspell-plugin.

##### IGNORE characters

Sets characters to ignore dictionary words, affixes and input words. Useful for optional characters,
as Arabic (harakat) or Hebrew (niqqud) diacritical marks (see tests/ignore.* test dictionary in
Hunspell distribution).

> Not supported by crosswords-hunspell-plugin.

##### AF

Either `AF number_of_flag_vector_aliases` or `AF flag_vector`

Hunspell can substitute affix flag sets with ordinal numbers in affix rules (alias compression, see
makealias tool).

> Not supported by crosswords-hunspell-plugin.

##### AM

Either `AM number_of_morphological_aliases` or `AM morphological_fields`

Hunspell can substitute also morphological data with ordinal numbers in affix rules
(alias compression). See tests/alias* examples. 

> Not supported by crosswords-hunspell-plugin.

#### Affix Creation Options

These are the core options.

```
PFX flag cross_product number
PFX flag stripping prefix [condition [morphological_fields...]]
SFX flag cross_product number
SFX flag stripping suffix [condition [morphological_fields...]]
```

An affix is either a prefix or a suffix attached to root words to make other words.
We can define affix classes with arbitrary number affix rules.

Affix classes are signed with affix flags. The first line of an affix class definition is the 
header. The fields of an affix class header:

1. Option name (PFX or SFX)
2. Flag (name of the affix class)
3. Cross product (permission to combine prefixes and suffixes). Possible values: Y (yes) or N (no)
4. Line count of the following rules.

Fields of an affix rules:

1. Option name
2. Flag
3. Stripping characters from beginning (at prefix rules) or end (at suffix rules) of the word
4. Affix (optionally with flags of continuation classes, separated by a slash)
5. Condition
6. Optional morphological fields separated by spaces or tabulators.

Zero stripping or affix are indicated by zero.

Zero condition is indicated by dot.

Condition is a simplified, regular expression-like pattern, which must be met before the affix 
can be applied. (Dot signs an arbitrary character. Characters in braces sign an arbitrary 
character from the character subset. Dash hasn't got special meaning, but circumflex (^) next 
the first brace sets the complementer character set.)


> TODO: What does "6. Optional morphological fields separated by spaces or tabulators" represent 
> exactly?

> TODO: Support status?

#### Compounding Options

TODO

```

BREAK number_of_break_definitions
BREAK character_or_character_sequence
    Define new break points for breaking words and checking word parts separately. Use ^ and $ to delete characters at end and start of the word. Rationale: useful for compounding with joining character or strings (for example, hyphen in English and German or hyphen and n-dash in Hungarian). Dashes are often bad break points for tokenization, because compounds with dashes may contain not valid parts, too.) With BREAK, Hunspell can check both side of these compounds, breaking the words at dashes and n-dashes: 

    BREAK 2
    BREAK -
    BREAK --    # n-dash

Breaking are recursive, so foo-bar, bar-foo and foo-foo--bar-bar would be valid compounds. Note: The default word break of Hunspell is equivalent of the following BREAK definition:

    BREAK 3
    BREAK -
    BREAK ^-
    BREAK -$

Hunspell doesn't accept the "-word" and "word-" forms by this BREAK definition:

    BREAK 1
    BREAK -


Switching off the default values:

    BREAK 0


Note II: COMPOUNDRULE is better for handling dashes and other compound joining characters or character strings. Use BREAK, if you want to check words with dashes or other joining characters and there is no time or possibility to describe precise compound rules with COMPOUNDRULE (COMPOUNDRULE handles only the suffixation of the last word part of a compound word).
Note III: For command line spell checking of words with extra characters, set WORDCHARS parameters: WORDCHARS --- (see tests/break.*) example

COMPOUNDRULE number_of_compound_definitions
COMPOUNDRULE compound_pattern
    Define custom compound patterns with a regex-like syntax. The first COMPOUNDRULE is a header with the number of the following COMPOUNDRULE definitions. Compound patterns consist compound flags, parentheses, star and question mark meta characters. A flag followed by a `*' matches a word sequence of 0 or more matches of words signed with this compound flag. A flag followed by a `?' matches a word sequence of 0 or 1 matches of a word signed with this compound flag. See tests/compound*.* examples.
    Note: en_US dictionary of OpenOffice.org uses COMPOUNDRULE for ordinal number recognition (1st, 2nd, 11th, 12th, 22nd, 112th, 1000122nd etc.).
    Note II: In the case of long and numerical flag types use only parenthesized flags: (1500)*(2000)?
    Note III: COMPOUNDRULE flags work completely separately from the compounding mechanisms using COMPOUNDFLAG, COMPOUNDBEGIN, etc. compound flags. (Use these flags on different entries for words).
COMPOUNDMIN num
    Minimum length of words used for compounding. Default value is 3 letters. 
COMPOUNDFLAG flag
    Words signed with COMPOUNDFLAG may be in compound words (except when word shorter than COMPOUNDMIN). Affixes with COMPOUNDFLAG also permits compounding of affixed words. 
COMPOUNDBEGIN flag
    Words signed with COMPOUNDBEGIN (or with a signed affix) may be first elements in compound words. 
COMPOUNDLAST flag
    Words signed with COMPOUNDLAST (or with a signed affix) may be last elements in compound words. 
COMPOUNDMIDDLE flag
    Words signed with COMPOUNDMIDDLE (or with a signed affix) may be middle elements in compound words. 
ONLYINCOMPOUND flag
    Suffixes signed with ONLYINCOMPOUND flag may be only inside of compounds (Fuge-elements in German, fogemorphemes in Swedish). ONLYINCOMPOUND flag works also with words (see tests/onlyincompound.*). Note: also valuable to flag compounding parts which are not correct as a word by itself. 
COMPOUNDPERMITFLAG flag
    Prefixes are allowed at the beginning of compounds, suffixes are allowed at the end of compounds by default. Affixes with COMPOUNDPERMITFLAG may be inside of compounds. 
COMPOUNDFORBIDFLAG flag
    Suffixes with this flag forbid compounding of the affixed word. 
COMPOUNDMORESUFFIXES
    Allow twofold suffixes within compounds. 
COMPOUNDROOT flag
    COMPOUNDROOT flag signs the compounds in the dictionary (Now it is used only in the Hungarian language specific code). 
COMPOUNDWORDMAX number
    Set maximum word count in a compound word. (Default is unlimited.) 
CHECKCOMPOUNDDUP
    Forbid word duplication in compounds (e.g. foofoo). 
CHECKCOMPOUNDREP
    Forbid compounding, if the (usually bad) compound word may be a non compound word with a REP fault. Useful for languages with `compound friendly' orthography. 
CHECKCOMPOUNDCASE
    Forbid upper case characters at word boundaries in compounds. 
CHECKCOMPOUNDTRIPLE
    Forbid compounding, if compound word contains triple repeating letters (e.g. foo|ox or xo|oof). Bug: missing multi-byte character support in UTF-8 encoding (works only for 7-bit ASCII characters). 
SIMPLIFIEDTRIPLE
    Allow simplified 2-letter forms of the compounds forbidden by CHECKCOMPOUNDTRIPLE. It's useful for Swedish and Norwegian (and for the old German orthography: Schiff|fahrt -> Schiffahrt). 
CHECKCOMPOUNDPATTERN number_of_checkcompoundpattern_definitions
CHECKCOMPOUNDPATTERN endchars[/flag] beginchars[/flag] [replacement]
    Forbid compounding, if the first word in the compound ends with endchars, and next word begins with beginchars and (optionally) they have the requested flags. The optional replacement parameter allows simplified compound form.
    The special "endchars" pattern 0 (zero) limits the rule to the unmodified stems (stems and stems with zero affixes): 

    CHECKCOMPOUNDPATTERN 0/x /y

Note: COMPOUNDMIN doesn't work correctly with the compound word alternation, so it may need to set COMPOUNDMIN to lower value.

FORCEUCASE flag
    Last word part of a compound with flag FORCEUCASE forces capitalization of the whole compound word. Eg. Dutch word "straat" (street) with FORCEUCASE flags will allowed only in capitalized compound forms, according to the Dutch spelling rules for proper names. 
COMPOUNDSYLLABLE max_syllable vowels
    Need for special compounding rules in Hungarian. First parameter is the maximum syllable number, that may be in a compound, if words in compounds are more than COMPOUNDWORDMAX. Second parameter is the list of vowels (for calculating syllables). 
SYLLABLENUM flags
    Need for special compounding rules in Hungarian. 
```

#### Suggestion Options

See manual page. Note are supported by crossword-dictionary-hunspell-plugin.

> TODO is it unsupported or ignore? It should be just ignored, library should not fail parsing a 
> file with such options, library should not use it since it doesn't offer suggestion services

##### KEY characters_separated_by_vertical_line_optionally
##### TRY characters
##### NOSUGGEST flag
##### MAXCPDSUGS num
##### MAXNGRAMSUGS num 
##### MAXDIFF [0-10] 
##### ONLYMAXDIFF 
##### NOSPLITSUGS 
##### SUGSWITHDOTS 
##### REP number_of_replacement_definitions
##### REP what replacement
##### MAP number_of_map_definitions
##### MAP string_of_related_chars_or_parenthesized_character_sequences
##### PHONE number_of_phone_definitions
##### PHONE what replacement 
##### WARN flag 
##### FORBIDWARN

#### Other Options

TODO

```
CIRCUMFIX flag
    Affixes signed with CIRCUMFIX flag may be on a word when this word also has a prefix with CIRCUMFIX flag and vice versa (see circumfix.* test files in the source distribution). 
FORBIDDENWORD flag
    This flag signs forbidden word form. Because affixed forms are also forbidden, we can subtract a subset from set of the accepted affixed and compound words. Note: usefull to forbid erroneous words, generated by the compounding mechanism. 
FULLSTRIP
    With FULLSTRIP, affix rules can strip full words, not only one less characters, before adding the affixes, see fullstrip.* test files in the source distribution). Note: conditions may be word length without FULLSTRIP, too. 
KEEPCASE flag
    Forbid uppercased and capitalized forms of words signed with KEEPCASE flags. Useful for special orthographies (measurements and currency often keep their case in uppercased texts) and writing systems (e.g. keeping lower case of IPA characters). Also valuable for words erroneously written in the wrong case.
    Note: With CHECKSHARPS declaration, words with sharp s and KEEPCASE flag may be capitalized and uppercased, but uppercased forms of these words may not contain sharp s, only SS. See germancompounding example in the tests directory of the Hunspell distribution.
ICONV number_of_ICONV_definitions
ICONV pattern pattern2
    Define input conversion table. Note: useful to convert one type of quote to another one, or change ligature. 
OCONV number_of_OCONV_definitions
OCONV pattern pattern2
    Define output conversion table. 
LEMMA_PRESENT flag
    Deprecated. Use "st:" field instead of LEMMA_PRESENT. 
NEEDAFFIX flag
    This flag signs virtual stems in the dictionary, words only valid when affixed. Except, if the dictionary word has a homonym or a zero affix. NEEDAFFIX works also with prefixes and prefix + suffix combinations (see tests/pseudoroot5.*). 
PSEUDOROOT flag
    Deprecated. (Former name of the NEEDAFFIX option.) 
SUBSTANDARD flag
    SUBSTANDARD flag signs affix rules and dictionary words (allomorphs) not used in morphological generation (and in suggestion in the future versions). See also NOSUGGEST. 
WORDCHARS characters
    WORDCHARS extends tokenizer of Hunspell command line interface with additional word character. For example, dot, dash, n-dash, numbers, percent sign are word character in Hungarian. 
CHECKSHARPS
    SS letter pair in uppercased (German) words may be upper case sharp s (ß). Hunspell can handle this special casing with the CHECKSHARPS declaration (see also KEEPCASE flag and tests/germancompounding example) in both spelling and suggestion. 
```
### Encoding

The two files can be encoded in:

* UTF-8
* ISO8859-1
* ISO8859-10
* ISO8859-13
* ISO8859-15
* KOI8-R
* KOI8-U
* cp1251
* ISCII-DEVANAGARI

The encoding is explicitly declared in the affix file, see [the affix file section](#the-affix-file)
for details.