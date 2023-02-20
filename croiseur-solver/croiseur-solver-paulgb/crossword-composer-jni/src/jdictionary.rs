/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use crossword::dictionary::Dictionary;
use jni::JNIEnv;
use jni::objects::JObject;

use crate::jarray::JArray;

/// Wrapper for the `com.gitlab.super7ramp.crosswords.solver.paulgb.Dictionary` Java object.
pub struct JDictionary<'a> {
    /// The wrapped `Dictionary` Java object.
    dic: JObject<'a>,
}

impl<'a> JDictionary<'a> {
    pub fn new(dictionary: JObject<'a>) -> Self {
        Self { dic: dictionary }
    }

    pub fn into_dictionary(self, env: &mut JNIEnv) -> Dictionary {
        let words = self.into_string_vector(env);
        Dictionary::from_vec(words)
    }

    fn into_string_vector(self, env: &mut JNIEnv) -> Vec<String> {
        let array = env
            .call_method(self.dic, "words", "()[Ljava/lang/String;", &[])
            .expect("Failed to access dictionary words")
            .l()
            .expect("Failed to unwrap dictionary words into an object");

        JArray::new(array).into_vec_string(env)
    }
}
