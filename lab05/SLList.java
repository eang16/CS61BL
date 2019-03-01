import java.util.Objects;

/**
 * An SLList is a list of integers, which hides the terrible truth of the
 * nakedness within.
 */
public class SLList {
    private static class IntNode {
        public int item;
        public IntNode next;

        @Override
        public String toString() {
            return "IntNode{" + "item=" + item + ", next=" + next + '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            IntNode intNode = (IntNode) o;
            return item == intNode.item && Objects.equals(next, intNode.next);
        }


        public IntNode(int i, IntNode n) {
            item = i;
            next = n;
        }
    }

    /* The first item (if it exists) is at sentinel.next. */
    private IntNode sentinel;
    private int size;

    @Override
    public String toString() {
        return "SLList{" + "sentinel=" + sentinel + ", size=" + size + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SLList slList = (SLList) o;
        return size == slList.size && Objects.equals(sentinel, slList.sentinel);
    }


    /**
     * Creates an empty SLList.
     */
    public SLList() {
        sentinel = new IntNode(63, null);
        size = 0;
    }

    public SLList(int x) {
        sentinel = new IntNode(63, null);
        sentinel.next = new IntNode(x, null);
        size = 1;
    }

    /**
     * Returns an SLList consisting of the given values.
     */
    public static SLList of(int... values) {
        SLList list = new SLList();
        for (int i = values.length - 1; i >= 0; i -= 1) {
            list.addFirst(values[i]);
        }
        return list;
    }


    /**
     * Returns the size of the list.
     */
    public int size() {
        return size;
    }

    /**
     * Adds x to the front of the list.
     */
    public void addFirst(int x) {
        sentinel.next = new IntNode(x, sentinel.next);
        size += 1;
    }

    /**
     * Returns the first item in the list.
     */
    public int getFirst() {
        return sentinel.next.item;
    }

    /**
     * Return the value at the given index.
     */
    public int get(int index) {
        IntNode p = sentinel.next;
        while (index > 0) {
            p = p.next;
            index -= 1;
        }
        return p.item;
    }

    /**
     * Adds x to the end of the list.
     */
    public void addLast(int x) {
        size += 1;
        IntNode p = sentinel;
        while (p.next != null) {
            p = p.next;
        }
        p.next = new IntNode(x, null);
    }

    /**
     * Adds x to the list at the specified index.
     */
    public void add(int index, int x) {
        IntNode p = sentinel;
        if (index <= size) {
            for (int i = 0; i < index; i++) {
                p = p.next;
            }
            size += 1;
            p.next = new IntNode(x, p.next);
        } else {

            for (int j = size; j < index; j++) {
                if (p.next == null) {
                    p.next = new IntNode(0, null);
                    p = p.next;
                    size++;
                    break;
                } else {
                    p = p.next;
                }
            }


            p.item = x;
        }

    }

    /**
     * Returns the reverse of this list. This method is destructive.
     */
    public void reverse() {
        /**
         IntNode p = sentinel.next;
         if (p != null) {
         int[] valueList = new int[size];
         for (int i = 0; i < size; i++) {
         valueList[i] = get(i);
         }
         for (int j = 0; j < size; j++) {
         p.item = valueList[size - j - 1];
         p = p.next;
         }
         }

         int begin = first.item;
         int finish = second.item;
         first.item =
         */
        IntNode p = sentinel.next;
        if (p != null) {
            for (int i = 0; i < size / 2; i++) {
                IntNode head = bring(i, p);
                IntNode tail = bring(size - 1 - i, p);
                int headItem = head.item;
                head.item = tail.item;
                tail.item = headItem;
                //int tailItem = tail.item;
                //head.item = tailItem;
                //tail.item = headItem;

            }
        }

    }

    public static IntNode bring(int index, IntNode o) {
        IntNode p = o;
        if (index == 0) {
            return p;
        } else {
            return (bring(index - 1, p.next));
        }

    }


}


/**
 * if (this.size % 2 == 0) {
 * int first = 0;
 * int last = 0;
 * for (int i = 0; i < size / 2; i++) {
 * first = get(i);
 * last = get(size - i);
 * <p>
 * <p>
 * }
 * }
 */

