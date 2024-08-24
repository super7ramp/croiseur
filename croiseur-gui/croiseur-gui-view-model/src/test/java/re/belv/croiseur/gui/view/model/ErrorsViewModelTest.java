/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Tests for {@link ErrorsViewModel}. */
final class ErrorsViewModelTest {

    /** The view model under tests. */
    private ErrorsViewModel errorsViewModel;

    /** Creates a fresh model for each test. */
    @BeforeEach
    void beforeEach() {
        errorsViewModel = new ErrorsViewModel();
    }

    /** Verifies that {@link ErrorsViewModel#addError} correctly sets the current error property. */
    @Test
    void addError() {
        errorsViewModel.addError("Sample error");

        assertEquals(errorsViewModel.currentErrorProperty().get(), "Sample error");
    }

    /**
     * Verifies that {@link ErrorsViewModel#addError} correctly queues the given error if the current error property
     * already contains an unacknowledged error.
     */
    @Test
    void addError_queue() {
        errorsViewModel.addError("Sample error #1");
        errorsViewModel.addError("Sample error #2");

        assertEquals(errorsViewModel.currentErrorProperty().get(), "Sample error #1");
    }

    /** Verifies that {@link ErrorsViewModel#addError} rejects {@code null} errors. */
    @Test
    void addError_invalid() {
        assertThrows(NullPointerException.class, () -> errorsViewModel.addError(null));
    }

    /**
     * Verifies that {@link ErrorsViewModel#acknowledgeError()} correctly clears the current error property if no error
     * is queued.
     */
    @Test
    void acknowledgeError() {
        errorsViewModel.addError("Sample error #1");
        errorsViewModel.acknowledgeError();

        assertNull(errorsViewModel.currentErrorProperty().get());
    }

    /**
     * Verifies that {@link ErrorsViewModel#acknowledgeError()} correctly updates the current error property with next
     * queued error if present.
     */
    @Test
    void acknowledgeError_nextError() {
        errorsViewModel.addError("Sample error #1");
        errorsViewModel.addError("Sample error #2");
        errorsViewModel.addError("Sample error #3");

        errorsViewModel.acknowledgeError();
        assertEquals(errorsViewModel.currentErrorProperty().get(), "Sample error #2");

        errorsViewModel.acknowledgeError();
        assertEquals(errorsViewModel.currentErrorProperty().get(), "Sample error #3");

        errorsViewModel.acknowledgeError();
        assertNull(errorsViewModel.currentErrorProperty().get());
    }

    /**
     * Verifies that {@link ErrorsViewModel#acknowledgeError()} does not trigger any update when no unacknowledged error
     * is present.
     */
    @Test
    void acknowledgeError_noop() {
        errorsViewModel
                .currentErrorProperty()
                .addListener(observable -> fail("Should not have modified current error"));
        errorsViewModel.acknowledgeError();

        assertNull(errorsViewModel.currentErrorProperty().get());
    }
}
