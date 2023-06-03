<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## How To: Plug a Presenter

### Audience

This how-to is aimed at developers desiring to plug their own presenter to `croiseur`. It requires
basic knowledge of the Java programming language.

### In a nutshell

1. Create a presenter plugin project
2. Implement the presenter plugin
3. Declare the presenter plugin
4. Install the presenter plugin

### In details

#### 1. Create a presenter plugin project

You can develop a presenter plugin either:

1. Outside `croiseur` source tree, or
2. Inside `croiseur` source tree

The first way is simpler to set up but requires a distribution of `croiseur` and manual
manipulations to test the integration of the presenter plugin inside `croiseur`.

The second way is heavier to set up but allows to more comfortably test and debug the presenter
plugin when run by `croiseur`.

> A presenter is a module which presents the output of the `croiseur` service to the user. It is
> not necessarily bound to a specific controller - i.e. it can be an independent visualizer of the
> outputs of `croiseur` controlled by an application supporting multiple presenters. If this is the
> case, there may be a gain from developing your plugin inside `croiseur` source tree.
>
> However, if the intent is to develop a standalone application including both new controller and
> presenter, then it makes little sense to work within `croiseur` source tree: It would probably be
> more comfortable to develop the software in a new dedicated project, with its own conventions and
> just pull `croiseur` libraries as needed.

##### 1.1. First alternative: Create the project outside `croiseur` source tree

