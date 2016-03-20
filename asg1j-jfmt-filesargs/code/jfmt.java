// $Id: jfmt.java,v 1.6 2014-01-21 23:45:23-08 - - $
//
// Sam Fields sefields
// 
// Name: Asg 1 jfmt
//

import java.io.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import static java.lang.System.*;

class jfmt {
   // Static variables keeping the general status of the program.
   public static final String JAR_NAME = get_jarname();
   public static final int EXIT_SUCCESS = 0;
   public static final int EXIT_FAILURE = 1;
   public static int exit_status = EXIT_SUCCESS;
    public static int width = 65;

   // A basename is the final component of a pathname.
   // If a java program is run from a jar, the classpath is the
   // pathname of the jar.
   static String get_jarname() {
      String jarpath = getProperty ("java.class.path");
      int lastslash = jarpath.lastIndexOf ('/');
      if (lastslash < 0) return jarpath;
      return jarpath.substring (lastslash + 1);
   }


   // Formats a single file.
    static void format (Scanner infile) {
List<String> words = new LinkedList<String>();
      // This for loop reads in a line at a time from the file...
      for (int linenr = 1; infile.hasNextLine(); ++linenr) {
         String line = infile.nextLine(); 
         //... splits the line into words around white space...
         for (String word: line.split("\\s+")) {
         //... and adds each word to words,
         //provided the word isn't empty.
          if (word.length() == 0) continue;
          words.add(word);
        }
      }
      //Now print the words
      print_paragraph(words);
    }

    public static void print_paragraph(List<String> words) {
       //Start printing on a newline
       out.printf("%n");
       //charCount will keep track of number of characters on each line
       int charCount = 0;
       //Iterate through each word on the list
       for (String word : words) {
           //If we are on a new line, print the word 
           //with no space before, and increment charCount
          if (charCount == 0) {
            out.printf(word);
            charCount = word.length();
            //Otherwise increment charCount for a space
            //and the word...
           } else {
            charCount += 1 +word.length();
            //...and if printing that space and word will
            //exceed width limit, print a newline, the word,
            //and reset charCount...
              if (charCount > width) {
              out.printf("%n"+word);
              charCount = word.length();
              //...and if printing that space and word
              //won't exceed width limit, do it on this line.
           } else {
              out.printf(" " + word);
             }
          }
       }
       //If charCount exceeds the width limit, start a new line
       if (charCount > 0) {
           out.printf("%n");
       }
       //At the end, clear the linked list
       words.clear();
    }


   // Main function scans arguments and opens/closes files.
   public static void main (String[] args) {
         if (args.length == 0) {
         // There are no filenames given on the command line.
         format (new Scanner (in)); 
        } else {
       //There are arguments on the command line. Set argix=0,
       //the index of the first argument.
       int argix = 0;
       //If this first arguments starts with "-" and has length
       //greater than 1, then this is presumably an integer.
       //It is handed to width after going through parseInt and
       //being multiplied by -1.
       if (args[0].startsWith("-") && args[0].length() >1) {
             width = Integer.parseInt(args[0])*-1;
               argix = 1;
           
           }
         // Iterate over each filename given on the command line.
             for (; argix < args.length; ++argix) {
            String filename = args[argix];
            if (filename.equals ("-")) {
               format (new Scanner (in));
            }else {
               // Open the file and read it, or error out.
               try {
                  Scanner infile = new Scanner (new File (filename));
                  format (infile);
                  infile.close();
               }catch (IOException error) {
                  exit_status = EXIT_FAILURE;
                  err.printf ("%s: %s%n", JAR_NAME,
                              error.getMessage());
               }
            }
             }
        }
      exit (exit_status);
   }
}
