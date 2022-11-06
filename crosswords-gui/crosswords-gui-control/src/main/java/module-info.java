module com.gitlab.super7ramp.crosswords.gui.control {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive com.gitlab.super7ramp.crosswords.common;
    exports com.gitlab.super7ramp.crosswords.gui.control;
    exports com.gitlab.super7ramp.crosswords.gui.control.model;
    opens com.gitlab.super7ramp.crosswords.gui.control to javafx.fxml;
}