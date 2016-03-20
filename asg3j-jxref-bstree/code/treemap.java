// $Id: treemap.java,v 1.5 2014-02-18 23:30:27-08 - - $
//
//Sam Fields sefields
//
import static java.lang.System.*;

class treemap <key_t extends Comparable <key_t>, value_t> {

   private class node {
      key_t key;
      value_t value;
      node left;
      node right;
   }
   private node root;

   private void debug_dump_rec (node tree, int depth) {
       //If the tree is empty, return
       if (tree == null){
           depth -= 1;
           return;
       }
       //Otherwise perform an in-order transversal,
       //keeping track of depth
       depth += 1;
       debug_dump_rec(tree.left, depth);
       //Print the indentation, 2 x depth spaces
       for (int i = 1; i <= depth ; i++) out.printf("  ");
       //Print the information
       out.printf(depth + " " + tree.key.toString() 
       + " " + tree.value.toString() + "\n");
       depth += 1;
       debug_dump_rec(tree.right, depth);
   }

   private void do_visit_rec (visitor <key_t, value_t> visit_fn,
                              node tree) {
       //If the tree is empty return
       if (tree == null) return;
       //Otherwise perform an in-order transversal,
       //visiting each node
       do_visit_rec(visit_fn, tree.left);
       visit_fn.visit(tree.key , tree.value);
       do_visit_rec(visit_fn, tree.right);
   }

   public value_t get (key_t key) {
       node parent = null;
       node curr = root;
       int cmp = 1;
       //Navigate thru tree. If desired key is found,
       //return the value of the node with that key
       while (curr != null) {
           cmp = key.compareTo(curr.key);
           if (cmp == 0) return curr.value;
           if (cmp > 0) {
               parent = curr;
               curr = curr.right;
           }
           if (cmp < 0) {
               parent = curr;
               curr = curr.left;
           }
       }
       //If the value isn't found, return null
       return null;
   }

   public value_t put (key_t key, value_t value) {
       //Parent points to null
       node parent = null;
       //If the tree is empty, we make a new
       //node with our values. That's the root.
       if (root == null) {
           node t = new node();
           t.key = key;
           t.value = value;
           root = t;
           return t.value;
       }
       //Otherwise we start from the
       //already-established root
       node curr = root;
       int cmp = 1;
       //Navigate thru tree, find correct spot
       while (curr != null) {
           cmp = key.compareTo(curr.key);
           if (cmp == 0) break;
           if (cmp > 0) {
               parent = curr;
               curr = curr.right;
           }
           if (cmp < 0) {
               parent = curr;
               curr = curr.left;
           }
       }
       //Either make a new node w/ new value...
       if (cmp != 0){
           node t = new node();
           t.key = key;
           t.value = value;
           if (cmp < 0) parent.left = t;
           if (cmp > 0) parent.right = t;
           return null;
       }
       //...or replace the found node's value by
       //the new value.
       else {
           value_t oldValue = curr.value;
           curr.value = value;
           return oldValue;
       }
   }

   public void debug_dump () {
      debug_dump_rec (root, -1);
   }

   public void do_visit (visitor <key_t, value_t> visit_fn) {
      do_visit_rec (visit_fn, root);
   }

}
