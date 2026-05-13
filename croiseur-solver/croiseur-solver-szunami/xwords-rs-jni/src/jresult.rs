/*
 * SPDX-FileCopyrightText: 2026 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use jni::objects::{JObject, JValue};
use jni::signature::MethodSignature;
use jni::strings::JNIStr;
use jni::{jni_sig, jni_str, Env};
use xwords::crossword::Crossword;

use crate::jcrossword::JCrossword;

/// Wrapper for the `re.belv.croiseur.solver.szunami.Result` Java object.
pub struct JResult<'a> {
    /// The wrapper `Result` Java object.
    value: JObject<'a>,
}

impl<'a> JResult<'a> {
    /// Creates a new `JResult` wrapping the given `JObject`.
    fn new(result: JObject<'a>) -> Self {
        JResult { value: result }
    }

    /// Creates a new `JResult` wrapping a Java `Result` of type `Err`.
    pub fn err(error: &str, env: &mut Env<'a>) -> Self {
        let j_string = env.new_string(error).expect("Failed to create java String");
        let j_value = JValue::from(&j_string);
        let result = Self::call(
            env,
            jni_str!("err"),
            jni_sig!("(Ljava/lang/String;)Lre/belv/croiseur/solver/szunami/Result;"),
            &[j_value],
        );
        Self::new(result)
    }

    /// Creates a new `JResult` wrapping a Java `Result` of type `Err`.
    pub fn ok(solution: Crossword, env: &mut Env<'a>) -> Self {
        let solved_crossword = JCrossword::from_crossword(solution, env).into_object();
        let result = Self::call(
            env,
            jni_str!("ok"),
            jni_sig!("(Lre/belv/croiseur/solver/szunami/Crossword;)Lre/belv/croiseur/solver/szunami/Result;"),
            &[JValue::from(&solved_crossword)],
        );
        Self::new(result)
    }

    /// Unwraps the underlying `JObject`.
    pub fn into_object(self) -> JObject<'a> {
        self.value
    }

    /// Calls the specified method of `re.belv.croiseur.solver.szunami.Result` and
    /// returns its returned value under the form of a `JObject`.
    fn call(
        env: &mut Env<'a>,
        method: &JNIStr,
        signature: MethodSignature,
        args: &[JValue],
    ) -> JObject<'a> {
        let optional_class = env
            .find_class(jni_str!("re/belv/croiseur/solver/szunami/Result"))
            .expect("re.belv.croiseur.solver.szunami.Result could not be found");
        let j_value = env
            .call_static_method(optional_class, method, signature, args)
            .expect("Call to re.belv.croiseur.solver.szunami.Result failed");
        j_value
            .l()
            .expect("Creation of re.belv.croiseur.solver.szunami.Result failed")
    }
}
