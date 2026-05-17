/*
 * SPDX-FileCopyrightText: 2026 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use jni::bind_java_type;

bind_java_type! {
    pub JIterable => java.lang.Iterable,
    methods {
        /// Retrieves an iterator from the `Iterable` object.
        fn iter {
            name = iterator,
            sig = () -> JIterator,
        }
    }
}
