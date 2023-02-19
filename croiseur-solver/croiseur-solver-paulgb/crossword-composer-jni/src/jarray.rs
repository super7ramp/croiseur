/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use std::cell::RefCell;
use std::ops::Deref;
use std::rc::Rc;

use jni::JNIEnv;
use jni::objects::{JIntArray, JObject, JObjectArray, ReleaseMode};
use jni::sys::{jchar, jsize};

/// Wrapper for `J{Char,Int,...,Object}Array`s.
///
/// Allows to introduce some conversion methods that are not present in the `jni` crate. In
/// particular, it eases a bit the retrieval of the values. No method seems to exist on
/// `JNIEnv` for that (`get_array_elements` is for primitive types).
pub struct JArray<'a> {
    /// The [JNI environment](JNIEnv).
    env: Rc<RefCell<JNIEnv<'a>>>,
    /// The wrapped Java array.
    array: JObject<'a>,
}

impl<'a> JArray<'a> {
    pub fn new(jni_env: Rc<RefCell<JNIEnv<'a>>>, value: JObject<'a>) -> Self {
        Self {
            env: jni_env,
            array: value,
        }
    }

    pub fn unwrap_object(self) -> JObject<'a> {
        self.array
    }

    pub fn from(jni_env: Rc<RefCell<JNIEnv<'a>>>, vec: Vec<char>) -> Self {
        let length = vec.len() as jsize;

        // Convert to string back to a vector just to have the java char = u16 type
        // FIXME we shouldn't do that. Why does solver return lower case characters?
        let pivot_string: String = vec.into_iter().collect::<String>().to_uppercase();
        let j_chars: Vec<jchar> = pivot_string.encode_utf16().collect();
        let char_array = jni_env
            .borrow()
            .new_char_array(length)
            .expect("Failed to create char array");

        jni_env
            .borrow()
            .set_char_array_region(&char_array, 0, j_chars.as_slice())
            .expect("Failed to set char array region");

        Self {
            env: jni_env,
            array: JObject::from(char_array),
        }
    }

    fn length(&self) -> usize {
        let array = self.as_object_array();
        self.env
            .borrow()
            .get_array_length(&array)
            .expect("Failed to get array length") as usize
    }

    fn element(&self, index: usize) -> JObject<'a> {
        let array = self.as_object_array();
        self.env
            .borrow_mut()
            .get_object_array_element(&array, index as jsize)
            .expect("Failed to get object array element")
    }

    fn as_object_array(&self) -> JObjectArray {
        JObjectArray::from(unsafe { JObject::from_raw(*self.array.deref()) })
    }
}

impl<'a> From<JArray<'a>> for Vec<String> {
    fn from(wrapper: JArray<'a>) -> Self {
        let mut vec = Vec::new();
        let length = wrapper.length();
        for i in 0..length {
            let word = wrapper.element(i).into();
            let java_string = wrapper
                .env
                .borrow_mut()
                .get_string(&word)
                .expect("Failed to convert JString to JavaStr");
            let rust_string = java_string.into();
            vec.push(rust_string);
        }
        vec
    }
}

impl<'a> From<JArray<'a>> for Vec<usize> {
    fn from(value: JArray<'a>) -> Self {
        let int_array = JIntArray::from(value.array);
        let elements = unsafe {
            value
                .env
                .borrow_mut()
                .get_array_elements(&int_array, ReleaseMode::NoCopyBack)
        }
            .expect("Failed to retrieve integer elements");

        elements
            .iter()
            .map(|element| {
                usize::try_from(element.to_owned()).expect("Failed to convert jint to usize")
            })
            .collect()
    }
}

impl<'a> From<JArray<'a>> for Vec<JArray<'a>> {
    fn from(value: JArray<'a>) -> Self {
        let mut vec = Vec::new();
        let length = value.length();
        for i in 0..length {
            let object = value.element(i);
            let object_array = JArray::new(Rc::clone(&value.env), object);
            vec.push(object_array);
        }
        vec
    }
}
