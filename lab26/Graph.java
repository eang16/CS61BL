import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.io.IOException;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/* A mutable and finite Graph object. Edge labels are stored via a HashMap
   where labels are mapped to a key calculated by the following. The graph is
   undirected (whenever an Edge is added, the dual Edge is also added). Vertices
   are numbered starting from 0. */
public class Graph implements Comparator<Integer>{

    /* Maps vertices to a list of its neighboring vertices. */
    private HashMap<Integer, Set<Integer>> neighbors = new HashMap<>();
    /* Maps vertices to a list of its connected edges. */
    private HashMap<Integer, Set<Edge>> edges = new HashMap<>();
    /* A sorted set of all edges. */
    private TreeSet<Edge> allEdges = new TreeSet<>();

    private HashMap<Integer, Edge> distFromTree = new HashMap<>();

    /* Returns the vertices that neighbor V. */
    public TreeSet<Integer> getNeighbors(int v) {
        return new TreeSet<Integer>(neighbors.get(v));
    }

    /* Returns all edges adjacent to V. */
    public TreeSet<Edge> getEdges(int v) {
        return new TreeSet<Edge>(edges.get(v));
    }

    /* Returns a sorted list of all vertices. */
    public TreeSet<Integer> getAllVertices() {
        return new TreeSet<Integer>(neighbors.keySet());
    }

    /* Returns a sorted list of all edges. */
    public TreeSet<Edge> getAllEdges() {
        return new TreeSet<Edge>(allEdges);
    }

    /* Adds vertex V to the graph. */
    public void addVertex(Integer v) {
        if (neighbors.get(v) == null) {
            neighbors.put(v, new HashSet<Integer>());
            edges.put(v, new HashSet<Edge>());
        }
    }

    @Override
    public int compare(Integer x, Integer y) {
        Edge first = distFromTree.get(x);
        Edge second = distFromTree.get(y);
        Integer i = first.getWeight() - second.getWeight();
        return i;
    }

    /* Adds Edge E to the graph. */
    public void addEdge(Edge e) {
        addEdgeHelper(e.getSource(), e.getDest(), e.getWeight());
    }

    /* Creates an Edge between V1 and V2 with no weight. */
    public void addEdge(int v1, int v2) {
        addEdgeHelper(v1, v2, 0);
    }

    /* Creates an Edge between V1 and V2 with weight WEIGHT. */
    public void addEdge(int v1, int v2, int weight) {
        addEdgeHelper(v1, v2, weight);
    }

    /* Returns true if V1 and V2 are connected by an edge. */
    public boolean isNeighbor(int v1, int v2) {
        return neighbors.get(v1).contains(v2) && neighbors.get(v2).contains(v1);
    }

    /* Returns true if the graph contains V as a vertex. */
    public boolean containsVertex(int v) {
        return neighbors.get(v) != null;
    }

    /* Returns true if the graph contains the edge E. */
    public boolean containsEdge(Edge e) {
        return allEdges.contains(e);
    }

    /* Returns if this graph spans G. */
    public boolean spans(Graph g) {
        TreeSet<Integer> all = getAllVertices();
        if (all.size() != g.getAllVertices().size()) {
            return false;
        }
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> vertices = new ArrayDeque<>();
        Integer curr;

        vertices.add(all.first());
        while ((curr = vertices.poll()) != null) {
            if (!visited.contains(curr)) {
                visited.add(curr);
                for (int n : getNeighbors(curr)) {
                    vertices.add(n);
                }
            }
        }
        return visited.size() == g.getAllVertices().size();
    }

    /* Overrides objects equals method. */
    public boolean equals(Object o) {
        if (!(o instanceof Graph)) {
            return false;
        }
        Graph other = (Graph) o;
        return neighbors.equals(other.neighbors) && edges.equals(other.edges);
    }

    /* A helper function that adds a new edge from V1 to V2 with WEIGHT as the
       label. */
    private void addEdgeHelper(int v1, int v2, int weight) {
        addVertex(v1);
        addVertex(v2);

        neighbors.get(v1).add(v2);
        neighbors.get(v2).add(v1);

        Edge e1 = new Edge(v1, v2, weight);
        Edge e2 = new Edge(v2, v1, weight);
        edges.get(v1).add(e1);
        edges.get(v2).add(e2);
        allEdges.add(e1);
    }

    public Graph prims(int start) {
        Graph result = new Graph();
        List<Integer> in = new ArrayList<>();
        List<Integer> notIn = new ArrayList<>();
        in.add(start);
        for (int i : getAllVertices()) {
            distFromTree.put(i, new Edge(0, 0, 0));
        }
        result.addVertex(start);
        if(getNeighbors(start).size()==0){
            return result;
        }
//        PriorityQueue<Integer> notYet = new PriorityQueue<>((Object o1, Object o2) -> {
//            Edge first = getMinEdge((int) o1, in);
//            Edge second = getMinEdge((int) o2, in);
//
//            Integer i = first.getWeight() - second.getWeight();
//            return i;
//        });
//        PriorityQueue<Integer> notYet = new PriorityQueue<>((Object o1, Object o2) -> {
//            Edge first = distFromTree.get(o1);
//            Edge second = distFromTree.get(o2);
//            Integer i = first.getWeight() - second.getWeight();
//            return i;
//        });
        PriorityQueue<Integer> notYet = new PriorityQueue<>();
        int currVertex = start;

        for (int v : getNeighbors(start)) {
            if (v != start) {
                notIn.add(v);
                notYet.add(v);
            }
        }

        updateDist(in);
        int num = getAllVertices().size() - 1;
        //currVertex = notYet.poll();

        while (result.allEdges.size() != num) {
            currVertex = notYet.poll();

            Edge newEdge = getMinEdge(currVertex, in);

            if (!in.contains(currVertex)) {
                in.add(currVertex);
                result.addVertex(currVertex);
                result.addEdge(newEdge.getSource(), newEdge.getDest(), newEdge.getWeight());
            }
            //notYet.add(getVertex(in, notIn));
            updateDist(in);
//            notYet.clear();
            for (int i : getNeighbors(currVertex)) {
                if(notYet.contains(i)){
                    Edge newMinEdge = getMinEdge(i, in);
                    Edge oldMinEdge = distFromTree.get(i);
                    if(newMinEdge.getWeight()<oldMinEdge.getWeight()){
                        distFromTree.replace(i, newMinEdge);
                        notYet.add(i);
                    }
                }
                if (!result.getAllVertices().contains(i)) {
                    notYet.add(i);
                }
            }
        }
        return result;
    }

