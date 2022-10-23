package com.gitlab.super7ramp.crosswords.spi.presenter;

/**
 * Required presentation services.
 */
// TODO create specific types so that Presenter SPI does not depend on Dictionary and Solver SPIs?
public interface Presenter extends DictionaryPresenter, SolverPresenter {
    // Marker interface
}
