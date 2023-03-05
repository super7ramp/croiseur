<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## Reference: Available Service Providers

This page lists the available [Service Providers][] pluggable to croiseur.

### Dictionaries

#### Providers

| Name                                   | Description                                                   | Note                                     |
|----------------------------------------|---------------------------------------------------------------|------------------------------------------|
| [Local Hunspell Dictionary Provider][] | Provides access to local dictionaries in the Hunspell format. | Disabled by default (too slow and buggy) |
| [Local XML Dictionary Provider][]      | Provides access to local dictionaries in an XML format.       |                                          |
| [Local Text Dictionary Provider][]     | Provides access to local dictionaries in a simple text format |                                          |

#### Dictionary List

| Provider                       | Name                                | Locale                  | Description                                                        |
|--------------------------------|-------------------------------------|-------------------------|--------------------------------------------------------------------|
| Local Text Dictionary Provider | The UK Advanced Cryptics Dictionary | English (Great-Britain) | A popular British English dictionary for crossword solving         |
| Local XML Dictionary Provider  | General German dictionary           | German (Germany)        | Dictionary adapted from [LibreOffice German dictionary][]          |
| Local XML Dictionary Provider  | General British English dictionary  | English (Great-Britain) | Dictionary adapted from [LibreOffice British English dictionary][] |
| Local XML Dictionary Provider  | General Spanish dictionary          | Spanish (Spain)         | Dictionary adapted from [LibreOffice Spanish dictionary][]         |
| Local XML Dictionary Provider  | General French dictionary           | French (France)         | Dictionary adapted from [LibreOffice French dictionary][]          |
| Local XML Dictionary Provider  | General Italian dictionary          | Italian (Italia)        | Dictionary adapted from [LibreOffice Italian dictionary][]         |

### Solvers

| Name                            | Description                                                                                                         |
|---------------------------------|---------------------------------------------------------------------------------------------------------------------|
| [Ginsberg][]                    | A crossword solver based on [Ginsberg's papers][]. Written in Java.                                                 |
| [Crossword Composer (paulgb)][] | The solver powering [Crossword Composer][]. Doesn't support grids prefilled with words or letters. Written in Rust. |
| [XWords RS (szunami)][]         | The solver powering the [XWords RS][] tool. Written in Rust.                                                        |

### Presenters

| Name    | Description                                             |
|---------|---------------------------------------------------------|
| [cli][] | Presents croiseur output in the standard console output |
| [gui][] | Presents croiseur output in a graphical window          |

<!-- Reference Links -->

[cli]: ../../croiseur-cli

[Crossword Composer]: https://github.com/paulgb/crossword-composer

[Crossword Composer (paulgb)]: ../../croiseur-solver/croiseur-solver-paulgb-plugin

[Ginsberg]: ../../croiseur-solver/croiseur-solver-ginsberg-plugin

[Ginsberg's papers]: https://www.aaai.org/Papers/AAAI/1990/AAAI90-032.pdf

[gui]: ../../croiseur-gui

[LibreOffice British English dictionary]: ../../croiseur-dictionary/croiseur-dictionary-hunspell-data/libreoffice-dictionaries/en

[LibreOffice French dictionary]: ../../croiseur-dictionary/croiseur-dictionary-hunspell-data/libreoffice-dictionaries/fr_FR

[LibreOffice German dictionary]: ../../croiseur-dictionary/croiseur-dictionary-hunspell-data/libreoffice-dictionaries/de

[LibreOffice Italian dictionary]: ../../croiseur-dictionary/croiseur-dictionary-hunspell-data/libreoffice-dictionaries/it_IT

[LibreOffice Spanish dictionary]: ../../croiseur-dictionary/croiseur-dictionary-hunspell-data/libreoffice-dictionaries/es

[Local Hunspell Dictionary Provider]: ../../croiseur-dictionary/croiseur-dictionary-hunspell-plugin

[Local Text Dictionary Provider]: ../../croiseur-dictionary/croiseur-dictionary-txt-plugin

[Local XML Dictionary Provider]: ../../croiseur-dictionary/croiseur-dictionary-xml-plugin

[Service Providers]: ../../croiseur-spi

[XWords RS (szunami)]: ../../croiseur-solver/croiseur-solver-szunami-plugin

[XWords RS]: https://github.com/szunami/xwords-rs
