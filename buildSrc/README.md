<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## Gradle Plugins

This folder gather custom Gradle plugins used across all the project. Most of them are
simple [convention plugins](https://docs.gradle.org/current/samples/sample_convention_plugins.html).

### Convention Plugins

- [`base`][]: Defines the components group and the repositories. Extended by all other
  convention plugins.
- [`dictionary-data`][]: Defines an outgoing configuration so that dictionaries can be consumed
  elsewhere.
- [`java-acceptance-tests`][]: Adds a dependency on Cucumber for BDD-style tests. Extends the `java`
  conventions.
- [`java-aggregate-converage-tests`][]: Conventions for modules producing or aggregating test
  coverage on other modules.
- [`java-application`][]: Conventions for Java applications. Extends the `java` convention plugin.
- [`java-cli`][]: Conventions for Java CLI applications. Adds a dependency on the picocli framework
  ond a pre-configuration for native images. Extends the `java-application` conventions.
- [`java`][]: General conventions for Java components: Encoding, test and coverage.
- [`java-library`][]: General conventions for Java libraries: Adds a publishing configuration.
  Extends the `java` conventions.
- [`jni-rust`][]: Defines an incoming configuration for defining dependencies on native libraries
  implementing the Java Native Interface (JNI). The native libraries are imported as resources of
  the project; They are bundled in the output jar. Extends the `java-library` conventions.

### Other Plugins

- [`gradle-cargo-wrapper`][]: A Gradle wrapper to the Cargo build tool (used to compile components
  written in Rust). This
  is [Arc'blroth's plugin](https://github.com/Arc-blroth/gradle-cargo-wrapper)
  with minor modifications to support cross-compilation. Changes are meant to be merged to upstream,
  eventually.

<!-- Links -->

[`base`]: src/main/kotlin/com.gitlab.super7ramp.croiseur.base-conventions.gradle.kts

[`dictionary-data`]: src/main/kotlin/com.gitlab.super7ramp.croiseur.dictionary-data-conventions.gradle.kts

[`java-acceptance-tests`]: src/main/kotlin/com.gitlab.super7ramp.croiseur.java-acceptance-tests-conventions.gradle.kts

[`java-aggregate-converage-tests`]: src/main/kotlin/com.gitlab.super7ramp.croiseur.java-aggregate-coverage-conventions.gradle.kts

[`java-application`]: src/main/kotlin/com.gitlab.super7ramp.croiseur.java-application-conventions.gradle.kts

[`java-cli`]: src/main/kotlin/com.gitlab.super7ramp.croiseur.java-cli-conventions.gradle.kts

[`java`]: src/main/kotlin/com.gitlab.super7ramp.croiseur.java-conventions.gradle.kts

[`java-library`]: src/main/kotlin/com.gitlab.super7ramp.croiseur.java-library-conventions.gradle.kts

[`jni-rust`]: src/main/kotlin/com.gitlab.super7ramp.croiseur.jni-rust-conventions.gradle.kts

[`gradle-cargo-wrapper`]: src/main/java/ai/arcblroth/cargo/CargoWrapperPlugin.java