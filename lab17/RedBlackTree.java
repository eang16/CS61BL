public class RedBlackTree<T extends Comparable<T>> {

    /* Root of the tree. */
    RBTreeNode<T> root;

    /* Creates an empty RedBlackTree. */
    public RedBlackTree() {
        root = null;
    }

    /* Creates a RedBlackTree from a given BTree (2-3-4) TREE. */
    public RedBlackTree(BTree<T> tree) {
        Node<T> btreeRoot = tree.root;
        root = buildRedBlackTree(btreeRoot);
    }

    /* Builds a RedBlackTree that has isometry with given 2-3-4 tree rooted at
       given node R, and returns the root node. */
    RBTreeNode<T> buildRedBlackTree(Node<T> r) {
        if (r == null) {
            return null;
        } else {
            if (r.getItemCount() == 1) {
                return new RBTreeNode<T>(true, r.getItemAt(0),
                        buildRedBlackTree(r.getChildAt(0)),
                        buildRedBlackTree(r.getChildAt(1)));
            }

            if (r.getItemCount() == 2) {
                return new RBTreeNode<T>(true, r.getItemAt(0),
                        buildRedBlackTree(r.getChildAt(0)),
                        new RBTreeNode<T>(false, r.getItemAt(1),
                                buildRedBlackTree(r.getChildAt(1)),
                                buildRedBlackTree(r.getChildAt(2)))
                );
            }

            if (r.getItemCount() == 3) {
                return new RBTreeNode<T>(true, r.getItemAt(1),
                        new RBTreeNode<T>(false, r.getItemAt(0),
                                buildRedBlackTree(r.getChildAt(0)),
                                buildRedBlackTree(r.getChildAt(1))),
                        new RBTreeNode<T>(false, r.getItemAt(2),
                                buildRedBlackTree(r.getChildAt(2)),
                                buildRedBlackTree(r.getChildAt(3)))
                );
            }

/**
 for(int i =0; i<r.getChildrenCount(); i++){
 //if(r.getItemAt(0).compareTo(r.getChildAt(i).getItemAt(0))< 0);
 }



 if (r.getChildrenCount() == 0) { //1 item 0 children
 return new RBTreeNode<T>(true, r.getItemAt(0));
 } else if (r.getChildrenCount() == 1) { //1 item 1 child
 //if((r.getChildAt(0)).compareTo(r.getItemAt(0)))
 return new RBTreeNode<T>(true, r.getItemAt(0),
 buildRedBlackTree(r.getChildAt(0)),
 null);
 } else if (r.getChildrenCount() == 2) {  //1 item 2 children
 return new RBTreeNode<T>(true, r.getItemAt(0),
 buildRedBlackTree(r.getChildAt(0)),
 buildRedBlackTree(r.getChildAt(1));
 }
 } else if (r.getItemCount() == 2) {
 if (r.getChildrenCount() == 0) { //2 items 0 children
 return new RBTreeNode<T>(true, r.getItemAt(0),
 null,
 );
 //buildRedBlackTree(RBTreeNode(false, r.getItemAt(1))) );
 }
 } else {

 }
 }
 */
        }
        return null;
    }

    /* Flips the color of NODE and its children. Assume that NODE has both left
       and right children. */
    void flipColors(RBTreeNode<T> node) {
        node.isBlack = !node.isBlack;
        node.left.isBlack = !node.left.isBlack;
        node.right.isBlack = !node.right.isBlack;
    }

    /* Rotates the given node NODE to the right. Returns the new root node of
       this subtree. */
    RBTreeNode<T> rotateRight(RBTreeNode<T> node) {
        if (node.left != null) {
            return new RBTreeNode<T>(node.isBlack, node.left.item,
                    node.left.left,
                    new RBTreeNode<T>(false, node.item,
                            node.left.right,
                            node.right));
        }
        return node;
    }

    /* Rotates the given node NODE to the left. Returns the new root node of
       this subtree. */
    RBTreeNode<T> rotateLeft(RBTreeNode<T> node) {
        if (node.right != null) {
            return new RBTreeNode<T>(node.isBlack, node.right.item,
                    new RBTreeNode<T>(false, node.item,
                            node.left,
                            node.right.left
                    ),
                    node.right.right);
        }
        return node;
    }

    /* Insert ITEM into the red black tree, rotating
       it accordingly afterwards. */
    void insert(T item) {
        insertHelper(root, item);
    }

    private void insertHelper(RBTreeNode<T> x, T key) {
        if (x == null) {
            x = new RBTreeNode<T>(true, key);
        } else if (key.compareTo(x.item) < 0) {
            if (x.left == null) {
                x.left = new RBTreeNode<T>(false, key);
                helperHelper(x, key);
            } else {
                insertHelper(x.left, key);
            }
        } else {
            if (x.right == null) {
                x.right = new RBTreeNode<T>(false, key);
                helperHelper(x, key);
            } else {
                insertHelper(x.right, key);
            }
        }

    }

    private void helperHelper(RBTreeNode<T> x, T key) {
        if (!(x.left.item == key && x.right == null)) {
            if (x.right.item == key && x.left == null) {
                rotateLeft(x);
                /**
                 if (key.compareTo(x.item) < 0) {
                 x.left = new RBTreeNode<T>(false, key);
                 } else {
                 x.right = new RBTreeNode<T>(false, key);
                 rotateLeft(x);
                 }
                 */
            } else if (x.left != null || x.isBlack) {
                flipColors(x);
                helperHelper(x, key);
            } else if (x.left.item == key && !x.isBlack) {
                rotateRight(x);
                helperHelper(x, key);
            } else if (x.right.item == key && !x.isBlack) {
                rotateLeft(x);
                helperHelper(x, key);
            }
        }
    }

    /* Returns whether the given node NODE is red. Null nodes (children of leaf
       nodes are automatically considered black. */
    private boolean isRed(RBTreeNode<T> node) {
        return node != null && !node.isBlack;
    }

    static class RBTreeNode<T> {

        final T item;
        boolean isBlack;
        RBTreeNode<T> left;
        RBTreeNode<T> right;

        /* Creates a RBTreeNode with item ITEM and color depending on ISBLACK
           value. */
        RBTreeNode(boolean isBlack, T item) {
            this(isBlack, item, null, null);
        }

        /* Creates a RBTreeNode with item ITEM, color depending on ISBLACK
           value, left child LEFT, and right child RIGHT. */
        RBTreeNode(boolean isBlack, T item, RBTreeNode<T> left,
                   RBTreeNode<T> right) {
            this.isBlack = isBlack;
            this.item = item;
            this.left = left;
            this.right = right;
        }
    }

}
