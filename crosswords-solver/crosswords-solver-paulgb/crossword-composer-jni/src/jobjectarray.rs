use jni::JNIEnv;
use jni::objects::{JObject, JString};
use jni::sys::jobjectArray;

pub struct JObjectArray<'a> {
    env: JNIEnv<'a>,
    array: jobjectArray,
}

impl<'a> JObjectArray<'a> {
    pub(crate) fn new(jni_env: JNIEnv<'a>, value: JObject) -> Self {
        Self {
            env: jni_env,
            array: value.into_raw(),
        }
    }
    pub fn raw(&self) -> JObject<'a> {
        unsafe { JObject::from_raw(self.array) }
    }
    fn length(&self) -> i32 {
        self.env.get_array_length(self.array).unwrap()
    }
}

impl<'a> Into<Vec<JObjectArray<'a>>> for JObjectArray<'a> {
    fn into(self) -> Vec<JObjectArray<'a>> {
        let mut vec = Vec::new();
        for i in 0..self.length() {
            let object = self.env.get_object_array_element(self.array, i).unwrap();
            let object_array = JObjectArray::new(self.env, object);
            vec.push(object_array);
        }
        vec
    }
}

impl<'a> Into<Vec<usize>> for JObjectArray<'a> {
    fn into(self) -> Vec<usize> {
        let mut vec = Vec::new();
        for i in 0..self.length() {
            let object = self.env.get_object_array_element(self.array, i).unwrap();
            let object_long = object.into_raw() as usize;
            vec.push(object_long);
        }
        vec
    }
}

impl<'a> Into<Vec<String>> for JObjectArray<'a> {
    fn into(self) -> Vec<String> {
        let mut vec = Vec::new();
        for i in 0..self.length() {
            let java_string = self.env.get_object_array_element(self.array, i).map(JString::from)
                .unwrap();
            let java_string_ref = self.env.get_string(java_string).unwrap();
            let rust_string = java_string_ref.into();
            vec.push(rust_string);
        }
        vec
    }
}