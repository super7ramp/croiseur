/*
 * SPDX-FileCopyrightText: 2021 Arc'blroth
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: Apache-2.0
 */

package com.gitlab.super7ramp.croiseur.cargo;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.dsl.ArtifactHandler;

/**
 * A plugin that wraps Rust's Cargo build system, for embedding Rust libraries in Java projects.
 */
@SuppressWarnings("unused")
public final class CargoWrapperPlugin implements Plugin<Project> {
    @Override
    public void apply(final Project project) {
        project.getConfigurations().create("default");
        final CargoExtension extension =
                project.getExtensions().create("cargo", CargoExtension.class);
        project.getTasks().register("build", CargoTask.class);

        project.afterEvaluate(evaluatedProject ->
            project.getTasks().withType(CargoTask.class).forEach(task -> {
                task.configure(extension);
                final ArtifactHandler artifacts = project.getArtifacts();
                task.getOutputFiles().forEach(output -> artifacts.add("default", output));
            })
        );
    }
}
