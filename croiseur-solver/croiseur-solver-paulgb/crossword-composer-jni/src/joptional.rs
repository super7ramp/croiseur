/*
 * SPDX-FileCopyrightText: 2026 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use jni::bind_java_type;

bind_java_type!(
    pub JOptional => java.util.Optional,
    methods {
        /// Creates an empty `Optional` instance.
        static fn empty() -> JOptional,
        /// Creates an `Optional` instance containing the given object.
        static fn of(obj: JObject) -> JOptional,
    }
);
