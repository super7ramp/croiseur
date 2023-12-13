/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.cli.presenter;

import re.belv.croiseur.cli.l10n.ResourceBundles;
import re.belv.croiseur.cli.status.Status;
import re.belv.croiseur.common.puzzle.PuzzleCodecDetails;
import re.belv.croiseur.common.puzzle.SavedPuzzle;
import re.belv.croiseur.spi.presenter.puzzle.PuzzlePresenter;

import java.time.LocalDate;
import java.util.List;

/**
 * CLI implementation of {@link PuzzlePresenter}.
 */
final class CliPuzzlePresenter implements PuzzlePresenter {

    /** The format to present puzzle list. */
    private static final String PUZZLE_LIST_FORMAT = "%-4s\t%-4s\t%-16s\t%-16s\t%-16s%n";

    /** The format to present the puzzle codec list. */
    private static final String PUZZLE_CODEC_LIST_FORMAT = "%-16s\t%-32s\t%-16s%n";

    /**
     * Constructs an instance.
     */
    CliPuzzlePresenter() {
        // Nothing to do.
    }

    /**
     * Returns the localised message with given key.
     *
     * @param key the message key
     * @return the localised message
     */
    private static String $(final String key) {
        return ResourceBundles.$("presenter.puzzle." + key);
    }

    @Override
    public void presentAvailablePuzzles(final List<SavedPuzzle> puzzles) {

        if (puzzles.isEmpty()) {
            System.out.println($("list.none-found"));
            return;
        }

        final String idHeader = $("id");
        final String revisionHeader = $("rev");
        final String titleHeader = $("title");
        final String authorHeader = $("author");
        final String dateHeader = $("date");

        System.out.printf(PUZZLE_LIST_FORMAT, idHeader, revisionHeader, titleHeader, authorHeader,
                          dateHeader);
        System.out.printf(PUZZLE_LIST_FORMAT, CliPresenterUtil.lineOf(idHeader.length()),
                          CliPresenterUtil.lineOf(revisionHeader.length()), CliPresenterUtil.lineOf(titleHeader.length()),
                          CliPresenterUtil.lineOf(authorHeader.length()), CliPresenterUtil.lineOf(dateHeader.length()));
        puzzles.forEach(
                puzzle -> System.out.printf(PUZZLE_LIST_FORMAT, puzzle.id(), puzzle.revision(),
                                            puzzle.details().title(),
                                            puzzle.details().author(),
                                            puzzle.details().date().map(
                                                    LocalDate::toString).orElse("")));
    }

    @Override
    public void presentLoadedPuzzle(final SavedPuzzle puzzle) {
        final String formattedPuzzle = PuzzleFormatter.formatSavedPuzzle(puzzle);
        System.out.println(formattedPuzzle);
    }

    @Override
    public void presentPuzzleRepositoryError(final String error) {
        System.err.println(error);
        Status.setGeneralApplicativeError();
    }

    @Override
    public void presentSavedPuzzle(final SavedPuzzle puzzle) {
        final String confirmation = $("save.success");
        final String formattedPuzzle = PuzzleFormatter.formatSavedPuzzle(puzzle);
        System.out.println(
                confirmation + System.lineSeparator() + System.lineSeparator() + formattedPuzzle);
    }

    @Override
    public void presentDeletedAllPuzzles() {
        System.out.println($("deleted.all"));
    }

    @Override
    public void presentDeletedPuzzle(final long id) {
        System.out.printf($("deleted.single"), id);
    }

    @Override
    public void presentPuzzleDecoders(final List<PuzzleCodecDetails> decoders) {
        if (decoders.isEmpty()) {
            System.out.println($("list.decoders.none-found"));
            return;
        }
        presentCodecs(decoders);
    }

    @Override
    public void presentPuzzleImportError(final String error) {
        System.err.println(error);
        Status.setGeneralApplicativeError();
    }

    @Override
    public void presentPuzzleEncoders(final List<PuzzleCodecDetails> encoders) {
        if (encoders.isEmpty()) {
            System.out.println($("list.encoders.none-found"));
            return;
        }
        presentCodecs(encoders);
    }

    @Override
    public void presentPuzzleExportError(final String error) {
        System.err.println(error);
        Status.setGeneralApplicativeError();
    }

    /**
     * Presents a list of encoders or decoders.
     *
     * @param codecs the encoders or decoders
     */
    private static void presentCodecs(final List<PuzzleCodecDetails> codecs) {
        final String nameHeader = $("codec.name");
        final String descriptionHeader = $("codec.description");
        final String supportedFormatsHeader = $("codec.formats");

        System.out.printf(PUZZLE_CODEC_LIST_FORMAT, nameHeader, descriptionHeader,
                          supportedFormatsHeader);
        System.out.printf(PUZZLE_CODEC_LIST_FORMAT, CliPresenterUtil.lineOf(nameHeader.length()),
                          CliPresenterUtil.lineOf(descriptionHeader.length()),
                          CliPresenterUtil.lineOf(supportedFormatsHeader.length()));
        codecs.forEach(
                codec -> System.out.printf(PUZZLE_CODEC_LIST_FORMAT, codec.name(),
                                           codec.description(),
                                           String.join(", ", codec.supportedFormats())));
    }
}
