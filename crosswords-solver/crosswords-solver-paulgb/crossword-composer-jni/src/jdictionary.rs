/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use crate::jarray::JArray;
use crossword::dictionary::Dictionary;
use jni::objects::JObject;
use jni::JNIEnv;

pub struct JDictionary<'a> {
    env: JNIEnv<'a>,
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

impl<'a> Into<Vec<String>> for JDictionary<'a> {
    fn into(self) -> Vec<String> {
        let j_string = self
            .env
            .call_method(self.dic, "words", "()[Ljava/lang/String;", &[])
            .expect("Failed to access dictionary words");
        JArray::new(
            self.env,
            j_string
                .l()
                .expect("Failed to unwrap dictionary into a JObject"),
        )
        .into()
    }
}

impl<'a> Into<Dictionary> for JDictionary<'a> {
    fn into(self) -> Dictionary {
        let words = self.into();
        Dictionary::from_vec(words)
    }
}
