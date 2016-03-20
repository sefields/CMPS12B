// $Id: queue.java,v 1.2 2014-02-18 14:39:57-08 - - $
// Sam Fields sefields
//

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.io.*;
import static java.lang.System.*;
import static java.lang.String.*;

class queue <item_t> implements Iterable <item_t> {

   private class node {
      item_t item;
      node link;
   }
   private node head = null;
   private node tail = null;

   public boolean isempty () {
       return head == null;
   }

   public void insert (item_t newitem) {
       //Make a new node
       node a = new node();
       //new gets newitem as its item
       a.item = newitem;
       //new points to nothing
       a.link = null;
       //If the queue is empty, head gets new
       if (isempty()) head = a;
       //Otherwise the current rear node points to new
       else tail.link = a;
       //and new is the rear node
       tail = a;
   }

   public Iterator <item_t> iterator () {
      return new itor ();
   }

   class itor implements Iterator <item_t> {
      node next = head;
      public boolean hasNext () {
         return next != null;
      }
      public item_t next () {
         if (! hasNext ()) throw new NoSuchElementException ();
         item_t result = next.item;
         next = next.link;
         return result;
      }
      public void remove () {
         throw new UnsupportedOperationException ();
      }
   }

}
