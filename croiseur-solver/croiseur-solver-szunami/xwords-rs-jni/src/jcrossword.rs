/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use jni::objects::{JObject, JString, JValue, JValueOwned};
use jni::sys::jint;
use jni::JNIEnv;
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
    pub fn from_crossword(crossword: Crossword, env: &mut JNIEnv<'a>) -> Self {
        let contents = crossword.to_string();
        // Re-find height and width since they are not exposed
        let height = contents.chars().filter(|c| *c == '\n').count();
        let width = if height > 0 {
            (contents.len() - height) / height
        } else {
            contents.len()
        };

        let contents = env
            .new_string(contents)
            .expect("Failed to create Java String");
        let contents = JValue::from(&contents);
        let height = jint::try_from(height)
            .map(JValue::from)
            .expect("Failed to convert height to jint (i32)");
        let width = jint::try_from(width)
            .map(JValue::from)
            .expect("Failed to convert width to jint (i32)");

        let class = env
            .find_class("re/belv/croiseur/solver/szunami/Crossword")
            .expect("Crossword class not found");
        let value = env
            .new_object(class, "(Ljava/lang/String;II)V", &[contents, width, height])
            .expect("Failed to create Crossword Java object");
        Self::new(value)
    }

    /// Transforms this `JCrossword` into a `Crossword`.
    pub fn into_crossword(mut self, env: &mut JNIEnv<'a>) -> Crossword {
        let contents = self.contents(env);
        let width = self.width(env);
        let height = self.height(env);

        Crossword::rectangle(contents, width, height)
            .expect("Failed to convert Crossword object to native counterpart")
    }

    /// Unwraps the underlying `JObject`.
    pub fn into_object(self) -> JObject<'a> {
        self.value
    }

    /// Returns the `content` data encapsulated in the `Crossword` Java object.
    fn contents(&mut self, env: &mut JNIEnv<'a>) -> String {
        self.call_and_unwrap_string(env, "contents", "()Ljava/lang/String;", &[])
    }

    /// Returns the `height` data encapsulated in the `Crossword` Java object.
    fn height(&mut self, env: &mut JNIEnv<'a>) -> usize {
        self.call_and_unwrap_int(env, "height", "()I", &[])
    }

    /// Returns the width data encapsulated in the `Crossword` Java object.
    fn width(&mut self, env: &mut JNIEnv<'a>) -> usize {
        self.call_and_unwrap_int(env, "width", "()I", &[])
    }

    /// Calls the specified method of `re.belv.croiseur.solver.szunami.Crossword` and
    /// returns its returned value under the form of a `uzise`.
    fn call_and_unwrap_int(
        &mut self,
        env: &mut JNIEnv<'a>,
        method: &str,
        signature: &str,
        args: &[JValue],
    ) -> usize {
        let jint = self
            .call(env, method, signature, args)
            .i()
            .expect("Failed to unwrap Java value as a jint (i32)");
        jint.try_into()
            .expect("Failed to convert jsize (i32) to usize (u32 or u64)")
    }

    /// Calls the specified method of `re.belv.croiseur.solver.szunami.Crossword` and
    /// returns its returned value under the form of a `String`.
    fn call_and_unwrap_string(
        &mut self,
        env: &mut JNIEnv<'a>,
        method: &str,
        signature: &str,
        args: &[JValue],
    ) -> String {
        let jobject = self
            .call(env, method, signature, args)
            .l()
            .expect("Failed to unwrap Java value as a JObject");
        let j_string = JString::from(jobject);
        env.get_string(&j_string)
            .expect("Failed to convert JObject to String")
            .into()
    }

    /// Calls the specified method of `re.belv.croiseur.solver.szunami.Crossword` and
    /// returns its returned value under the form of a `JValueOwned`.
    fn call(
        &mut self,
        env: &mut JNIEnv<'a>,
        method: &str,
        signature: &str,
        args: &[JValue],
    ) -> JValueOwned<'_> {
        let object = &mut self.value;
        env.call_method(object, method, signature, args)
            .expect("Call to re.belv.croiseur.solver.szunami.Crossword method failed")
    }
}
