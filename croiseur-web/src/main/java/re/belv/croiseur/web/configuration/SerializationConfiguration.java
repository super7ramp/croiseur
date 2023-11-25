/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.web.configuration;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import re.belv.croiseur.common.puzzle.GridPosition;
import re.belv.croiseur.web.model.puzzle.GridPositionDeserializer;

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
