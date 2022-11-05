/**
 * Presenter SPI definition.
 */
module com.gitlab.super7ramp.crosswords.spi.presenter {
    requires transitive com.gitlab.super7ramp.crosswords.common;
    // TODO remove dependency solver SPI
    requires transitive com.gitlab.super7ramp.crosswords.spi.solver;
    exports com.gitlab.super7ramp.crosswords.spi.presenter;
    exports com.gitlab.super7ramp.crosswords.spi.presenter.solver;
    exports com.gitlab.super7ramp.crosswords.spi.presenter.dictionary;
}