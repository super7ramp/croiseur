<!--
SPDX-FileCopyrightText: 2025 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## Installation instructions

> No stable release of the application has been made yet. Software may break and change.

### From a package

No package of Croiseur CLI has been made yet. This section will be updated when a package is made.

### From sources

#### Prerequisites

In order to build `croiseur-cli` from sources, you need:

- [Gradle](https://gradle.org/) 9.0.0 or later
- [Java](https://adoptium.net/temurin/releases/) 25 or later (this one is required at run-time too)
- [Rust](https://www.rust-lang.org/tools/install) 2021 or later

Note that Rust is only needed for
the [Crossword Composer](../croiseur-solver/croiseur-solver-paulgb)
and [xwords-rs](../croiseur-solver/croiseur-solver-szunami) solvers. You may disable them by
commenting the following lines in `croiseur-cli`'s `build.gradle.kts`:

```gradle
runtimeOnly(project(":croiseur-solver:croiseur-solver-paulgb-plugin"))
runtimeOnly(project(":croiseur-solver:croiseur-solver-szunami-plugin"))
```

#### Standard build

You can build `croiseur-cli` with the following command:

```shell
gradle installDist
```

This will generate a portable distribution of `croiseur-cli` inside `build/install/croiseur-cli`.

You can then run the executable like this:

```shell
./croiseur-cli/build/install/croiseur-cli/bin/croiseur-cli help
```

Alternatively, you may run the executable directly via Gradle without installation with:

```shell
gradle run --args=help
```

#### Native image (experimental)

##### Additional prerequisites

Native build requires a specific JVM called [GraalVM](https://www.graalvm.org/). Any version will do
as long as it supports Java 25.

Once GraalVM is installed in a standard location, Gradle should be able to automatically detect and
use it when running the native compilation task.

> Note: Gradle's automatic toolchain detection is currently broken for GraalVM
> ([gh#gradle/gradle#24162](https://github.com/gradle/gradle/issues/24162)). For the time being, set
> the `GRAALVM_HOME` environment variable as explained in the [GraalVM Gradle plugin documentation](https://graalvm.github.io/native-build-tools/latest/gradle-plugin.html#_installing_graalvm_native_image_tool).

##### Procedure

The command to build a native image of `croiseur-cli` is:

```shell
gradle nativeCompile
```

You may then run the executable like this:

```shell
./croiseur-cli/build/native/nativeCompile/croiseur-cli help
```

Note that at the moment, on the contrary of [the standard build](#standard-build), no launcher
script is generated. The consequence is that the executable will not be able to find the
dictionaries nor the saved puzzles out-of-the-box. You need to indicate the paths to these elements
(e.g. taken from a standard build distribution) using system properties, like the following:

```shell
./croiseur-cli/build/native/nativeCompile/croiseur-cli \
  -Dre.belv.croiseur.dictionary.path=<YOUR_DICTIONARY_PATH> \
  -Dre.belv.croiseur.puzzle.path=<YOUR_PUZZLE_PATH> \
  dictionary ls
```
