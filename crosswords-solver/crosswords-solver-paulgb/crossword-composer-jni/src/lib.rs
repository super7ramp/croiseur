use jni::JNIEnv;
use jni::objects::{JObject, JValue};
use jni::sys::jvalue;

use crossword::solver;
use crossword::dictionary::Dictionary;
use crossword::grid::Grid;

use crate::jdictionary::JDictionary;
use crate::jgrid::JGrid;
use crate::joptional::JOptional;

mod joptional;
mod jgrid;
mod jdictionary;
mod jobjectarray;

#[no_mangle]
pub extern "system" fn Java_com_gitlab_super7ramp_crosswords_solver_paulgb_Solver_solve<'a>
(env: JNIEnv<'a>, _java_solver: JObject, java_grid: JObject, java_dictionary: JObject) -> jvalue {
    let grid: Grid = JGrid::new(env, java_grid).into();
    let dictionary: Dictionary = JDictionary::new(env, java_dictionary).into();

    let result = solver::solve(&grid, &dictionary);

    let joptional = JOptional::new(env);
    result.map(vec_char_to_jvalue).map(|val| joptional.of(val)).unwrap_or_else(|| joptional.empty())
        .to_jni()
}

fn vec_char_to_jvalue<'a>(vec: Vec<char>) -> JValue<'a> {
    // TODO
    JValue::Object(JObject::null())
}
