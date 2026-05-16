/*
 * SPDX-FileCopyrightText: 2026 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use jni::errors::Result;
use jni::objects::JThread as WrappedJThread;
use jni::{jni_sig, jni_str, Env};

/// Wrapper for Java `Thread` object.
pub struct JThread<'env> {
    /// The wrapped Java Thread object.
    value: WrappedJThread<'env>,
}

impl JThread<'_> {
    /// Creates a new `JThread` wrapping the given `Thread` Java object.
    fn new(value: WrappedJThread) -> JThread {
        JThread { value }
    }

    /// Creates a new `JThread` wrapping the current Java Thread object.
    pub fn current_thread<'env>(env: &mut Env<'env>) -> Result<JThread<'env>> {
        WrappedJThread::current_thread(env).map(Self::new)
    }

    /// Checks whether this thread is interrupted.
    pub fn is_interrupted(&self, env: &mut Env) -> bool {
        env.call_method(&self.value, jni_str!("isInterrupted"), jni_sig!("()Z"), &[])
            .expect("Failed to retrieve thread interruption status")
            .z()
            .expect("Invalid thread interruption status")
    }
}
