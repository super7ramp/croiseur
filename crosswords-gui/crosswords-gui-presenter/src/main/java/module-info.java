/**
 * Implementation of the Presenter SPI.
 */
module com.gitlab.super7ramp.crosswords.gui.presenter {
    requires transitive com.gitlab.super7ramp.crosswords.common;
    requires transitive com.gitlab.super7ramp.crosswords.spi.presenter;
    requires transitive com.gitlab.super7ramp.crosswords.gui.view.model;
    exports com.gitlab.super7ramp.crosswords.gui.presenter;
    // The following 'provides' should exist but GuiPresenter has no default constructor: The
    // view model it operates on is passed in constructor (for now).
    // provides Presenter with GuiPresenter;

    requires java.logging;
    requires javafx.graphics; // for Platform.runLater()
}