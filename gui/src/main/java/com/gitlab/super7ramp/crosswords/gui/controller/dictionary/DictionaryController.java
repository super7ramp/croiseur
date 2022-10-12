package com.gitlab.super7ramp.crosswords.gui.controller.dictionary;

import com.gitlab.super7ramp.crosswords.api.dictionary.DictionaryUsecase;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.logging.Logger;

/**
 * Controls the dictionary.
 */
public final class DictionaryController {

    /** Logger. */
    private static final Logger LOGGER = Logger.getLogger(DictionaryController.class.getName());

    /** The worker executing the dictionary tasks. */
    private final Service<Void> dictionaryService;

    /**
     * Constructs an instance.
     *
     * @param usecase the "dictionary" usecase
     */
    public DictionaryController(final DictionaryUsecase usecase) {
        dictionaryService = new Service<>() {
            @Override
            protected Task<Void> createTask() {
                return new ListDictionariesTask(usecase);
            }
        };
        dictionaryService.setOnSucceeded(e -> LOGGER.info("Dictionary retrieval succeeded"));
        dictionaryService.setOnRunning(e -> LOGGER.info("Dictionary retrieval in progress"));
        dictionaryService.setOnFailed(e -> LOGGER.info("Dictionary retrieval failed"));
    }

    /**
     * Starts a new dictionary task.
     * <p>
     * Any pending task will be stopped.
     */
    public void start() {
        stop();
        dictionaryService.start();
    }

    /**
     * Stops the running dictionary task, if any.
     */
    private void stop() {
        dictionaryService.cancel();
        dictionaryService.reset();
    }

}
