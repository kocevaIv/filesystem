package com.ikc.oop.commands

import com.ikc.oop.files.{DirEntry, Directory}
import com.ikc.oop.filesystem.State

class Mkdir(name: String) extends CreateEntry(name) {

  override def createSpecificEntry(state: State): DirEntry = {
    Directory.empty(state.wd.path, name)
  }
}
