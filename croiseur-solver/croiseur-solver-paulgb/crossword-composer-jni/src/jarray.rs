/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use jni::objects::{JIntArray, JObject, JObjectArray, JString};
use jni::sys::jchar;
use jni::JNIEnv;

/// Wrapper for `J{Char,Int,...,Object}Array`s.
///
/// Provides conversion methods for convenience.
pub struct JArray<'a> {
    /// The wrapped Java array.
    array: JObject<'a>,
}

impl<'a> JArray<'a> {
    /// Creates new `JArray` wrapping the given `JObject`.
    pub fn new(value: JObject<'a>) -> Self {
        Self { array: value }
    }

    /// Creates a new `JArray` from the given vector of `char`s.
    pub fn from_vec_chars(vec: Vec<char>, env: &mut JNIEnv<'a>) -> Self {
        let length = vec
            .len()
            .try_into()
            .expect("Failed to convert usize (u32 or u64) to jsize (i32)");

        // Convert to string back to a vector just to have the java char = u16 type
        // FIXME we shouldn't do that. Why does solver return lower case characters?
        let pivot_string: String = vec.into_iter().collect::<String>().to_uppercase();
        let j_chars: Vec<jchar> = pivot_string.encode_utf16().collect();
        let char_array = env
            .new_char_array(length)
            .expect("Failed to create char array");

        env.set_char_array_region(&char_array, 0, j_chars.as_slice())
            .expect("Failed to set char array region");

        Self::new(JObject::from(char_array))
    }

    /// Transforms this `JArray` into a vector of `JArray`s.
    ///
    /// Useful for 2-Dimensional arrays.
    pub fn into_vec_jarray(self, env: &mut JNIEnv<'a>) -> Vec<JArray<'a>> {
        let mut vec = Vec::new();
        let length = self.length(env);
        for i in 0..length {
            let object = self.element(i, env);
            let object_array = JArray::new(object);
            vec.push(object_array);
        }
        vec
    }

    /// Transforms this `JArray` into a vector of `String`s.
    pub fn into_vec_string(self, env: &mut JNIEnv<'a>) -> Vec<String> {
        let mut vec = Vec::new();
        let length = self.length(env);
        for i in 0..length {
            let j_string = self.element(i, env).into();
            let rust_string = Self::rust_string_from(&j_string, env);
            vec.push(rust_string);
            env.delete_local_ref(j_string)
                .expect("Failed to delete local ref")
        }
        vec
    }

    /// Transforms this `JArray` into a vector of `usize`.
    pub fn into_vec_usize(self, env: &mut JNIEnv) -> Vec<usize> {
        let length = self.length(env);
        let mut elements = vec![0; length];
        let int_array = JIntArray::from(self.array);
        env.get_int_array_region(int_array, 0, elements.as_mut_slice())
            .expect("Failed to retrieve integer elements from array");

        elements
            .into_iter()
            .map(|element| {
                element
                    .try_into()
                    .expect("Failed to convert jint (i32) to usize (u32 or u64)")
            })
            .collect()
    }

    /// Unwraps the underlying `JObject`.
    pub fn into_object(self) -> JObject<'a> {
        self.array
    }

    /// Represents `self.array` as a `JObjectArray`. For internal use only.
    fn as_object_array(&self) -> JObjectArray {
        unsafe { JObjectArray::from_raw(self.array.as_raw()) }
    }

    /// Returns the array element at given index.
    fn element(&self, index: usize, env: &mut JNIEnv<'a>) -> JObject<'a> {
        let array = self.as_object_array();
        let j_index = index
            .try_into()
            .expect("Failed to convert usize (u32 or u64) to jsize (i32)");
        env.get_object_array_element(&array, j_index)
            .expect("Failed to get object array element")
    }

    /// Returns the length of this array.
    fn length(&self, env: &JNIEnv) -> usize {
        let array = self.as_object_array();
        let length = env
            .get_array_length(&array)
            .expect("Failed to get array length");
        length
            .try_into()
            .expect("Failed to convert jsize (i32) to usize (u32 or u64)")
    }

    /// Converts a `JString` into a `String`.
    fn rust_string_from(j_string: &JString, env: &mut JNIEnv) -> String {
        unsafe {
            // Use unchecked flavour of get_string() for performance reason. Also, safe
            // get_string() seems to create local references behind the hood so it is not very
            // practical when called in a loop.
            env.get_string_unchecked(j_string)
                .expect("Failed to convert JObject to String")
                .into()
        }
    }
}
