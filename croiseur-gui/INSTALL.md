<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## Installation instructions

### From release

No official release of the application has been made yet. This section will be updated when an
official release is made.

### From sources

`croiseur-gui` is written in Java and uses [gradle](https://gradle.org/) as build tool.

Assuming you have a local installation of gradle, you can build `croiseur-gui` with the following
commands:

```
gradle installDist
```

This will generate a portable distribution of `croiseur-gui` inside `build/install/croiseur-gui`.

You may then run the executable like this:

```
./croiseur-gui/build/install/croiseur-gui/bin/croiseur-gui
```

Alternatively, you may run the executable directly via gradle without installation with:

```
gradle run
```

