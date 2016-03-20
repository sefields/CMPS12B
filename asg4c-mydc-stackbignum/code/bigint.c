// $Id: bigint.c,v 1.14 2014-03-03 20:37:39-08 - - $
// Sam Fields sefields

#include <assert.h>
#include <ctype.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "bigint.h"
#include "debug.h"

#define MIN_CAPACITY 16

struct bigint {
   size_t capacity;
   size_t size;
   bool negative;
   char *digits;
};

static void trim_zeros (bigint *this) {
   while (this->size > 0) {
      size_t digitpos = this->size - 1;
      if (this->digits[digitpos] != 0) break;
      --this->size;
   }
}

static int doCompare (bigint *this, bigint *that) {
  if (this->size > that->size) return 1;
  if (that->size > this->size) return -1;
  for (size_t index = 0; index < this->size; index++) {
    if (strcmp(this->digits,that->digits)>0) return 1;
    if (strcmp(that->digits,this->digits)>0) return -1; 
  }
  return 0;
}

bigint *new_bigint (size_t capacity) {
   bigint *this = malloc (sizeof (bigint));
   assert (this != NULL);
   this->capacity = capacity;
   this->size = 0;
   this->negative = false;
   this->digits = calloc (this->capacity, sizeof (char));
   assert (this->digits != NULL);
   DEBUGS ('b', show_bigint (this));
   return this;
}


bigint *new_string_bigint (char *string) {
   assert (string != NULL);
   size_t length = strlen (string);
   bigint *this = new_bigint (length > MIN_CAPACITY
                            ? length : MIN_CAPACITY);
   char *strdigit = &string[length - 1];
   if (*string == '_') {
      this->negative = true;
      ++string;
   }
   char *thisdigit = this->digits;
   while (strdigit >= string) {
      assert (isdigit (*strdigit));
      *thisdigit++ = *strdigit-- - '0';
   }
   this->size = thisdigit - this->digits;
   trim_zeros (this);
   DEBUGS ('b', show_bigint (this));
   return this;
}

static bigint *do_add (bigint *this, bigint *that) {
  DEBUGS ('b', show_bigint (this));
  DEBUGS ('b', show_bigint (that));
  bigint *result = new_bigint(this->capacity > that->capacity
                ? this->capacity+1 : that->capacity+1);
  int digit = 0;
  int carry = 0;
  int length = 0;
  if (this->capacity > that->capacity) length = this->capacity;
  if (that->capacity > this->capacity) length = that->capacity;
  else length = this->capacity;
  for (int index = 0; index < length; index++ ) {
    digit = this->digits[index] + that->digits[index] + carry;
    result->digits[index] = digit % 10;
    carry = digit /10;
    }
  return result;
}

static bigint *do_sub (bigint *this, bigint *that) {
  DEBUGS ('b', show_bigint (this));
  DEBUGS ('b', show_bigint (that));
  bigint *result = new_bigint(this->capacity);
  result->size = this->size + that->size;
  int digit = 0;
  int borrow = 0;
  int length = this->capacity;
  for (int index = 0; index < length; index ++) {
    digit = this->digits[index] - that->digits[index] - borrow + 10;
    result->digits[index] = digit % 10;
    borrow = 1 - digit /10;
  }
  trim_zeros(result);
  return result;
}

void free_bigint (bigint *this) {
   free (this->digits);
   free (this);
}

void print_bigint (bigint *this, FILE *file) {
  DEBUGS ('b', show_bigint(this));
  if (this->negative) fprintf(file,"-");
  for(char *i = &this->digits[this->size-1]; i>= this->digits; --i){
  fprintf(file,"%d",*i);
  }
  fprintf(file,"\n");
}

bigint *add_bigint (bigint *this, bigint *that) {
  DEBUGS ('b', show_bigint (this));
  DEBUGS ('b', show_bigint (that));
  //Make a new bigint to store the result in:
  bigint *result = new_bigint(this->capacity > that->capacity
      ? this->capacity+1 : that->capacity+1);
  //If both have the same sign, do_add
  if (this->negative == that->negative) {
    result = do_add(this, that);
    result -> negative = this -> negative;
  }
  //If they are different signs, do_sub
  else {
    //Make sure the larger number is first
    if (doCompare(this, that) == 1 
        || doCompare(this,that)== 0) {
      result = do_sub(this,that);
      result -> negative = this -> negative;
    }
    if (doCompare(this, that) == -1) {
      result = do_sub(that,this);
      result -> negative = that -> negative;
    }
  }
  result->size = this->size + that->size;
  trim_zeros(result);
  return result;
}

bigint *sub_bigint (bigint *this, bigint *that) {
  DEBUGS ('b', show_bigint (this));
  DEBUGS ('b', show_bigint (that));
  bigint *result = new_bigint(this->capacity > that->capacity
                        ? this->capacity+1 : that->capacity+1);
  //If the signs are different, call do_add
  if (this->negative != that->negative) {
    result = do_add(this, that);
    //If the first number is smaller, the result is negative 
    if (doCompare(this, that) > 0) result->negative = true;
    //Otherwise the result is positive
    else result->negative = false; 
  }
  //If the signs are the same, use do_sub
  else {
   if (doCompare(this,that)>=0) {
     result = do_sub(this,that);
     result->negative = false;
   }
   else {
     result = do_sub(that,this);
     result->negative = true;
   }
  }
  result->size = this->size + that->size;
  trim_zeros(result);
  return result;
}


bigint *mul_bigint (bigint *this, bigint *that) {
  DEBUGS ('b', show_bigint (this));
  DEBUGS ('b', show_bigint (that));
  bigint *result = new_bigint(this->capacity > that->capacity
                       ? this->capacity+1 : that->capacity+1);
  result->size = this->size + that->size;
  for (size_t i = 0; i < this->size ; i++) {
    int carry = 0;
    for (size_t j = 0; j < that->size; j++) {
      int digit = result->digits[i+j] + 
      this->digits[i]*that->digits[j]+carry;
      result->digits[i+j] = digit % 10;
      carry = digit / 10;
    }result->digits[i+that->size] = carry;
  }
  if (this->negative == that->negative) result->negative = false;
  else result->negative = true;
  return result;
}

void show_bigint (bigint *this) {
   fprintf (stderr, "bigint@%p->{%lu,%lu,%c,%p->", this,
            this->capacity, this->size, this->negative ? '-' : '+',
            this->digits);
   for (char *byte = &this->digits[this->size - 1];
        byte >= this->digits; --byte) {
      fprintf (stderr, "%d", *byte);
   }
   fprintf (stderr, "}\n");
}
