/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

/**
 * Conventions for modules producing or aggregating test coverage on other modules.
 */

plugins {
    id("com.gitlab.super7ramp.croiseur.java-conventions")
    id("jacoco-report-aggregation")
}

configurations.named("jacocoAggregation") {
    // Extend test configurations: Test modules may use test configurations instead of main
    // configuration to reference tested projects.
    extendsFrom(configurations.getByName("testImplementation"))
    extendsFrom(configurations.getByName("testRuntimeOnly"))
}
