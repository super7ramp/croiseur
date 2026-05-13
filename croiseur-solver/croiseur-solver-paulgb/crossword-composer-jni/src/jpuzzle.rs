/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use crossword::grid::Grid;
use jni::objects::{JIntArray, JObject, JObjectArray};
use jni::{jni_sig, jni_str, Env, JValueOwned};

/// Wrapper for the `re.belv.croiseur.solver.paulgb.Puzzle` Java object.
pub struct JPuzzle<'a> {
    /// The wrapped `Puzzle` Java object.
    puzzle: JObject<'a>,
}

impl<'a> JPuzzle<'a> {
    /// Creates a new `JPuzzle` wrapping the given `JObject`.
    pub fn new(puzzle_arg: JObject<'a>) -> Self {
        Self { puzzle: puzzle_arg }
    }

    /// Transforms this `JPuzzle` to a [`Grid`][].
    pub fn into_grid(self, env: &mut Env) -> Grid {
        let j_array_2d = env
            .call_method(self.puzzle, jni_str!("slots"), jni_sig!("()[[I"), &[])
            .and_then(JValueOwned::l)
            .and_then(|obj| env.cast_local::<JObjectArray<JIntArray>>(obj))
            .expect("Failed to read puzzle slots");

        let mut vec_2d: Vec<Vec<usize>> = Vec::new();
        let row_count = j_array_2d.len(env).unwrap();

        for row_index in 0..row_count {
            let row = j_array_2d.get_element(env, row_index).unwrap();

            let mut jint_row = vec![0; row.len(env).unwrap()];
            row.get_region(env, 0, jint_row.as_mut_slice()).unwrap();

            let usize_row = jint_row
                .into_iter()
                .map(|jint| jint.try_into().expect("Failed to convert jint to usize"))
                .collect();

            vec_2d.push(usize_row);
        }

        Grid::new(vec_2d)
    }
}
