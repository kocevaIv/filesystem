package com.ikc.oop.files

abstract class DirEntry(val parentPath: String, val name: String) {

  def path: String = {
    val separatorIfNecessary = if (Directory.ROOT_PATH.equals(parentPath)) ""
    else Directory.SEPARATOR
    parentPath + separatorIfNecessary + name
  }

  def asDirectory: Directory

  def getType: String

  def asFile: File

  def isDirectory: Boolean

  def isFile: Boolean
}
