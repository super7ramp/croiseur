/*
 * SPDX-FileCopyrightText: 2026 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use crate::jdictionary::JDictionary;
use crate::joptional::JOptional;
use crate::jpuzzle::JPuzzle;
use crate::jsolution::JSolution;
use crate::jthread::JThread;
use crossword::solver;
use jni::errors::{Error, ThrowRuntimeExAndDefault};
use jni::objects::JObject;
use jni::{jni_str, Env, EnvUnowned};

mod jdictionary;
mod jiterable;
mod joptional;
mod jpuzzle;
mod jsolution;
mod jthread;

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
pub extern "system" fn Java_re_belv_croiseur_solver_paulgb_Solver_solve<'a>(
    mut env_unowned: EnvUnowned<'a>,
    _java_solver: JObject,
    java_puzzle: JObject,
    java_dictionary: JObject,
) -> JObject<'a> {
    env_unowned
        .with_env(|env| {
            let result = solve(env, java_puzzle, java_dictionary);
            Ok::<JObject<'_>, Error>(result)
        })
        .resolve::<ThrowRuntimeExAndDefault>()
}

/// Where the actual solve job is done.
fn solve<'a>(env: &mut Env<'a>, java_puzzle: JObject, java_dictionary: JObject) -> JObject<'a> {
    let grid = JPuzzle::new(java_puzzle).into_grid(env);
    let dictionary = JDictionary::new(java_dictionary).into_dictionary(env);

    let current_thread = JThread::current_thread(env);
    let mut is_interrupted = || current_thread.is_interrupted(env);
    let result = solver::solve_interruptible(&grid, &dictionary, &mut is_interrupted);

    if is_interrupted() {
        let _ = env.throw_new(
            jni_str!("java/lang/InterruptedException"),
            jni_str!("Solver interrupted"),
        );
        JObject::default()
    } else {
        result
            .map(|chars| JSolution::from(chars, env))
            .map(|solution| JOptional::of(solution.into_object(), env))
            .unwrap_or_else(|| JOptional::empty(env))
            .into_object()
    }
}
