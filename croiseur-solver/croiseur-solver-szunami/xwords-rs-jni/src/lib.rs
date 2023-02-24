/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use std::any::Any;
use std::ops::DerefMut;
use std::panic::catch_unwind;

use jni::JNIEnv;
use jni::objects::JObject;
use jni::sys::jobject;
use xwords::fill::Fill;
use xwords::fill::filler::Filler;

use crate::jcrossword::JCrossword;
use crate::jdictionary::JDictionary;
use crate::jresult::JResult;

mod jcrossword;
mod jdictionary;
mod jresult;

///
/// Solves the given puzzle with the given dictionary. Implements Java's `Filler#fill()` native
/// method.
///
/// # Arguments
/// * `env`: The [JNI environment](JNIEnv).
/// * `_java_filler`: The corresponding Java `Filler`. Unused, just here to respect the JNI.
/// * `java_crossword`: The crossword puzzle. See the `Crossword` class on Java side.
/// * `java_dictionary`: The dictionary. See the `Dictionary` class on Java side.
///
/// # Returns
///
/// * The `Result` Java object (see Java side).
///
#[no_mangle]
pub extern "system" fn Java_com_gitlab_super7ramp_croiseur_solver_szunami_Filler_fill<'a>(
    mut env: JNIEnv<'a>,
    _java_filler: JObject,
    java_crossword: JObject<'a>,
    java_dictionary: JObject,
) -> jobject {
    let mut wrapped_env = std::panic::AssertUnwindSafe(&mut env);
    let result =
        catch_unwind(move || solve(wrapped_env.deref_mut(), java_crossword, java_dictionary));
    result.unwrap_or_else(|err| handle_panic(env, err))
}

/// Where the actual solve job is done.
fn solve<'a>(
    env: &mut JNIEnv<'a>,
    java_crossword: JObject<'a>,
    java_dictionary: JObject,
) -> jobject {
    let crossword = JCrossword::new(java_crossword).into_crossword(env);
    let trie = JDictionary::new(java_dictionary).into_trie(env);

    let result = Filler::new(&trie).fill(&crossword);

    result
        .map(|solution| JResult::ok(solution, env))
        .unwrap_or_else(|err| JResult::err(err.as_str(), env))
        .into_object()
        .into_raw()
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
        "com/gitlab/super7ramp/croiseur/solver/szunami/NativePanicException",
        error_msg,
    );
    JObject::default().into_raw()
}
