use jni::JNIEnv;
use jni::objects::{JObject, JValue};

use crossword::grid::Grid;
use crate::jarray::JArray;

pub struct JGrid<'a> {
    env: JNIEnv<'a>,
    grid: JObject<'a>,
}

impl<'a> JGrid<'a> {
    pub fn new(jni_env: JNIEnv<'a>, grid_arg: JObject<'a>) -> Self {
        Self {
            env: jni_env,
            grid: grid_arg,
        }
    }
}

impl<'a> Into<Grid> for JGrid<'a> {
    fn into(self) -> Grid {
        let j_array_2d = self.env.call_method(self.grid, "slots", "()[[J", &[])
            .unwrap_or_else(|_e| {
                let _ = self.env.exception_describe();
                JValue::Object(JObject::null())
            });

        let vec_of_j_array: Vec<JArray> = JArray::new(self.env,
                                                      j_array_2d.l().unwrap()).into();

        let mut vec_2d: Vec<Vec<usize>> = Vec::new();
        vec_of_j_array.iter().for_each(|entry | {
            let value = JArray::new(self.env, entry.as_object()).into();
            vec_2d.push(value);
        });

        Grid::new(vec_2d)
    }
}