    public void updateDist(List<Integer> in) {
        distFromTree.clear();
        for (int i : getAllVertices()) {
            if (!in.contains(i)) {
                distFromTree.put(i, getMinEdge(i, in));
            }
        }
    }

//    public Edge findMinEdge(int vertex, List<Integer> in) {
//        Edge minEdge= null;
//        int minWeight = 100000000;
//
//        for (Edge e : getEdges(vertex)) {
//            if (in.contains(e.getDest())) {
//                if (e.getWeight() < minWeight) {
//                    minEdge = e;
//                    minWeight = e.getWeight();
//                }
//            }
//        }
//
//        return minEdge;
//    }

//    public Edge findEdge(int start, int end) {
//        for (Edge e : getEdges(start)) {
//            if (e.getDest() == end) {
//                return e;
//            }
//        }
//        return null;
//    }
//
//    public Edge getEdge(int start, List<Integer> in){
//        for(Edge e : getEdges(start)){
//            if(in.contains(e.getDest())){
//                return e;
//            }
//        }
//    }
//


    public Edge getMinEdge(int c, List<Integer> in) {
        Edge ed = null;
        int minWeight = 100000000;
        if (in == null || in.size() == 0) {
            return null;
        }
        for (Edge e : getEdges(c)) {
            if (in.contains(e.getDest())) {
                if (e.getWeight() < minWeight) {
                    ed = e;
                    minWeight = e.getWeight();
                }
            }
        }

        return ed;
    }

//    public Edge findEdge(int c, List<Integer> in) {
//        Edge ed = null;
//        int minWeight = 100000000;
//
//        for (Edge e : getEdges(c)) {
//            if (in.contains(e.getDest())) {
//                if (e.getWeight() < minWeight) {
//                    ed = e;
//                    minWeight = e.getWeight();
//                }
//            }
//        }
//
//        return ed;
//    }


    public Graph kruskals() {
        Graph g = new Graph();
        TreeSet edgeSet = getAllEdges();

        for (int i : getAllVertices()) {
            g.addVertex(i);
        }
        WeightedQuickUnionUF u = new WeightedQuickUnionUF(getAllVertices().size());

        for (Edge e : getAllEdges()) {
            if (!u.connected(e.getSource(), e.getDest())) {
                u.union(e.getSource(), e.getDest());
                g.addEdge(e.getSource(), e.getDest(), e.getWeight());
            }

        }
        return g;
    }

    /* Returns a randomly generated graph with VERTICES number of vertices and
       EDGES number of edges with max weight WEIGHT. */
    public static Graph randomGraph(int vertices, int edges, int weight) {
        Graph g = new Graph();
        Random rng = new Random();
        for (int i = 0; i < vertices; i += 1) {
            g.addVertex(i);
        }
        for (int i = 0; i < edges; i += 1) {
            Edge e = new Edge(rng.nextInt(vertices), rng.nextInt(vertices), rng.nextInt(weight));
            g.addEdge(e);
        }
        return g;
    }

    /* Returns a Graph object with integer edge weights as parsed from
       FILENAME. Talk about the setup of this file. */
    public static Graph loadFromText(String filename) {
        Charset cs = Charset.forName("US-ASCII");
        try (BufferedReader r = Files.newBufferedReader(Paths.get(filename), cs)) {
            Graph g = new Graph();
            String line;
            while ((line = r.readLine()) != null) {
                String[] fields = line.split(", ");
                if (fields.length == 3) {
                    int from = Integer.parseInt(fields[0]);
                    int to = Integer.parseInt(fields[1]);
                    int weight = Integer.parseInt(fields[2]);
                    g.addEdge(from, to, weight);
                } else if (fields.length == 1) {
                    g.addVertex(Integer.parseInt(fields[0]));
                } else {
                    throw new IllegalArgumentException("Bad input file!");
                }
            }
            return g;
        } catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
            System.exit(1);
            return null;
        }
    }

    public static void main(String[] args) {
        Graph p = randomGraph(10, 10, 5);
        p.prims(0);
    }

//    public class Distance implements Comparator<Integer>{
//
//        public int findDist(int start, int end){
//            for(Edge e: getEdges(start)){
//                if(e.getDest() == end){
//                    return e.getWeight();
//                }
//            }
//            return -1;
//        }
//
//        public int compare(Integer x, Integer y){
//
//        }
//    }
}
