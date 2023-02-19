/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use std::cell::RefCell;
use std::rc::Rc;

use jni::JNIEnv;
use jni::objects::{JObject, JValue};

use crate::jarray::JArray;

/// Wrapper for the `com.gitlab.super7ramp.crosswords.solver.paulgb.Solution` Java object.
pub struct JSolution<'a> {
    /// The wrapped `Solution` Java object
    solution: JObject<'a>,
}

impl<'a> JSolution<'a> {
    fn new(env: Rc<RefCell<JNIEnv<'a>>>, solution_arg: JArray<'a>) -> Self {
        let class = env
            .borrow_mut()
            .find_class("com/gitlab/super7ramp/croiseur/solver/paulgb/Solution")
            .expect("Solution class not found");
        let object = solution_arg.unwrap_object();
        let array = JValue::from(&object);
        let solution = env
            .borrow_mut()
            .new_object(class, "([C)V", &[array])
            .expect("Failed to create a Solution object");
        Self { solution }
    }

    pub fn from(jni_env: Rc<RefCell<JNIEnv<'a>>>, chars: Vec<char>) -> Self {
        let wrapper = JArray::from(Rc::clone(&jni_env), chars);
        Self::new(jni_env, wrapper)
    }

    pub fn unwrap_object(self) -> JObject<'a> {
        self.solution
    }
}
