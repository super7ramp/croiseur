module com.gitlab.super7ramp.crosswords.gui.control {
    requires javafx.controls;
    requires javafx.fxml;
    exports com.gitlab.super7ramp.crosswords.gui.control;
    exports com.gitlab.super7ramp.crosswords.gui.control.model;
    opens com.gitlab.super7ramp.crosswords.gui.control to javafx.fxml;
}