package com.ikc.oop.commands

import com.ikc.oop.filesystem.State


trait Command extends (State => State) {
}

object Command {

  val MKDIR = "mkdir"
  val LS = "ls"
  val PWD = "pwd"
  val TOUCH = "touch"
  val CD = "cd"
  val RM = "rm"
  val ECHO = "echo"
  val CAT = "cat"

  def emptyCommand: Command = new Command {
    override def apply(state: State): State = state
  }

  def incompleteCommand(name: String): Command = new Command {
    override def apply(state: State): State =
      state.setMessage(name + " incomplete command!")
  }

  def from(input: String): Command = {
    val tokens: Array[String] = input.split(" ")
    if (input.isEmpty || tokens.isEmpty) emptyCommand

      // TODO use pattern matching
    else if (tokens(0).equals(MKDIR)) {
      if (tokens.length < 2) incompleteCommand(MKDIR)
      else
        new Mkdir(tokens(1))
    }
    else if (tokens(0).equals(LS)) {
      new Ls
    }
    else if (tokens(0).equals(PWD)) {
      new Pwd
    }
    else if (tokens(0).equals(TOUCH)) {
      if (tokens.length < 2) incompleteCommand(TOUCH)
      else new Touch(tokens(1))
    }
    else if (tokens(0).equals(CD)) {
      if (tokens.length < 2) incompleteCommand(CD)
      else new Cd(tokens(1))
    }
    else if (tokens(0).equals(RM)) {
      if (tokens.length < 2) incompleteCommand(RM)
      else new Rm(tokens(1))
    }
      else if (tokens(0).equals(ECHO)) {
      if (tokens.length < 2) incompleteCommand(ECHO)
      else new Echo(tokens.tail.toList)
    }
      else if (tokens(0).equals(CAT)) {
      if (tokens.length < 2) incompleteCommand(CAT)
      else new Cat(tokens(1))
    }
    else new UnknownCommand
  }
}