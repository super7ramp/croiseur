<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## Installation instructions

### From release

No stable release of the application has been made yet. This section will be updated when a stable
release is made.

### From sources

#### Prerequisites

In order to build `croiseur-cli` from sources, you need:

- [Gradle](https://gradle.org/) 7.6 or later
- [Java](https://adoptium.net/temurin/releases/) 17 or later (this one is required at run-time too)
- [Rust](https://www.rust-lang.org/tools/install) 2021 or later

Note that Rust is only needed for
the [Crossword Composer](../croiseur-solver/croiseur-solver-paulgb)
and [xwords-rs](../croiseur-solver/croiseur-solver-szunami) solvers. You may disable them by
commenting the following lines in `croiseur-cli`'s `build.gradle`:

```gradle
runtimeOnly project(':croiseur-solver:croiseur-solver-paulgb-plugin')
runtimeOnly project(':croiseur-solver:croiseur-solver-szunami-plugin')
```

#### Standard build

You can build `croiseur-cli` with the following command:

```shell
gradle installDist
```

This will generate a portable distribution of `croiseur-cli` inside `build/install/croiseur-cli`.

You can then run the executable like this:

```shell
./build/install/croiseur-cli/bin/croiseur-cli help
```

Alternatively, you may run the executable directly via Gradle without installation with:

```shell
gradle run --args=help
```

#### Native image (experimental)

##### Additional prerequisites

Native build requires a specific JVM called [GraalVM](https://www.graalvm.org/). Any version will do
as long as it supports Java 17.

If you encounter issues with Gradle not detecting GraalVM, check
out [this section](https://graalvm.github.io/native-build-tools/latest/gradle-plugin.html#_installing_graalvm_native_image_tool)
of the GraalVM Gradle plugin documentation for troubleshooting.

##### Procedure

The command to build a native image of `croiseur-cli` is:

```shell
gradle nativeCompile
```

You may then run the executable like this:

```shell
./build/native/nativeCompile/croiseur-cli help
```

Note that at the moment, on the contrary of [the standard build](#standard-build), no launcher
script is generated. The consequence is that the executable won't be able to find the
dictionaries out-of-the-box. You need to indicate the path to the dictionaries (e.g. taken from a
standard build distribution) via the `com.gitlab.super7ramp.croiseur.dictionary.path` property,
e.g.:

```shell
./build/native/nativeCompile/croiseur-cli \
  -Dcom.gitlab.super7ramp.croiseur.dictionary.path=<YOUR_DICTIONARY_PATH> \
  dictionary ls
```
