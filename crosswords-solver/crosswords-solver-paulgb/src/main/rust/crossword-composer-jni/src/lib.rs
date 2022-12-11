use jni::JNIEnv;
use jni::objects::{JObject, JValue};
use jni::sys::jvalue;

use crossword::solver;
use crossword::dictionary::Dictionary;
use crossword::grid::Grid;
use crate::jarray::JArray;

use crate::jdictionary::JDictionary;
use crate::jgrid::JGrid;
use crate::joptional::JOptional;

mod joptional;
mod jgrid;
mod jdictionary;
mod jarray;

#[no_mangle]
pub extern "system" fn Java_com_gitlab_super7ramp_crosswords_solver_paulgb_Solver_solve<'a>
(env: JNIEnv<'a>, _java_solver: JObject, java_grid: JObject, java_dictionary: JObject) -> jvalue {
    let grid = JGrid::new(env, java_grid).into();
    let dictionary = JDictionary::new(env, java_dictionary).into();

    let result = solver::solve(&grid, &dictionary);

    result
        .map(|chars| JArray::from((env, chars)))
        .map(|val| JOptional::of(env, JValue::Object(val.as_object())))
        .unwrap_or_else(|| JOptional::empty(env))
        .as_value()
        .to_jni()
}
