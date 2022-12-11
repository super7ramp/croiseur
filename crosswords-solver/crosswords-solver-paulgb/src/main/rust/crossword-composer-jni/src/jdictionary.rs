use jni::JNIEnv;
use jni::objects::{JObject, JValue};
use crossword::dictionary::Dictionary;
use crate::jarray::JArray;

pub struct JDictionary<'a> {
    env: JNIEnv<'a>,
    dic: JObject<'a>
}

impl<'a> JDictionary<'a> {
    pub fn new(jni_env: JNIEnv<'a>, dictionary: JObject<'a>) -> Self {
        Self {
            env: jni_env,
            dic: dictionary
        }
    }
}

impl<'a> Into<Vec<String>> for JDictionary<'a> {
    fn into(self) -> Vec<String> {
        let j_string = self.env.call_method(self.dic, "words", "()[Ljava/lang/String;", &[])
            .unwrap_or_else(|_e| {
                let _ = self.env.exception_describe();
                JValue::Object(JObject::null())
            });
        JArray::new(self.env, j_string.l().unwrap()).into()
    }
}

impl<'a> Into<Dictionary> for JDictionary<'a> {
    fn into(self) -> Dictionary {
        let words = self.into();
        Dictionary::from_vec(words)
    }
}