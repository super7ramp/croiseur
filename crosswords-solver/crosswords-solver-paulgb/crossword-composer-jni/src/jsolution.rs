use jni::JNIEnv;
use jni::objects::{JObject, JValue};
use crate::jarray::JArray;

pub struct JSolution<'a> {
    solution: JObject<'a>,
}

impl<'a> JSolution<'a> {
    fn new(env: JNIEnv<'a>, solution_arg: JArray<'a>) -> Self {
        let class = env.find_class("com/gitlab/super7ramp/crosswords/solver/paulgb/Solution")
            .unwrap();
        Self {
            solution: env.new_object(class, "([C)V", &[solution_arg.as_value()]).unwrap(),
        }
    }

    pub fn from(jni_env: JNIEnv<'a>, chars: Vec<char>) -> Self {
        Self::new(jni_env, JArray::from((jni_env, chars)))
    }

    pub fn as_value(&self) -> JValue<'a> {
        JValue::Object(self.solution)
    }
}