package com.ikc.oop.commands

import com.ikc.oop.filesystem.State

class UnknownCommand extends Command {

  override def apply(state: State): State =
    state.setMessage("Command not found!")
}
