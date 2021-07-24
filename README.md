# Filesystem

A simple bash like cli app build with Scala. The app simulates a bash command line that runs in memory and supports several bash commands 
for handling the file system. 
The user can create files/directories, remove them, navigate throguh the system and view files contents.

## Installation

To run the project you need to have java installed on your machine. Make sure you have the Java 8 JDK or higher.
Then you will also need to install Scala's build tool sbt. 
If you have Intellij IDE you can install the Scala plugin instead.

## Interacting with the app 

While running the app, the commands that are supported are:
- `cd <directory name>`
- `ls `
- `mkdir <directory name>`
- `cat <file name>`
- `echo`
- `echo <some text> > filename`
- `echo <some text> >> filename`
- `touch <file name>`
- `pwd`
- `rm <file name or directory name>`
- `cat <file name>`