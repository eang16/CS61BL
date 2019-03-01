//import java.util.*;

import java.util.Collections;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Stack;
import java.util.HashSet;


public class Graph implements Iterable<Integer> {

    private LinkedList<Edge>[] adjLists;
    private int vertexCount;

    /* Initializes a graph with NUMVERTICES vertices and no Edges. */
    public Graph(int numVertices) {
        adjLists = (LinkedList<Edge>[]) new LinkedList[numVertices];
        for (int k = 0; k < numVertices; k++) {
            adjLists[k] = new LinkedList<Edge>();
        }
        vertexCount = numVertices;
    }

    /* Adds a directed Edge (V1, V2) to the graph. */
    public void addEdge(int v1, int v2) {
        addEdge(v1, v2, 0);
    }

    /* Adds an undirected Edge (V1, V2) to the graph. */
    public void addUndirectedEdge(int v1, int v2) {
        addUndirectedEdge(v1, v2, 0);
    }

    /* Adds a directed Edge (V1, V2) to the graph with weight WEIGHT. If the
       Edge already exists, replaces the current Edge with a new Edge with
       weight WEIGHT. */
    public void addEdge(int v1, int v2, int weight) {
        boolean tf = false;
        for (Edge e : adjLists[v1]) {
            if (e.to == v2) {
                e.weight = weight;
                tf = true;
            }
        }
        if (!tf) {
            adjLists[v1].add(new Edge(v1, v2, weight));
        }


    }

    /* Adds an undirected Edge (V1, V2) to the graph with weight WEIGHT. If the
       Edge already exists, replaces the current Edge with a new Edge with
       weight WEIGHT. */
    public void addUndirectedEdge(int v1, int v2, int weight) {
        boolean tf = false;
        for (Edge e : adjLists[v1]) {
            if (e.to == v2) {
                e.weight = weight;
                tf = true;
            }
        }
        if (!tf) {
            adjLists[v1].add(new Edge(v1, v2, weight));
        }
        tf = false;
        for (Edge e : adjLists[v2]) {
            if (e.to == v1) {
                e.weight = weight;
                tf = true;
            }
        }
        if (!tf) {
            adjLists[v2].add(new Edge(v2, v1, weight));
        }

    }

    /* Returns true if there exists an Edge from vertex FROM to vertex TO.
       Returns false otherwise. */
    public boolean isAdjacent(int from, int to) {
        for (Edge e : adjLists[from]) {
            if (e.to == to) {
                return true;
            }
        }
        return false;
    }

    /* Returns a list of all the vertices u such that the Edge (V, u)
       exists in the graph. */
    public List<Integer> neighbors(int v) {
//        if(v>adjLists.length){
//            return null;
//        }
        List<Integer> l = new ArrayList<>();
        for (Edge e : adjLists[v]) {
            l.add(e.to);
        }
        return l;
    }

