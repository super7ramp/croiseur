/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use std::cell::RefCell;
use std::rc::Rc;

use crossword::solver;
use jni::JNIEnv;
use jni::objects::JObject;
use jni::sys::jobject;

use crate::jdictionary::JDictionary;
use crate::joptional::JOptional;
use crate::jpuzzle::JPuzzle;
use crate::jsolution::JSolution;

mod jarray;
mod jdictionary;
mod joptional;
mod jpuzzle;
mod jsolution;

///
/// Solves the given puzzle with the given dictionary. Implements Java's `Solver#solve()` native
/// method.
///
/// # Arguments
/// * `env`: The [JNI environment](JNIEnv).
/// * `_java_solver`: The corresponding Java `Solver`. Unused, just here to respect the JNI.
/// * `java_puzzle`: The crossword puzzle. See the `Puzzle` class on Java side.
/// * `java_dictionary`: The dictionary. See the `Dictionary` class on Java side.
///
/// # Returns
///
/// * The `Solution` Java object (see Java side).
///
#[no_mangle]
pub extern "system" fn Java_com_gitlab_super7ramp_croiseur_solver_paulgb_Solver_solve<'a>(
    env: JNIEnv<'a>,
    _java_solver: JObject,
    java_puzzle: JObject,
    java_dictionary: JObject,
) -> jobject {
    let shared_env = Rc::new(RefCell::new(env));

    let grid = JPuzzle::new(Rc::clone(&shared_env), java_puzzle).into();
    let dictionary = JDictionary::new(Rc::clone(&shared_env), java_dictionary).into();

    let result = solver::solve(&grid, &dictionary);

    result
        .map(|chars| JSolution::from(Rc::clone(&shared_env), chars))
        .map(|solution| JOptional::of(Rc::clone(&shared_env), solution.unwrap_object()))
        .unwrap_or_else(|| JOptional::empty(Rc::clone(&shared_env)))
        .unwrap_object()
        .into_raw()
}
