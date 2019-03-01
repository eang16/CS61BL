public class BinaryTree<T> {

    TreeNode<T> root;

    public BinaryTree() {
        root = null;
    }

    public BinaryTree(TreeNode<T> t) {
        root = t;
    }

    public TreeNode<T> getRoot() {
        return root;
    }

    /* Returns the height of the tree. */
    public int height() {
        if (root != null) {
            return root.getHeight();
        }
        return 0;
    }

    /* Returns true if the tree's left and right children are the same height
       and are themselves completely balanced. */
    public boolean isCompletelyBalanced() {
        return false;
    }

    /* Returns a BinaryTree representing the Fibonacci calculation for N. */
    public static BinaryTree<Integer> fibTree(int N) {
        BinaryTree<Integer> f = new BinaryTree<Integer>();
        f.root = f.fHelper(N);
//        f.root.fib(N);

        return f;

    }

    private TreeNode fHelper(int N) {
        if (N == 0 || N == 1) {
            return new TreeNode(N);
        }

        TreeNode l = fHelper(N - 1);
        TreeNode r = fHelper(N - 2);
        return new TreeNode(((Integer) l.item).intValue()
                + ((Integer) r.item).intValue(), l, r);

    }

    /* Print the values in the tree in preorder: root value first, then values
       in the left subtree (in preorder), then values in the right subtree
       (in preorder). */
    public void printPreorder() {
        if (root == null) {
            System.out.println("(empty tree)");
        } else {
            root.printPreorder();
            System.out.println();
        }
    }

    /* Print the values in the tree in inorder: values in the left subtree
       first (in inorder), then the root value, then values in the first
       subtree (in inorder). */
    public void printInorder() {
        if (root == null) {
            System.out.println("(empty tree)");
        } else {
            root.printInorder();
            System.out.println();
        }
    }

    /* Prints out the contents of a BinaryTree with a description in both
       preorder and inorder. */
    private static void print(BinaryTree t, String description) {
        System.out.println(description + " in preorder");
        t.printPreorder();
        System.out.println(description + " in inorder");
        t.printInorder();
        System.out.println();
    }

    /* Fills this BinaryTree with values a, b, and c. DO NOT MODIFY. */
    public void sampleTree1() {
        root = new TreeNode("a", new TreeNode("b"), new TreeNode("c"));
    }

    /* Fills this BinaryTree with values a, b, and c, d, e, f. DO NOT MODIFY. */
    public void sampleTree2() {
        root = new TreeNode("a",
                new TreeNode("b", new TreeNode("d", new TreeNode("e"),
                        new TreeNode("f")), null), new TreeNode("c"));
    }

    /* Fills this BinaryTree with the values a, b, c, d, e, f. DO NOT MODIFY. */
    public void sampleTree3() {
        root = new TreeNode("a", new TreeNode("b"), new TreeNode("c",
                new TreeNode("d", new TreeNode("e"), new TreeNode("f")), null));
    }

    /* Fills this BinaryTree with the same leaf TreeNode. DO NOT MODIFY. */
    public void sampleTree4() {
        TreeNode leafNode = new TreeNode("c");
        root = new TreeNode("a", new TreeNode("b", leafNode, leafNode),
                new TreeNode("d", leafNode, leafNode));
    }

    /* Creates two BinaryTrees and prints them out in inorder. */
    public static void main(String[] args) {
        BinaryTree t;
        t = new BinaryTree();
        print(t, "the empty tree");
        System.out.println(t.height());
        t.sampleTree1();
        print(t, "sample tree 1");
        System.out.println(t.height());
        t.sampleTree2();
        print(t, "sample tree 2");
        System.out.println(t.height());
        t.height();
        t = fibTree(5);
        print(t, "fib");
    }

    /* Note: this class is public in this lab for testing purposes. However,
       in professional settings as well as the rest of your labs and projects,
       we recommend that you keep your inner classes private. */
    static class TreeNode<T> {

        private T item;
        private TreeNode left;
        private TreeNode right;

        TreeNode(T obj) {
            item = obj;
            left = null;
            right = null;
        }

        TreeNode(T obj, TreeNode<T> left, TreeNode<T> right) {
            item = obj;
            this.left = left;
            this.right = right;
        }

        public T getItem() {
            return item;
        }

        public TreeNode<T> getLeft() {
            return left;
        }

        public TreeNode<T> getRight() {
            return right;
        }

        void setItem(T item) {
            this.item = item;
        }

        void setLeft(TreeNode<T> left) {
            this.left = left;
        }

        void setRight(TreeNode<T> right) {
            this.right = right;
        }

        private void printPreorder() {
            System.out.print(item + " ");
            if (left != null) {
                left.printPreorder();
            }
            if (right != null) {
                right.printPreorder();
            }
        }

        private void printInorder() {
            if (left != null) {
                left.printInorder();
            }
            System.out.print(item + " ");
            if (right != null) {
                right.printInorder();
            }
        }

        public int getHeight() {
            int l = 0;
            int r = 0;
            if (left == null && right == null) {
                return 0;
            }
            if (left != null) {
                l = 1 + left.getHeight();
            }
            if (right != null) {
                r = 1 + right.getHeight();
            }
            if (left == null) {
                return r;
            }
            if (right == null) {
                return l;
            }
            if (l < r) {
                return r;
            }
            return l;
        }

        /**
         * public TreeNode<Integer> fib(int N) {
         * //TreeNode<Integer> p = new TreeNode<>(N);
         * if (N == 0 || N == 1) {
         * return new TreeNode<Integer>(N);
         * }
         * int ri = N / 2;
         * int li = N - N / 2;
         * TreeNode<Integer> r = new TreeNode<>(ri, fib(ri - 1), fib(ri - 2));
         * TreeNode<Integer> l = new TreeNode<>(li, fib(li - 1), fib(li - 2));
         * return new TreeNode<>(N, r, l);
         * <p>
         * }


         public void fib(int N) {
         //TreeNode<Integer> p = new TreeNode<>(N);
         if (N == 0 || N == 1) {
         //item = N;
         } else {
         int ri = N / 2;
         int li = N - N / 2;
         //TreeNode<Integer> r = new TreeNode<>(ri, new TreeNode<>(0, null, null),
            new TreeNode<>(0, null, null));
         r.left.fib(ri - 1);
         r.right.fib(ri - 2);
         //TreeNode<Integer> l = new TreeNode<>(li, new TreeNode<>(0, null, null),
            new TreeNode<>(0, null, null));
         l.left.fib(li - 1);
         l.right.fib(ri - 2);
         left = l;
         right = r;
         }
         }
         */
    }


}
