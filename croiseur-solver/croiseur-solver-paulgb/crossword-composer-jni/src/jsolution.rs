/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use jni::objects::{JObject, JValue};
use jni::JNIEnv;

use crate::jarray::JArray;

/// Wrapper for the `com.gitlab.super7ramp.crosswords.solver.paulgb.Solution` Java object.
pub struct JSolution<'a> {
    /// The wrapped `Solution` Java object
    solution: JObject<'a>,
}

impl<'a> JSolution<'a> {
    /// Creates a new `JSolution` wrapping the given `JObject`.
    fn new(solution: JObject<'a>) -> Self {
        Self { solution }
    }

    /// Creates a new `JSolution` from given vector of `char`s.
    pub fn from(chars: Vec<char>, env: &mut JNIEnv<'a>) -> Self {
        let array = JArray::from_vec_chars(chars, env);
        let class = env
            .find_class("com/gitlab/super7ramp/croiseur/solver/paulgb/Solution")
            .expect("Solution class not found");
        let array_object = array.into_object();
        let array_value = JValue::from(&array_object);
        let solution = env
            .new_object(class, "([C)V", &[array_value])
            .expect("Failed to create a Solution object");
        Self::new(solution)
    }

    /// Unwraps the underlying `JObject`.
    pub fn into_object(self) -> JObject<'a> {
        self.solution
    }
}
