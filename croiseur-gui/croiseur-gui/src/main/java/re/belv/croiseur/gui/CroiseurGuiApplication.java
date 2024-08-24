/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import re.belv.croiseur.api.CrosswordService;
import re.belv.croiseur.gui.concurrent.MoreExecutors;
import re.belv.croiseur.gui.presenter.GuiPresenter;
import re.belv.croiseur.gui.view.model.ApplicationViewModel;
import re.belv.croiseur.gui.view.model.ErrorsViewModel;
import re.belv.croiseur.spi.presenter.Presenter;

/** The Croiseur GUI application. */
public final class CroiseurGuiApplication extends Application {

    /** The stage's title. */
    private static final String STAGE_TITLE = "Croiseur";

    /** The stage's min width. */
    private static final int MIN_WIDTH = 510;

    /** The stage's min height. */
    private static final int MIN_HEIGHT = 400;

    /** The application icon name. */
    private static final String ICON_NAME = "application-icon.png";

    /** The application stylesheet. */
    private static final String STYLESHEET = "theme-light.css";

    /** The number of background threads. At least 2 so that dictionaries can be browsed while solver is running. */
    private static final int NUMBER_OF_BACKGROUND_THREADS = 2;

    /** Resources to be closed upon application shutdown. */
    private final Collection<AutoCloseable> resources;

    /** Constructor. */
    public CroiseurGuiApplication() {
        resources = new ArrayList<>();
    }

    @Override
    public void start(final Stage stage) throws IOException {
        final SceneSwitcher sceneSwitcher = new SceneSwitcher(stage);
        final Executor executor = createExecutor();
        loadComponents(stage, sceneSwitcher, executor);
        configureStage(stage);
        configureStyleSheet();
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
     * @param stage the stage to configure
     */
    private static void configureStage(final Stage stage) {
        // Stage's scene is managed by SceneSwitcher and already set at this point
        stage.setTitle(STAGE_TITLE);
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);
        final Image icon = loadIcon();
        stage.getIcons().add(icon);
        // Make sure to close the application even if some views haven't been shown
        stage.setOnCloseRequest(event -> Platform.exit());
    }

    /**
     * Loads the application icon.
     *
     * @return the application icon
     * @throws NullPointerException if icon is not found
     */
    private static Image loadIcon() {
        final InputStream iconLocation = CroiseurGuiApplication.class.getResourceAsStream(ICON_NAME);
        Objects.requireNonNull(iconLocation, "Application icon not found");
        return new Image(iconLocation);
    }

    /**
     * Loads application stylesheet and sets it as user agent stylesheet.
     *
     * @throws NullPointerException if stylesheet is not found
     */
    private static void configureStyleSheet() {
        final URL themeUrl = CroiseurGuiApplication.class.getResource(STYLESHEET);
        Objects.requireNonNull(themeUrl, "Application stylesheet not found");
        Application.setUserAgentStylesheet(themeUrl.toString());
    }

    /**
     * Loads the application components.
     *
     * @param stage the stage
     * @param sceneSwitcher the scene switcher
     * @param executor the background task executor
     * @throws IOException if loading from FXML files fails
     */
    private static void loadComponents(final Stage stage, final SceneSwitcher sceneSwitcher, final Executor executor)
            throws IOException {

        // Dependencies for construction: view model <- presenter <- use-cases <- controllers/views
        final ApplicationViewModel applicationViewModel = new ApplicationViewModel();
        loadErrorPopup(applicationViewModel.errorsViewModel(), stage);

        final Presenter presenter = new GuiPresenter(applicationViewModel);
        final CrosswordService crosswordService = CrosswordServiceLoader.load(presenter);
        loadWelcomeScreen(applicationViewModel, crosswordService, sceneSwitcher, executor);
        loadCrosswordEditor(applicationViewModel, crosswordService, sceneSwitcher, executor);
    }

    /**
     * Loads the special error popup control logic. Popup can happen on all scenes of the given stage.
     *
     * @param errorsViewModel the errors view model
     * @param stage the application stage
     */
    private static void loadErrorPopup(final ErrorsViewModel errorsViewModel, final Stage stage) {
        errorsViewModel.currentErrorProperty().addListener((observable, oldError, newError) -> {
            if (newError != null) {
                final Alert errorAlert = new Alert(Alert.AlertType.ERROR, newError);
                errorAlert.initOwner(stage.getScene().getWindow());
                errorAlert.showAndWait();
                errorsViewModel.acknowledgeError();
            }
        });
    }

    /**
     * Loads the welcome screen.
     *
     * @param applicationViewModel the application view-models
     * @param crosswordService the croiseur core library
     * @param sceneSwitcher the scene switcher
     * @param executor the background task executor
     * @throws IOException if loading from FXML file fails
     */
    private static void loadWelcomeScreen(
            final ApplicationViewModel applicationViewModel,
            final CrosswordService crosswordService,
            final SceneSwitcher sceneSwitcher,
            final Executor executor)
            throws IOException {
        final var welcomeScreenController = new WelcomeScreenController(
                applicationViewModel.puzzleSelectionViewModel(),
                applicationViewModel.puzzleEditionViewModel(),
                applicationViewModel.puzzleCodecsViewModel(),
                crosswordService.puzzleService(),
                sceneSwitcher,
                executor);
        final Parent parent = ViewLoader.load(welcomeScreenController);
        sceneSwitcher.registerScene(SceneSwitcher.SceneId.WELCOME_SCREEN, new Scene(parent));
    }

    /**
     * Loads the crossword editor.
     *
     * @param applicationViewModel the application view model
     * @param crosswordService the croiseur core library
     * @param sceneSwitcher the scene switcher
     * @param executor the executor
     */
    private static void loadCrosswordEditor(
            final ApplicationViewModel applicationViewModel,
            final CrosswordService crosswordService,
            final SceneSwitcher sceneSwitcher,
            final Executor executor)
            throws IOException {
        final var editorController =
                new CrosswordEditorController(crosswordService, applicationViewModel, sceneSwitcher, executor);
        final Parent parent = ViewLoader.load(editorController);
        sceneSwitcher.registerScene(SceneSwitcher.SceneId.CROSSWORD_EDITOR, new Scene(parent));
    }

    /**
     * Creates an executor to run tasks on the background. The created executor will be added to this application
     * closeable {@link #resources}.
     *
     * @return the executor
     */
    private Executor createExecutor() {
        final ExecutorService executor = MoreExecutors.newQuickClosureFixedThreadPool(NUMBER_OF_BACKGROUND_THREADS);
        resources.add(executor);
        return executor;
    }
}
