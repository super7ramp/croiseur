/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Conventions for Java libraries.
 */

plugins {
    id("com.gitlab.super7ramp.croiseur.java-conventions")
    id("java-library")
    id("maven-publish")
}

java {
    withSourcesJar()
}

publishing {
    publications {
        register<MavenPublication>(camelCasedProjectName()) {
            from(components.getByName("java"))
        }
    }
    repositories {
        maven {
            name = "GitlabMaven"
            url = uri("https://gitlab.com/api/v4/projects/43029946/packages/maven")
            credentials(HttpHeaderCredentials::class) {
                name = (findProperty("croiseur.maven.token.name") as String?) ?: "<none_found>"
                value = (findProperty("croiseur.maven.token.value") as String?) ?: "<none_found>"
            }
            authentication {
                create("header", HttpHeaderAuthentication::class)
            }
        }
    }
}

fun camelCasedProjectName(): String {
    val pattern: Pattern = Pattern.compile("-([a-z])")
    val dashLetterMatcher: Matcher = pattern.matcher(project.name)
    return dashLetterMatcher.replaceAll { it.group(1).uppercase() }
}