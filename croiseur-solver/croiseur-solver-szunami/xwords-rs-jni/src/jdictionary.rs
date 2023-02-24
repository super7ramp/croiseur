/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use jni::JNIEnv;
use jni::objects::{JObject, JString};
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

        let mut words = Vec::new();
        let mut iterator = words_jlist
            .iter(env)
            .expect("Failed to create word list iterator");
        while let Some(obj) = iterator.next(env).expect("Failed to iterate on word list") {
            // Wrap object into AutoLocal as recommended by JList::iter to limit memory usage
            let auto_local_word = env.auto_local(JString::from(obj));
            let word = env
                .get_string(&auto_local_word)
                .expect("Failed to convert JObject to String")
                .into();
            words.push(word);
        }
        words
    }
}
