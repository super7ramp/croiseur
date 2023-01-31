/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

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

    pub fn empty(env: JNIEnv<'a>) -> Self {
        let value = Self::call(env, "empty", "()Ljava/util/Optional;", &[]);
        Self::new(value)
    }

    pub fn of(env: JNIEnv<'a>, value: JValue<'a>) -> Self {
        let value = Self::call(
            env,
            "of",
            "(Ljava/lang/Object;)Ljava/util/Optional;",
            &[value],
        );
        Self::new(value)
    }

    pub fn as_object(&self) -> JObject<'a> {
        self.val
    }

    fn call(env: JNIEnv<'a>, method: &str, signature: &str, args: &[JValue]) -> JObject<'a> {
        let optional_class = env.find_class("java/util/Optional").unwrap();
        let j_value = env
            .call_static_method(optional_class, method, signature, args)
            .unwrap();
        j_value.l().unwrap()
    }
}
