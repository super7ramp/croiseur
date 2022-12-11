use jni::descriptors::Desc;
use jni::JNIEnv;
use jni::objects::{AutoArray, JObject, JString, JValue, ReleaseMode};
use jni::sys::{jlong, jlongArray, jsize};

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
        self.env.get_array_length(self.array.into_raw()).unwrap() as usize
    }

    fn element(&self, index: usize) -> JObject<'a> {
        self.env.get_object_array_element(self.array.into_raw(), index as jsize).unwrap()
    }

    fn long_element(&self, index: usize) -> jlong {
        let long_array: AutoArray<jlong> =
            self.env.get_long_array_elements(self.array.into_raw(),
                                             ReleaseMode::NoCopyBack)
                .expect("Expect a long[], wasn't one.");
        unsafe { *(long_array.as_ptr().offset(index as isize)) }
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
        for i in 0..self.length() {
            let j_long = self.long_element(i);
            let object_long = j_long as usize;
            vec.push(object_long);
        }
        vec
    }
}

impl<'a> Into<Vec<String>> for JArray<'a> {
    fn into(self) -> Vec<String> {
        let mut vec = Vec::new();
        for i in 0..self.length() {
            let java_string = JString::from(self.element(i));
            let java_string_ref = self.env.get_string(java_string).unwrap();
            let rust_string = java_string_ref.into();
            vec.push(rust_string);
        }
        vec
    }
}

// FIXME this doesn't seem to work
impl<'a> From<(JNIEnv<'a>, Vec<char>)> for JArray<'a> {
    fn from(arg: (JNIEnv<'a>, Vec<char>)) -> Self {
        let (jni_env, vec) = arg;
        vec.iter().for_each(|c| print!("{} ", c));
        println!();
        let char_array = jni_env.new_char_array(vec.len() as jsize).unwrap();
        println!("size = {}", vec.len() as jsize);
        Self {
            env: jni_env,
            array: unsafe { JObject::from_raw(char_array) },
        }
    }
}