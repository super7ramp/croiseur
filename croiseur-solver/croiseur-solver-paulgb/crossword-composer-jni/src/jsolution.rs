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
    fn new(env: JNIEnv<'a>, solution_arg: JArray<'a>) -> Self {
        let class = env
            .find_class("com/gitlab/super7ramp/croiseur/solver/paulgb/Solution")
            .expect("Solution class not found");
        Self {
            solution: env
                .new_object(class, "([C)V", &[solution_arg.as_value()])
                .expect("Failed to create a Solution object"),
        }
    }

    pub fn from(jni_env: JNIEnv<'a>, chars: Vec<char>) -> Self {
        Self::new(jni_env, JArray::from((jni_env, chars)))
    }

    pub fn as_value(&self) -> JValue<'a> {
        JValue::Object(self.solution)
    }
}
