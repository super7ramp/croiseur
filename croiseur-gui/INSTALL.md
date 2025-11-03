<!--
SPDX-FileCopyrightText: 2025 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## Installation instructions

> No stable release of the application has been made yet. Software may break and change.

### From a package

Installable packages for Windows, macOS and Linux are available
on [a dedicated download page](https://croiseur.belv.re) as well as on the
Gitlab [release page](https://gitlab.com/super7ramp/croiseur/-/releases).

Important notes:

* macOS and Windows packages are auto-updatable: Application will
  contact [this website](https://croiseur.belv.re) on startup for updates;
* On Linux, installing the Debian package will add [this APT repository](https://croiseur.belv.re)
  to the system's repository list so that the application can be updated using the system's update
  manager;
* Packages only contain two crossword solvers among the four
  available (issue [#81](https://gitlab.com/super7ramp/croiseur/-/issues/81)).
* Packages are not thoroughly tested. Feedbacks are welcome!

### From sources

In order to build Croiseur GUI from sources, you need:

- [Gradle](https://gradle.org/) 9.0.0 or later
- [Java](https://adoptium.net/temurin/releases/) 25 or later (this one is required at run-time too)
- [Rust](https://www.rust-lang.org/tools/install) 2021 or later

Note that Rust is only needed for
the [Crossword Composer](../croiseur-solver/croiseur-solver-paulgb)
and [XWords RS](../croiseur-solver/croiseur-solver-szunami) solvers. You may disable them by
commenting the following lines in `croiseur-gui`'s `build.gradle.kts`:

```gradle
runtimeOnly(project(":croiseur-solver:croiseur-solver-paulgb-plugin"))
runtimeOnly(project(":croiseur-solver:croiseur-solver-szunami-plugin"))
```

You can build Croiseur GUI with the following command:

```shell
gradle installDist
```

This will generate a portable distribution of Croiseur GUI inside `build/install/croiseur-gui`.

You may then run the executable like this:

```shell
./croiseur-gui/build/install/croiseur-gui/bin/croiseur-gui
```

Alternatively, you may run the executable directly via gradle without installation with:

```shell
gradle run
```
