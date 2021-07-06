package com.ikc.oop.commands

import com.ikc.oop.files.{DirEntry, File}
import com.ikc.oop.filesystem.State

class Touch(name: String) extends CreateEntry(name) {
  override def createSpecificEntry(state: State): DirEntry =
    File.empty(state.wd.path, name)
}
