/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use std::cell::RefCell;
use std::rc::Rc;

use jni::JNIEnv;
use jni::objects::{JObject, JValue};

/// Wrapper for Java `Optional`.
pub struct JOptional<'a> {
    /// The wrapped `Optional` Java object
    val: JObject<'a>,
}

impl<'a> JOptional<'a> {
    fn new(value: JObject<'a>) -> Self {
        Self { val: value }
    }

    pub fn empty(env: Rc<RefCell<JNIEnv<'a>>>) -> Self {
        let value = Self::call(env, "empty", "()Ljava/util/Optional;", &[]);
        Self::new(value)
    }

    pub fn of(env: Rc<RefCell<JNIEnv<'a>>>, obj: JObject<'a>) -> Self {
        let value = JValue::from(&obj);
        let value = Self::call(
            env,
            "of",
            "(Ljava/lang/Object;)Ljava/util/Optional;",
            &[value],
        );
        Self::new(value)
    }

    pub fn unwrap_object(self) -> JObject<'a> {
        self.val
    }

    fn call(
        env: Rc<RefCell<JNIEnv<'a>>>,
        method: &str,
        signature: &str,
        args: &[JValue],
    ) -> JObject<'a> {
        let optional_class = env.borrow_mut().find_class("java/util/Optional").unwrap();
        let j_value = env
            .borrow_mut()
            .call_static_method(optional_class, method, signature, args)
            .unwrap();
        j_value.l().unwrap()
    }
}
