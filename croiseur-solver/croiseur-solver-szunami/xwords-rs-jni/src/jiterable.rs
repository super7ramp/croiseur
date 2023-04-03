/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use std::marker::PhantomData;

use jni::errors::Result;
use jni::JNIEnv;
use jni::objects::{AutoLocal, JMethodID, JObject};
use jni::signature::{Primitive, ReturnType};

/// Wrapper for `JObject`s that implement `java/lang/Iterable`. Provides methods to get
/// an iterator to iterate over iterable elements.
///
/// Looks up the class and method ids on creation rather than for every method
/// call.
pub struct JIterable<'other_local_1: 'obj_ref, 'obj_ref> {
    internal: &'obj_ref JObject<'other_local_1>,
    iterator: JMethodID,
}

impl<'other_local_1: 'obj_ref, 'obj_ref> AsRef<JIterable<'other_local_1, 'obj_ref>>
for JIterable<'other_local_1, 'obj_ref>
{
    fn as_ref(&self) -> &JIterable<'other_local_1, 'obj_ref> {
        self
    }
}

impl<'other_local_1: 'obj_ref, 'obj_ref> AsRef<JObject<'other_local_1>>
for JIterable<'other_local_1, 'obj_ref>
{
    fn as_ref(&self) -> &JObject<'other_local_1> {
        self.internal
    }
}

impl<'other_local_1: 'obj_ref, 'obj_ref> JIterable<'other_local_1, 'obj_ref> {
    /// Create an iterable from the environment and an object. This looks up the
    /// necessary class and method ids to call all of the methods on it so that
    /// extra work doesn't need to be done on every method call.
    pub fn from_env(
        env: &mut JNIEnv,
        obj: &'obj_ref JObject<'other_local_1>,
    ) -> Result<JIterable<'other_local_1, 'obj_ref>> {
        let class = AutoLocal::new(env.find_class("java/lang/Iterable")?, env);

        let iterator = env.get_method_id(&class, "iterator", "()Ljava/util/Iterator;")?;

        Ok(JIterable {
            internal: obj,
            iterator,
        })
    }

    /// Get element iterator for the iterable.
    ///
    /// The returned iterator does not implement [`Iterator`] and
    /// cannot be used with a `for` loop. This is because its `next` method
    /// uses a `&mut JNIEnv` to call the Java iterator. Use a `while let` loop
    /// instead:
    ///
    /// ```rust,no_run
    /// # use jni::{errors::Result, JNIEnv, objects::{AutoLocal, JIterable, JObject}};
    /// #
    /// # fn example(env: &mut JNIEnv, iterable: JIterable) -> Result<()> {
    /// let mut iterator = iterable.iter(env)?;
    ///
    /// while let Some(element) = iterator.next(env)? {
    ///     let element: AutoLocal<JObject> = env.auto_local(element);
    ///
    ///     // Do something with `element` here
    /// }
    /// # Ok(())
    /// # }
    /// ```
    ///
    /// Each call to `next` creates two new local references. To prevent
    /// excessive memory usage or overflow error, the local references should
    /// be deleted using [`JNIEnv::delete_local_ref`] or [`JNIEnv::auto_local`]
    /// before the next loop iteration. Alternatively, if the iterable is known to
    /// have a small, predictable size, the loop could be wrapped in
    /// [`JNIEnv::with_local_frame`] to delete all of the local references at
    /// once.
    pub fn iter<'iterable, 'iter_local>(
        &'iterable self,
        env: &mut JNIEnv<'iter_local>,
    ) -> Result<JIter<'iterable, 'other_local_1, 'obj_ref, 'iter_local>> {
        let iter_class = AutoLocal::new(env.find_class("java/util/Iterator")?, env);

        let has_next = env.get_method_id(&iter_class, "hasNext", "()Z")?;

        let next = env.get_method_id(&iter_class, "next", "()Ljava/lang/Object;")?;

        let iter = AutoLocal::new(
            unsafe {
                env.call_method_unchecked(self.internal, self.iterator, ReturnType::Object, &[])
            }?
                .l()?,
            env,
        );

        Ok(JIter {
            _phantom_iterable: PhantomData,
            has_next,
            next,
            iter,
        })
    }
}

/// An iterator over the elements of an iterable. See [`JIterable::iter`] for more
/// information.
pub struct JIter<'iterable, 'other_local_1: 'obj_ref, 'obj_ref, 'iter_local> {
    _phantom_iterable: PhantomData<&'iterable JIterable<'other_local_1, 'obj_ref>>,
    has_next: JMethodID,
    next: JMethodID,
    iter: AutoLocal<'iter_local, JObject<'iter_local>>,
}

impl<'iterable, 'other_local_1: 'obj_ref, 'obj_ref, 'iter_local>
JIter<'iterable, 'other_local_1, 'obj_ref, 'iter_local>
{
    /// Advances the iterator and returns the next element in the
    /// `java.lang.Iterable`, or `None` if there are no more objects.
    ///
    /// See [`JIterable::iter`] for more information.
    ///
    /// This method creates two new local references. To prevent excessive
    /// memory usage or overflow error, the local references should be deleted
    /// using [`JNIEnv::delete_local_ref`] or [`JNIEnv::auto_local`] before the
    /// next loop iteration. Alternatively, if the iterable is known to have a
    /// small, predictable size, the loop could be wrapped in
    /// [`JNIEnv::with_local_frame`] to delete all of the local references at
    /// once.
    ///
    /// This method returns:
    ///
    /// * `Ok(Some(_))`: if there was another element in the iterable.
    /// * `Ok(None)`: if there are no more element in the iterable.
    /// * `Err(_)`: if there was an error calling the Java method to
    ///   get the next element.
    ///
    /// This is like [`Iterator::next`], but requires a parameter of
    /// type `&mut JNIEnv` in order to call into Java.
    pub fn next<'other_local_2>(
        &mut self,
        env: &mut JNIEnv<'other_local_2>,
    ) -> Result<Option<JObject<'other_local_2>>> {

        let has_next = unsafe {
            env.call_method_unchecked(
                &self.iter,
                self.has_next,
                ReturnType::Primitive(Primitive::Boolean),
                &[],
            )
        }?
            .z()?;

        if !has_next {
            return Ok(None);
        }
        let next =
            unsafe { env.call_method_unchecked(&self.iter, self.next, ReturnType::Object, &[]) }?
                .l()?;

        Ok(Some(next))
    }
}
