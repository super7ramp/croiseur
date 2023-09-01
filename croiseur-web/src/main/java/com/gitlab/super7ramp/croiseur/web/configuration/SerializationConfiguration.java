/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.web.configuration;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.gitlab.super7ramp.croiseur.common.puzzle.GridPosition;
import com.gitlab.super7ramp.croiseur.web.io.GridPositionDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Declares bean relative to serialization/deserialization.
 */
@Configuration
public class SerializationConfiguration {

    /**
     * Constructs an instance.
     */
    public SerializationConfiguration() {
        // Nothing to do.
    }

    /**
     * A deserializer for {@link GridPosition} when used as a key of a Map.
     *
     * @return the {@link GridPosition} key deserializer
     */
    @Bean
    public com.fasterxml.jackson.databind.Module gridPositionKeyDeserializer() {
        final SimpleModule module = new SimpleModule();
        module.addKeyDeserializer(GridPosition.class, new GridPositionDeserializer());
        return module;
    }
}
