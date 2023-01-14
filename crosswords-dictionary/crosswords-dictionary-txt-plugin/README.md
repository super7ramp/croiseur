<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## crosswords-dictionary-txt-plugin

`crosswords-dictionary-txt-plugin` is a crossword dictionary provider that reads simple text 
dictionaries.

In order to be read, the dictionaries must satisfy the following requirements:

* Their file names shall be suffixed with ".txt", e.g. "example.txt";
* They shall contain exactly 1 entry per line;
* They shall be accompanied by a description file which itself shall satisfy the following 
  requirements:
    * Name: Dictionary file name + ".properties" extension, e.g. "example.txt.properties"
    * Content: Valid properties with at least the keys "locale", "name" and "description"

Example of companion description file:

```
locale=en-GB
name=The UK Advanced Cryptics Dictionary
name[fr]=UK Advanced Cryptics Dictionary
description=The UKACD is a word list compiled for the crossword community.
description[fr]=L'UKACD est une liste de mots compilée pour la communauté cruciverbiste.
```