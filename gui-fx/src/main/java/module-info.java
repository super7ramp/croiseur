module com.gitlab.super7ramp.crosswords.gui.fx {

    requires javafx.controls;
    requires javafx.fxml;

    exports com.gitlab.super7ramp.crosswords.gui.fx.model;
    exports com.gitlab.super7ramp.crosswords.gui.fx.view;
    opens com.gitlab.super7ramp.crosswords.gui.fx.view to javafx.fxml;

}