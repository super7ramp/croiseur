/*
 * SPDX-FileCopyrightText: 2026 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
use crate::jiterable::JIterable;
use crossword::dictionary::Dictionary;
use jni::errors::Result;
use jni::objects::JString;
use jni::{bind_java_type, Env};

bind_java_type!(
    pub JDictionary => re.belv.croiseur.solver.paulgb.Dictionary,
    type_map = {
        JIterable => java.lang.Iterable,
    },
    methods {
        /// Retrieves the words of the `Dictionary` object.
        fn words() -> JIterable
    }
);

impl JDictionary<'_> {
    /// Transforms the given `JDictionary` into a [`Dictionary`][].
    pub fn into_dictionary(self, env: &mut Env) -> Result<Dictionary> {
        let words = self.into_vec_string(env)?;
        Ok(Dictionary::from_vec(words))
    }

    /// Transforms this `JDictionary` into a vector of `String`s.
    fn into_vec_string(self, env: &mut Env) -> Result<Vec<String>> {
        let iterator = self.words(env)?.iter(env)?;
        let mut words = Vec::new();
        while let Some(obj) = iterator.next(env)? {
            let word = env.cast_local::<JString>(obj)?.to_string();
            words.push(word);
        }
        Ok(words)
    }
}
