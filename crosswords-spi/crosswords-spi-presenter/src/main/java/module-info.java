/**
 * Presenter SPI definition.
 */
module com.gitlab.super7ramp.crosswords.spi.presenter {
    requires transitive com.gitlab.super7ramp.crosswords.spi.solver;
    requires transitive com.gitlab.super7ramp.crosswords.spi.dictionary;
    exports com.gitlab.super7ramp.crosswords.spi.presenter;
    exports com.gitlab.super7ramp.crosswords.spi.presenter.solver;
    exports com.gitlab.super7ramp.crosswords.spi.presenter.dictionary;
}