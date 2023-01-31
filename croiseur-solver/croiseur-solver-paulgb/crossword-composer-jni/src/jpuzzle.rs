/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use crossword::grid::Grid;
use jni::JNIEnv;
use jni::objects::JObject;

use crate::jarray::JArray;

/// Wrapper for the `com.gitlab.super7ramp.crosswords.solver.paulgb.Puzzle` Java object.
pub struct JPuzzle<'a> {
    /// The [JNI environment](JNIEnv).
    env: JNIEnv<'a>,
    /// The wrapped `Puzzle` Java object.
    puzzle: JObject<'a>,
}

impl<'a> JPuzzle<'a> {
    pub fn new(jni_env: JNIEnv<'a>, puzzle_arg: JObject<'a>) -> Self {
        Self {
            env: jni_env,
            puzzle: puzzle_arg,
        }
    }
}

impl<'a> From<JPuzzle<'a>> for Grid {
    fn from(val: JPuzzle<'a>) -> Self {
        let j_array_2d = val
            .env
            .call_method(val.puzzle, "slots", "()[[I", &[])
            .expect("Failed to access puzzle slots");

        let vec_of_j_array: Vec<JArray> = JArray::new(
            val.env,
            j_array_2d.l().expect("Failed to read puzzle slots"),
        )
        .into();

        let mut vec_2d: Vec<Vec<usize>> = Vec::new();
        vec_of_j_array.iter().for_each(|entry| {
            let value = JArray::new(val.env, entry.as_object()).into();
            vec_2d.push(value);
        });

        Grid::new(vec_2d)
    }
}
