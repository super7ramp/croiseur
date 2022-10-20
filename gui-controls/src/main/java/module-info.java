module com.gitlab.super7ramp.crosswords.gui.controls {
    requires javafx.controls;
    requires javafx.fxml;
    exports com.gitlab.super7ramp.crosswords.gui.controls;
    exports com.gitlab.super7ramp.crosswords.gui.controls.model;
    opens com.gitlab.super7ramp.crosswords.gui.controls to javafx.fxml;
}