`croiseur-presenter-spi` jar is published
in [a Maven repository](https://gitlab.com/super7ramp/croiseur/-/packages). It can be retrieved with
a build tool such as Maven or Gradle to develop a new presenter plugin without having to download
and rebuild all `croiseur` sources.

Assuming a Gradle project, add the following lines to your project's `build.gradle.kts`:

```gradle
plugins {
    id("java-library")
}

repositories {
    maven {
        name = "CroiseurMaven"
        url = uri("https://gitlab.com/api/v4/projects/43029946/packages/maven")
    }
}

dependencies {
    api("com.gitlab.super7ramp:croiseur-spi-presenter:0.4")
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

###### 1.2.2. Create a new presenter plugin subproject inside `croiseur-presenter`

Create a subproject folder under `croiseur-presenter`:

```sh
mkdir -p croiseur/croiseur-presenter
cd croiseur/croiseur-presenter
mkdir croiseur-presenter-<new_presenter_name>-plugin
```

Add a `build.gradle.kts` (since `croiseur` uses Gradle as build system) in
`croiseur-presenter-<new_presenter_name>-plugin`:

```gradle
plugins {
    id("com.gitlab.super7ramp.croiseur.java-library-conventions")
}

dependencies {
    api(project(":croiseur-spi:croiseur-spi-presenter"))
    // You may add additional implementation dependencies here
}
```

Finally, declare the subproject by adding the following line in root's `settings.gradle.kts`:

```gradle
include("croiseur-presenter:croiseur-presenter-<new_presenter_name>-plugin")
```

At this point, check that everything is OK by running `gradle build`.

#### 2. Implement the presenter plugin

Create a class inside the new plugin project implementing the `CroiseurPresenter` interface defined
in `croiseur-spi-presenter`.

As an example, you may look
at [`croiseur-cli`'s presenter](../../croiseur-cli/src/main/java/com/gitlab/super7ramp/croiseur/cli/presenter/CliPresenter.java),
which just formats the received results and writes it to standard output.

#### 3. Declare the presenter plugin

In order for `croiseur` to use the plugin at run-time, the implementation needs either:

* to advertise itself as a presenter plugin, so that `croiseur` can find it, or
* to be explicitly passed to `croiseur` when instantiating its `CrosswordService`, using one of the
  alternative factory methods.

The second method may be more convenient when developing a new application with both new controllers
and new presenters, and dependencies between them. See
[how it is done in `croiseur-gui`](../../croiseur-gui/croiseur-gui/src/main/java/com/gitlab/super7ramp/croiseur/gui/CrosswordServiceLoader.java)
for an example.

The rest of this section explains the first method.

There are two ways to do that:

- Use the modern module `provides` directive, or
- Use the traditional `META-INF/services` folder.

The first way should be preferred since the `croiseur` project is fully modularised.

For compatibility with custom non-modular deployments (like in `croiseur-tests`), it is advised
to declare the presenter plugin using the second method in addition to the first method.

##### 3.1. Using the module `provides` directive

Create a `module-info.java` file in `src/main/java`. Here is a template of `module-info`:

```
import com.gitlab.super7ramp.croiseur.presenter.<new_presenter_name>.plugin.<NewPresenterName>Presenter;
import com.gitlab.super7ramp.croiseur.spi.presenter.Presenter;

module com.gitlab.super7ramp.croiseur.presenter.<new_presenter_name>.plugin {
    requires com.gitlab.super7ramp.croiseur.spi.presenter;
    provides Presenter with <NewPresenterName>Presenter;
}
```

##### 3.2. Using the `META-INF/services` folder

Create the file `com.gitlab.super7ramp.croiseur.presenter.spi.Presenter`
in `src/main/resources/META-INF/services` and add the qualified name of your implementation as
content, e.g.:

```
com.gitlab.super7ramp.croiseur.presenter.<new_presenter_name>.plugin.<NewPresenterName>Presenter
```

#### 4. Install the presenter plugin

In order for `croiseur` to find the presenter plugin, this one needs to be present in `croiseur`'s
module path.

Assuming a distribution of [`croiseur-cli`](../../croiseur-cli/INSTALL.md), perform the following
actions:

- Put the plugin's jar into the distribution's `lib` folder
- Adjust the `MODULE_PATH` variable in the launcher scripts in the distribution's `bin` folder

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
└── lib                                     # Put the presenter plugin jar in this folder
    ├── croiseur-cli.jar
    ├── (...)
```

##### 4.2. Adjust the `MODULE_PATH` variable in the launcher scripts in the distribution's `bin` folder

Append the path to the presenter plugin jar to the `MODULE_PATH` variable in `bin/croiseur-cli` (for
Unix) and/or `bin/croiseur-cli.bat` (for Windows) so that it includes the new jar:

In `bin/croiseur-cli`:

```sh
MODULE_PATH=$APP_HOME/lib/croiseur-cli.jar:(...):$APP_HOME/lib/<new_presenter_plugin>.jar
```

In `bin/croiseur-cli.bat`:

```bat
set MODULE_PATH=%APP_HOME%\lib\croiseur-cli.jar;(...);%APP_HOME%\lib\<new_presenter_plugin>.jar
```

At this point, it should be possible to interact with the new presenter.

> Note: Modifying the `MODULE_PATH` is tedious. The currently generated `MODULE_PATH` is very
> restrictive. It could be relaxed to include all jars present in `$APP_HOME/lib` instead of
> including only the jars known at distribution time. This way, one could add new plugins to an
> existing installation by just moving the jar to the `lib` folder.
>
> This change is likely to be implemented in the future.

##### Alternative when developing in `croiseur` source tree

When developing a plugin directly inside `croiseur` source tree, the plugin may be added as a
dependency of `croiseur-cli` so that it is included when running the applications
via `gradle run`, or when creating a distribution using `gradle installDist`.

This is a single line to add in the `dependencies` block of the applications' `build.gradle.kts`:

```gradle
runtimeOnly(project(":croiseur-presenter:croiseur-presenter-<your_presenter_name>-plugin"))
```

### See also

- Real plugin implementations:
    - [`croiseur-cli's presenter`](../../croiseur-cli/src/main/java/com/gitlab/super7ramp/croiseur/cli/presenter)
    - [`croiseur-gui's presenter`](../../croiseur-gui/croiseur-gui-presenter)
- TODO croiseur presenter SPI generated Javadoc
- [Java's `ServiceLoader` documentation](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/ServiceLoader.html)
  which defines the plugin declaration format used by `croiseur`.
- [Project Jigsaw homepage](https://openjdk.org/projects/jigsaw/): General information on Java's
  module system.
