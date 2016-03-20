// $Id: jxref.java,v 1.7 2014-02-18 23:25:08-08 - - $
// Sam Fields sefields
//
//NAME: Xref Binary Search Tree
//
//SYNOPSIS: jxref [-df] [filename...]
//
//DESCRIPTION: Reads a text input. Keeps track of
//every unique word and which lines each word appears
//in. Prints this information to output.

import java.io.*;
import java.util.Scanner;
import static java.lang.System.*;

class jxref {
   static final String STDIN_NAME = "-";
    public static boolean debug = false;
    public static boolean lowerCase = false;

   static class printer implements visitor <String, queue <Integer>> {
      public void visit (String key, queue <Integer> value) {
         out.printf ("%s",key);
         for (int linenr: value) out.printf (" %d", linenr);
         out.printf ("%n");
      }
   }


   static void xref_file (String filename, Scanner scan) {
       //Print header
       out.printf("\n");
       for (int i = 1; i <= 65; i++) out.printf(":");
       out.printf("\n");
       out.printf(filename);
       out.printf("\n");
       for (int i = 1; i <= 65; i++) out.printf(":");
       out.printf("\n");
       out.printf("\n");
       //New treemap to store the words
      treemap <String, queue <Integer>> map =
            new treemap <String, queue <Integer>> ();
      //Iterate thru input lines
      for (int linenr = 1; scan.hasNextLine (); ++linenr) {
         //Iterate thru each word on each line
         for (String word: scan.nextLine().split ("\\W+")) {
             //If option "-f" was called, convert
             //word to lower case
            if (lowerCase == true) word = word.toLowerCase();
            if (word.matches ("^\\d*$")) continue;
            //If the word is already in the tree, add
            //the line number where it appeared to the queue
            //in that node
            if (map.get(word) != null) map.get(word).insert(linenr);
            //Otherwise make a new queue, insert the line
            //number into that queue, and add a new node
            //containing that word and queue.
            else {
                queue <Integer> linez = new queue <Integer>();
                linez.insert(linenr);
                map.put(word, linez);
              }
         }
      }
      //If option "-d" was called, print debug info. Otherwise
      //produce normal output.
      if (debug == true) map.debug_dump();
      else {
      visitor <String, queue <Integer>> print_fn = new printer ();
      map.do_visit (print_fn);
      }
   }

   public static void main (String[] args) {
       //If there are no args, read stdin
      if (args.length == 0) {
         xref_file (STDIN_NAME, new Scanner (in));
      }else {
         //Otherwise iterate thru the args...
         for (int argi = 0; argi < args.length; ++argi) {
             //..and if the first one starts 
             //with "-", scan opts
             if (argi==0 && args[argi].startsWith("-")){
                scanOpts(args[0]);
                argi++;
            }
            String filename = args[argi];
            //If user specifies "-" for filename
            //read from command line...
            if (filename.equals (STDIN_NAME)) {
               xref_file (STDIN_NAME, new Scanner (in));
            }else {
                //..and otherwise attempt to use the
                //specified file
               try {
                  Scanner scan = new Scanner (new File (filename));
                  xref_file (filename, scan);
                  scan.close ();
               }catch (IOException error) {
                  auxlib.warn (error.getMessage ());
               }
            }
         }
      }
      auxlib.exit ();
   }

    public static void scanOpts(String op) {
        if (op.equals("-d")) debug = true;
        else if (op.equals("-f")) lowerCase = true;
        else if (op.equals("-fd")) {
            debug = true;
            lowerCase = true;
        }
        else if (op.equals("-df")) {
            debug = true;
            lowerCase = true;
        }
        //An unsupported option produces an
        //error to stderr and exit status of 1
        else {
            System.err.println("Error: Unsupported Operation.");
            auxlib.usage_exit(op);
        }
        return;
    }
}
