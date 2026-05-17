/*
 * SPDX-FileCopyrightText: 2026 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use jni::errors::Result;
use jni::sys::jint;
use jni::{Env, bind_java_type};
use xwords::crossword::Crossword;

bind_java_type!(
    pub JCrossword => re.belv.croiseur.solver.szunami.Crossword,
    constructors {
        fn new(contents: JString, width: jint, height: jint),
    },
    methods {
        /// Returns the `content` data encapsulated in the `Crossword` Java object.
        fn contents() -> JString,
        /// Returns the `height` data encapsulated in the `Crossword` Java object.
        fn height() -> jint,
        /// Returns the `height` data encapsulated in the `Crossword` Java object.
        fn width() -> jint,
    }
);

impl JCrossword<'_> {
    /// Creates a new `JCrossword` wrapping a new Java `Crossword` object built from the given native
    /// `Crossword` structure.
    pub fn from_crossword<'env>(
        env: &mut Env<'env>,
        crossword: Crossword,
    ) -> Result<JCrossword<'env>> {
        let contents = crossword.to_string();
        // Re-find height and width since they are not exposed
        let height = contents.chars().filter(|c| *c == '\n').count();
        let width = if height > 0 {
            (contents.len() - height) / height
        } else {
            contents.len()
        };

        let contents = env.new_string(contents)?;
        let height = jint::try_from(height).expect("Failed to convert height to jint (i32)");
        let width = jint::try_from(width).expect("Failed to convert width to jint (i32)");

        JCrossword::new(env, contents, width, height)
    }

    /// Transforms this `JCrossword` into a `Crossword`.
    pub fn into_crossword(self, env: &mut Env) -> Result<Crossword> {
        let contents = self.contents(env)?.to_string();
        let width = self
            .width(env)?
            .try_into()
            .expect("Failed to convert width to usize");
        let height = self
            .height(env)?
            .try_into()
            .expect("Failed to convert height to usize");
        Ok(Crossword::rectangle(contents, width, height)
            .expect("Failed to convert Crossword object to native counterpart"))
    }
}
