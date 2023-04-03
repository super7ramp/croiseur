/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use crossword::dictionary::Dictionary;
use jni::JNIEnv;
use jni::objects::{JObject, JString};

use crate::jiterable::JIterable;

/// Wrapper for the `com.gitlab.super7ramp.crosswords.solver.paulgb.Dictionary` Java object.
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
    pub fn into_dictionary(self, env: &mut JNIEnv) -> Dictionary {
        let words = self.into_vec_string(env);
        Dictionary::from_vec(words)
    }

    /// Transforms this `JDictionary` into a vector of `String`s.
    fn into_vec_string(self, env: &mut JNIEnv) -> Vec<String> {
        let words_jobject = env
            .call_method(&self.dic, "words", "()Ljava/lang/Iterable;", &[])
            .expect("Failed to retrieve dictionary words")
            .l()
            .expect("Failed to convert JValue to JObject");

        let words_jiterable = JIterable::from_env(env, &words_jobject)
            .expect("Failed to get word list from Dictionary");

        let mut iterator = words_jiterable
            .iter(env)
            .expect("Failed to create word list iterator");

        let mut words = Vec::new();
        while let Some(obj) = iterator.next(env).expect("Failed to iterate on word list") {
            let j_string = env.auto_local(JString::from(obj));
            let word = Self::rust_string_from(&j_string, env);
            words.push(word);
        }
        words
    }

    /// Converts a `JString` into a `String`.
    fn rust_string_from(j_string: &JString, env: &mut JNIEnv) -> String {
        unsafe {
            // Use unchecked flavour of get_string() for performance reason. Also, safe
            // get_string() seems to create local references behind the hood so it is not very
            // practical when called in a loop.
            env.get_string_unchecked(j_string)
                .expect("Failed to convert JObject to String")
                .into()
        }
    }
}
