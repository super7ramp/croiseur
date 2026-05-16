/*
 * SPDX-FileCopyrightText: 2026 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use crossword::grid::Grid;
use jni::errors::Result;
use jni::{bind_java_type, Env};

bind_java_type!(
    pub JPuzzle => re.belv.croiseur.solver.paulgb.Puzzle,
    methods {
        /// Retrieves the 2D array of slots from the `Puzzle` object.
        fn slots() -> jint[][],
    }
);

impl JPuzzle<'_> {
    /// Transforms this `JPuzzle` to a [`Grid`][].
    pub fn into_grid(self, env: &mut Env) -> Result<Grid> {
        let j_array_2d = self.slots(env)?;

        let mut vec_2d: Vec<Vec<usize>> = Vec::new();
        let row_count = j_array_2d.len(env)?;

        for row_index in 0..row_count {
            let row = j_array_2d.get_element(env, row_index)?;

            let mut jint_row = vec![0; row.len(env)?];
            row.get_region(env, 0, jint_row.as_mut_slice())?;

            let usize_row = jint_row
                .into_iter()
                .map(|jint| jint.try_into().expect("Failed to convert jint to usize"))
                .collect();

            vec_2d.push(usize_row);
        }

        Ok(Grid::new(vec_2d))
    }
}
