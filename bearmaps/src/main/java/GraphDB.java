
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.Collections;
import java.util.List;
import java.util.HashSet;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Set;
import java.util.PriorityQueue;
import java.util.Iterator;
import java.util.Comparator;
import java.util.NoSuchElementException;
/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Kevin Lowe, Antares Chen, Kevin Lin
 */
public class GraphDB {
    /**
     * This constructor creates and starts an XML parser, cleans the nodes, and prepares the
     * data structures for processing. Modify this constructor to initialize your data structures.
     *
     * @param dbPath Path to the XML file to be parsed.
     */

    /* Maps vertices to a list of its neighboring vertices. */
    private HashMap<Long, Set<Long>> neighbors = new HashMap<>();
    private HashMap<Long, Vertex> idMap = new HashMap<>();

    private TreeSet<Long> allVertices = new TreeSet<>();

    private HashMap<String, Long> locations = new HashMap<>();
    private HashMap<String, List<Edge>> roads = new HashMap<>();
    private List<Edge> possibleEdges = new ArrayList<>();
    private HashMap<Long, Double> xMap = new HashMap<>();
    private HashMap<Long, Double> yMap = new HashMap<>();

    /* Maps vertices to a list of its connected edges. */
    private HashMap<Long, Set<Edge>> edges = new HashMap<>();
    /* A sorted set of all edges. */
    private TreeSet<Edge> allEdges = new TreeSet<>();
    KDTree kd;
    double minimumLat;
    double minimumLon;


    //private HashMap<Vertex, Edge> distFromTree = new HashMap<>();

    /* Returns the vertices that neighbor V. */
    private TreeSet<Long> getNeighbors(long v) {
        return new TreeSet<Long>(neighbors.get(v));
    }

    /* Returns all edges adjacent to V. */
    private TreeSet<Edge> getEdges(long v) {
        return new TreeSet<Edge>(edges.get(v));
    }

    /* Returns a sorted list of all vertices. */
    private TreeSet<Long> getAllVertices() {
        return new TreeSet<Long>(neighbors.keySet());
    }

    /* Returns a sorted list of all edges. */
    public TreeSet<Edge> getAllEdges() {
        return new TreeSet<Edge>(allEdges);
    }

    /* Adds vertex V to the graph. */
    public void addVertex(long id, double lon, double lat) {
        if (neighbors.get(id) == null) {
            Vertex v = new Vertex(lon, lat, id);
            neighbors.put(id, new HashSet<Long>());
            edges.put(id, new HashSet<Edge>());
            idMap.put(id, v);
            allVertices.add(id);
            xMap.put(id, projectToX(lon, lat));
            yMap.put(id, projectToY(lon, lat));
        }
    }

    public void deleteVertex(long id) {
        allVertices.remove(id);
        idMap.remove(id);
        neighbors.remove(id);
        edges.remove(id);
    }
//
//    public Vertex getVertex(long vid){
//        for(Vertex v : allVertices){
//            if(v.id == vid){
//                return v;
//            }
//        }
//        return null;
//    }

    /* Creates an Edge between V1 and V2. */
    public void addEdge(long v1, long v2) {

        neighbors.get(v1).add(v2);
        neighbors.get(v2).add(v1);

        Edge e1 = new Edge(v1, v2);
        Edge e2 = new Edge(v2, v1);
        edges.get(v1).add(e1);
        edges.get(v2).add(e2);
        allEdges.add(e1);
    }

    public void addPossibleEdge(long v1, long v2) {
        Edge e1 = new Edge(v1, v2);
//        Edge e2 = new Edge(v2, v1);
        possibleEdges.add(e1);
//        possibleEdges.add(e2);
    }

    public void addPossibles(String name) {
        if (!roads.containsKey(name)) {
            roads.put(name, new ArrayList<Edge>());
        }
        for (Edge e : possibleEdges) {
            addEdge(e.dest, e.src);
            roads.get(name).add(e);
        }
        possibleEdges.clear();
    }

    public void clearPossibles() {
        possibleEdges.clear();
    }

