/**
 * The GUI view models.
 */
module com.gitlab.super7ramp.crosswords.gui.view.model {
    // Requiring only javafx.base for javafx.beans
    requires transitive javafx.base;
    requires transitive com.gitlab.super7ramp.crosswords.common;
    exports com.gitlab.super7ramp.crosswords.gui.view.model;
}