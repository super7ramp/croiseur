<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## How to: Add a dictionary to an existing dictionary provider

### In a nutshell

1. Create or retrieve a dictionary supported by the dictionary providers.
2. Locate the `croiseur` dictionary path.
3. Add the dictionary file(s) to the dictionary path.

### In details

#### 1. Create or retrieve a dictionary supported by the dictionary providers

Each dictionary provider supports a particular format:

* Text dictionary provider: A text file with one word per line + a description file. Very simple,
  perfect for simple user-defined dictionaries.
  Check [the provider documentation](../../croiseur-dictionary/croiseur-dictionary-txt-plugin/README.md)
  for more details.
* XML dictionary provider: An XML file respecting a certain schema. Interesting if you have to
  programmatically convert from another dictionary format. Check [the provider
  documentation](../../croiseur-dictionary/croiseur-dictionary-xml-codec/README.md)
  for more details.
* Hunspell dictionary provider: A [Hunspell](https://hunspell.github.io/) dictionary. Popular format
  for dictionaries,
  but [not well-supported by `croiseur` and disabled by default](../reference/Available-service-providers.md).
  Only useful if you have an existing dictionary and no way to convert it.

#### 2. Locate the `croiseur` dictionary path

The dictionary path is where `croiseur` dictionary providers are looking for dictionaries. It is
typically `${INSTALL_ROOT}/data/dictionaries` and is actually set via the system
property `re.belv.croiseur.dictionary.path`.

Example of `croiseur-gui` distribution:

```
croiseur-gui/
├── bin
│   ├── croiseur-gui              // The launchers; System property is set there
│   └── croiseur-gui.bat
├── data
│   └── dictionaries              // This is where the dictionaries are
│       ├── general-de_DE.xml
│       ├── general-en_GB.xml
│       ├── general-es_ES.xml
│       ├── general-fr_FR.xml
│       ├── general-it_IT.xml
│       ├── UKACD18plus.txt
│       └── UKACD18plus.txt.properties
└── lib
    ├── (a bunch of jars)
```

#### 3. Add the dictionary file(s) to the dictionary path

Simply copy-paste the file(s) to the location determined in previous step.

Alternatively, you may customize the value of the `re.belv.croiseur.dictionary.path` system
property. This can be done by manually modifying the value in the launcher script or by
using the environment variables `CROISEUR_CLI_OPTS` and `CROISEUR_GUI_OPTS`, e.g.:

```
export CROISEUR_CLI_OPTS="-Dre.belv.croiseur.dictionary.path=${MY_CUSTOM_DICTIONARIES}"
./bin/croiseur-cli
```
