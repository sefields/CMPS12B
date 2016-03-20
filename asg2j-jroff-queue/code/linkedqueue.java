// Sam Fields sefields
// Sajan Patel savipate
// $Id: linkedqueue.java,v 1.14 2014-02-06 00:34:42-08 - - $

import java.util.NoSuchElementException;
import java.io.*;
import static java.lang.System.*;
import static java.lang.String.*;

class linkedqueue <item_t> {

   public class node{
      item_t item;
      node link;
   }

   //
   // INVARIANT:  front == null && rear == null
   //          || front != null && rear != null
   // In the latter case, following links from the node pointed 
   // at by front will lead to the node pointed at by rear.
   //
   public node front = null;
   private node rear = null;

   public boolean empty (){
      return front == null;
   }

   public void insert (item_t any) {
       //Make a new node a
       node a = new node();
       //a gets any as its value
       a.item = any;
       //a points to nothing
       a.link = null;
       //If the linkedqueue is empty, front gets a
       if (empty()) {
	   front = a;
	   //Otherwise the current rear node points to a...
       } else {
	   rear.link = a;
       }
       //... which is now the rear node
       rear = a;
   }

   public item_t remove (){
       //If queue is empty, nothing to remove
      if (empty ()) throw new NoSuchElementException ();
      //b gets the value of the front node
      item_t b = front.item;
      //If we only have one node , it points to null
      if (front.link == null) {
	  rear.link = null;
      }
      //Other wise front is now gonna be the next node
      front = front.link;
      //And we return the item from the node we just removed
      return b;
   }
}