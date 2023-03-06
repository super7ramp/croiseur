/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use std::any::Any;
use std::ops::DerefMut;
use std::panic::catch_unwind;

use crossword::dictionary::Dictionary;
use crossword::grid::Grid;
use crossword::solver;
use jni::objects::JObject;
use jni::sys::jobject;
use jni::JNIEnv;

use crate::jdictionary::JDictionary;
use crate::joptional::JOptional;
use crate::jpuzzle::JPuzzle;
use crate::jsolution::JSolution;
use crate::jthread::JThread;

mod jarray;
mod jdictionary;
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
pub extern "system" fn Java_com_gitlab_super7ramp_croiseur_solver_paulgb_Solver_solve<'a>(
    mut env: JNIEnv<'a>,
    _java_solver: JObject,
    java_puzzle: JObject,
    java_dictionary: JObject,
) -> jobject {
    let mut wrapped_env = std::panic::AssertUnwindSafe(&mut env);
    let result = catch_unwind(move || solve(wrapped_env.deref_mut(), java_puzzle, java_dictionary));
    result.unwrap_or_else(|err| handle_panic(env, err))
}

/// Where the actual solve job is done.
fn solve<'a>(env: &mut JNIEnv<'a>, java_puzzle: JObject, java_dictionary: JObject) -> jobject {
    let grid = JPuzzle::new(java_puzzle).into_grid(env);
    let dictionary = JDictionary::new(java_dictionary).into_dictionary(env);

    let current_thread = JThread::current_thread(env);
    let mut is_interrupted = || current_thread.is_interrupted(env);
    let result = solver::solve_interruptible(&grid, &dictionary, &mut is_interrupted);

    if is_interrupted() {
        let _ = env.throw_new("java/lang/InterruptedException", "Solver interrupted");
        JObject::default().into_raw()
    } else {
        result
            .map(|chars| JSolution::from(chars, env))
            .map(|solution| JOptional::of(solution.into_object(), env))
            .unwrap_or_else(|| JOptional::empty(env))
            .into_object()
            .into_raw()
    }
}

/// Handles panic from native code by throwing an appropriate exception to the JVM.
///
/// Inspired by:
/// - The [`catch_panic`](https://github.com/HermitSocialClub/catch_panic) macro
/// - This [blog post](https://engineering.avast.io/scala-and-rust-interoperability-via-jni/)
fn handle_panic(mut env: JNIEnv, err: Box<dyn Any + Send>) -> jobject {
    let error_msg = match err.downcast_ref::<&'static str>() {
        Some(s) => *s,
        None => match err.downcast_ref::<String>() {
            Some(s) => &s[..],
            None => "Unknown error in native code.",
        },
    };
    let _ = env.throw_new(
        "com/gitlab/super7ramp/croiseur/solver/paulgb/NativePanicException",
        error_msg,
    );
    JObject::default().into_raw()
}
