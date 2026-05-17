/*
 * SPDX-FileCopyrightText: 2026 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use jni::errors::Result;
use jni::objects::JCharArray;
use jni::sys::jchar;
use jni::{Env, bind_java_type};

bind_java_type!(
    pub JSolution => re.belv.croiseur.solver.paulgb.Solution,
    constructors = {
        fn new(chars: jchar[]),
    }
);

impl JSolution<'_> {
    /// Creates a new `JSolution` from given vector of `char`s.
    pub fn from<'env>(env: &mut Env<'env>, chars: Vec<char>) -> Result<JSolution<'env>> {
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

        JSolution::new(env, jchar_array)
    }
}
