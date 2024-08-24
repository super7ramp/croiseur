/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.cli;

import java.util.function.Function;
import picocli.CommandLine.ITypeConverter;
import picocli.CommandLine.TypeConversionException;

/**
 * A {@link ITypeConverter} generic implementation.
 *
 * <p>Allows converting raw strings into a bit less impractical types.
 *
 * @param <T> the type of the object that is the result of the conversion
 */
final class TypeConverter<T> implements ITypeConverter<T> {

    /** The actual parser. */
    private final Function<String, T> parser;

    /**
     * Private constructor, build with factory method only.
     *
     * @param aParser actual parser
     */
    private TypeConverter(final Function<String, T> aParser) {
        parser = aParser;
    }

    /**
     * Wrap parsing function in a {@link ITypeConverter}. Basically {@link TypeConverter} is just a decorator which
     * catches all exceptions and turn them into {@link TypeConversionException}.
     *
     * @param parser the parser function
     * @param <T> the type of the object that is the result of the conversion
     * @return the {@link TypeConverter} wrapping the given parser function
     */
    static <T> TypeConverter<T> wrap(final Function<String, T> parser) {
        return new TypeConverter<>(parser);
    }

    @Override
    public T convert(final String value) {
        try {
            return parser.apply(value);
        } catch (final Exception e) {
            throw new TypeConversionException(e.getMessage());
        }
    }
}
