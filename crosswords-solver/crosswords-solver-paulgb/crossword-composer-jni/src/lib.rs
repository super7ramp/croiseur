use jni::JNIEnv;
use jni::objects::{JObject, JValue};
use jni::sys::jvalue;

use crossword::solver;
use crate::jarray::JArray;

use crate::jdictionary::JDictionary;
use crate::jpuzzle::JPuzzle;
use crate::joptional::JOptional;
use crate::jsolution::JSolution;

mod jarray;
mod jdictionary;
mod joptional;
mod jpuzzle;
mod jsolution;

#[no_mangle]
pub extern "system" fn Java_com_gitlab_super7ramp_crosswords_solver_paulgb_Solver_solve<'a>
(env: JNIEnv<'a>, _java_solver: JObject, java_puzzle: JObject, java_dictionary: JObject) -> jvalue {
    let grid = JPuzzle::new(env, java_puzzle).into();
    let dictionary = JDictionary::new(env, java_dictionary).into();

    let result = solver::solve(&grid, &dictionary);

    result
        .map(|chars| JSolution::from(env, chars))
        .map(|solution| JOptional::of(env, solution.as_value()))
        .unwrap_or_else(|| JOptional::empty(env))
        .as_value()
        .to_jni()
}
