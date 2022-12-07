use jni::JNIEnv;
use jni::objects::{JObject, JValue};

use crossword::grid::Grid;
use crate::jobjectarray::JObjectArray;

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
        let jobjectarray_jobjectarray = self.env.call_method(self.grid, "slots", "()[[J", &[])
            .unwrap_or_else(|e| {
                let _ = self.env.exception_describe();
                JValue::Object(JObject::null())
            });
        let vec_jobjectarray: Vec<JObjectArray> = JObjectArray::new(self.env,
                                                                    jobjectarray_jobjectarray.l().unwrap()).into();
        let mut vec_vec: Vec<Vec<usize>> = Vec::new();
        vec_jobjectarray.iter().for_each(| entry | {
            let value = JObjectArray::new(self.env, entry.raw()).into();
            vec_vec.push(value);
        });

        Grid::new(vec_vec)
    }
}

