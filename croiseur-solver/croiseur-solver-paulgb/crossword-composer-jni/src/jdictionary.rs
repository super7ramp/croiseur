/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use std::cell::RefCell;
use std::rc::Rc;

use crossword::dictionary::Dictionary;
use jni::JNIEnv;
use jni::objects::JObject;

use crate::jarray::JArray;

/// Wrapper for the `com.gitlab.super7ramp.crosswords.solver.paulgb.Dictionary` Java object.
pub struct JDictionary<'a> {
    /// The [JNI environment](JNIEnv).
    env: Rc<RefCell<JNIEnv<'a>>>,
    /// The wrapped `Dictionary` Java object.
    dic: JObject<'a>,
}

impl<'a> JDictionary<'a> {
    pub fn new(jni_env: Rc<RefCell<JNIEnv<'a>>>, dictionary: JObject<'a>) -> Self {
        Self {
            env: jni_env,
            dic: dictionary,
        }
    }
}

impl<'a> From<JDictionary<'a>> for Vec<String> {
    fn from(wrapper: JDictionary<'a>) -> Self {
        let array = wrapper
            .env
            .borrow_mut()
            .call_method(wrapper.dic, "words", "()[Ljava/lang/String;", &[])
            .expect("Failed to access dictionary words")
            .l()
            .expect("Failed to unwrap dictionary words into an object");

        JArray::new(Rc::clone(&wrapper.env), array).into()
    }
}

impl<'a> From<JDictionary<'a>> for Dictionary {
    fn from(val: JDictionary<'a>) -> Self {
        let words = val.into();
        Dictionary::from_vec(words)
    }
}
