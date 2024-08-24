/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view.model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javafx.collections.ListChangeListener;
import org.junit.jupiter.api.Test;

/**
 * Tests on {@link ObservableAggregateListImpl}.
 */
final class ObservableAggregateListImplTest {

    @Test
    void aggregate() throws InterruptedException {
        final AggregateList<Integer> backingList = new AggregateListImpl<>();
        final ObservableAggregateListImpl<Integer> list = new ObservableAggregateListImpl<>(backingList);
        final CountDownLatch eventFiredLatch = new CountDownLatch(1);
        list.addListener((ListChangeListener<Integer>) c -> {
            assertTrue(c.next());
            assertTrue(c.wasAdded());
            assertEquals(3, c.getAddedSize());
            assertEquals(List.of(1, 2, 3), c.getAddedSubList());
            assertEquals(0, c.getFrom());
            assertEquals(3, c.getTo());
            assertFalse(c.next());
            eventFiredLatch.countDown();
        });

        list.aggregate(List.of(1, 2, 3));
        final boolean eventFired = eventFiredLatch.await(100, TimeUnit.MILLISECONDS);

        assertTrue(eventFired);
    }

    @Test
    void aggregateAtIndex() throws InterruptedException {
        final AggregateList<Integer> backingList = new AggregateListImpl<>(List.of(1, 2, 3), List.of(6, 7));
        final ObservableAggregateListImpl<Integer> list = new ObservableAggregateListImpl<>(backingList);
        final CountDownLatch eventFiredLatch = new CountDownLatch(1);
        list.addListener((ListChangeListener<Integer>) c -> {
            assertTrue(c.next());
            assertTrue(c.wasAdded());
            assertEquals(2, c.getAddedSize());
            assertEquals(List.of(4, 5), c.getAddedSubList());
            assertEquals(3, c.getFrom());
            assertEquals(5, c.getTo());
            assertFalse(c.next());
            eventFiredLatch.countDown();
        });

        list.aggregate(1, List.of(4, 5));
        final boolean eventFired = eventFiredLatch.await(100, TimeUnit.MILLISECONDS);

        assertTrue(eventFired);
    }

    @Test
    void disaggregate() throws InterruptedException {
        final AggregateList<Integer> backingList =
                new AggregateListImpl<>(List.of(1, 2, 3), List.of(4, 5), List.of(6, 7));
        final ObservableAggregateListImpl<Integer> list = new ObservableAggregateListImpl<>(backingList);
        final CountDownLatch eventFiredLatch = new CountDownLatch(1);
        list.addListener((ListChangeListener<Integer>) c -> {
            assertTrue(c.next());
            assertTrue(c.wasRemoved());
            assertEquals(2, c.getRemovedSize());
            assertEquals(List.of(4, 5), c.getRemoved());
            assertEquals(3, c.getFrom());
            assertEquals(3, c.getTo());
            assertFalse(c.next());
            eventFiredLatch.countDown();
        });

        list.disaggregate(1);
        final boolean eventFired = eventFiredLatch.await(100, TimeUnit.MILLISECONDS);

        assertTrue(eventFired);
    }
}
