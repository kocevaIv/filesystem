package com.ikc.oop.commands

import com.ikc.oop.filesystem.State

class Cat(filename: String) extends Command {

  override def apply(state: State): State = {
    val wd = state.wd

    val dirEntry = wd.findEntry(filename)
    if (!dirEntry.isFile || dirEntry == null)
      state.setMessage(filename + ": no such file")
    else
      state.setMessage(dirEntry.asFile.contents)
  }
}
