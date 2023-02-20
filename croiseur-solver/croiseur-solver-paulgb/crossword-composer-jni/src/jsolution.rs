/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use jni::JNIEnv;
use jni::objects::{JObject, JValue};

use crate::jarray::JArray;

/// Wrapper for the `com.gitlab.super7ramp.crosswords.solver.paulgb.Solution` Java object.
pub struct JSolution<'a> {
    /// The wrapped `Solution` Java object
    solution: JObject<'a>,
}

impl<'a> JSolution<'a> {
    fn new(solution_arg: JArray, env: &mut JNIEnv<'a>) -> Self {
        let class = env
            .find_class("com/gitlab/super7ramp/croiseur/solver/paulgb/Solution")
            .expect("Solution class not found");
        let object = solution_arg.into_object();
        let array = JValue::from(&object);
        let solution = env
            .new_object(class, "([C)V", &[array])
            .expect("Failed to create a Solution object");
        Self { solution }
    }

    pub fn from(chars: Vec<char>, env: &mut JNIEnv<'a>) -> Self {
        let wrapper = JArray::from_vec_chars(chars, env);
        Self::new(wrapper, env)
    }

    pub fn unwrap_object(self) -> JObject<'a> {
        self.solution
    }
}
