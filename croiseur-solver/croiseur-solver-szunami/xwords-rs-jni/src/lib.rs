/*
 * SPDX-FileCopyrightText: 2026 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use jni::errors::{Result, ThrowRuntimeExAndDefault};
use jni::objects::JObject;
use jni::{jni_str, Env, EnvUnowned};
use xwords::fill::filler::Filler;
use xwords::fill::Fill;

use crate::jcrossword::JCrossword;
use crate::jdictionary::JDictionary;
use crate::jresult::JResult;
use crate::jthread::JThread;

mod jcrossword;
mod jdictionary;
mod jiterable;
mod jresult;
mod jthread;

///
/// Solves the given puzzle with the given dictionary. Implements Java's `Filler#fill()` native
/// method.
///
/// # Arguments
/// * `env`: The [JNI environment](EnvUnowned).
/// * `_java_filler`: The corresponding Java `Filler`. Unused, just here to respect the JNI.
/// * `java_crossword`: The crossword puzzle. See the `Crossword` class on Java side.
/// * `java_dictionary`: The dictionary. See the `Dictionary` class on Java side.
///
/// # Returns
///
/// * The `Result` Java object (see Java side).
///
#[no_mangle]
pub extern "system" fn Java_re_belv_croiseur_solver_szunami_Filler_fill<'a>(
    mut env_unowned: EnvUnowned<'a>,
    _java_filler: JObject,
    java_crossword: JObject<'a>,
    java_dictionary: JObject,
) -> JObject<'a> {
    env_unowned
        .with_env(|env| solve(env, java_crossword, java_dictionary))
        .resolve::<ThrowRuntimeExAndDefault>()
}

/// Where the actual solve job is done.
fn solve<'a>(
    env: &mut Env<'a>,
    java_crossword: JObject<'a>,
    java_dictionary: JObject,
) -> Result<JObject<'a>> {
    let crossword = JCrossword::new(java_crossword).into_crossword(env)?;
    let trie = JDictionary::new(java_dictionary).into_trie(env)?;

    let current_thread = JThread::current_thread(env)?;
    let mut is_interrupted = || current_thread.is_interrupted(env);
    let result = Filler::new(&trie, &mut is_interrupted).fill(&crossword);

    if is_interrupted() {
        env.throw_new(
            jni_str!("java/lang/InterruptedException"),
            jni_str!("Filler interrupted"),
        )
        .map(|_| JObject::default())
    } else {
        result
            .map(|solution| JResult::ok(solution, env))
            .unwrap_or_else(|err| JResult::err(err.as_str(), env))
            .map(JResult::into_object)
    }
}
