/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package com.gitlab.super7ramp.croiseur.gui;

import com.gitlab.super7ramp.croiseur.api.CrosswordService;
import com.gitlab.super7ramp.croiseur.gui.concurrent.AutoCloseableExecutorService;
import com.gitlab.super7ramp.croiseur.gui.concurrent.AutoCloseableExecutors;
import com.gitlab.super7ramp.croiseur.gui.presenter.GuiPresenter;
import com.gitlab.super7ramp.croiseur.gui.view.model.CrosswordSolverViewModel;
import com.gitlab.super7ramp.croiseur.spi.presenter.Presenter;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * The Croiseur GUI application.
 */
public final class CroiseurGuiApplication extends Application {

    /** The stage's title. */
    public static final String STAGE_TITLE = "Croiseur";

    /** The stage's min width. */
    private static final int MIN_WIDTH = 510;

    /** The stage's min height. */
    private static final int MIN_HEIGHT = 400;

    /** The application icon name. */
    private static final String ICON_NAME = "application-icon.png";

    /** The application stylesheet. */
    private static final String STYLESHEET = "theme-light.css";

    /**
     * The number of background threads. At least 2 so that dictionaries can be browsed while solver
     * is running.
     */
    private static final int NUMBER_OF_BACKGROUND_THREADS = 2;

    /** Resources to be closed upon application shutdown. */
    private final Collection<AutoCloseable> resources;

    /**
     * Constructor.
     */
    public CroiseurGuiApplication() {
        resources = new ArrayList<>();
    }

    @Override
    public void start(final Stage stage) throws IOException {
        final CroiseurRootController controller = loadController();
        final Parent view = CroiseurRootViewLoader.load(controller);
        configureStage(stage, view);
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
     * @param stage               the stage to configure
     * @param crosswordSolverView the view to set as scene
     */
    private static void configureStage(final Stage stage, final Parent crosswordSolverView) {
        final Scene scene = new Scene(crosswordSolverView);
        stage.setScene(scene);
        stage.setTitle(STAGE_TITLE);
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);
        final Image icon = loadIcon();
        stage.getIcons().add(icon);
    }

    /**
     * Loads the application icon.
     *
     * @return the application icon
     * @throws NullPointerException if icon is not found
     */
    private static Image loadIcon() {
        final InputStream iconLocation =
                CroiseurGuiApplication.class.getResourceAsStream(ICON_NAME);
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
     * Loads the controller.
     *
     * @return the loaded controller
     */
    private CroiseurRootController loadController() {
        // Dependencies for construction: view model <- presenter <- use-cases <- controller
        final CrosswordSolverViewModel crosswordSolverViewModel = new CrosswordSolverViewModel();
        final Presenter presenter = new GuiPresenter(crosswordSolverViewModel);
        final CrosswordService crosswordService = CrosswordServiceLoader.load(presenter);

        // Additional dependency: Background executor
        final AutoCloseableExecutorService executor =
                AutoCloseableExecutors.newFixedThreadPool(NUMBER_OF_BACKGROUND_THREADS);
        resources.add(executor);

        return new CroiseurRootController(crosswordService, crosswordSolverViewModel, executor);
    }

}