    public void addLocation(String name, long id) {
        locations.put(name, id);
    }

    /* Returns true if V1 and V2 are connected by an edge. */
    public boolean isNeighbor(long v1, long v2) {
        return neighbors.get(v1).contains(v2) && neighbors.get(v2).contains(v1);
    }

    /* Returns true if the graph contains V as a vertex. */
    public boolean containsVertex(long v) {
        return neighbors.get(v) != null;
    }


    public double getEdgeDistance(long v1, long v2) {
        for (Edge e : getEdges(v1)) {
            if (e.dest == v2) {
                return e.distance;
            }
        }
        return 0;
    }

    public Edge getEdge(long v1, long v2) {
        for (Edge e : getEdges(v1)) {
            if (e.dest == v2) {
                return e;
            }
        }
        return null;
    }


    public List<Long> shortestPath(long start, long stop) {
        if (start == stop) {
            return null;
        }
        HashMap<Long, Double> weightMap = new HashMap<>();
        HashMap<Long, List<Long>> pathMap = new HashMap<>();
        List<Long> visited = new ArrayList<>();
        PriorityQueue<Long> fringe = new PriorityQueue<>((Object o1, Object o2) -> {
            double first = weightMap.get(o1) + getHeuristic((long) o1, stop);
            double second = weightMap.get(o2) + getHeuristic((long) o1, stop);

            double i = first - second;
            if (i < 0) {
                return -1;
            } else if (i > 0) {
                return 1;
            } else {
                return 0;
            }
        });
        if (getEdges(start).isEmpty()) {
            return null;
        }
        weightMap.put(start, 0.0);
        pathMap.put(start, new ArrayList<>());
        pathMap.get(start).add(start);
        for (Edge e : getEdges(start)) {
            weightMap.put(e.dest, e.distance);
            pathMap.put(e.dest, new ArrayList<>());
            pathMap.get(e.dest).add(start);
            pathMap.get(e.dest).add(e.dest);
        }
        fringe.add(start);
        long v;
        while (!fringe.isEmpty()) {
            v = fringe.poll();
            if (v == stop) {
                ArrayList<Long> copy = new ArrayList<>(pathMap.get(v));
                pathMap.put(stop, copy);
                break;
            }
            visited.add(v);
            for (long i : getNeighbors(v)) {
                if (!visited.contains(i)) {
                    double newWeight = weightMap.get(v) + getEdgeDistance(v, i);
                    if (!fringe.contains(i)) {
                        weightMap.put(i, newWeight);
                        ArrayList<Long> copy = new ArrayList<>(pathMap.get(v));
                        pathMap.put(i, copy);
                        pathMap.get(i).add(i);
                        fringe.add(i);
                    } else {
                        if (newWeight < weightMap.get(i)) {
                            weightMap.replace(i, newWeight);
                            ArrayList<Long> copy = new ArrayList<>(pathMap.get(v));
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

    public double getHeuristic(long v1, long v2) {
        double dist = distance(v1, v2);
        return dist * 1.8;
    }


    public GraphDB(String dbPath) {
        File inputFile = new File(dbPath);
        try (FileInputStream inputStream = new FileInputStream(inputFile)) {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(inputStream, new GraphBuildingHandler(this));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
        kd = new KDTree(this);

    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     *
     * @param s Input string.
     * @return Cleaned string.
     */
    private static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     * Remove nodes with no connections from the graph.
     * While this does not guarantee that any two nodes in the remaining graph are connected,
     * we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        for (long v : getAllVertices()) {
            if (getNeighbors(v).size() == 0) {
                deleteVertex(v);
            }
        }
    }

    /**
     * Returns the longitude of vertex <code>v</code>.
     *
     * @param v The ID of a vertex in the graph.
     * @return The longitude of that vertex, or 0.0 if the vertex is not in the graph.
     */
    double lon(long v) {
        Vertex vert = idMap.get(v);
        return vert.lon;
    }

    /**
     * Returns the latitude of vertex <code>v</code>.
     *
     * @param v The ID of a vertex in the graph.
     * @return The latitude of that vertex, or 0.0 if the vertex is not in the graph.
     */
    double lat(long v) {
        Vertex vert = idMap.get(v);
        return vert.lat;
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     *
     * @return An iterable of all vertex IDs in the graph.
     */
    Iterable<Long> vertices() {

        return new Iterable<Long>() {
            @Override
            public Iterator<Long> iterator() {
                return new Iterator<Long>() {
                    TreeSet<Long> l = allVertices;

                    @Override
                    public boolean hasNext() {
                        return !l.isEmpty();
                    }

                    @Override
                    public Long next() {
                        if (!hasNext()) {
                            throw new NoSuchElementException();
                        }
                        long v = l.first();
                        l.remove(v);
                        return v;
                    }
                };
            }
        };
        //return Collections.emptySet();
    }

    /**
     * Returns an iterable over the IDs of all vertices adjacent to <code>v</code>.
     *
     * @param v The ID for any vertex in the graph.
     * @return An iterable over the IDs of all vertices adjacent to <code>v</code>, or an empty
     * iterable if the vertex is not in the graph.
     */
    Iterable<Long> adjacent(long v) {

        return new Iterable<Long>() {
            @Override
            public Iterator<Long> iterator() {
                return new Iterator<Long>() {
                    TreeSet<Long> l = getNeighbors(v);

                    @Override
                    public boolean hasNext() {
                        return !l.isEmpty();
                    }

                    @Override
                    public Long next() {
                        if (!hasNext()) {
                            throw new NoSuchElementException();
                        }
                        long v = l.first();
                        l.remove(v);
                        return v;

                    }
                };
            }
        };
        //return Collections.emptySet();
    }

    /**
     * Returns the great-circle distance between two vertices, v and w, in miles.
     * Assumes the lon/lat methods are implemented properly.
     *
     * @param v The ID for the first vertex.
     * @param w The ID for the second vertex.
     * @return The great-circle distance between vertices and w.
     * @source https://www.movable-type.co.uk/scripts/latlong.html
     */
    public double distance(long v, long w) {
        double phi1 = Math.toRadians(lat(v));
        double phi2 = Math.toRadians(lat(w));
        double dphi = Math.toRadians(lat(w) - lat(v));
        double dlambda = Math.toRadians(lon(w) - lon(v));

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    public double distanceFromMinimum(long v) {
        double phi1 = Math.toRadians(lat(v));
        double phi2 = Math.toRadians(minimumLat);
        double dphi = Math.toRadians(minimumLat - lat(v));
        double dlambda = Math.toRadians(minimumLon - lon(v));

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    /**
     * Returns the ID of the vertex closest to the given longitude and latitude.
     *
     * @param lon The given longitude.
     * @param lat The given latitude.
     * @return The ID for the vertex closest to the <code>lon</code> and <code>lat</code>.
     */
//    public long closest(double lon, double lat) {
//
//
//        long close = 100;
//        double dist = 100;
//        Vertex temp = new Vertex(lon, lat, 0);
//        idMap.put(temp.id, temp);
//        for (long v : getAllVertices()) {
//            if (distance(v, temp.id) < dist) {
//                close = v;
//                dist = distance(v, temp.id);
//            }
//        }
//        idMap.remove(temp.id);
//        return close;
//    }



//
    public long closest(double lon, double lat) {
        double dist = 100;
        double x = projectToX(lon, lat);
        double y = projectToY(lon, lat);
        long close = kd.findClosest(x, y);
        return close;
    }


    static double euclidean(double x1, double x2, double y1, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }


    /**
     * Return the Euclidean x-value for some point, p, in Berkeley. Found by computing the
     * Transverse Mercator projection centered at Berkeley.
     *
     * @param lon The longitude for p.
     * @param lat The latitude for p.
     * @return The flattened, Euclidean x-value for p.
     * @source https://en.wikipedia.org/wiki/Transverse_Mercator_projection
     */
    static double projectToX(double lon, double lat) {
        double dlon = Math.toRadians(lon - ROOT_LON);
        double phi = Math.toRadians(lat);
        double b = Math.sin(dlon) * Math.cos(phi);
        return (K0 / 2) * Math.log((1 + b) / (1 - b));
    }

    /**
     * Return the Euclidean y-value for some point, p, in Berkeley. Found by computing the
     * Transverse Mercator projection centered at Berkeley.
     *
     * @param lon The longitude for p.
     * @param lat The latitude for p.
     * @return The flattened, Euclidean y-value for p.
     * @source https://en.wikipedia.org/wiki/Transverse_Mercator_projection
     */
    static double projectToY(double lon, double lat) {
        double dlon = Math.toRadians(lon - ROOT_LON);
        double phi = Math.toRadians(lat);
        double con = Math.atan(Math.tan(phi) / Math.cos(dlon));
        return K0 * (con - Math.toRadians(ROOT_LAT));
    }

    /**
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     *
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */
    public List<String> getLocationsByPrefix(String prefix) {
        return Collections.emptyList();
    }

    /**
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     *
     * @param locationName A full name of a location searched for.
     * @return A <code>List</code> of <code>LocationParams</code> whose cleaned name matches the
     * cleaned <code>locationName</code>
     */
    public List<LocationParams> getLocations(String locationName) {
        return Collections.emptyList();
    }

    /**
     * Returns the initial bearing between vertices <code>v</code> and <code>w</code> in degrees.
     * The initial bearing is the angle that, if followed in a straight line along a great-circle
     * arc from the starting point, would take you to the end point.
     * Assumes the lon/lat methods are implemented properly.
     *
     * @param v The ID for the first vertex.
     * @param w The ID for the second vertex.
     * @return The bearing between <code>v</code> and <code>w</code> in degrees.
     * @source https://www.movable-type.co.uk/scripts/latlong.html
     */
    double bearing(long v, long w) {
        double phi1 = Math.toRadians(lat(v));
        double phi2 = Math.toRadians(lat(w));
        double lambda1 = Math.toRadians(lon(v));
        double lambda2 = Math.toRadians(lon(w));

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Radius of the Earth in miles.
     */
    private static final int R = 3963;
    /**
     * Latitude centered on Berkeley.
     */
    private static final double ROOT_LAT = (MapServer.ROOT_ULLAT + MapServer.ROOT_LRLAT) / 2;
    /**
     * Longitude centered on Berkeley.
     */
    private static final double ROOT_LON = (MapServer.ROOT_ULLON + MapServer.ROOT_LRLON) / 2;
    /**
     * Scale factor at the natural origin, Berkeley. Prefer to use 1 instead of 0.9996 as in UTM.
     *
     * @source https://gis.stackexchange.com/a/7298
     */
    private static final double K0 = 1.0;


    /* An Edge class representing an edge between two integer vertices. The weight
   of the vertex is also an int because I'm lazy and didn't want to deal with
   generics. */


    /* An Edge class representing an edge between two integer vertices. The weight
   of the vertex is also an int because I'm lazy and didn't want to deal with
   generics. */

    public class Edge implements Comparable<Edge> {

        long src;
        long dest;
        double distance;

        /* Creates an Edge (SRC, DEST) with edge weight WEIGHT. */
        Edge(long src, long dest) {
            this.src = src;
            this.dest = dest;
            this.distance = distance(src, dest);
        }

        /* Returns the edge's source node. */
        public long getSource() {
            return src;
        }

        /* Returns the edge's destination node. */
        public long getDest() {
            return dest;
        }

        /* Returns the weight of the edge. */
        public double getWeight() {
            return distance;
        }

        public int compareTo(Edge other) {
            double cmp = distance - other.distance;
            //return cmp == 0 ? 1 : cmp;
            if (cmp < 0) {
                return -1;
            } else if (cmp > 0) {
                return 1;
            } else {
                return 0;
            }
        }


        /* Returns the string representation of an edge. */
        public String toString() {
            return "{" + src + ", " + dest + "} -> " + distance;
        }
    }


    public class Vertex {

        double lon;
        double lat;
        long id;

        /* Creates an Edge (SRC, DEST) with edge weight WEIGHT. */
        Vertex(double lon, double lat, long id) {
            this.lon = lon;
            this.lat = lat;
            this.id = id;
        }

        /* Returns the edge's source node. */
        public long getID() {
            return this.id;
        }

        /* Returns the edge's destination node. */
        public double getLat() {
            return this.lat;
        }

        /* Returns the weight of the edge. */
        public double getLon() {
            return this.lat;
        }

//        public int compareTo(Vertex other) {
//            int cmp =  weight - other.weight;
//            return cmp == 0 ? 1 : cmp;
//        }

        /* Returns true if two Vertices have the same lon, lat, and
           id. */


        /* Returns the string representation of an edge. */
        public String toString() {
            return "(" + lon + ", " + lat + ") -> " + id;
        }
    }

    public class KDTree {

        int factor = 2;
        int size = 0;
        Node root;


        private Comparator<Long> compareH = new Comparator<Long>() {
            public int compare(Long v1, Long v2) {
//                double diff = (projectToX(idMap.get(v1).lon, idMap.get(v1).lat))
//                        - (projectToX(idMap.get(v2).lon, idMap.get(v2).lat));
                double diff = xMap.get(v1) - xMap.get(v2);
                if (diff < 0) {
                    return -1;
                } else if (diff > 0) {
                    return 1;
                } else {
                    return 0;
                }
            }
        };

        private Comparator<Long> compareV = new Comparator<Long>() {
            public int compare(Long v1, Long v2) {
//                double diff = (projectToY(idMap.get(v1).lon, idMap.get(v1).lat))
//                        - (projectToY(idMap.get(v2).lon, idMap.get(v2).lat));
                double diff = yMap.get(v1) - yMap.get(v2);

                if (diff < 0) {
                    return -1;
                } else if (diff > 0) {
                    return 1;
                } else {
                    return 0;
                }
            }
        };
        public KDTree(GraphDB g) {
            List<Long> l = new ArrayList<>(g.allVertices);
            if (l.size() != 0) {
                if (l.size() == 1) {
                    root = new Node(l.get(0));
                } else {


                    root = helper3(l, null, true);

                }
            }
        }


        private Node helper3(List<Long> v, Node p, boolean hv) {
            if (v == null || v.isEmpty()) {
                return null;
            }

            if (hv) {
                Collections.sort(v, compareH);
            } else {
                Collections.sort(v, compareV);
            }

            List<Long> left = new ArrayList<>();
            List<Long> right = new ArrayList<>();
            Node n = null;

            if (!v.isEmpty()) {
                int i = v.size() / 2;
                long median = v.get(i);
                n = new Node(median);
                n.parent = p;

                for (int k = 0; k < v.size(); k++) {
                    if (k < i) {
                        left.add(v.get(k));
                    }
                    if (k > i) {
                        right.add(v.get(k));
                    }
                }

                if (!left.isEmpty() && i != 0) {

                    n.left = helper3(left, n, !hv);
                }
                if (!right.isEmpty() && i != left.size() - 1) {
                    n.right = helper3(right, n, !hv);
                }
            }

            return n;
        }

        public long findClosest(double x, double y) {
            return getClosestDown(root, x, y, root.id, true);

        }

        public long getClosestDown(Node r, double x, double y, long best, boolean hv) {
            if (r != null) {
                double newDist;
                double thisDist = euclidean(r.x, x, r.y, y);
                double bestDist = euclidean(xMap.get(best), x, yMap.get(best), y);
                if (thisDist < bestDist) {
                    bestDist = thisDist;
                    best = r.id;
                }
                if (hv) {
                    if (r.left != null) {
                        if (r.left.isLeaf()) {
                            newDist = euclidean(r.left.x, x, r.left.y, y);
                            if (newDist < bestDist) {
                                bestDist = newDist;
                                best = r.left.id;
                            }
                        } else {
                            best = getClosestDown(r.left, x, y, best, !hv);
                        }
                    } else {
                        if (r.right != null) {
                            if (r.right.isLeaf()) {
                                newDist = euclidean(r.right.x, x, r.right.y, y);
                                if (newDist < bestDist) {
                                    bestDist = newDist;
                                    best = r.right.id;
                                }
                            } else {
                                best = getClosestDown(r.right, x, y, best, !hv);
                            }
                        }
                    }
                    if (r.right != null) {
                        if (r.right.isLeaf()) {
                            newDist = euclidean(r.right.x, x, r.right.y, y);
                            if (newDist < bestDist) {
                                bestDist = newDist;
                                best = r.right.id;
                            }
                        } else {
                            best = getClosestDown(r.right, x, y, best, !hv);
                        }
                    } else {
                        if (r.left != null) {
                            if (r.left.isLeaf()) {
                                newDist = euclidean(r.left.x, x, r.left.y, y);
                                if (newDist < bestDist) {
                                    bestDist = newDist;
                                    best = r.left.id;
                                }
                            } else {
                                best = getClosestDown(r.left, x, y, best, !hv);
                            }
                        }
                    }

                } else {
                    if (r.left != null) {
                        if (r.left.isLeaf()) {
                            newDist = euclidean(r.left.x, x, r.left.y, y);
                            if (newDist < bestDist) {
                                bestDist = newDist;
                                best = r.left.id;
                            }
                        } else {
                            best = getClosestDown(r.left, x, y, best, !hv);
                        }
                    } else {
                        if (r.right != null) {
                            if (r.right.isLeaf()) {
                                newDist = euclidean(r.right.x, x, r.right.y, y);
                                if (newDist < bestDist) {
                                    bestDist = newDist;
                                    best = r.right.id;
                                }
                            } else {
                                best = getClosestDown(r.right, x, y, best, !hv);
                            }
                        }
                    }
                    if (r.right != null) {
                        if (r.right.isLeaf()) {
                            newDist = euclidean(r.right.x, x, r.right.y, y);
                            if (newDist < bestDist) {
                                bestDist = newDist;
                                best = r.right.id;
                            }
                        } else {
                            best = getClosestDown(r.right, x, y, best, !hv);
                        }
                    } else {
                        if (r.left != null) {
                            if (r.left.isLeaf()) {
                                newDist = euclidean(r.left.x, x, r.left.y, y);
                                if (newDist < bestDist) {
                                    bestDist = newDist;
                                    best = r.left.id;
                                }
                            } else {
                                best = getClosestDown(r.left, x, y, best, !hv);
                            }
                        }

                    }
                }
            }

//            return getClosestUp(r, x, y, contains, best);
            return best;
        }


        public long getClosestUp(Node r, double x, double y, long best) {

            if (r.parent == null) {
                return best;
            }

            long newbest;

            double newDist = euclidean(r.parent.x, x, r.parent.y, y);
            double bestDist = euclidean(xMap.get(best), x, yMap.get(best), y);

            if (newDist < bestDist) {
                newbest = r.parent.id;
                bestDist = newDist;
            } else {
                newbest = best;
            }

            if (r.parent.left != null && r.id == r.parent.left.id) {
                if (r.parent.right != null) {
                    newDist = euclidean(r.parent.right.x, x, r.parent.right.y, y);
                    if (newDist < bestDist) {
                        newbest = r.parent.right.id;
                        bestDist = newDist;
                    }
                }
            } else if (r.parent.right != null && r.id == r.parent.right.id) {
                if (r.parent.left != null) {
                    newDist = euclidean(r.parent.left.x, x, r.parent.left.y, y);
                    if (newDist < bestDist) {
                        newbest = r.parent.left.id;
                        bestDist = newDist;
                    }
                }
            }

            return getClosestUp(r.parent, x, y, newbest);

        }


        private class Node {
            Node parent;
            long id;
            Node left;
            Node right;
            double x;
            double y;
            double lon;
            double lat;


            Node(long id) {
                this.id = id;
                this.left = null;
                this.right = null;
                this.x = xMap.get(id);
                this.y = yMap.get(id);
                this.lon = idMap.get(id).lon;
                this.lat = idMap.get(id).lat;
            }


            boolean isLeaf() {
                return (this.right == null && this.left == null);
            }

        }

    }
}
