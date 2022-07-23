/**
 * Publisher SPI definition.
 */
module com.gitlab.super7ramp.crosswords.spi.publisher {
    requires transitive com.gitlab.super7ramp.crosswords.spi.solver;
    requires transitive com.gitlab.super7ramp.crosswords.spi.dictionary;
    exports com.gitlab.super7ramp.crosswords.spi.publisher;
}