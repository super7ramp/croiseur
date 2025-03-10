// SPDX-FileCopyrightText: 2025 Antoine Belvire
// SPDX-License-Identifier: GPL-3.0-or-later

use anyhow::anyhow;
use extism_pdk::*;
use xwords::crossword::Crossword;
use xwords::fill::filler::Filler;
use xwords::fill::Fill;
use xwords::trie::Trie;

#[derive(serde::Deserialize, FromBytes)]
#[encoding(Json)]
struct Input {
    contents: String,
    width: usize,
    height: usize,
    words: Vec<String>,
}

#[derive(serde::Serialize, ToBytes)]
#[encoding(Json)]
struct Output {
    contents: String,
    width: usize,
    height: usize,
}

#[plugin_fn]
pub fn fill(input: Input) -> FnResult<Output> {
    do_fill(input).map_err(|err| WithReturnCode::from(anyhow!(err)))
}

fn do_fill(mut input: Input) -> Result<Output, String> {
    let crossword = Crossword::rectangle(input.contents, input.width, input.height)?;
    input.words.retain(|word| word.is_ascii());
    let trie = Trie::build(input.words);
    let mut never_interrupt = || false;
    Filler::new(&trie, &mut never_interrupt)
        .fill(&crossword)
        .map(|filled_crossword| Output {
            contents: filled_crossword.to_string(),
            width: input.width,
            height: input.height,
        })
}
