/*
 * SPDX-FileCopyrightText: 2026 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use jni::errors::Result;
use jni::objects::{JObject, JString};
use jni::{jni_sig, jni_str, Env};
use xwords::trie::Trie;

use crate::jiterable::JIterable;

/// Wrapper for the `re.belv.croiseur.solver.szunami.Dictionary` Java object.
pub struct JDictionary<'a> {
    /// The wrapped Java `Dictionary` object
    value: JObject<'a>,
}

impl<'a> JDictionary<'a> {
    /// Creates a new `JDictionary` wrapping the given Java `Dictionary` object
    pub fn new(value: JObject<'a>) -> Self {
        Self { value }
    }

    /// Transforms this `JDictionary` into a [`Trie`][]
    pub fn into_trie(self, env: &mut Env) -> Result<Trie> {
        let words = self.words(env)?;
        Ok(Trie::build(words))
    }

    /// Retrieves the words of the `Dictionary` object and returns them as a vector of `String`s.
    fn words(&self, env: &mut Env) -> Result<Vec<String>> {
        let words_jiterable = env
            .call_method(
                &self.value,
                jni_str!("words"),
                jni_sig!("()Ljava/lang/Iterable;"),
                &[],
            )?
            .l()?;

        let iterator = JIterable::new(words_jiterable).iter(env)?;

        let mut words = Vec::new();
        while let Some(obj) = iterator.next(env)? {
            let word = env.cast_local::<JString>(obj)?.to_string();
            if word.is_ascii() {
                // xwords-rs only supports ASCII: https://github.com/szunami/xwords-rs/issues/2
                words.push(word);
            }
        }
        Ok(words)
    }
}
