/*
 * SPDX-FileCopyrightText: 2026 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use crate::jcrossword::JCrossword;
use jni::errors::Result;
use jni::objects::JString;
use jni::{bind_java_type, Env};
use xwords::crossword::Crossword;

bind_java_type!(
    pub JResult => re.belv.croiseur.solver.szunami.Result,
    type_map = {
        JCrossword => re.belv.croiseur.solver.szunami.Crossword,
    },
    methods {
        /// Creates a new `JResult` wrapping a Java `Result` of type `Err`.
        static fn err(error: JString) -> JResult,
        /// Creates a new `JResult` wrapping a Java `Result` of type `Ok`.
        static fn ok(solution: JCrossword) -> JResult,
    }
);

impl JResult<'_> {
    /// Creates a new `JResult` wrapping a Java `Result` of type `Err`, with the given error message.
    pub fn from_error<'env>(env: &mut Env<'env>, error: String) -> Result<JResult<'env>> {
        let error = JString::new(env, error)?;
        JResult::err(env, error)
    }
    /// Creates a new `JResult` wrapping a Java `Result` of type `Ok`, with the given solution.
    pub fn from_solution<'env>(env: &mut Env<'env>, solution: Crossword) -> Result<JResult<'env>> {
        let jcrossword = JCrossword::from_crossword(env, solution)?;
        JResult::ok(env, jcrossword)
    }
}
