import com.gitlab.super7ramp.crosswords.gui.presenter.GuiPresenter;
import com.gitlab.super7ramp.crosswords.spi.presenter.Presenter;

module com.gitlab.super7ramp.crosswords.gui {

    // Base modules
    requires java.logging;

    // JavaFX stuff
    requires javafx.controls;
    requires javafx.fxml;
    requires com.gitlab.super7ramp.crosswords.gui.fx;

    exports com.gitlab.super7ramp.crosswords.gui;
    opens com.gitlab.super7ramp.crosswords.gui to javafx.fxml;
    exports com.gitlab.super7ramp.crosswords.gui.binder;
    opens com.gitlab.super7ramp.crosswords.gui.binder to javafx.fxml;

    // Actual dependency to core library
    requires com.gitlab.super7ramp.crosswords;

    // GUI provides core library with a presenter
    provides Presenter with GuiPresenter;

}