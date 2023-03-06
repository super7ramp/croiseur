/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use jni::objects::{JObject, JString};
use jni::JNIEnv;
use xwords::trie::Trie;

/// Wrapper for the `com.gitlab.croiseur.solver.szunami.Dictionary` Java object.
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
    pub fn into_trie(self, env: &mut JNIEnv) -> Trie {
        let words = self.words(env);
        Trie::build(words)
    }

    /// Retrieves the words of the `Dictionary` object and returns them as a vector of `String`s.
    fn words(&self, env: &mut JNIEnv) -> Vec<String> {
        let words_jobject = env
            .call_method(&self.value, "words", "()Ljava/util/List;", &[])
            .expect("Failed to retrieve dictionary words")
            .l()
            .expect("Failed to convert JValue to JObject");

        let words_jlist = env
            .get_list(&words_jobject)
            .expect("Failed to get word list from Dictionary");

        let mut iterator = words_jlist
            .iter(env)
            .expect("Failed to create word list iterator");

        let mut words = Vec::new();
        while let Some(obj) = iterator.next(env).expect("Failed to iterate on word list") {
            let j_string = env.auto_local(JString::from(obj));
            let word = Self::rust_string_from(&j_string, env);
            if word.is_ascii() {
                // xwords-rs only supports ASCII: https://github.com/szunami/xwords-rs/issues/2
                words.push(word);
            }
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
