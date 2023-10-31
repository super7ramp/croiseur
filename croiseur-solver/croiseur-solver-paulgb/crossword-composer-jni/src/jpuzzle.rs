/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use crossword::grid::Grid;
use jni::objects::JObject;
use jni::JNIEnv;

use crate::jarray::JArray;

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
    pub fn into_grid(self, env: &mut JNIEnv) -> Grid {
        let j_array_2d = env
            .call_method(self.puzzle, "slots", "()[[I", &[])
            .expect("Failed to access puzzle slots");

        let j_array_2d = j_array_2d
            .l()
            .map(JArray::new)
            .expect("Failed to read puzzle slots");
        let vec_of_j_array: Vec<JArray> = j_array_2d.into_vec_jarray(env);

        let mut vec_2d: Vec<Vec<usize>> = Vec::new();
        vec_of_j_array.into_iter().for_each(|entry| {
            let value = entry.into_vec_usize(env);
            vec_2d.push(value);
        });

        Grid::new(vec_2d)
    }
}
