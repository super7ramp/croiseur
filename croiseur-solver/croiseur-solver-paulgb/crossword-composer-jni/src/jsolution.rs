/*
 * SPDX-FileCopyrightText: 2026 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use jni::errors::Result;
use jni::objects::{JCharArray, JObject, JValue};
use jni::sys::jchar;
use jni::{jni_sig, jni_str, Env};

/// Wrapper for the `re.belv.croiseur.solver.paulgb.Solution` Java object.
pub struct JSolution<'a> {
    /// The wrapped `Solution` Java object
    solution: JObject<'a>,
}

impl<'a> JSolution<'a> {
    /// Creates a new `JSolution` wrapping the given `JObject`.
    fn new(solution: JObject<'a>) -> Self {
        Self { solution }
    }

    /// Creates a new `JSolution` from given vector of `char`s.
    pub fn from(chars: Vec<char>, env: &mut Env<'a>) -> Result<Self> {
        // Convert to string back to a vector just to have the java char = u16 type
        let jchars: Vec<jchar> = chars
            .into_iter()
            .collect::<String>()
            // FIXME we shouldn't do that. Why does solver return lower case characters?
            .to_uppercase()
            .encode_utf16()
            .collect();

        let jchar_array = JCharArray::new(env, jchars.len())?;
        jchar_array.set_region(env, 0, jchars.as_slice())?;

        let class = env.find_class(jni_str!("re/belv/croiseur/solver/paulgb/Solution"))?;
        let array_object = JObject::from(jchar_array);
        let array_value = JValue::from(&array_object);
        let solution = env.new_object(class, jni_sig!("([C)V"), &[array_value])?;
        Ok(Self::new(solution))
    }

    /// Unwraps the underlying `JObject`.
    pub fn into_object(self) -> JObject<'a> {
        self.solution
    }
}
