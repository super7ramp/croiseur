<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## croiseur-dictionary-txt-plugin

`croiseur-dictionary-txt-plugin` is a `croiseur` dictionary provider using word lists written
as simple text files.

The word lists are exclusively retrieved from the local path(s) declared in
the `com.gitlab.super7ramp.croiseur.dictionary.path` system property. Dictionaries are not retrieved
from network.

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