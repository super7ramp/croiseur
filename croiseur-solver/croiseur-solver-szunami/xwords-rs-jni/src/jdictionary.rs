/*
 * SPDX-FileCopyrightText: 2026 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use jni::errors::Result;
use jni::objects::JString;
use jni::{Env, bind_java_type};
use xwords::trie::Trie;

use crate::jiterable::JIterable;

bind_java_type!(
    pub JDictionary => re.belv.croiseur.solver.szunami.Dictionary,
    type_map = {
        JIterable => java.lang.Iterable,
    },
    methods {
        /// Retrieves the words of the `Dictionary` object.
        fn words() -> JIterable
    }
);

impl JDictionary<'_> {
    /// Transforms this `JDictionary` into a [`Trie`][]
    pub fn into_trie(self, env: &mut Env) -> Result<Trie> {
        let words = self.words_as_vec(env)?;
        Ok(Trie::build(words))
    }

    /// Retrieves the words of the `Dictionary` object and returns them as a vector of `String`s.
    fn words_as_vec(&self, env: &mut Env) -> Result<Vec<String>> {
        let iterator = self.words(env)?.iter(env)?;
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
