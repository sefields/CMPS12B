.so Tmac.mm-etc
.if t .Newcentury-fonts
.INITR* \n[.F]
.SIZE 12
.TITLE CMPS-012B Winter\~2014 Program\~2 "Text formatting and queues"
.RCS "$Id: asg2j-jroff-queue.mm,v 1.20 2014-01-24 11:45:08-08 - - $"
.PWD
.URL
.EQ
delim $$
.EN
.nr HTTP_COUNT 0
.de HTTP_REF
.   nr HTTP_COUNT \\n[HTTP_COUNT]+1
.   nop [\\n[HTTP_COUNT]]\\$2
.   ds HTTP_REF_\\n[HTTP_COUNT] \\$1
..
.nr HTTP_INDEX 0
.de HTTP_PRINT
.   while \\n[HTTP_INDEX]<\\n[HTTP_COUNT] \{\
.      nr HTTP_INDEX \\n[HTTP_INDEX]+1
.      br
.      nop [\\n[HTTP_INDEX]]
.      V= "\\*[HTTP_REF_\\n[HTTP_INDEX]]"
.      br
.   \}
..
.H 1 "Overview"
.if t .ds TeX T\h'-.2m'\v'.5n'E\v'-.5n'\h'-.06m'X
.if n .ds TeX TeX
.if t .ds LaTeX L\v'-.5n'\h'-.36m'A\h'-.16m'\v'.5n'\*[TeX]
.if n .ds LaTeX LaTeX
In this assignment you will write a very simple text formatter in
the spirit of
.V= nroff ,
which is similar to the more powerful text formatters such as
.V= troff
.HTTP_REF http://www.troff.org/
which is proprietary, and
.V= groff
.HTTP_REF http://www.gnu.org/software/groff/
which is Gnu
.HTTP_REF http://www.gnu.org/philosophy/free-sw.html ]
free software.
.RI (`` "Free software is a matter of liberty, not price."
.IR "To understand the concept,"
.IR "you should think of free as in free speech,"
.IR "not as in free beer." "'')"
similar programs are \*[TeX]
.HTTP_REF http://www.tug.org/texlive/
created by Donald Knuth
.HTTP_REF http://www-cs-staff.stanford.edu/~uno/
and
\*[LaTeX]
.HTTP_REF http://www.latex-project.org/ .
You will also write code for the simple linear abstract data type
.V= queue ,
implemented as a linked list.
.P
.HTTP_PRINT
.P
.E= Note\(::
The only code you may use is code you or your partner wrote yourselves,
or copied from the course directory,
or which is available as part of the
.V= java.lang
and
.V= java.io
packages.
You may not use any classes from
.V= java.util ,
except for those classes specifically listed in the Syllabus.
.H 1 "Manual Page and \f[CB]example-output\f[P] Directory"
The manual page describing the software you are to implement
is in the subdirectory
.V= example-output/ .
In that directory, you will find several files\(::
.nr FILE_Pi \n[Pi]*4
.VL \n[FILE_Pi]
.V=LI proff.perl
is a program which is a wrapper around
.V= groff ,
which does the work your program has to do.
.V=LI jroff.rf
is a 
.V= jroff
source file containing markup language specifying your project.
.V=LI jroff.out
is a plain text file which is in readable format for printing.
When your program reads
.V= jroff.rf ,
it should produce exactly this file.
.V=LI test*.rf
are other test files (input data).
.V=LI test*.out
are other output files created from these input files.
.V=LI test*.err
are some samples of error messages that might be generated.
.H 1 "Provided Code"
You are also provided with a
.V= code/
subdirectory,
which you may use as a source to begin your program\(::
.VL \n[FILE_Pi]
.V=LI Makefile
which you can modify to build your 
.V= jroff 
jar file from Java sources.
.V=LI jroff.java
is a debugging trace starter for your main program.
Delete all lines of code marked
.=V `` STUB ''.
.V=LI commands.java
contains a 
.V= HashMap
that will be used to dispatch each of the control sequence commands.
.V=LI linkedqueue.java
is a stub for a linked list implementation of a queue.
Finish the code and documentation for this file.
.V=LI auxlib.java
the same file from a previous assignment.
use the facilities provided herein to print message, etc.
.LE
.H 1 "Implementation Strategy"
It is important to implement your program a little at a time.
Begin by copying the Java programs from the
.V= code
subdirectory into your project development directory.
Then proceed in steps.
One possible sequence is\\(:
.ALX i ()
.LI
Do a recursive copy
.=V ( "cp -R" )
of the
.V= code
subdirectory and begin with the code provided.
Your results will be written to
.V= stdout
and error messages to
.V= stderr .
.LI
Note that each word is printed one word at a time boxed in by
brackets for visibility.
The function
.V= java.lang.String.split
accepts a regular expression and splits the string into an
array of strings.
Note that if there are leading spaces,
the first word is empty.
.LI
For each empty line in the input, print the stub message
.=V `` ".sp 1" ''
as if it were a dot command.
.LI
Implement
.V= linkedqueue \(::
replace code that throws
.V= UnsupportedOperationException
with working code.
Your implementation is a linked list.
The function
.V= insert
creates a new node and appends it to the end of the list.
The function
.V= remove
unlinks the first node from the list and return its value.
.LI
Add the following statement to your main function\(::
.VCODE* 1 "linkedqueue<String> wordqueue = new linkedqueue<String> ();"
.LI
Change your code so that instead of just printing words read,
each word read is inserted into the end of the queue.
Whenever you see a control line,
read each word from the queue and print it.
.LI
Delete the printing code and move it into an auxiliary function
which has a local
.V= StringBuffer .
Implement
.V= printparagraph .
The main loop of this function should proceed as follows\(::
.ALX a ()
.LI
If the string buffer is empty,
.V= append
the new word to it.
.LI
If not empty, figure out how many spaces to append to it\(::
A sentence-ending character gets 2 spaces.
Any other character gets one space.
.LI
If this does not make the buffer exceed the line length,
append the space(s) and the word and cycle to the next word.
If it does exceed the line length, print the string buffer,
clear it out, and put the word at the front of the buffer.
.LI
When the queue is exhausted, print the string buffer,
unless it is empty.
.LE
.LI
The class
.V= commands
implements each of the commands with a stub.
Dispatch is done via a switch statement, which is kept
deliberately small by only calling functions.
Note that Java 1.7 allows the argument to a switch to be a string.
Java 1.6 does not allow that,
so if you develop on your own computer,
you will need at least Java 1.7.
.LI
Implement each of the control commands in some arbitrary
sequence\(::
.nr DOT_Pi \n[Pi]*2
.VL \n[DOT_Pi]
.V=LI ".\[rs]\[Dq]"
The line is simply discarded and does not break a paragraph.
Any other command causes the current paragraph to be dumped
and the queue to be emptied.
.V=LI ".bp"
The program switches to top-of-page mode.
.V=LI ".br"
Call dump paragraph.
The queue is flushed to the standard output and then cleared.
.V=LI ".cc C"
Change the control character to
.RI ` C ',
for any character.
Your must now retrofit the code that looks for dots in the
first position and recognize this character instead.
.V=LI ".in N"
Every line printed is moved to the right by both the page offset
and the indentation.
This number is remembered for future output.
.V=LI ".ll N"
Set the line length as specified.
The dump paragraph routine wraps when a word exceeds this length.
.V=LI ".mt N"
Sets the height of the top margin.
.V=LI ".pl N"
Sets the page length to this number.
A page eject occurs whenever this directive is found,
or printing a line causes the page to be full.
.V=LI ".po N"
Every line of output except for empty lines is preceded by this
number of spaces.
.V=LI ".sp N"
Dump the paragraph.
Then remember the number of blank lines to be printed.
The next time a paragraph is dumped, this number of empty lines
is printed.
However, if printing that number of empty lines would fill the page,
the program switches to top-of-page mode.
.LE
.LI
Fix your paragraph output routine\(::
Whenever you print,
your program is either in top-of-page mode or in mid-page mode.
Initially, it is in top of page mode.
In top-of-page mode, before printing an output line,
it must print a form feed
.=V ( \[rs]f ),
followed by $ N / 2 $ blank lines,
followed by the page title,
followed by $ N / 2 - 1 $ blank lines,
followed by the line to print.
In mid-page mode, it just prints the line.
The line feed is suppressed on the first page.
When a line feed is printed, it is not printed on its own line.
The page headers is left, mid, and right justified with any
occurrence of the character
.=V `` % ''
replaced by the current page number.
.LI
See the subdirectory
.V= example-output/ .
The files
.V= *.out
from your program should
.V= diff (1)
identical to those generated by your program.
The files
.V= *.err
should be similar.
The files
.V= *.log
should show exactly the same exit status.
.LE
.H 1 "Other nodes"
Normally you are prohibited from using anything from
.V= java.util,
except for those specific classes listed in Figure 1 of the syllabus.
In this program, however,
.V= commands.java
does make use of a
.V= HashMap
for eas of looking up commands in lieu of a switch statement,
which does not work with strings.
.P
When 
.V= proff.perl
runs
.V= groff ,
there is a bogus error message that is printed\(::
.VCODE* 1 \
"grotty:<standard input>:5: character above first line discarded"
Ignore this.
It is a bug in
.V= grotty.
Not your problem.
.P
The subdirectory
.V= example-output
contains some sample runs.
You may test the correctness of your program by using
.V= diff
to compare your program's output with that of 
.V= proff.perl .
.H 1 "What to submit"
Submit the files
.V= README ,
.V= Makefile ,
and the necessary Java source files.
Your
.V= Makefile
should have the targets\(::
.V= all ,
the first target, which builds the jar\(;;
.V= ci
which checks in all files into an 
.V= RCS
subdirectory.
.V= submit ,
which submits files,
.V= clean ,
which deletes any files built by 
.V= "gmake all" ,
except for the jar.
Verify that your submit works using the command
.V= testsubmit .
.P
If you are doing pair programming,
carefully read the document in
.V= cmps012b-wm/Syllabus/pair-programming/
and submit the 
.V= PARTNER
file as required.
Pair programming is encouraged in all assignments.
.P
Every file you submit should have as its first line your name
and username.
Its second line should be an RCS comment
.V= \(DoId\(Do
string in a comment.
.E= CAUTION\(::
Before submitting anything,
carefully read the section in the syllabus that covers cheating.
.FINISH
