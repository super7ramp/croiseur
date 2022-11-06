import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.crosswords.spi.solver.CrosswordSolver;

module com.gitlab.super7ramp.crosswords.gui {

    // Base modules
    requires java.logging;

    // JavaFX stuff
    requires javafx.controls;
    requires javafx.fxml;

    exports com.gitlab.super7ramp.crosswords.gui to javafx.graphics;
    opens com.gitlab.super7ramp.crosswords.gui to javafx.fxml;

    requires com.gitlab.super7ramp.crosswords.gui.controller;
    requires com.gitlab.super7ramp.crosswords.gui.presenter;
    requires com.gitlab.super7ramp.crosswords.gui.view.model;
    requires com.gitlab.super7ramp.crosswords.gui.view;

    // Actual dependency to core library
    requires com.gitlab.super7ramp.crosswords;

    // GUI loads dictionary providers and solver itself
    uses DictionaryProvider;
    uses CrosswordSolver;

}