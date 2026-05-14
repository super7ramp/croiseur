/*
 * SPDX-FileCopyrightText: 2026 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use jni::errors::Result;
use jni::objects::{JObject, JValue};
use jni::signature::MethodSignature;
use jni::strings::JNIStr;
use jni::{jni_sig, jni_str, Env};

/// Wrapper for Java `Optional`.
pub struct JOptional<'a> {
    /// The wrapped `Optional` Java object
    val: JObject<'a>,
}

impl<'a> JOptional<'a> {
    /// Creates a new `JOptional` wrapping the given `JObject`.
    fn new(value: JObject<'a>) -> Self {
        Self { val: value }
    }

    /// Calls the specified method of `java.util.Optional` and returns its result under the form
    /// of a `JObject`.
    fn call(
        env: &mut Env<'a>,
        method: &JNIStr,
        signature: MethodSignature,
        args: &[JValue],
    ) -> Result<JObject<'a>> {
        let optional_class = env.find_class(jni_str!("java/util/Optional"))?;
        env.call_static_method(optional_class, method, signature, args)?
            .l()
    }

    /// Creates a new `JOptional` wrapping a new empty `java.util.Optional`.
    pub fn empty(env: &mut Env<'a>) -> Result<Self> {
        let value = Self::call(
            env,
            jni_str!("empty"),
            jni_sig!("()Ljava/util/Optional;"),
            &[],
        )?;
        Ok(Self::new(value))
    }

    /// Creates a new `JOptional` wrapping a new `java.util.Optional` containing the given object.
    pub fn of(obj: JObject<'a>, env: &mut Env<'a>) -> Result<Self> {
        let value = JValue::from(&obj);
        let value = Self::call(
            env,
            jni_str!("of"),
            jni_sig!("(Ljava/lang/Object;)Ljava/util/Optional;"),
            &[value],
        )?;
        Ok(Self::new(value))
    }

    /// Unwraps the underlying `JObject`.
    pub fn into_object(self) -> JObject<'a> {
        self.val
    }
}
