/*
 * SPDX-FileCopyrightText: 2026 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use jni::errors::Result;
use jni::objects::{JObject, JString, JValue, JValueOwned};
use jni::signature::MethodSignature;
use jni::strings::JNIStr;
use jni::sys::jint;
use jni::{jni_sig, jni_str, Env};
use xwords::crossword::Crossword;

/// Wrapper for the `re.belv.croiseur.solver.szunami.Crossword` Java object.
pub struct JCrossword<'a> {
    /// The wrapper `Crossword` Java object.
    value: JObject<'a>,
}

impl<'a> JCrossword<'a> {
    /// Creates a new `JCrossword` wrapping a Java `Crossword` object.
    pub fn new(value: JObject<'a>) -> Self {
        Self { value }
    }

    /// Creates a new `JCrossword` wrapping a new Java `Crossword` object built from the given native
    /// `Crossword` structure.
    pub fn from_crossword(crossword: Crossword, env: &mut Env<'a>) -> Result<Self> {
        let contents = crossword.to_string();
        // Re-find height and width since they are not exposed
        let height = contents.chars().filter(|c| *c == '\n').count();
        let width = if height > 0 {
            (contents.len() - height) / height
        } else {
            contents.len()
        };

        let contents = env.new_string(contents)?;
        let contents = JValue::from(&contents);
        let height = jint::try_from(height)
            .map(JValue::from)
            .expect("Failed to convert height to jint (i32)");
        let width = jint::try_from(width)
            .map(JValue::from)
            .expect("Failed to convert width to jint (i32)");

        let class = env.find_class(jni_str!("re/belv/croiseur/solver/szunami/Crossword"))?;
        let value = env.new_object(
            class,
            jni_sig!("(Ljava/lang/String;II)V"),
            &[contents, width, height],
        )?;
        Ok(Self::new(value))
    }

    /// Transforms this `JCrossword` into a `Crossword`.
    pub fn into_crossword(mut self, env: &mut Env<'a>) -> Result<Crossword> {
        let contents = self.contents(env)?;
        let width = self.width(env)?;
        let height = self.height(env)?;

        Ok(Crossword::rectangle(contents, width, height)
            .expect("Failed to convert Crossword object to native counterpart"))
    }

    /// Unwraps the underlying `JObject`.
    pub fn into_object(self) -> JObject<'a> {
        self.value
    }

    /// Returns the `content` data encapsulated in the `Crossword` Java object.
    fn contents(&mut self, env: &mut Env<'a>) -> Result<String> {
        self.call_and_unwrap_string(
            env,
            jni_str!("contents"),
            jni_sig!("()Ljava/lang/String;"),
            &[],
        )
    }

    /// Returns the `height` data encapsulated in the `Crossword` Java object.
    fn height(&mut self, env: &mut Env<'a>) -> Result<usize> {
        self.call_and_unwrap_int(env, jni_str!("height"), jni_sig!("()I"), &[])
    }

    /// Returns the width data encapsulated in the `Crossword` Java object.
    fn width(&mut self, env: &mut Env<'a>) -> Result<usize> {
        self.call_and_unwrap_int(env, jni_str!("width"), jni_sig!("()I"), &[])
    }

    /// Calls the specified method of `re.belv.croiseur.solver.szunami.Crossword` and
    /// returns its returned value under the form of a `uzise`.
    fn call_and_unwrap_int(
        &mut self,
        env: &mut Env<'a>,
        method: &JNIStr,
        signature: MethodSignature,
        args: &[JValue],
    ) -> Result<usize> {
        let jint = self.call(env, method, signature, args)?.i()?;
        Ok(jint
            .try_into()
            .expect("Failed to convert jsize (i32) to usize (u32 or u64)"))
    }

    /// Calls the specified method of `re.belv.croiseur.solver.szunami.Crossword` and
    /// returns its returned value under the form of a `String`.
    fn call_and_unwrap_string(
        &mut self,
        env: &mut Env<'a>,
        method: &JNIStr,
        signature: MethodSignature,
        args: &[JValue],
    ) -> Result<String> {
        let jobject = self.call(env, method, signature, args)?.l()?;
        Ok(env.as_cast::<JString>(&jobject)?.to_string())
    }

    /// Calls the specified method of `re.belv.croiseur.solver.szunami.Crossword` and
    /// returns its returned value under the form of a `JValueOwned`.
    fn call(
        &mut self,
        env: &mut Env<'a>,
        method: &JNIStr,
        signature: MethodSignature,
        args: &[JValue],
    ) -> Result<JValueOwned<'_>> {
        let object = &mut self.value;
        env.call_method(object, method, signature, args)
    }
}
