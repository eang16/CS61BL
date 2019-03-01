//import java.util.*;

import java.util.*;


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

    public void addWEdge(int v1, int v2, int weight) {
        addEdge(v1, v2, weight);
    }

    /* Adds an undirected Edge (V1, V2) to the graph. */
    public void addUndirectedEdge(int v1, int v2) {
        addUndirectedEdge(v1, v2, 0);
    }

    public void addUndirectedWEdge(int v1, int v2, int weight) {
        addUndirectedEdge(v1, v2, weight);
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

            if (isAdjacent(start, adj)) {
                l.add(start);
                Collections.reverse(l);
                return l;
            }
        }
        l.add(start);
        Collections.reverse(l);
        return l;

    }

    private int adjFind(int start, int stop, List<Integer> l) {
        for (int i : l) {
            if (isAdjacent(i, stop) && pathExists(start, i)) {
                return i;
            }
        }
        return 10000;
    }

    public List<Integer> shortestPath(int start, int stop) {

        if (start == stop) {
            return null;
        }

        HashMap<Integer, Integer> weightMap = new HashMap<>();
        HashMap<Integer, List<Integer>> pathMap = new HashMap<>();
        List<Integer> visited = new ArrayList<>();

        PriorityQueue<Integer> fringe = new PriorityQueue<>((Object o1, Object o2) -> {
            double first = weightMap.get(o1);
            double second = weightMap.get(o2);

            double i = first - second;
            if (i < 0) {
                return -1;
            } else if (i > 0) {
                return 1;
            } else {
                return 0;
            }
        });


        if (adjLists[start].isEmpty()) {
            return null;
        }
        weightMap.put(start, 0);
        pathMap.put(start, new ArrayList<>());
        pathMap.get(start).add(start);

        for (Edge e : adjLists[start]) {
            weightMap.put(e.to, e.weight);
            pathMap.put(e.to, new ArrayList<>());
            pathMap.get(e.to).add(start);
            pathMap.get(e.to).add(e.to);
        }
        fringe.add(start);

        int v;

        while (!fringe.isEmpty()) {
            v = fringe.poll();

            if (v == stop) {
                ArrayList<Integer> copy = new ArrayList<>();
                for (int x = 0; x < pathMap.get(v).size(); x++) {
                    copy.add(pathMap.get(v).get(x));
                }
                pathMap.put(stop, copy);
                break;
            }


            visited.add(v);
            for (int i : neighbors(v)) {
                if (!visited.contains(i)) {
                    int newWeight = weightMap.get(v) + getEdge(v, i).weight;
                    if (!fringe.contains(i)) {
                        weightMap.put(i, newWeight);
                        //pathMap.put(i, new ArrayList<>());
                        ArrayList<Integer> copy = new ArrayList<>();
                        for (int x = 0; x < pathMap.get(v).size(); x++) {
                            copy.add(pathMap.get(v).get(x));
                        }
                        pathMap.put(i, copy);
                        pathMap.get(i).add(i);
                        fringe.add(i);

                    } else {
                        if (newWeight < weightMap.get(i)) {
                            weightMap.replace(i, newWeight);
                            ArrayList<Integer> copy = new ArrayList<>();
                            for (int x = 0; x < pathMap.get(v).size(); x++) {
                                copy.add(pathMap.get(v).get(x));
                            }
                            pathMap.put(i, copy);
                            pathMap.get(i).add(i);
                            fringe.add(i);
                        }
                    }

                }

            }

        }

        return pathMap.get(stop);
    }

    public Edge getEdge(int u, int v) {
        for (Edge e : adjLists[u]) {
            if (e.to == v) {
                return e;
            }
        }
        return null;
    }

    private int getPathWeight(int start, int stop) {
        int weight = 0;
        List<Integer> pathList = dfs(start);

        pathList = path(start, stop);
        for (int k = 1; k < pathList.size(); k++) {
            weight = weight + getEdge(pathList.get(k - 1), pathList.get(k)).weight;
        }
        return weight;
    }

    private List<Integer> pathFromList(int start, int stop, int prev, List<Integer> l) {
        List<Integer> result = new ArrayList<>();
        result.add(stop);
        result.add(prev);
//        int temp1 = stop;
//       int temp2 = prev;
        boolean tf = false;
        while (!tf) {
            for (Integer i : l) {
                if (isAdjacent(i, result.get(result.size() - 1)) && !result.contains(i)) {
                    result.add(i);
//                    temp1 = i;
                    if (i == start) {
                        Collections.reverse(result);
                        tf = true;
                        break;
                    }
                }
            }
        }
        return result;
    }


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

    public class Edge implements Comparable<Edge> {

        private int from;
        private int to;
        private int weight;

        Edge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        @Override
        public int compareTo(Edge n1) {
            // compare n1 and n2
            int diff = this.weight - n1.weight;
            if (diff > 0) {
                return 1;
            } else if (diff < 0) {
                return -1;
            } else {
                return 0;
            }
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

    private void printShortestPath(int start, int end) {
        System.out.println("Shortest path from " + start + " to " + end);
        List<Integer> result = shortestPath(start, end);
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
//            addEdge(0, 1);
//            addEdge(0, 2);
//            addEdge(0, 4);
//            addEdge(1, 2);
//            addEdge(2, 0);
//            addEdge(2, 3);
//            addEdge(4, 3);

//        g1.printDFS(0);
//        g1.printDFS(1);
//        g1.printDFS(2);
//        g1.printDFS(3);
//        g1.printDFS(4);
//
//        g1.printPath(0, 3);
//        g1.printPath(0, 2);
//        g1.printPath(0, 4);
//        g1.printPath(1, 3);
//        g1.printPath(1, 4);
//        g1.printPath(4, 0);

//        g1.printShortestPath(0, 3);
//        g1.printShortestPath(0, 4);
//        g1.printShortestPath(1, 3);
//        g1.printShortestPath(1, 4);
//        g1.printShortestPath(4, 0);

        Graph g2 = new Graph(5);
        g2.generateG2();
//        addEdge(0, 1);
//        addEdge(0, 2);
//        addEdge(0, 4);
//        addEdge(1, 2);
//        addEdge(2, 3);
//        addEdge(4, 3);

        Graph g3 = new Graph(7);
        //g3.generateG3();
        g3.addUndirectedWEdge(0, 6, 10);
        g3.addWEdge(0, 1, 1);
        g3.addWEdge(1, 2, 2);
        g3.addWEdge(2, 3, 4);
        g3.addWEdge(3, 4, 2);
        g3.addWEdge(4, 5, 0);
        g3.addWEdge(5, 6, 3);

        //       g3.addWEdge(0, 6, 5);
        g3.addWEdge(4, 6, 0);
//        g3.addWEdge(5, 6, 3);

        g3.printShortestPath(0, 6);

        Graph g4 = new Graph(5);
        //g3.generateG3();

        g4.addWEdge(0, 1, 10);
        g4.addWEdge(0, 3, 30);
        g4.addWEdge(0, 4, 100);


        g4.addWEdge(1, 2, 50);
        g4.addWEdge(2, 4, 10);
        g4.addWEdge(3, 2, 20);
        g4.addWEdge(3, 4, 60);

        g4.printShortestPath(0, 4);


    }


}
