import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.crosswords.spi.solver.CrosswordSolver;

module com.gitlab.super7ramp.crosswords.gui {

    // Base modules
    requires java.logging;

    // JavaFX stuff
    requires javafx.controls;
    requires javafx.fxml;
    requires com.gitlab.super7ramp.crosswords.gui.controls;

    exports com.gitlab.super7ramp.crosswords.gui to javafx.graphics;
    opens com.gitlab.super7ramp.crosswords.gui to javafx.fxml;
    opens com.gitlab.super7ramp.crosswords.gui.controller to javafx.fxml;

    // Actual dependency to core library
    requires com.gitlab.super7ramp.crosswords;

    // GUI loads dictionary providers and solver itself
    uses DictionaryProvider;
    uses CrosswordSolver;

}