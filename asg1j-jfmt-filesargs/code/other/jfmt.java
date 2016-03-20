// $Id: jfmt.java,v 1.3 2014-01-19 16:18:58-08 - - $
//
// Starter code for the jfmt utility.  This program is similar
// to the example code jcat.java, which iterates over all of its
// input files, except that this program shows how to use
// String.split to extract non-whitespace sequences of characters
// from each line.
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
      // Read each line from the opened file, one after the other.
      // Stop the loop at end of file.
      for (int linenr = 1; infile.hasNextLine(); ++linenr) {
         String line = infile.nextLine(); 
	 //Split the line into words around white space and iterate
	 //over the words
	 for (String word: line.split ("\\s+")) {
	     //Skip an empty word if such is found.
	     if (word.length() == 0) continue;
	     //Add the word to words
	     words.add(word);
	 }
      }
      print_paragraph(words);
    }

    public static void print_paragraph(List<String> words) {
	/*charCount keeps track of how many characters on a line. runtimes keeps track of
	  how many times we've gone through the while loop. */
	int charCount = 0;
	int runtimes = 0;
	//While there are still words left in the list, iterate through it
	while (words.size() != 0) {
	    String currentWord = words.get(0);
	    //charCount is incremented by the length of the current word
	    charCount += currentWord.length();
	    //The following if statement checks if charCount has exceeded the limit set.
	    if (charCount > width) {
		out.printf("%n");
		charCount = currentWord.length();
	    }
	    //The current word is printed, then removed. Now the next word is first on the list.
	    out.printf("%s", currentWord);
	    words.remove(0);
	    runtimes++;
	    //If there is still at least one more word on the list, print a space.
	    if (words.isEmpty() == false) {
		out.printf("%s");
		charCount++;
		}
	}
	words.clear();
    }


   // Main function scans arguments and opens/closes files.
   public static void main (String[] args) {
       System.out.print("diddly boop");
         if (args.length == 0) {
         // There are no filenames given on the command line.
         out.printf ("FILE: -%n");
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
		  System.out.print("WIDTH IS " + width);
	      
	      }
         // Iterate over each filename given on the command line.
	      for (; argix < args.length; ++argix) {
            String filename = args[argix];
            if (filename.equals ("-")) {
               // Treat a filename of "-" to mean System.in.
               out.printf ("FILE: -%n");
               format (new Scanner (in));
            }else {
               // Open the file and read it, or error out.
               try {
                  Scanner infile = new Scanner (new File (filename));
                  out.printf ("FILE: %s%n", filename);
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
