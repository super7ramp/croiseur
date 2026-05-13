/*
 * SPDX-FileCopyrightText: 2026 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use jni::objects::JObject;
use jni::{jni_sig, jni_str, Env};

/// Wrapper for Java `Thread` object.
pub struct JThread<'a> {
    /// The wrapped Java Thread object.
    value: JObject<'a>,
}

impl<'a> JThread<'a> {
    /// Creates a new `JThread` wrapping the given `Thread` Java object.
    fn new(current_thread: JObject<'a>) -> Self {
        JThread {
            value: current_thread,
        }
    }

    /// Creates a new `JThread` wrapping the current Java Thread object.
    pub fn current_thread(env: &mut Env<'a>) -> Self {
        let clazz = env.find_class(jni_str!("java/lang/Thread")).unwrap();
        let current_thread = env
            .call_static_method(
                clazz,
                jni_str!("currentThread"),
                jni_sig!("()Ljava/lang/Thread;"),
                &[],
            )
            .expect("Failed to retrieve current thread")
            .l()
            .expect("Invalid current thread object");
        Self::new(current_thread)
    }

    /// Checks whether this thread is interrupted.
    pub fn is_interrupted(&self, env: &mut Env) -> bool {
        env.call_method(&self.value, jni_str!("isInterrupted"), jni_sig!("()Z"), &[])
            .expect("Failed to retrieve thread interruption status")
            .z()
            .expect("Invalid thread interruption status")
    }
}