    /* Returns the number of incoming Edges for vertex V. */
    public int inDegree(int v) {
//        if(v>adjLists.length){
//            return 0;
//        }
        int count = 0;
        for (int i = 0; i < adjLists.length; i++) {
            if (i != v) {
                for (Edge e : adjLists[i]) {
                    if (e.to == v) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    /* Returns an Iterator that outputs the vertices of the graph in topological
       sorted order. */
    public Iterator<Integer> iterator() {
        return new TopologicalIterator();
    }

    /* A class that iterates through the vertices of this graph, starting with
       vertex START. If the iteration from START has no path from START to some
       vertex v, then the iteration will not include v. */
    private class DFSIterator implements Iterator<Integer> {

        private Stack<Integer> fringe;
        private HashSet<Integer> visited;

        DFSIterator(int start) {
            fringe = new Stack<>();
            visited = new HashSet<>();
            fringe.push(start);
        }

        public boolean hasNext() {
            return !fringe.isEmpty();
        }

        public Integer next() {
            if (hasNext()) {
                int x = fringe.pop();
                if (visited.contains(x)) {
                    return next();
                } else {
                    if (!visited.isEmpty()) {

                        for (Edge e : adjLists[x]) {
//                            for (int i : visited) {
                            //if (!adjLists[i].equals(adjLists[e.to])) {
                            if (!visited.contains(e.to)) {
                                fringe.push(e.to);
//                                }
                            }
                        }
                    } else {
                        for (Edge e : adjLists[x]) {
                            fringe.push(e.to);
                        }
                    }
                    visited.add(x);

                    return x;
                }
            }
            return null;


        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    /* Returns the collected result of performing a depth-first search on this
       graph's vertices starting from V. */
    public List<Integer> dfs(int v) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> iter = new DFSIterator(v);

        while (iter.hasNext()) {
            result.add(iter.next());
        }
        if (result.contains(null)) {
            result.remove(null);
        }
        return result;
    }

    /* Returns true iff there exists a path from START to STOP. Assumes both
       START and STOP are in this graph. If START == STOP, returns true. */
    public boolean pathExists(int start, int stop) {
        if (start == stop) {
            return true;
        }
//        Iterator<Integer> i = new DFSIterator(start);
//        Integer x = i.next();
//        while (i.hasNext()) {
//            if (x == stop) {
//                return true;
//            }
//            x = i.next();
//        }
//        return false;
        List<Integer> i = dfs(start);
        if (i.contains(stop)) {
            return true;
        }
        return false;
    }


    /* Returns the path from START to STOP. If no path exists, returns an empty
       List. If START == STOP, returns a List with START. */
    public List<Integer> path(int start, int stop) {
        List<Integer> l = new ArrayList<>();
        if (!pathExists(start, stop)) {
            return l;
        }

        List<Integer> it = dfs(start);
        boolean tf = true;

        ArrayList<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> iter = new DFSIterator(start);

        while (iter.hasNext()) {
            result.add(iter.next());
            if (result.contains(stop)) {
                break;
            }
        }


        boolean b = false;
        l.add(stop);
        int adj = stop;
        while (!b) {
            adj = adjFind(start, adj, result);
            if (adj == start) {
                l.add(start);
                Collections.reverse(l);
                return l;
            }
            l.add(adj);
//            result.remove(adj);

            if (isAdjacent(start, adj)) {
                l.add(start);
                Collections.reverse(l);
                return l;
            }
        }
        l.add(start);
        Collections.reverse(l);
        return l;


//        Iterator<Integer> i = new DFSIterator(start);
//        l.add(start);
//        while (i.hasNext()) {
//            Integer x = i.next();
//            if (pathExists(l.get(l.size()-1), x) && pathExists(x, stop)) {
//                l.add(x);
//            }
//        }
//        return l;
    }

    private int adjFind(int start, int stop, List<Integer> l) {
        for (int i : l) {
            if (isAdjacent(i, stop) && pathExists(start, i)) {
                return i;
            }
        }
        return 10000;
    }

//    private boolean adjHelper(int start, int stop, int origin){
//        for(Edge e : adjLists[start]){
//            if(e.to != origin){
//                if(e.to == stop){
//                    return true;
//                } else {
//                    if(adjHelper(e.to, stop, ));
//                }
//            }
//        }
//        return false;
//    }

    public List<Integer> topologicalSort() {
        ArrayList<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> iter = new TopologicalIterator();
        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }

    private class TopologicalIterator implements Iterator<Integer> {

        private Stack<Integer> fringe;


        TopologicalIterator() {
            fringe = new Stack<Integer>();
        }

        public boolean hasNext() {
            return false;
        }

        public Integer next() {
            return 0;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    private class Edge {

        private int from;
        private int to;
        private int weight;

        Edge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        public String toString() {
            return "(" + from + ", " + to + ", weight = " + weight + ")";
        }

    }

    private void generateG1() {
        addEdge(0, 1);
        addEdge(0, 2);
        addEdge(0, 4);
        addEdge(1, 2);
        addEdge(2, 0);
        addEdge(2, 3);
        addEdge(4, 3);
    }

    private void generateG2() {
        addEdge(0, 1);
        addEdge(0, 2);
        addEdge(0, 4);
        addEdge(1, 2);
        addEdge(2, 3);
        addEdge(4, 3);
    }

    private void generateG3() {
        addUndirectedEdge(0, 2);
        addUndirectedEdge(0, 3);
        addUndirectedEdge(1, 4);
        addUndirectedEdge(1, 5);
        addUndirectedEdge(2, 3);
        addUndirectedEdge(2, 6);
        addUndirectedEdge(4, 5);
    }

    private void generateG4() {
        addEdge(0, 1);
        addEdge(1, 2);
        addEdge(2, 0);
        addEdge(2, 3);
        addEdge(4, 2);
    }

    private void printDFS(int start) {
        System.out.println("DFS traversal starting at " + start);
        List<Integer> result = dfs(start);
        Iterator<Integer> iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
        System.out.println();
        System.out.println();
    }

    private void printPath(int start, int end) {
        System.out.println("Path from " + start + " to " + end);
        List<Integer> result = path(start, end);
        if (result.size() == 0) {
            System.out.println("No path from " + start + " to " + end);
            return;
        }
        Iterator<Integer> iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
        System.out.println();
        System.out.println();
    }

    private void printTopologicalSort() {
        System.out.println("Topological sort");
        List<Integer> result = topologicalSort();
        Iterator<Integer> iter = result.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next() + " ");
        }
    }

    public static void main(String[] args) {
        Graph g1 = new Graph(5);
        g1.generateG1();
        g1.printDFS(0);
        g1.printDFS(2);
        g1.printDFS(3);
        g1.printDFS(4);

        g1.printPath(0, 3);
        g1.printPath(0, 4);
        g1.printPath(1, 3);
        g1.printPath(1, 4);
        g1.printPath(4, 0);

        Graph g2 = new Graph(5);
        g2.generateG2();
//        addEdge(0, 1);
//        addEdge(0, 2);
//        addEdge(0, 4);
//        addEdge(1, 2);
//        addEdge(2, 3);
//        addEdge(4, 3);
        System.out.println(g2.pathExists(0, 4)); //true
        System.out.println(g2.pathExists(0, 3)); //false
        System.out.println(g2.pathExists(0, 2)); //true
        System.out.println(g2.pathExists(0, 1)); //true
        System.out.println(g2.pathExists(1, 2)); //true
        System.out.println(g2.pathExists(2, 3)); //true
        System.out.println(g2.pathExists(3, 2)); //false

//        Graph g3 = new Graph(7);
//        //g3.generateG3();
//        g3.addUndirectedEdge(0, 2);
//        g3.addUndirectedEdge(2, 3);
//        g3.addUndirectedEdge(3, 4);
//        g3.addUndirectedEdge(4, 5);
//        g3.addUndirectedEdge(5, 6);
////        g3.addUndirectedEdge(3, 6);
////        g3.addUndirectedEdge(1, 5);
//        System.out.println(g3.pathExists(0, 2)); //true
//        System.out.println(g3.pathExists(0, 3)); //false
//        System.out.println(g3.pathExists(0, 4)); //true
//        System.out.println(g3.pathExists(0, 5)); //false
////        System.out.println(g2.pathExists(1,2));//true
////        System.out.println(g2.pathExists(2,3));//true
////        System.out.println(g2.pathExists(3,2));//false
//        g3.printPath(0, 2);
//        g3.printPath(0, 4);
//        g3.printPath(0, 5);
//        g3.printPath(3, 6);
//        g3.printPath(4, 0);
////        g2.printTopologicalSort();



        Graph g3 = new Graph(5);
        //g3.generateG3();
        g3.addEdge(0, 1);
        g3.addEdge(1, 0);
        g3.addEdge(0, 2);
        g3.addEdge(2, 0);
        g3.addEdge(0, 3);
        g3.addEdge(3, 0);
        g3.addEdge(0, 4);
        g3.addEdge(4, 0);
        g3.addEdge(1, 2);
        g3.addEdge(2, 1);
        g3.addEdge(1, 3);
        g3.addEdge(3, 1);
        g3.addEdge(1, 4);
        g3.addEdge(4, 1);
        g3.addEdge(2, 3);
        g3.addEdge(3, 2);
        g3.addEdge(2, 4);
        g3.addEdge(4, 2);
        g3.addEdge(3, 4);
        g3.addEdge(4, 3);
//        g3.addUndirectedEdge(3, 6);
//        g3.addUndirectedEdge(1, 5);
        System.out.println(g3.pathExists(0, 4)); // all true
        System.out.println(g3.pathExists(1, 3)); //false
        System.out.println(g3.pathExists(3, 0)); //true
        System.out.println(g3.pathExists(4, 2)); //false
//        System.out.println(g2.pathExists(1,2));//true
//        System.out.println(g2.pathExists(2,3));//true
//        System.out.println(g2.pathExists(3,2));//false
        g3.printPath(0, 1);
        g3.printPath(0, 2);
        g3.printPath(0, 3);
        g3.printPath(0, 4);
        g3.printPath(1, 0);
        g3.printPath(1, 2);
        g3.printPath(1, 3);
        g3.printPath(1, 4);
        g3.printPath(2, 0);
        g3.printPath(2, 1);
        g3.printPath(2, 3);
        g3.printPath(2, 4);
        g3.printPath(3, 0);
        g3.printPath(3, 2);
        g3.printPath(3, 1);
        g3.printPath(3, 4);
        g3.printPath(4, 0);
        g3.printPath(4, 2);
        g3.printPath(4, 3);
        g3.printPath(4, 1);
        g3.printDFS(0);
        g3.printDFS(1);
        g3.printDFS(2);
        g3.printDFS(3);
        g3.printDFS(4);


    }
}
