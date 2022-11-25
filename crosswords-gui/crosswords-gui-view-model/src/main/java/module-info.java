/**
 * The GUI view models.
 */
module com.gitlab.super7ramp.crosswords.gui.view.model {
    requires transitive com.gitlab.super7ramp.crosswords.common;
    exports com.gitlab.super7ramp.crosswords.gui.view.model;

    requires transitive javafx.base; // Requiring only javafx.base for javafx.beans
}