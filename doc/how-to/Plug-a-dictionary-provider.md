<!--
SPDX-FileCopyrightText: 2025 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## How To: Plug a Dictionary Provider

### Audience

This how-to is aimed at developers desiring to plug their own dictionary provider to `croiseur`. It
requires basic knowledge of the Java programming language.

### In a nutshell

1. Create a dictionary provider plugin project
2. Implement the dictionary provider plugin
3. Declare the dictionary provider plugin
4. Install the dictionary provider plugin

### In details

#### 1. Create a dictionary provider plugin project

You can develop a dictionary provider plugin either:

1. Outside `croiseur` source tree, or
2. Inside `croiseur` source tree

The first way is simpler to set up but requires a distribution of `croiseur` and manual
manipulations to test the integration of the dictionary provider plugin inside `croiseur`.

The second way is heavier to set up but allows to more comfortably test and debug the dictionary
provider plugin when run by `croiseur`.

##### 1.1. First alternative: Create the project outside `croiseur` source tree

`croiseur-dictionary-spi` jar is published
in [a Maven repository](https://gitlab.com/super7ramp/croiseur/-/packages). It can be retrieved with
a build tool such as Maven or Gradle to develop a new dictionary provider plugin without having to
download and rebuild all `croiseur` sources.

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
    api("re.belv:croiseur-spi-dictionary:0.12")
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

###### 1.2.2. Create a new dictionary provider plugin subproject inside `croiseur-dictionary`

Create a subproject folder under `croiseur-dictionary`:

```sh
cd croiseur/croiseur-dictionary
mkdir croiseur-dictionary-<new_dictionary_provider_name>-plugin
```

Add a `build.gradle.kts` (since `croiseur` uses Gradle as build system) in
`croiseur-dictionary-<new_dictionary_provider_name>-plugin`:

```gradle
plugins {
    id("re.belv.croiseur.java-library")
}

dependencies {
    api(project(":croiseur-spi:croiseur-spi-dictionary"))
    // You may add additional implementation dependencies here
}
```

Finally, declare the subproject by adding the following line in root's `settings.gradle.kts`:

```gradle
include("croiseur-dictionary:croiseur-dictionary-<new_dictionary_provider_name>-plugin")
```

At this point, check that everything is OK by running `gradle build`.

#### 2. Implement the dictionary provider plugin

Create a class inside the new plugin project implementing the `DictionaryProvider` interface defined
in `croiseur-spi-dictionary`.

A commented example is available in `croiseur-dictionary-example-plugin`:
[`ExampleDictionaryProvider`](../../croiseur-dictionary/croiseur-dictionary-example-plugin/src/main/java/re/belv/croiseur/dictionary/example/plugin/ExampleDictionaryProvider.java).

#### 3. Declare the dictionary provider plugin

In order for `croiseur` to find the plugin at run-time, the implementation needs to advertise
itself as a dictionary provider plugin.

There are two ways to do that:

- Use the modern module `provides` directive, or
- Use the traditional `META-INF/services` folder.

The first way should be preferred since the `croiseur` project is fully modularised.

For compatibility with custom non-modular deployments (like in `croiseur-tests`), it is advised
to declare the dictionary provider plugin using the second method in addition to the first method.

##### 3.1. Using the module `provides` directive

Create a `module-info.java` file in `src/main/java`. Here is a template of `module-info`:

```
import re.belv.croiseur.dictionary.<new_dictionary_provider_name>.plugin.<NewDictionaryProviderName>DictionaryProvider;
import re.belv.croiseur.spi.dictionary.DictionaryProvider;

module re.belv.croiseur.dictionary.<new_dictionary_provider_name>.plugin {
    requires re.belv.croiseur.spi.dictionary;
    provides DictionaryProvider with <NewDictionaryProviderName>DictionaryProvider;
}
```

##### 3.2. Using the `META-INF/services` folder

Create the file `re.belv.croiseur.dictionary.spi.DictionaryProvider`
in `src/main/resources/META-INF/services` and add the qualified name of your implementation as
content, e.g.:

```
re.belv.croiseur.dictionary.<new_dictionary_provider_name>.plugin.<NewDictionaryProviderName>DictionaryProvider
```

#### 4. Install the dictionary provider plugin

In order for `croiseur` to find the dictionary provider plugin, this one needs to be present
in `croiseur`'s module path.

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
└── lib                                     # Put the dictionary provider plugin jar in this folder
    ├── croiseur-cli.jar
    ├── (...)
```

##### 4.2. Adjust the `MODULE_PATH` variable in the launcher scripts in the distribution's `bin` folder

Append the path to the dictionary provider plugin jar to the `MODULE_PATH` variable
in `bin/croiseur-cli` (for Unix) and/or `bin/croiseur-cli.bat` (for Windows) so that it includes the
new jar:

In `bin/croiseur-cli`:

```sh
MODULE_PATH=$APP_HOME/lib/croiseur-cli.jar:(...):$APP_HOME/lib/<new_dictionary_provider_plugin>.jar
```

In `bin/croiseur-cli.bat`:

```bat
set MODULE_PATH=%APP_HOME%\lib\croiseur-cli.jar;(...);%APP_HOME%\lib\<new_dictionary_provider_plugin>.jar
```

At this point, it should be possible to interact with the new dictionary provider. Check that it is
detected using the following command:

```sh
croiseur-cli dictionary list-providers
```

The dictionary provider should appear in the dictionary provider list.

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

This is a single line to add in the `dependencies` block of the applications' `build.gradle.kts`:

```gradle
runtimeOnly(project(":croiseur-dictionary:croiseur-dictionary-<your_dictionary_provider_name>-plugin"))
```

### See also

- [`croiseur-dictionary-example-plugin`](../../croiseur-dictionary/croiseur-dictionary-example-plugin):
  A minimal example plugin.
- Real plugin implementations:
    - [`croiseur-dictionary-hunspell-plugin`](../../croiseur-dictionary/croiseur-dictionary-hunspell-plugin)
    - [`croiseur-dictionary-txt-plugin`](../../croiseur-dictionary/croiseur-dictionary-txt-plugin)
    - [`croiseur-dictionary-xml-plugin`](../../croiseur-dictionary/croiseur-dictionary-xml-plugin)
- [Dictionary SPI Javadoc](https://super7ramp.gitlab.io/croiseur/re.belv.croiseur.spi.dictionary/re/belv/croiseur/spi/dictionary/package-summary.html)
- [Java's `ServiceLoader` documentation](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/ServiceLoader.html)
  which defines the plugin declaration format used by `croiseur`.
- [Project Jigsaw homepage](https://openjdk.org/projects/jigsaw/): General information on Java's
  module system.
