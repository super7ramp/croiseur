<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## Installation instructions

### From release

No stable release of the application has been made yet. This section will be updated when a stable
release is made.

### From sources

In order to build `croiseur-gui` from sources, you need:

- [Gradle](https://gradle.org/) 7.6 or later
- [Java](https://adoptium.net/temurin/releases/) 17 or later (this one is required at run-time too)
- [Rust](https://www.rust-lang.org/tools/install) 2021 or later

Note that Rust is only needed for
the [Crossword Composer](../croiseur-solver/croiseur-solver-paulgb)
and [XWords RS](../croiseur-solver/croiseur-solver-szunami) solvers. You may disable them by
commenting the following lines in `croiseur-gui`'s `build.gradle`:

```gradle
runtimeOnly project(':croiseur-solver:croiseur-solver-paulgb-plugin')
runtimeOnly project(':croiseur-solver:croiseur-solver-szunami-plugin')
```

You can build `croiseur-gui` with the following command:

```shell
gradle installDist
```

This will generate a portable distribution of `croiseur-gui` inside `build/install/croiseur-gui`.

You may then run the executable like this:

```shell
./croiseur-gui/build/install/croiseur-gui/bin/croiseur-gui
```

Alternatively, you may run the executable directly via gradle without installation with:

```shell
gradle run
```
