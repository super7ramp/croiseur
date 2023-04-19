<!--
SPDX-FileCopyrightText: 2023 Antoine Belvire
SPDX-License-Identifier: GPL-3.0-or-later
-->

## Environment

* Operating system: <!-- e.g. openSUSE Tumbleweed (20230130) -->
* croiseur version: <!-- e.g. master (bb1052d) -->
* Java version: <!-- e.g. GraalVM 22.0.0.2 Java 17 CE -->

## Issue

<!-- Here write a short description of the issue, e.g.:

The application crashes at startup systematically, indicating that the native library for crossword
composer solver cannot be found. Some configuration is probably missing to make JNI work.

-->

### Steps to reproduce

<!-- Here write the steps to reproduce the issue. This part is crucial. The more the bug is reproducible, the faster it can be analyzed and fixed. Here's an example:

1. Compile a native image of croiseur-cli: `gradle croiseur-cli:nativeCompile`
2. Run the image with no argument: `./build/native/nativeCompile/croiseur-cli`

-->

### Expected result

<!-- Example:

Application prints a usage message then exits.

-->

### Actual result

<!-- Example:

The application crashes with the following stack trace:

```
(the stacktrace)
```
-->

### Workaround

<!-- Optional; May be useful for other users in order not to be blocked. Example:

Comment the line `runtimeOnly project(':croiseur-solver:croiseur-solver-paulgb-plugin')` in `build.gradle.kts` so that plugin is not included in build.

-->