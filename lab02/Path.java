/** A class that represents a path via pursuit curves. */
public class Path {

    // TODO
    public Point curr;
    public Point next;

    public Path(double x, double y){
    	curr = new Point(300, 300);

    	next = new Point(x, y);
    }

    public void iterate(double dx, double dy){

    	curr = next;
    	next = new Point(curr.x + dx, curr.y + dy);
    }

}
