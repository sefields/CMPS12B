// Sam Fields sefields
// Sajan Patel savipate
// $Id: commands.java,v 1.11 2014-02-06 00:34:42-08 - - $

import java.util.HashMap;
import static java.lang.System.*;

class commands {

   private static void command_00 (String[] words) {
      // Executing a comment does nothing.
   }

   private static void command_bp (String[] words) {
      STUB (words);
   }

   private static void command_br (String[] words) {
      STUB (words);
   }

   private static void command_cc (String[] words) {
      STUB (words);
   }

   private static void command_in (String[] words) {
       jroff.indent = Integer.parseInt(words[1]);
       jroff.indentation = jroff.indent + jroff.pageOff;
   }

   private static void command_ll (String[] words) {
       jroff.width = Integer.parseInt(words[1]);
   }

   private static void command_mt (String[] words) {
       jroff.marginTop = Integer.parseInt(words[1]);
   }

   private static void command_pl (String[] words) {
       jroff.pageLen = Integer.parseInt(words[1]);
   }

   private static void command_po (String[] words) {
       jroff.pageOff = Integer.parseInt(words[1]);
   }

   private static void command_sp (String[] words) {
      STUB (words);
   }

   private static void STUB (String[] words) {
      out.printf ("%s: STUB: %s",
                  auxlib.PROGNAME, auxlib.caller());
      for (String word: words) out.printf (" %s", word);
      out.printf ("%n");
   }

   public static void do_command (String[] words) {
      switch (words[0]) {
         case ".\\\"": command_00 (words); break;
         case ".bp"  : command_bp (words); break;
         case ".br"  : command_br (words); break;
         case ".cc"  : command_cc (words); break;
         case ".in"  : command_in (words); break;
         case ".ll"  : command_ll (words); break;
         case ".mt"  : command_mt (words); break;
         case ".pl"  : command_pl (words); break;
         case ".po"  : command_po (words); break;
         case ".sp"  : command_sp (words); break;
         default     : throw new IllegalArgumentException (words[0]);
      }
   }

}

