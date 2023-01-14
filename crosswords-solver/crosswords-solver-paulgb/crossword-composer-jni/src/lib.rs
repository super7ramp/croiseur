/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use std::any::Any;
use jni::JNIEnv;
use jni::objects::JObject;
use jni::sys::jobject;

use catch_panic::catch_panic;
use crossword::solver;

use crate::jdictionary::JDictionary;
use crate::jpuzzle::JPuzzle;
use crate::joptional::JOptional;
use crate::jsolution::JSolution;

mod jarray;
mod jdictionary;
mod joptional;
mod jpuzzle;
mod jsolution;

#[no_mangle]
#[catch_panic(default = "std::ptr::null_mut()", handler = "panic_handler")]
pub extern "system" fn Java_com_gitlab_super7ramp_crosswords_solver_paulgb_Solver_solve<'a>
(env: JNIEnv<'a>, _java_solver: JObject, java_puzzle: JObject, java_dictionary: JObject) -> jobject {
    let grid = JPuzzle::new(env, java_puzzle).into();
    let dictionary = JDictionary::new(env, java_dictionary).into();

    let result = solver::solve(&grid, &dictionary);

    result
        .map(|chars| JSolution::from(env, chars))
        .map(|solution| JOptional::of(env, solution.as_value()))
        .unwrap_or_else(|| JOptional::empty(env))
        .as_object()
        .into_raw()
}

fn panic_handler(env: JNIEnv, err: Box<dyn Any + Send + 'static>) {
    let msg = match err.downcast_ref::<&'static str>() {
        Some(s) => *s,
        None => match err.downcast_ref::<String>() {
            Some(s) => &s[..],
            None => "this is a certified `std::panic::panic_any` moment",
        },
    };
    env.throw_new("com/gitlab/super7ramp/crosswords/solver/paulgb/SolverErrorException", msg)
        .unwrap();
}