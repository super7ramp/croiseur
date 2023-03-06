/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use jni::objects::JObject;
use jni::JNIEnv;

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
    pub fn current_thread(env: &mut JNIEnv<'a>) -> Self {
        let clazz = env.find_class("java/lang/Thread").unwrap();
        let current_thread = env
            .call_static_method(clazz, "currentThread", "()Ljava/lang/Thread;", &[])
            .expect("Failed to retrieve current thread")
            .l()
            .expect("Invalid current thread object");
        Self::new(current_thread)
    }

    /// Checks whether this thread is interrupted.
    pub fn is_interrupted(&self, env: &mut JNIEnv) -> bool {
        env.call_method(&self.value, "isInterrupted", "()Z", &[])
            .expect("Failed to retrieve thread interruption status")
            .z()
            .expect("Invalid thread interruption status")
    }
}
