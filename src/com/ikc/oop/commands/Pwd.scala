package com.ikc.oop.commands

import com.ikc.oop.filesystem.State

class Pwd extends Command {

  override def apply(state: State): State =
    state.setMessage(state.wd.path)
}
