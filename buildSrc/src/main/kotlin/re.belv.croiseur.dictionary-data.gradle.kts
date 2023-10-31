/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * Conventions for dictionary data.
 */

plugins {
    id("re.belv.croiseur.base")
}

/**
 * A configuration for dictionary artifacts
 * <p>
 * Producer (on which this plugin is applied) shall add dictionaries like this:
 *
 * <pre>{@code
 * artifacts {
 *     add("dictionary", layout.projectDirectory.file("my-dictionary.txt"))
 * }
 *}</pre>
 *
 * Consumer can refer to it:
 *
 * <pre>{@code
 * dependencies {
 *     runtimeOnly(project(":example-producer", "dictionary")
 * }
 *}</pre>
 *
 * Or more simply, since default configuration is configured to extend dictionary configuration:
 *
 * <pre>{@code
 * dependencies {
 *     runtimeOnly(project(":example-producer"))
 * }
 *}</pre>
 */
configurations.register("dictionary") {
    isCanBeConsumed = true
    isCanBeResolved = false
}

configurations.named("default") {
    extendsFrom(configurations.getByName("dictionary"))
}
