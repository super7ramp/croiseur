/*
 * SPDX-FileCopyrightText: 2026 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use jni::errors::Result;
use jni::objects::{JIterator, JObject};
use jni::{jni_sig, jni_str, Env};

// A wrapper over a Java `Iterable`.
pub struct JIterable<'a> {
    value: JObject<'a>,
}

impl<'a> JIterable<'a> {
    // Creates a new `JIterable` wrapping the given Java `Iterable` object.
    pub fn new(value: JObject<'a>) -> Self {
        Self { value }
    }

    // Retrieves an iterator from the `Iterable` object.
    pub fn iter(&mut self, env: &mut Env<'a>) -> Result<JIterator<'a>> {
        let iterator_obj = env
            .call_method(
                &mut self.value,
                jni_str!("iterator"),
                jni_sig!("()Ljava/util/Iterator;"),
                &[],
            )?
            .l()?;
        env.cast_local::<JIterator>(iterator_obj)
    }
}
