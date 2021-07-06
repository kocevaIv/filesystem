package com.ikc.oop.filesystem

import java.util.Scanner

import com.ikc.oop.commands.Command
import com.ikc.oop.files.Directory

object Filesystem extends App {

  val root = Directory.ROOT
  //  var state = State(root, root)
  //  val scanner = new Scanner(System.in)
  //
  //  while (true) {
  //    state.show
  //    val input = scanner.nextLine()
  //    state = Command.from(input).apply(state)
  //    println("The working dir after the command is: " + state.wd.path)
  //  }
  io.Source.stdin.getLines().foldLeft(State(root, root))((currentState, newLine) => {
    currentState.show
    Command.from(newLine).apply(currentState)
  })
}
