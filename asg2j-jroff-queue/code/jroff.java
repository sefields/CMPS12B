// $Id: jroff.java,v 1.58 2014-02-06 01:33:53-08 - - $
// Sam Fields sefields
// Sajan Patel savipate
//
// Name: jroff
//
// Description: Takes text files as input, formats and prints them
//
import java.io.*;
import java.util.Scanner;
import static java.lang.System.*;

class jroff{
   static final String STDIN_NAME = "-";
    public static int width = 65; //Defined by .ll
    public static int indent = 0; //Defined by .in
    public static int pageOff = 0; //Defined by .po
    public static int indentation = indent + pageOff; //Actual offset
    public static int pageLen = 60; //Defined by .pl
    public static int marginTop = 6; //Defined by .mt
    public static int numLines = 0; //Keeps track of number of lines

   static void scanfile (String filename, Scanner infile) {
     linkedqueue<String> wordqueue = new linkedqueue<String> ();
     //Iterate thru each line of the file
     for (int linenr = 1; infile.hasNextLine(); ++linenr) {
        String line = infile.nextLine();
        //If a line is empty, print two newlines to start 
        //a new paragraph
        if (line.trim().length() == 0) {
           out.print("\n");
           out.print("\n");
           numLines += 2;
        }
        //If a line's not empty, check if it's a command line
        else {
           String[] words = line.split ("\\s+");
           //If so, try to execute a command...
           if (words.length > 0 && words[0].startsWith (".")) {
              try {
                 commands.do_command (words);
                 printparagraph(wordqueue);
              }catch (IllegalArgumentException error) {
                 auxlib.warn (filename, linenr, words[0],
                            "invalid command");
            }
          //If not, insert the words to wordqueue.
         }else if (words.length > 0){
              for (String word: words) wordqueue.insert(word) ;
         }
         }
      }
      printparagraph(wordqueue);
   }

    public static void printparagraph (linkedqueue<String> queue) {
       //New StringBuffer of words
       StringBuffer words = new StringBuffer();
       //Iterate thru queue
       while (queue.empty() == false) {
          String currentWord = queue.remove();
          //If words is empty, append the word and a space
          if (words.length() == 0) words.append(currentWord+" ");
            //If words isn't empty, check for punctuation
            else {
               //If the current word ends with punctuation...
               if (currentWord.endsWith("!") || 
                   currentWord.endsWith(".") || 
                   currentWord.endsWith("?") || 
                   currentWord.endsWith(";") || 
                   currentWord.endsWith(":") || 
                   currentWord.endsWith("-")) {
                   //Check if adding that word and
                   //2 spaces will exceed width...
                   if (words.length() + currentWord.length()
                         + 2 > width) {
                      //...And if so: go to new line, print words, clear
                      //words, append the word and two spaces to words.
                      out.printf("\n");
                      numLines++;
                      if (numLines > pageLen) {
                         for (int i =1; i < marginTop ; i++) 
                                 out.printf("\n");
                             numLines = 0;
                     }
               for (int i = 1; i <= indentation ; i++) out.printf(" ");
               out.printf("%s", words);
               words.delete(0, words.length());
               words.append(currentWord);
               words.append("  ");
               } else {
                // if not, append the word and two spaces to words.
                words.append(currentWord);
                words.append("  ");
           }
        }
        else {
           //If the word doesn't end in punctuation && adding that word
           //and a space would exceed width, print words and add the
           //current word and a space to words.
           if (words.length() + 1 > width) {
               out.printf("\n");
               numLines++;
                      if (numLines > pageLen) {
                            for (int i =1; i < marginTop ; i++) 
                                  out.printf("\n");
                            numLines = 0;
                       }
                      for (int i = 1; i <= indentation; i++)
                            out.printf(" ");
                        out.printf("%s",words);
                        words.delete(0, words.length());
                        words.append(currentWord);
                        words.append(" ");
                        } else {
                        //And if the word doesn't end in punctuation &&
                        //adding the word and a space wouldn't exceed
                        //width, append the word and a space to words.
                        words.append(currentWord);
                        words.append(" ");
                     }
                 }
             }
        }
        //If words has stuff in it, print it out.
        out.printf("%s",words);
    }

       public static void main (String[] args) {
       //New linkedqueue words
      linkedqueue <String> words = new linkedqueue <String> ();
      //If no filename is given, read from the command line
      if (args.length == 0) {
         scanfile (STDIN_NAME, new Scanner (in));
         //Otherwise iterate thru files given
         //and put them thru scanfile
      }else {
         for (String filename : args) {
            if (filename.equals (STDIN_NAME)) {
               scanfile (STDIN_NAME, new Scanner (in));
            }else {
               try {
                  Scanner scan = new Scanner (new File (filename));
                  scanfile (filename, scan);
                  scan.close();
               }catch (IOException error) {
                  auxlib.warn (error.getMessage());
               }
            }
         }
      }
   }

}
