<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## Installation instructions

### From release

No official release of the application has been made yet. This section will be updated when an 
official release is made.

### From sources

`croiseur-cli` is written in Java and uses [gradle](https://gradle.org/) as build tool.

Assuming you have a local installation of gradle, you can build `croiseur-cli` with the following 
commands:

```
gradle installDist
```

This will generate a portable distribution of `croiseur-cli` inside `build/install/croiseur-cli`.

You may then run the executable like this:

```
./build/install/croiseur-cli/bin/croiseur-cli help
```

Alternatively, you may run the executable directly via gradle without installation with:

```
gradle run --args=help
```
