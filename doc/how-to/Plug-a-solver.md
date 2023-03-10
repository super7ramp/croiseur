<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## How To: Plug a New Solver

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

##### 1.1. First alternative: Create the project outside `croiseur` source tree

> `croiseur-spi-solver` is not published to an artifact repository such as Maven Central yet. As a
> result, it is not possible for now to just pull the SPI jar using Maven or Gradle and develop your
> solver plugin outside `croiseur` source code tree. This section will be updated when SPIs are
> published to an artifact repository.

##### 1.2. Second alternative: Create the project inside `croiseur` source tree

###### 1.2.1. Create a local clone of `croiseur` (or of your own `croiseur`'s fork)

For example:

```
git clone https://gitlab.com/super7ramp/croiseur.git
```

Alternatively, if you are not familiar with git, you may just download a zip archive of `croiseur`
and unzip it.

###### 1.2.2. Create new solver plugin subproject inside `croiseur-solver`

Create a subproject folder under `croiseur-solver`:

```
cd croiseur/croiseur-solver
mkdir croiseur-solver-<your_solver_name>-plugin
```

Add a `build.gradle` (since `croiseur` uses Gradle as build system) in
`croiseur-solver-<your_solver_name>-plugin`:

```
plugins {
    id 'com.gitlab.super7ramp.croiseur.java-library-conventions'
}

dependencies {
    api project(':croiseur-spi:croiseur-spi-solver')
    // You may add additional implementation dependencies here
}
```

Finally, declare the subproject by adding the following line in root's `settings.gradle`:

```
include 'croiseur-solver:croiseur-solver-<your_solver_name>-plugin'
```

At this point, you may check that everything is OK by running `gradle build`.

#### 2. Implement the solver plugin

Create a class inside your plugin project implementing the `CrosswordSolver` interface defined
in `croiseur-spi-solver`.

A commented example is available in `croiseur-solver-example-plugin`:
[ExampleCrosswordSolver](../../croiseur-solver/croiseur-solver-example-plugin/src/main/java/com/gitlab/super7ramp/croiseur/solver/example/plugin/ExampleCrosswordSolver.java).

#### 3. Declare the solver plugin

In order for `croiseur` to find your plugin at run-time, your implementation needs to advertise
itself as a solver plugin.

To do so, create the file `com.gitlab.super7ramp.croiseur.solver.spi.CrosswordSolver` in
`src/main/resources/META-INF/services` and add the qualified name of your implementation as
content, e.g.:

```
com.gitlab.super7ramp.croiseur.solver.<your_solver_name>.plugin.<YourSolverName>CrosswordSolver
```

Also, as `croiseur` is fully modularised, it requires that your implementation is modularised as
well. Basically, it means that your project dependencies shall be declared in a `module-info.java`
file. Here is a template of `module-info`:

```
module com.gitlab.super7ramp.croiseur.solver.<your_solver_name>.plugin {
    requires com.gitlab.super7ramp.croiseur.spi.solver;
    provides CrosswordSolver with <YourSolverName>CrosswordSolver;
}
```

#### 4. Install the solver plugin

In order for `croiseur` to find your solver plugin, this one needs to be present in `croiseur`'s
module path.

Assuming you have a distribution of [`croiseur-cli`](../../croiseur-cli/INSTALL.md) or
[`croiseur-gui`](../../croiseur-gui/INSTALL.md), you have to:

- Put the plugin's jar into the distribution's `lib` folder
- Adjust the `MODULE_PATH` variable in the launcher scripts in `bin` folder

Follows an example for `croiseur-cli` – the process for `croiseur-gui` is similar.

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
└── lib                                              # Put the solver plugin jar in this folder
    ├── croiseur-cli.jar
    ├── (...)
```

##### 4.2. Adjust the `MODULE_PATH` variable in the launcher scripts in the `bin` folder

Append the path to your jar to the `MODULE_PATH` variable in `bin/croiseur-cli` (for Unix) and/or
`bin/croiseur-cli.bat` (for Windows) so that it includes the new jar:

```
# In bin/croiseur-cli
MODULE_PATH=$APP_HOME/lib/croiseur-cli.jar:(...):$APP_HOME/lib/<your_solver_plugin>.jar
```

```
@rem In bin/croiseur-cli.bat
set MODULE_PATH=%APP_HOME%\lib\croiseur-cli.jar;(...);%APP_HOME%\lib\<your_solver_plugin>.jar
```

At this point, you should be able to interact with your solver. You can check that it is detected
using the following command:

```
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

If you are developing your plugin directly inside `croiseur` source tree, you may add your plugin
as a dependency of `croiseur-cli` or `croiseur-gui` so that your plugin is included when you run
the applications using `gradle run`, or when you create a distribution using `gradle installDist`.

This is a single line to add in the `dependencies` block of the applications' `build.gradle`:

```
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
- [Java's `ServiceLoader` documentation](https://docs.oracle.com/javase/8/docs/api/java/util/ServiceLoader.html)
  which defines the plugin declaration format used by `croiseur`.
- [Project Jigsaw homepage](https://openjdk.org/projects/jigsaw/): General information on Java's
  module system.
