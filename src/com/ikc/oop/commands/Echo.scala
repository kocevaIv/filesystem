package com.ikc.oop.commands

import com.ikc.oop.files.{Directory, File}
import com.ikc.oop.filesystem.State

import scala.annotation.tailrec

class Echo(args: List[String]) extends Command {

  override def apply(state: State): State = {
    /*
    if no args return the same state
    else if just one arg, print to console
    else if multiple args
    {
    operator = next to last argument
      if > echo to a file (may create a file if doesn't exist)
      if  >>
     append to a file
     else
     just echo everything to console
    }
     */
    if (args.isEmpty) state
    else if (args.length == 1) state.setMessage(args(0))
    else {
      val operator = args(args.length - 2)
      val fileName = args(args.length - 1)
      val contents = createContent(args, args.length - 2)
      if (operator.equals(">>"))
        doEcho(state, contents, fileName, append = true)
      else if (operator.equals(">"))
        doEcho(state, contents, fileName, append = false)
      else
        state.setMessage(createContent(args, args.length))
    }
  }

  def getRootAfterEcho(currentDirectory: Directory, path: List[String], contents: String, append: Boolean):Directory = {
    if (path.isEmpty) currentDirectory
    else if (path.tail.isEmpty) {
      val dirEntry = currentDirectory.findEntry(path.head)

      if(dirEntry == null)
        currentDirectory.addEntry(new File(currentDirectory.path, path.head, contents))
      else if (dirEntry.isDirectory) currentDirectory
      else if (append) currentDirectory.replaceEntry(path.head, dirEntry.asFile.appendContents(contents))
      else currentDirectory.replaceEntry(path.head, dirEntry.asFile.setContents(contents))

    }
    else {

      val nextDirectory = currentDirectory.findEntry(path.head).asDirectory
      val newNextDirectory = getRootAfterEcho(nextDirectory, path.tail, contents, append)
      if (newNextDirectory == newNextDirectory) currentDirectory
      else currentDirectory.replaceEntry(path.head, newNextDirectory)
    }
  }


  def doEcho(state: State, contents: String, fileName: String, append: Boolean): State = {
      if (fileName.contains(Directory.SEPARATOR))
        state.setMessage("Echo: filename must not contain operators")
    else {
        val newRoot: Directory = getRootAfterEcho(state.root, state.wd.getAllFolderNamesInPath:+ fileName, contents, append)
        if (newRoot == state.root)
          state.setMessage(fileName + ": no such file")
        else
          State(newRoot, newRoot.findDescendant(state.wd.getAllFolderNamesInPath))
      }
  }

  // topIndex NON-INCLUSIVE

  def createContent(args: List[String], topIndex: Int): String = {
    @tailrec
    def createContentHelper(currentIndex: Int, accumulator: String): String = {
      if (currentIndex >= topIndex) accumulator
      else createContentHelper(currentIndex + 1, accumulator + " " + args(currentIndex))
    }

    createContentHelper(0, "")
  }
}
