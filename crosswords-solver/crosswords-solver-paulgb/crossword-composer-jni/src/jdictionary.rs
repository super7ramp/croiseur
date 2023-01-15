/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use crate::jarray::JArray;
use crossword::dictionary::Dictionary;
use jni::objects::JObject;
use jni::JNIEnv;

/// Wrapper for the `com.gitlab.super7ramp.crosswords.solver.paulgb.Dictionary` Java object.
pub struct JDictionary<'a> {
    /// The [JNI environment](JNIEnv).
    env: JNIEnv<'a>,
    /// The wrapped `Dictionary` Java object.
    dic: JObject<'a>,
}

impl<'a> JDictionary<'a> {
    pub fn new(jni_env: JNIEnv<'a>, dictionary: JObject<'a>) -> Self {
        Self {
            env: jni_env,
            dic: dictionary,
        }
    }
}

impl<'a> From<JDictionary<'a>> for Vec<String> {
    fn from(val: JDictionary<'a>) -> Self {
        let j_string = val
            .env
            .call_method(val.dic, "words", "()[Ljava/lang/String;", &[])
            .expect("Failed to access dictionary words");
        JArray::new(
            val.env,
            j_string
                .l()
                .expect("Failed to unwrap dictionary into a JObject"),
        )
        .into()
    }
}

impl<'a> From<JDictionary<'a>> for Dictionary {
    fn from(val: JDictionary<'a>) -> Self {
        let words = val.into();
        Dictionary::from_vec(words)
    }
}
