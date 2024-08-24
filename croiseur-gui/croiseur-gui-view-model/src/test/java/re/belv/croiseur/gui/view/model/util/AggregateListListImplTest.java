/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

package re.belv.croiseur.gui.view.model.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Tests for {@link AggregateListImpl}. */
final class AggregateListListImplTest {

    // Tests on get/size

    @Test
    void getEmptyList() {
        final List<Object> list = new AggregateListImpl<>();

        assertEquals(0, list.size());
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
    }

    @Test
    void getListOfEmptyList() {
        final List<Object> list = new AggregateListImpl<>(Collections.emptyList());

        assertEquals(0, list.size());
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
    }

    @Test
    void getOneList() {
        final List<Integer> list = new AggregateListImpl<>(List.of(1, 2, 3));

        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(3));
    }

    @Test
    void getThreeLists() {
        final List<Integer> list = new AggregateListImpl<>(List.of(1, 2, 3), List.of(4, 5), List.of(6, 7, 8, 9));

        assertEquals(9, list.size());
        assertEquals(1, list.get(0));
        assertEquals(4, list.get(3));
        assertEquals(7, list.get(6));
        assertEquals(9, list.get(8));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(9));
    }

    @Test
    void getEmptyAndNonEmptyLists() {
        final List<Integer> list = new AggregateListImpl<>(
                List.of(1, 2, 3),
                Collections.emptyList(),
                Collections.emptyList(),
                List.of(4, 5),
                Collections.emptyList(),
                List.of(6, 7, 8, 9));

        assertEquals(9, list.size());
        assertEquals(1, list.get(0));
        assertEquals(4, list.get(3));
        assertEquals(7, list.get(6));
        assertEquals(9, list.get(8));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(9));
    }

    // Tests on aggregate/disaggregate

    @Test
    void aggregate() {
        final AggregateList<Integer> list = new AggregateListImpl<>(List.of(1, 2, 3));
        final List<Integer> toAggregate = List.of(4, 5, 6);

        list.aggregate(toAggregate);

        assertEquals(2, list.aggregateCount());
        assertEquals(toAggregate, list.aggregatedAt(1));
        assertEquals(3, list.elementIndex(1));
    }

    @Test
    void aggregateAtIndex() {
        final AggregateList<Integer> list = new AggregateListImpl<>(List.of(1, 2, 3));
        final List<Integer> toAggregate = List.of(4, 5, 6);

        list.aggregate(0, toAggregate);

        assertEquals(2, list.aggregateCount());
        assertEquals(toAggregate, list.aggregatedAt(0));
        assertEquals(0, list.elementIndex(0));
    }

    @Test
    void disaggregate() {
        final AggregateList<Integer> list = new AggregateListImpl<>(List.of(1, 2, 3));

        list.disaggregate(0);

        assertEquals(0, list.aggregateCount());
        assertThrows(IndexOutOfBoundsException.class, () -> list.aggregatedAt(0));
        assertThrows(IndexOutOfBoundsException.class, () -> list.elementIndex(0));
    }
}
