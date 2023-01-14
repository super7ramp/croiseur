/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use jni::JNIEnv;
use jni::objects::JObject;

use crossword::grid::Grid;
use crate::jarray::JArray;

pub struct JPuzzle<'a> {
    env: JNIEnv<'a>,
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

impl<'a> Into<Grid> for JPuzzle<'a> {
    fn into(self) -> Grid {
        let j_array_2d =
            self.env
                .call_method(self.puzzle, "slots", "()[[I", &[])
                .expect("Failed to access puzzle slots");

        let vec_of_j_array: Vec<JArray> =
            JArray::new(self.env, j_array_2d.l().expect("Failed to read puzzle slots"))
                .into();

        let mut vec_2d: Vec<Vec<usize>> = Vec::new();
        vec_of_j_array.iter().for_each(|entry | {
            let value = JArray::new(self.env, entry.as_object()).into();
            vec_2d.push(value);
        });

        Grid::new(vec_2d)
    }
}

