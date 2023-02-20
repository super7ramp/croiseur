/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use std::ops::Deref;

use jni::JNIEnv;
use jni::objects::{JIntArray, JObject, JObjectArray, ReleaseMode};
use jni::sys::{jchar, jsize};

/// Wrapper for `J{Char,Int,...,Object}Array`s.
///
/// Allows to introduce some conversion methods that are not present in the `jni` crate. In
/// particular, it eases a bit the retrieval of the values. No method seems to exist on
/// `JNIEnv` for that (`get_array_elements` is for primitive types).
pub struct JArray<'a> {
    /// The wrapped Java array.
    array: JObject<'a>,
}

impl<'a> JArray<'a> {
    pub fn new(value: JObject<'a>) -> Self {
        Self { array: value }
    }

    pub fn from_vec_chars(vec: Vec<char>, env: &mut JNIEnv<'a>) -> Self {
        let length = vec.len() as jsize;

        // Convert to string back to a vector just to have the java char = u16 type
        // FIXME we shouldn't do that. Why does solver return lower case characters?
        let pivot_string: String = vec.into_iter().collect::<String>().to_uppercase();
        let j_chars: Vec<jchar> = pivot_string.encode_utf16().collect();
        let char_array = env
            .new_char_array(length)
            .expect("Failed to create char array");

        env.set_char_array_region(&char_array, 0, j_chars.as_slice())
            .expect("Failed to set char array region");

        Self {
            array: JObject::from(char_array),
        }
    }

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

    pub fn into_vec_string(self, env: &mut JNIEnv<'a>) -> Vec<String> {
        let mut vec = Vec::new();
        let length = self.length(env);
        for i in 0..length {
            let word = self.element(i, env).into();
            let java_string = env
                .get_string(&word)
                .expect("Failed to convert JString to JavaStr");
            let rust_string = java_string.into();
            vec.push(rust_string);
        }
        vec
    }

    pub fn into_vec_usize(self, env: &mut JNIEnv) -> Vec<usize> {
        let int_array = JIntArray::from(self.array);
        let elements = unsafe { env.get_array_elements(&int_array, ReleaseMode::NoCopyBack) }
            .expect("Failed to retrieve integer elements");

        elements
            .iter()
            .map(|element| {
                usize::try_from(element.to_owned()).expect("Failed to convert jint to usize")
            })
            .collect()
    }

    pub fn into_object(self) -> JObject<'a> {
        self.array
    }

    fn as_object_array(&self) -> JObjectArray {
        JObjectArray::from(unsafe { JObject::from_raw(*self.array.deref()) })
    }

    fn element(&self, index: usize, env: &mut JNIEnv<'a>) -> JObject<'a> {
        let array = self.as_object_array();
        env.get_object_array_element(&array, index as jsize)
            .expect("Failed to get object array element")
    }

    fn length(&self, env: &JNIEnv) -> usize {
        let array = self.as_object_array();
        env.get_array_length(&array)
            .expect("Failed to get array length") as usize
    }
}
