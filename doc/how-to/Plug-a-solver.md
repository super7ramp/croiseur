<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## How To: Plug a Solver

### Audience

This how-to is aimed at developers desiring to plug their own crossword solvers to `croiseur`. It
requires basic knowledge of the Java programming language.

### In a nutshell

1. Create a solver plugin project
2. Implement the solver plugin
3. Declare the solver plugin
4. Install the solver plugin

### In details

#### 1. Create a solver plugin project

You can develop a solver plugin either:

1. Outside `croiseur` source tree, or
2. Inside `croiseur` source tree

The first way is simpler to set up but requires a distribution of `croiseur` and manual
manipulations to test the integration of the solver plugin inside `croiseur`.

The second way is heavier to set up but allows to more comfortably test and debug the solver plugin
when run by `croiseur`.

##### 1.1. First alternative: Create the project outside `croiseur` source tree

`croiseur-solver-spi` jar is published
in [a Maven repository](https://gitlab.com/super7ramp/croiseur/-/packages). It can be retrieved with
a build tool such as Maven or Gradle to develop a new solver plugin without having to download
and rebuild all `croiseur` sources.

Assuming a Gradle project, add the following lines to your project's `build.gradle`:

```gradle
plugins {
    id 'java-library'
}

repositories {
    maven {
        name = 'CroiseurMaven'
        url = 'https://gitlab.com/api/v4/projects/43029946/packages/maven'
    }
}

dependencies {
    api 'com.gitlab.super7ramp:croiseur-spi-solver:0.1'
}
```

##### 1.2. Second alternative: Create the project inside `croiseur` source tree

###### 1.2.1. Retrieve `croiseur` sources

For example, using Git:

```sh
git clone https://gitlab.com/super7ramp/croiseur.git
```

Alternatively,
download [a zip archive of `croiseur` sources](https://gitlab.com/super7ramp/croiseur/-/archive/master/croiseur-master.zip)
and unzip it.

###### 1.2.2. Create a new solver plugin subproject inside `croiseur-solver`

Create a subproject folder under `croiseur-solver`:

```sh
cd croiseur/croiseur-solver
mkdir croiseur-solver-<new_solver_name>-plugin
```

Add a `build.gradle` (since `croiseur` uses Gradle as build system) in
`croiseur-solver-<new_solver_name>-plugin`:

```gradle
plugins {
    id 'com.gitlab.super7ramp.croiseur.java-library-conventions'
}

dependencies {
    api project(':croiseur-spi:croiseur-spi-solver')
    // You may add additional implementation dependencies here
}
```

Finally, declare the subproject by adding the following line in root's `settings.gradle`:

```gradle
include 'croiseur-solver:croiseur-solver-<new__solver_name>-plugin'
```

At this point, check that everything is OK by running `gradle build`.

#### 2. Implement the solver plugin

Create a class inside the new plugin project implementing the `CrosswordSolver` interface defined
in `croiseur-spi-solver`.

A commented example is available in `croiseur-solver-example-plugin`:
[`ExampleCrosswordSolver`](../../croiseur-solver/croiseur-solver-example-plugin/src/main/java/com/gitlab/super7ramp/croiseur/solver/example/plugin/ExampleCrosswordSolver.java).

#### 3. Declare the solver plugin

In order for `croiseur` to find the plugin at run-time, the implementation needs to advertise
itself as a solver plugin.

There are two ways to do that:

- Use the modern module `provides` directive, or
- Use the traditional `META-INF/services` folder.

The first way should be preferred since the `croiseur` project is fully modularised.

For compatibility with custom non-modular deployments (like in `croiseur-tests`), it is advised
to declare the solver plugin using the second method in addition to the first method.

##### 3.1. Using the module `provides` directive

Create a `module-info.java` file in `src/main/java`. Here is a template of `module-info`:

```
import com.gitlab.super7ramp.croiseur.solver.<new_solver_name>.plugin.<NewSolverName>CrosswordSolver;
import com.gitlab.super7ramp.croiseur.spi.solver.CrosswordSolver;

module com.gitlab.super7ramp.croiseur.solver.<new_solver_name>.plugin {
    requires com.gitlab.super7ramp.croiseur.spi.solver;
    provides CrosswordSolver with <NewSolverName>CrosswordSolver;
}
```

##### 3.2. Using the `META-INF/services` folder

Create the file `com.gitlab.super7ramp.croiseur.solver.spi.CrosswordSolver`
in `src/main/resources/META-INF/services` and add the qualified name of your implementation as
content, e.g.:

```
com.gitlab.super7ramp.croiseur.solver.<new_solver_name>.plugin.<NewSolverName>CrosswordSolver
```

#### 4. Install the solver plugin

In order for `croiseur` to find the solver plugin, this one needs to be present in `croiseur`'s
module path.

Assuming a distribution of [`croiseur-cli`](../../croiseur-cli/INSTALL.md)
or [`croiseur-gui`](../../croiseur-gui/INSTALL.md), perform the following actions:

- Put the plugin's jar into the distribution's `lib` folder
- Adjust the `MODULE_PATH` variable in the launcher scripts in the distribution's `bin` folder

The next two paragraphs detail these steps for `croiseur-cli` – the process for `croiseur-gui`
is similar.

##### 4.1. Put the plugin's jar into the distribution's `lib` folder

```
.
├── bin
│   ├── croiseur-cli
│   └── croiseur-cli.bat
├── data
│   └── dictionaries
│       ├── general-de_DE.xml
│       ├── ...
└── lib                                     # Put the solver plugin jar in this folder
    ├── croiseur-cli.jar
    ├── (...)
```

##### 4.2. Adjust the `MODULE_PATH` variable in the launcher scripts in the distribution's `bin` folder

Append the path to the solver plugin jar to the `MODULE_PATH` variable in `bin/croiseur-cli` (for
Unix) and/or `bin/croiseur-cli.bat` (for Windows) so that it includes the new jar:

In `bin/croiseur-cli`:

```sh
MODULE_PATH=$APP_HOME/lib/croiseur-cli.jar:(...):$APP_HOME/lib/<new_solver_plugin>.jar
```

In `bin/croiseur-clit.bat`:

```bat
set MODULE_PATH=%APP_HOME%\lib\croiseur-cli.jar;(...);%APP_HOME%\lib\<new_solver_plugin>.jar
```

At this point, it should be possible to interact with the new solver. Check that it is detected
using the following command:

```sh
croiseur-cli solver ls
```

The solver should appear in the solvers list.

> Note: Modifying the `MODULE_PATH` is tedious. The currently generated `MODULE_PATH` is very
> restrictive. It could be relaxed to include all jars present in `$APP_HOME/lib` instead of
> including only the jars known at distribution time. This way, one could add new plugins to an
> existing installation by just moving the jar to the `lib` folder.
>
> This change is likely to be implemented in the future.

##### Alternative when developing in `croiseur` source tree

When developing a plugin directly inside `croiseur` source tree, the plugin may be added as a
dependency of `croiseur-cli` or `croiseur-gui` so that it is included when running the applications
via `gradle run`, or when creating a distribution using `gradle installDist`.

This is a single line to add in the `dependencies` block of the applications' `build.gradle`:

```gradle
runtimeOnly project(':croiseur-solver:croiseur-solver-<your_solver_name>-plugin')
```

### See also

- [`croiseur-solver-example-plugin`](../../croiseur-solver/croiseur-solver-example-plugin): A
  minimal example plugin.
- Real plugin implementations:
    - [`croiseur-solver-ginsberg-plugin`](../../croiseur-solver/croiseur-solver-ginsberg-plugin)
    - [`croiseur-solver-paulgb-plugin`](../../croiseur-solver/croiseur-solver-paulgb-plugin)
    - [`croiseur-solver-szunami-plugin`](../../croiseur-solver/croiseur-solver-szunami-plugin)
- TODO croiseur solver SPI generated Javadoc
- [Java's `ServiceLoader` documentation](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/ServiceLoader.html)
  which defines the plugin declaration format used by `croiseur`.
- [Project Jigsaw homepage](https://openjdk.org/projects/jigsaw/): General information on Java's
  module system.
