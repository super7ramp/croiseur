<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## Gradle Convention Plugins

This folder gathers custom Gradle convention plugins used by the project.

- [`base`][]: Defines the components group and the repositories. Extended by all other
  convention plugins.
- [`dictionary-data`][]: Defines an outgoing configuration so that dictionaries can be consumed
  elsewhere.
- [`java`][]: General conventions for Java components: Encoding, test and coverage.
- [`java-acceptance-tests`][]: Adds a dependency on Cucumber for BDD-style tests. Extends the `java`
  conventions.
- [`java-aggregate-coverage-tests`][]: Conventions for modules producing or aggregating test
  coverage on other modules.
- [`java-application`][]: Conventions for Java applications. Extends the `java` convention plugin.
- [`java-cli`][]: Conventions for Java CLI applications. Adds a dependency on the picocli framework
  ond a pre-configuration for native images. Extends the `java-application` conventions.
- [`java-library`][]: General conventions for Java libraries: Adds a publishing configuration.
  Extends the `java` conventions.
- [`jni-rust`][]: Defines an incoming configuration for defining dependencies on native
  libraries using the Java Native Interface (JNI). The native libraries are imported as
  resources of the project; They are bundled in the output jar. Extends the `java-library`
  conventions.

<!-- Links -->

[`base`]: src/main/kotlin/re.belv.croiseur.base.gradle.kts

[`dictionary-data`]: src/main/kotlin/re.belv.croiseur.dictionary-data.gradle.kts

[`java-acceptance-tests`]: src/main/kotlin/re.belv.croiseur.java-acceptance-tests.gradle.kts

[`java-aggregate-coverage-tests`]: src/main/kotlin/re.belv.croiseur.java-aggregate-coverage.gradle.kts

[`java-application`]: src/main/kotlin/re.belv.croiseur.java-application.gradle.kts

[`java-cli`]: src/main/kotlin/re.belv.croiseur.java-cli.gradle.kts

[`java`]: src/main/kotlin/re.belv.croiseur.java.gradle.kts

[`java-library`]: src/main/kotlin/re.belv.croiseur.java-library.gradle.kts

[`jni-rust`]: src/main/kotlin/re.belv.croiseur.jni-rust.gradle.kts
