// $Id: testtrees.java,v 1.1 2014-01-16 17:43:14-08 - - $

import static java.lang.System.*;

class testtrees {

   static class printer implements visitor<String> {
      public void visit (String item) {
         out.printf ("%s%n", item);
      }
   }

   public static void main (String[] args) {
      String[] arguments = new String [args.length];
      for (int itor = 0; itor < args.length; ++itor) {
         arguments[itor] = "args[" + itor + "]=\"" + args[itor] + "\"";
      }
      tree<String> the_tree = new tree<String> (arguments);
      the_tree.visit (new printer ());
   }

}
