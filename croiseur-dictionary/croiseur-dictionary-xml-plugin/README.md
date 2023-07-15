<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## croiseur-dictionary-xml-plugin

`croiseur-dictionary-xml-plugin` is a `croiseur` dictionary provider using word lists written
in [XML files](../croiseur-dictionary-xml-codec).

The word lists are exclusively retrieved from:

- `croiseur/dictionaries` directory relative to user home, if any, and;
- Local path(s) declared in the `com.gitlab.super7ramp.croiseur.dictionary.path` system property,
  if any, and;
- `dictionaries` directory relative to installation directory

Dictionaries are not retrieved from network.

