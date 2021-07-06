package com.ikc.oop.commands

import com.ikc.oop.files.{DirEntry, Directory}
import com.ikc.oop.filesystem.State

abstract class CreateEntry(entryName: String) extends Command {

  override def apply(state: State): State = {
    val wd = state.wd
    if (wd.hasEntry(entryName)) {
      state.setMessage(s"Entry $entryName already exists")
    } else if (entryName.contains(Directory.SEPARATOR)) {
      // creating a directory with mkdir command like: mkdir dir1/dir2 won't be allowed
      state.setMessage(s"$entryName must not contain separators!")
    } else if (checkIllegal(entryName)) {
      state.setMessage(s"$entryName: Illegal entry name!")
    } else {
      doCreateEntry(state, entryName)
    }
  }

  def checkIllegal(name: String): Boolean = {
    name.contains(".")
  }

  def doCreateEntry(state: State, name: String): State = {

    def updateStructure(currentDirectory: Directory, path: List[String], newEntry: DirEntry): Directory = {
      if (path.isEmpty) {
        currentDirectory.addEntry(newEntry)
      }
      else {
        val oldEntry = currentDirectory.findEntry(path.head).asDirectory
        currentDirectory.replaceEntry(oldEntry.name, updateStructure(oldEntry, path.tail, newEntry))
      }
    }

    val wd = state.wd

    // 1. all the directories in the full path
    val allDirsInPath = wd.getAllFolderNamesInPath

    // 2. create new directory entry in the working directory
    val newEntry: DirEntry = createSpecificEntry(state)

    // 3. update the whole directory structure starting from the root (the directory structure is IMMUTABLE)
    val newRoot = updateStructure(state.root, allDirsInPath, newEntry)

    // 4. find new working directory INSTANCE given working directory full path, in the NEW directory structure
    val newWd = newRoot.findDescendant(allDirsInPath)

    State(newRoot, newWd)
  }

  def createSpecificEntry(state: State): DirEntry
}
