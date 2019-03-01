import java.util.LinkedList;
import java.util.Iterator;
//import java.util.List;

public class BST<T> {

    BSTNode<T> root;

    public BST(LinkedList<T> list) {
        root = sortedIterToTree(list.iterator(), list.size());
    }

    /* Returns the root node of a BST (Binary Search Tree) built from the given
       iterator ITER  of N items. ITER will output the items in sorted order,
       and ITER will contain objects that will be the item of each BSTNode. */
    private BSTNode<T> sortedIterToTree(Iterator<T> iter, int N) {
        //List<T>[] l = new List<T>[10];
        T[] l = (T[]) new Object[N];
        for (int i = 0; i < N; i++) {
            //while(iter.hasNext()){
            l[i] = (iter.next());
        }
        return helper(l, N);

    }

    private BSTNode<T> helper(T[] l, int N) {
        BSTNode<T> t = new BSTNode<>(null);
        if (N == 0) {
            return null;
        } else if (N == 1) {
            t.item = l[0];
        } else if (N == 2) {
            t.item = l[1];
            t.left = new BSTNode<T>(l[0]);
        } else if (N == 3) {
            t.item = l[1];
            t.left = new BSTNode<T>(l[0]);
            t.right = new BSTNode<T>(l[2]);
        } else {
            int mid = N / 2 + N % 2;
            //int sub1 = mid/2;
            T[] s1 = (T[]) new Object[mid];
            System.arraycopy(l, 0, s1, 0, mid);
            //int sub2 = mid + mid/2;
            T[] s2 = (T[]) new Object[N - mid - 1];
            System.arraycopy(l, mid + 1, s2, 0, N - mid - 1);
            t.item = l[mid];
            t.left = helper(s1, s1.length);
            t.right = helper(s2, s2.length);

        }
        return t;
    }

    /* Prints the tree represented by ROOT. */
    private void print() {
        print(root, 0);
    }

    private void print(BSTNode<T> node, int d) {
        if (node == null) {
            return;
        }
        for (int i = 0; i < d; i++) {
            System.out.print("  ");
        }
        System.out.println(node.item);
        print(node.left, d + 1);
        print(node.right, d + 1);
    }

    class BSTNode<T> {
        T item;
        BSTNode<T> left;
        BSTNode<T> right;

        BSTNode(T item) {
            this.item = item;
        }
    }

}
