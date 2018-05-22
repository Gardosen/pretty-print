# PrettyPrint

## Description

Pretty Print is a java based library which helps on printing out information into the console or a file in a formated way.

the following possiblities do exist in PrettyPrint

  - write information to log files (default logfile or a custom one can be addressed)
  - write information to the console in default colors
  - write information to the console colored
  - run pretty print in silent mode

## How to use Pretty Print

to use pretty print the library only needs to be implemented into the project, and the Object needs to be initialized.


###Simple Example:
```
PrettyPrint printer = new PrettyPrint();
printer.parseBoxStart();
printer.parseText("Hello World");
printer.parseBoxEnd();
```
this is a simple example for how to parse text with pretty print. the result in the console will look like this

```
┌--------------------------------------------┐
|  Hello World                               |
└--------------------------------------------┘
```

the project contains a javadoc which describes all possible method calls


###Extended Example

```
PrettyPrint printer = new PrettyPrint(80,2, PrettyPrintConst.PRINT_TYPE.ALL, null);
printer.parseBoxStart();
printer.parseText("Headline for the example");
printer.parseHeadline();
printer.parseText("Some Content for the first line");
printer.queueText("First Part");
printer.parseText("Some Content for the second line");
printer.queueText(" for the third line");
printer.parseQueue();
printer.parseBoxEnd();
```

this example shows how to queue elements for printing it later via the printer.

```
┌------------------------------------------------------------------------------------┐
|  Headline for the example                                                          |
├------------------------------------------------------------------------------------┤
|  Some Content for the first line                                                   |
|  Some Content for the second line                                                  |
|  First Part for the third line                                                     |
└------------------------------------------------------------------------------------┘
```
