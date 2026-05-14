/*
 * SPDX-FileCopyrightText: 2026 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
use crossword::dictionary::Dictionary;
use jni::errors::Result;
use jni::objects::{JObject, JString};
use jni::{jni_sig, jni_str, Env};

use crate::jiterable::JIterable;

/// Wrapper for the `re.belv.croiseur.solver.paulgb.Dictionary` Java object.
pub struct JDictionary<'a> {
    /// The wrapped `Dictionary` Java object.
    dic: JObject<'a>,
}

impl<'a> JDictionary<'a> {
    /// Creates a new `JDictionary` wrapping the given `JObject`.
    pub fn new(dictionary: JObject<'a>) -> Self {
        Self { dic: dictionary }
    }

    /// Transforms the given `JDictionary` into a [`Dictionary`][].
    pub fn into_dictionary(self, env: &mut Env) -> Result<Dictionary> {
        let words = self.into_vec_string(env)?;
        Ok(Dictionary::from_vec(words))
    }

    /// Transforms this `JDictionary` into a vector of `String`s.
    fn into_vec_string(self, env: &mut Env) -> Result<Vec<String>> {
        let words_jobject = env
            .call_method(
                &self.dic,
                jni_str!("words"),
                jni_sig!("()Ljava/lang/Iterable;"),
                &[],
            )?
            .l()?;

        let iterator = JIterable::new(words_jobject).iter(env)?;

        let mut words = Vec::new();
        while let Some(obj) = iterator.next(env)? {
            let word = env
                .cast_local::<JString>(obj)
                .expect("Failed to convert JObject to String")
                .to_string();
            words.push(word);
        }
        Ok(words)
    }
}
