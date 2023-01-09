package com.gitlab.super7ramp.crosswords.gui;

import com.gitlab.super7ramp.crosswords.api.CrosswordService;
import com.gitlab.super7ramp.crosswords.gui.presenter.GuiPresenter;
import com.gitlab.super7ramp.crosswords.gui.view.model.CrosswordSolverViewModel;
import com.gitlab.super7ramp.crosswords.spi.presenter.Presenter;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The crossword GUI application.
 */
public final class CrosswordGuiApplication extends Application {

    /** The stage's title. */
    public static final String STAGE_TITLE = "Crossword Solver";

    /** The stage's min width. */
    private static final int MIN_WIDTH = 510;

    /** The stage's min height. */
    private static final int MIN_HEIGHT = 400;

    /** The application icon name. */
    private static final String ICON_NAME = "application-icon.png";

    /** Resources to be closed upon application shutdown. */
    private final Collection<AutoCloseable> resources;

    /**
     * Constructor.
     */
    public CrosswordGuiApplication() {
        resources = new ArrayList<>();
    }

    @Override
    public void start(final Stage stage) throws IOException {
        final CrosswordSolverRootController controller = loadController();
        final Parent view = CrosswordSolverViewLoader.load(controller);
        configureStage(stage, view);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        for (final AutoCloseable resource : resources) {
            resource.close();
        }
    }

    /**
     * Configures the stage.
     *
     * @param stage               the stage to configure
     * @param crosswordSolverView the view to set as scene
     */
    private static void configureStage(final Stage stage, final Parent crosswordSolverView) {
        final Scene scene = new Scene(crosswordSolverView);
        stage.setScene(scene);
        stage.setTitle(STAGE_TITLE);
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);
        final InputStream iconLocation =
                CrosswordGuiApplication.class.getResourceAsStream(ICON_NAME);
        if (iconLocation != null) {
            stage.getIcons().add(new Image(iconLocation));
        }
    }

    /**
     * Loads the controller.
     *
     * @return the loaded controller
     */
    private CrosswordSolverRootController loadController() {
        // Dependencies for construction: view model <- presenter <- use-cases <- controller
        final CrosswordSolverViewModel crosswordSolverViewModel = new CrosswordSolverViewModel();
        final Presenter presenter = new GuiPresenter(crosswordSolverViewModel);
        final CrosswordService crosswordService = CrosswordServiceLoader.load(presenter);

        // Additional dependency: Background executor
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        resources.add(executor::shutdown);

        return new CrosswordSolverRootController(crosswordService, crosswordSolverViewModel,
                executor);
    }
}