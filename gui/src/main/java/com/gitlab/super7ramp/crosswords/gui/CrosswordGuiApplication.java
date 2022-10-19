package com.gitlab.super7ramp.crosswords.gui;

import com.gitlab.super7ramp.crosswords.api.CrosswordService;
import com.gitlab.super7ramp.crosswords.gui.controller.CrosswordSolverController;
import com.gitlab.super7ramp.crosswords.gui.presenter.GuiPresenter;
import com.gitlab.super7ramp.crosswords.gui.viewmodel.CrosswordSolverViewModel;
import com.gitlab.super7ramp.crosswords.spi.dictionary.DictionaryProvider;
import com.gitlab.super7ramp.crosswords.spi.presenter.Presenter;
import com.gitlab.super7ramp.crosswords.spi.solver.CrosswordSolver;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.function.Supplier;

/**
 * The crossword GUI application.
 */
public final class CrosswordGuiApplication extends Application {

    /** The stage's title. */
    public static final String STAGE_TITLE = "Crossword Solver";

    /** The path to the fxml file. */
    public static final String FXML_LOCATION = "view/CrosswordSolver.fxml";

    /** The stage's min width. */
    private static final int MIN_WIDTH = 300;

    /** The stage's min height. */
    private static final int MIN_HEIGHT = 300;

    /**
     * Constructor.
     */
    public CrosswordGuiApplication() {
        // Nothing to do
    }

    /**
     * Creates the {@link CrosswordService}.
     *
     * @param presenter the presenter
     * @return the {@link CrosswordService}
     */
    private static CrosswordService loadUseCases(final Presenter presenter) {
        final Collection<DictionaryProvider> dictionaryProviders =
                ServiceLoader.load(DictionaryProvider.class).stream()
                             .map(Supplier::get)
                             .toList();
        final Collection<CrosswordSolver> solvers =
                ServiceLoader.load(CrosswordSolver.class)
                             .stream()
                             .map(Supplier::get)
                             .toList();
        return CrosswordService.create(dictionaryProviders, solvers, presenter);
    }

    @Override
    public void start(final Stage stage) throws IOException {
        // Dependencies for construction: view model <- presenter <- use-cases <- controller
        final CrosswordSolverViewModel crosswordSolverViewModel = new CrosswordSolverViewModel();
        final Presenter presenter = new GuiPresenter(crosswordSolverViewModel);
        final CrosswordService crosswordService = loadUseCases(presenter);
        final CrosswordSolverController crosswordSolverController =
                new CrosswordSolverController(crosswordService, crosswordSolverViewModel);

        // Load the views
        final FXMLLoader loader = new FXMLLoader();
        final URL fxmlLocation = Objects.requireNonNull(CrosswordGuiApplication.class.getResource(
                FXML_LOCATION));
        loader.setLocation(fxmlLocation);
        loader.setControllerFactory(unusedClassParam -> crosswordSolverController);
        final Parent crosswordSolverView = loader.load();

        // Set the stage
        final Scene scene = new Scene(crosswordSolverView);
        stage.setTitle(STAGE_TITLE);
        stage.setScene(scene);
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);
        stage.show();
    }
}