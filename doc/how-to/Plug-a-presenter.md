<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## How To: Plug a Presenter

### Audience

This how-to is aimed at developers desiring to plug their own presenter to `croiseur`. It requires
basic knowledge of the Java programming language.

### Assumptions

A presenter is a module which presents the output of the `croiseur` service to the user. It is
not necessarily bound to a specific controller - i.e. it can be an independent visualizer of the
outputs of `croiseur` controlled by an application supporting multiple presenters. Technically,
`croiseur-cli` supports multiple presenters.

However, developing a presenter is more likely to occur when developing a new application with its
own controllers. Thus, this how-to assumes that the development of the presenter is made outside
croiseur source tree and an application calling `croiseur` service exists. It just describes how to
plug the presentation part.

### In a nutshell

1. Create a presenter plugin project
2. Implement the presenter plugin
3. Declare the presenter plugin
4. Install the presenter plugin

### In details

#### 1. Create a presenter plugin project

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

#### 2. Implement the presenter plugin

Create a class inside the new plugin project implementing the `CroiseurPresenter` interface defined
in `croiseur-spi-presenter`.

As an example, you may look
at [`croiseur-cli`'s presenter](../../croiseur-cli/src/main/java/com/gitlab/super7ramp/croiseur/cli/presenter/CliPresenter.java),
which just formats the received results and writes it to standard output.

#### 3. Declare the presenter plugin

In order for `croiseur` to use the plugin at run-time, the implementation needs either:

* To be explicitly passed to `croiseur` when instantiating its `CrosswordService`, using one of the
  alternative factory methods, or
* To advertise itself as a presenter plugin, so that `croiseur` can find it.

The first method may be more convenient within an application where both controllers and presenters
share dependencies.
See [how it is done in `croiseur-gui`](../../croiseur-gui/croiseur-gui/src/main/java/com/gitlab/super7ramp/croiseur/gui/CrosswordServiceLoader.java)
for an example.

The rest of this section explains the second method.

There are two ways to do that:

- Use the modern module `provides` directive, or
- Use the traditional `META-INF/services` folder.

The first way should be preferred since the Croiseur project is fully modularised.

For compatibility with custom non-modular deployments (like in `croiseur-tests`), it is advised
to declare the presenter plugin using the second method in addition to the first method.

##### 3.1. Using the module `provides` directive

Create a `module-info.java` file in presenter's `src/main/java`. Here is a template
of `module-info`:

```
import com.example.app.presenter.<new_presenter_name>.plugin.<NewPresenterName>Presenter;
import com.gitlab.super7ramp.croiseur.spi.presenter.Presenter;

module com.example.app.presenter.<new_presenter_name>.plugin {
    requires com.gitlab.super7ramp.croiseur.spi.presenter;
    provides Presenter with <NewPresenterName>Presenter;
}
```

##### 3.2. Using the `META-INF/services` folder

Create the file `com.gitlab.super7ramp.croiseur.presenter.spi.Presenter` in
presenter's `src/main/resources/META-INF/services` and add the qualified name of
the implementation as content, e.g.:

```
com.example.app.presenter.<new_presenter_name>.plugin.<NewPresenterName>Presenter
```

#### 4. Install the presenter plugin

In order for `croiseur` to find the presenter plugin, this one needs to be present in `croiseur`'s
module path.

##### 4.1. For a new application

In order for `croiseur` to find the presenter plugin, this one needs to be present in `croiseur`'s
module path.

In this case, it suffices to add the following line to the `dependencies` block of the new
applications' `build.gradle.kts`:

```gradle
runtimeOnly(project(":croiseur-presenter:croiseur-presenter-<your_presenter_name>-plugin"))
```

##### 4.2. For `croiseur-cli`

Assuming a distribution of [`croiseur-cli`](../../croiseur-cli/INSTALL.md), perform the following
actions:

- Put the plugin's jar into the distribution's `lib` folder
- Adjust the `MODULE_PATH` variable in the launcher scripts in the distribution's `bin` folder

###### 4.2.1. Put the plugin's jar into the distribution's `lib` folder

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

###### 4.2.2. Adjust the `MODULE_PATH` variable in the launcher scripts in the distribution's `bin` folder

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

### See also

- Real plugin implementations:
    - [`croiseur-cli's presenter`](../../croiseur-cli/src/main/java/com/gitlab/super7ramp/croiseur/cli/presenter)
    - [`croiseur-gui's presenter`](../../croiseur-gui/croiseur-gui-presenter)
- TODO croiseur presenter SPI generated Javadoc
- [Java's `ServiceLoader` documentation](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/ServiceLoader.html)
  which defines the plugin declaration format used by `croiseur`.
- [Project Jigsaw homepage](https://openjdk.org/projects/jigsaw/): General information on Java's
  module system.
