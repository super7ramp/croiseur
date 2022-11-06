module com.gitlab.super7ramp.crosswords.gui.view {
    requires javafx.controls;
    requires javafx.fxml;

    requires transitive com.gitlab.super7ramp.crosswords.common;
    requires transitive com.gitlab.super7ramp.crosswords.gui.view.model;
    exports com.gitlab.super7ramp.crosswords.gui.control;

    opens com.gitlab.super7ramp.crosswords.gui.control to javafx.fxml;
}