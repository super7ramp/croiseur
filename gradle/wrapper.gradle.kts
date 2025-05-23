/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/*
 * Use: "gradle wrapper --build-file=gradle/wrapper.gradle.kts" to generate a gradle wrapper whose
 * version is verified to be compatible with the gradle features used by this project.
 */
tasks.wrapper {
    gradleVersion = "8.5"
}
