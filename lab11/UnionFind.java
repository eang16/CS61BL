//import java.util.Collection;
//import java.util.Iterator;
//import java.util.Set;

public class UnionFind {

    int[] parent;


    /* Creates a UnionFind data structure holding N vertices. Initially, all
       vertices are in disjoint sets. */
    public UnionFind(int N) {
        parent = new int[N];
        /**
         parent[0] = -1;
         for (int i = 1; i < N; i++) {
         parent[i] = i - 1;
         parent[0] = parent[0] - 1;
         }
         */

        for (int i = 0; i < N; i++) {
            parent[i] = -1;
        }


    }

    /* Returns the size of the set V belongs to. */
    public int sizeOf(int v) {
        if (parent.length < v || v < 0) {
            return -1;
        }
        for (int i = v; i > 0; i--) {
            if (parent[i] < 0) {
                return parent[i] * -1;
            }
        }
        return -1;
    }

    /* Returns the parent of V. If V is the root of a tree, returns the
       negative size of the tree for which V is the root. */
    public int parent(int v) {
        if (parent.length < v || v < 0) {
            return -1;
        }
        if (parent[v] < 0) {
            return parent[v] * -1;
        } else {
            for (int i = v; i > 0; i--) {
                if (parent[i] == parent[v] - 1) {
                    return i;
                }
            }
        }
        return -1;
    }

    /* Returns true if nodes V1 and V2 are connected. */
    public boolean connected(int v1, int v2) {
        int p1 = -1;
        int p2 = -2;
        if (parent.length < v1 || parent.length < v2 || v1 < 0 || v2 < 0) {
            return false;
        }
        if (v1 == v2) {
            return true;
        }
        if (v1 != v2 && parent[v1] < 0 && parent[v2] < 0) {
            return false;
        }
        for (int i = v1; i >= 0; i--) {
            if (parent[i] < 0) {
                p1 = i;
                break;
            }
        }
        for (int i = v2; i >= 0; i--) {
            if (parent[i] < 0) {
                p2 = i;
                break;
            }

        }

        //p1 = find(v1);
        //p2 = find(v2);
        return (p1 == p2);
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       allowing for fast search-time. If invalid vertices are passed into this
       function, throw an IllegalArgumentException. */
    public int find(int v) {
        if (parent.length < v || v < 0) {
            throw new IllegalArgumentException();
        }
        if (parent[v] < 0) {
            return v;
        }

        for (int i = v; i > 0; i--) {
            if (parent[i] < 0) {
                parent[v] = i;
                return i;
            }
            if (parent[i] < parent[v]) {
                parent[i] = find(i);
            }
        }

        /**
         while (v != parent[v]) {
         parent[v] = parent[parent[v]];
         v = parent[v];
         }
         return v;
         */
        return v;
    }

    /* Connects two elements V1 and V2 together. V1 and V2 can be any element,
       and a union-by-size heuristic is used. If the sizes of the sets are
       equal, tie break by connecting V1's root to V2's root. Union-ing a vertex
       with itself or vertices that are already connected should not change the
       structure. */
    public void union(int v1, int v2) {
        if (!(parent.length < v1 || parent.length < v2 || v1 < 0 || v2 < 0 || v1 == v2)) {
            if (!(connected(v1, v2))) {
                int v1size = sizeOf(v1);
                int v2size = sizeOf(v2);
                int p1 = find(v1);
                int p2 = find(v2);

                if (v1size > v2size) {
                    parent[p1] += parent[p2];
                    parent[p2] = p1;
                } else {
                    parent[p2] += parent[p1];
                    parent[p1] = p2;
                }
                /**
                 for (int i = v1; i > 0; i--) {
                 if (parent[i] < 0) {
                 p1 = i;
                 }
                 }
                 for (int i = v2; i > 0; i--) {
                 if (parent[i] < 0) {
                 p2 = i;
                 }

                 }
                 */

                /**
                 if (v1size > v2size) {

                 for (int i = v1; i >= 0; i--) {
                 if (parent[i] < 0) {
                 parent[i] += v2size;
                 break;
                 }

                 }

                 //parent[v1] += v2size;
                 parent[v2] = v1;
                 //parent[v2] = v1;
                 } else {

                 for (int i = v2; i >= 0; i--) {
                 if (parent[i] < 0) {
                 parent[i] += v1size;
                 break;
                 }

                 }
                 //parent[v2] += v1size;
                 parent[v1] = v2;
                 //parent[v1] = v2;
                 }
                 */


            }
        }
    }

/**
 public static void main(String[] args) {
 UnionFind uf = new UnionFind(5);
 uf.union(0, 1);
 uf.union(1, 2);
 uf.union(2, 3);
 int x = uf.find(3);
 System.out.println(x);

 }
 */


}
