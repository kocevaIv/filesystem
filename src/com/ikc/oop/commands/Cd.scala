package com.ikc.oop.commands

import com.ikc.oop.files.{DirEntry, Directory}
import com.ikc.oop.filesystem.State

import scala.annotation.tailrec

class Cd(dir: String) extends Command {

  override def apply(state: State): State = {

    /*
    cd /something/somethingElse/.../ - absolute path
    cd a/b/c = relative to the current working directory
     */

    // 1.find the root
    val root = state.root
    val wd = state.wd

    // 2.find the absolute path of the directory I want to cd to
    val absolutePath =
      if (dir.startsWith(Directory.SEPARATOR)) dir
      else if (wd.isRoot) {
        wd.path + dir
      }
      else {
        wd.path + Directory.SEPARATOR + dir
      }


    // 3.find the directory to cd to, given the path
    val destinationDirectory = doFindEntry(root, absolutePath)
    // 4.change the state given the new directory
    // checks if the destination is a directory or not
    if (destinationDirectory == null || !destinationDirectory.isDirectory)
      state.setMessage(s"$dir : no such directory")
    else State(root, destinationDirectory.asDirectory)

  }

  def doFindEntry(root: Directory, path: String): DirEntry = {

    @tailrec
    def findEntryHelper(currentDirectory: Directory, path: List[String]): DirEntry =
      if (path.isEmpty || path.head.isEmpty) currentDirectory
      else if (path.tail.isEmpty) currentDirectory.findEntry(path.head)
      else {
        val nextDir = currentDirectory.findEntry(path.head)
        if (nextDir == null || !nextDir.isDirectory) null
        else findEntryHelper(nextDir.asDirectory, path.tail)
      }

    @tailrec
    def collapseRelativeTokens(path: List[String], result: List[String]): List[String] = {
      /*
      /a/b => ["a", "b"]
      path.isEmpty?
      result = List :+ "a" = ["a"]

      /a/.. => ["a", ".."]
      path.isEmpty?
      cRT(["..", "a"]) =>
       */

      if (path.isEmpty) result
      else if (".".equals(path.head)) collapseRelativeTokens(path.tail, result)
      else if ("..".equals(path.head)) {
        if (result.isEmpty) null
        else collapseRelativeTokens(path.tail, result.init)
      }
      else collapseRelativeTokens(path.tail, result :+ path.head)
    }

    // 1.tokens
    val tokens: List[String] = path.substring(1).split(Directory.SEPARATOR).toList

    // eliminate/collapse relative tokens
    /*
    /a => ["a", "."] => ["a"]
    /a/b/./. => ["a", "b", ".", "."] => ["a", "b"]

    /a/../ => ["a", ".."] => [] (the .. will move us to the parent of a)
    /a/b/.. => ["a", "b", ".."] => ["a"]

     */
    val newTokens = collapseRelativeTokens(tokens, List())
    if (newTokens == null) null
    // 2.navigate to the correct entry
    else findEntryHelper(root, newTokens)
  }
}
