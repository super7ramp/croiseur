package com.gitlab.super7ramp.crosswords.gui;

import com.gitlab.super7ramp.crosswords.api.CrosswordService;
import com.gitlab.super7ramp.crosswords.gui.controller.CrosswordSolverController;
import com.gitlab.super7ramp.crosswords.gui.controller.dictionary.DictionaryController;
import com.gitlab.super7ramp.crosswords.gui.controller.solver.SolverController;
import com.gitlab.super7ramp.crosswords.gui.presenter.GuiPresenter;
import com.gitlab.super7ramp.crosswords.gui.viewmodel.SolverViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * The crossword GUI application.
 */
public final class CrosswordGuiApplication extends Application {

    /**
     * Constructor.
     */
    public CrosswordGuiApplication() {
        // Nothing to do
    }

    @Override
    public void start(final Stage stage) throws IOException {
        // Load usecases
        final CrosswordService crosswordService = CrosswordService.create();

        // Load controllers/presenters/view model
        final DictionaryController dictionaryController =
                new DictionaryController(crosswordService.dictionaryService());
        final SolverViewModel solverViewModel = new SolverViewModel();
        final SolverController solverController = new SolverController(solverViewModel,
                crosswordService.solverService());
        GuiPresenter.inject(solverViewModel);

        // Load the views
        CrosswordSolverController.inject(solverController, dictionaryController, solverViewModel);
        final URL fxmlLocation = Objects.requireNonNull(CrosswordGuiApplication.class.getResource(
                "view/CrosswordSolver.fxml"));
        final Parent solver = FXMLLoader.load(fxmlLocation);
        final Scene scene = new Scene(solver);
        stage.setTitle("Crossword Solver");
        stage.setScene(scene);
        stage.setMinWidth(300);
        stage.setMinHeight(300);
        stage.show();
    }
}