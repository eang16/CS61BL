
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {


    boolean[][] open;
    int size;
    int end;
    int numOpen = 0;
    WeightedQuickUnionUF check;

    /* Creates an N-by-N grid with all sites initially blocked. */
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        size = N;
        open = new boolean[N][N];
        end = N * N + 1;
        check = new WeightedQuickUnionUF(size * size + 2);
    }

    /* Opens the site (row, col) if it is not open already. */
    public void open(int row, int col) {
        if (valid(row, col)) {
            if (!isOpen(row, col)) {
                open[row][col] = true;
                numOpen++;
/**
 if(r-1>=0&&isOpen(r, col)){
 check.union(xyTo1D(row, col), xyTo1D(r, col));
 }

 if(r+1<size&&isOpen(row +1, col)){
 check.union(xyTo1D(row, col), xyTo1D(row+1, col));
 }

 if(c-1>=0&&isOpen(row, c)){
 check.union(xyTo1D(row, col), xyTo1D(row, c));
 }

 if(c+1<size&&isOpen(row, col+1)){
 check.union(xyTo1D(row, col), xyTo1D(row, col+1));
 }

 */


                if (row == 0) {
                    check.union(0, xyTo1D(row, col));
                }
                if (row == size - 1) {
                    check.union(xyTo1D(row, col), end);
                }

                isAdjacent(row, col);

            }
        }
//
    }

    private void isAdjacent(int row, int col) {
        if (row > 0 && isOpen(row - 1, col)) { //up
            check.union(xyTo1D(row, col), xyTo1D(row - 1, col));
        }
        if (row < size - 1 && isOpen(row + 1, col)) { //down
            check.union(xyTo1D(row, col), xyTo1D(row + 1, col));
        }
        if (col > 0 && isOpen(row, col - 1)) {  //left
            check.union(xyTo1D(row, col), xyTo1D(row, col - 1));
        }
        if (col < size - 1 && isOpen(row, col + 1)) { //right
            check.union(xyTo1D(row, col), xyTo1D(row, col + 1));
        }
    }

    /* Returns true if the site at (row, col) is open. */
    public boolean isOpen(int row, int col) {
        if (valid(row, col)) {
            return open[row][col];
        }
        return false;
    }

    /* Returns true if the site (row, col) is full. */
    public boolean isFull(int row, int col) {
        if (valid(row, col)) {
            if (isOpen(row, col)) {
                return check.connected(0, xyTo1D(row, col));
            }
            return false;
        }
        return false;
    }


    /* Returns the number of open sites. */
    public int numberOfOpenSites() {
        return numOpen;
    }

    /* Returns true if the system percolates. */
    public boolean percolates() {
        return check.connected(0, end);
    }

    /* Converts row and column coordinates into a number. This will be helpful
       when trying to tie in the disjoint sets into our NxN grid of sites. */
    private int xyTo1D(int row, int col) {
        if (valid(row, col)) {
            if (row == 0) {
                return col;
            }
            return size * (row) + (col);
        }
        return -1;
    }

    /* Returns true if (row, col) site exists in the NxN grid of sites.
       Otherwise, return false. */
    private boolean valid(int row, int col) {
        /**
         if ((row < 1 || row > size) || (col < 1 || col > size)) {
         return false;
         }
         return true;
         */
        if (row >= 0 && row < size && col >= 0 && col < size) {
            return true;
        } else {
            return false;
        }
    }
}
