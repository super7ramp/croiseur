/*
 * SPDX-FileCopyrightText: 2023 Antoine Belvire
 * SPDX-License-Identifier: GPL-3.0-or-later
 */

use jni::JNIEnv;
use jni::objects::{AutoArray, JObject, JString, JValue, ReleaseMode};
use jni::sys::{jchar, jint, jsize};

pub struct JArray<'a> {
    env: JNIEnv<'a>,
    array: JObject<'a>,
}

impl<'a> JArray<'a> {
    pub fn new(jni_env: JNIEnv<'a>, value: JObject<'a>) -> Self {
        Self {
            env: jni_env,
            array: value,
        }
    }

    pub fn as_object(&self) -> JObject<'a> {
        self.array
    }

    pub fn as_value(&self) -> JValue<'a> {
        self.as_object().into()
    }

    fn length(&self) -> usize {
        self.env.get_array_length(self.array.into_raw())
            .expect("Failed to get array length") as usize
    }

    fn element(&self, index: usize) -> JObject<'a> {
        self.env.get_object_array_element(self.array.into_raw(), index as jsize)
            .expect("Failed to get object array element")
    }

    fn int_element(&self, index: usize) -> jint {
        let int_array: AutoArray<jint> =
            self.env.get_int_array_elements(self.array.into_raw(),
                                             ReleaseMode::NoCopyBack)
                .expect("Failed to get int array element");
        unsafe { *(int_array.as_ptr().offset(index as isize)) }
    }
}

impl<'a> Into<Vec<JArray<'a>>> for JArray<'a> {
    fn into(self) -> Vec<JArray<'a>> {
        let mut vec = Vec::new();
        for i in 0..self.length() {
            let object = self.element(i);
            let object_array = JArray::new(self.env, object);
            vec.push(object_array);
        }
        vec
    }
}

impl<'a> Into<Vec<usize>> for JArray<'a> {
    fn into(self) -> Vec<usize> {
        let mut vec = Vec::new();
        // FIXME self.int_element do a jni call each time, there must be a better way to do that
        for i in 0..self.length() {
            let j_int = self.int_element(i);
            let r_int = j_int as usize;
            vec.push(r_int);
        }
        vec
    }
}

impl<'a> Into<Vec<String>> for JArray<'a> {
    fn into(self) -> Vec<String> {
        let mut vec = Vec::new();
        for i in 0..self.length() {
            let java_string = JString::from(self.element(i));
            let java_string_ref = self.env.get_string(java_string)
                .expect("Failed to convert JString to JavaStr");
            let rust_string = java_string_ref.into();
            vec.push(rust_string);
        }
        vec
    }
}

impl<'a> From<(JNIEnv<'a>, Vec<char>)> for JArray<'a> {
    fn from(arg: (JNIEnv<'a>, Vec<char>)) -> Self {
        let (jni_env, vec) = arg;
        let length = vec.len() as jsize;

        // Convert to string back to a vector just to have the java char = u16 type
        // FIXME why does solver return lower case characters?
        let pivot_string : String = vec.into_iter().collect::<String>().to_uppercase();
        let j_chars : Vec<jchar> = pivot_string.encode_utf16().collect();
        let char_array = jni_env.new_char_array(length)
            .expect("Failed to create char array");

        jni_env.set_char_array_region(char_array, 0, j_chars.as_slice())
            .expect("Failed to set char array region");

        Self {
            env: jni_env,
            array: unsafe { JObject::from_raw(char_array) },
        }
    }
}