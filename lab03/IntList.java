/**
 * A data structure to represent a Linked List of Integers.
 * Each IntList represents one node in the overall Linked List.
 */
public class IntList {
    public int first;
    public IntList rest;

    public IntList(int f, IntList r) {
        first = f;
        rest = r;
    }

    /** Returns an IntList consisting of the given values. */
    public static IntList of(int... values) {
        if (values.length == 0) {
            return null;
        }
        IntList p = new IntList(values[0], null);
        IntList front = p;
        for (int i = 1; i < values.length; i++) {
            p.rest = new IntList(values[i], null);
            p = p.rest;
        }
        return front;
    }

    /** Returns the size of the list. */
    public int size() {
        if (rest == null) {
            return 1;
        }
        return 1 + rest.size();
    }

    /** Returns [position]th value in this list. */
    public int get(int position) {
        if(position==0){
            return this.first;
        }else{
            return(this.rest.get(position-1));
        }
        //return 0;
    }

    /** Returns the string representation of the list. */
    public String toString() {
        // TODO: YOUR CODE HERE
        if (this.rest == null){
            return (String.valueOf(this.first));
        } else {
            return (String.valueOf(this.first) + " " + this.rest.toString());
        }
    }

    /** Returns whether this and the given list or object are equal. */
    public boolean equals(Object o) {
        IntList other = (IntList) o;
        // TODO: YOUR CODE HERE
        /**if (this == other){
            return true;
        } else {
            return false;
        }
        */
        if (this.first != other.first){
            return false;
        } else if (this.rest != null && other.rest != null){
            return (this.rest.equals(other.rest));
        } else if (this.first == other.first && this.rest == null && other.rest == null){
            return true;
        } else {
            return false;
        }

    }
}
