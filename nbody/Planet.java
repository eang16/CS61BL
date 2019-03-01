//import static java.lang.Math.sqrt;


public class Planet{
	double xxPos;
	double yyPos;
	double xxVel;
	double yyVel;
	double mass;
	private static final double grav = 6.67e-11;
	String imgFileName;

	public Planet(double xP, double yP, double xV, double yV, double m, String img){
		this.xxPos = xP;
		this.yyPos = yP;
		this.xxVel = xV;
		this.yyVel = yV;
		this.mass = m;
		this.imgFileName = img;

	}

	public Planet(Planet p){
		this.xxPos = p.xxPos;
		this.yyPos = p.yyPos;
		this.xxVel = p.xxVel;
		this.yyVel = p.yyVel;
		this.mass = p.mass;
		this.imgFileName = p.imgFileName;
	}

	public double calcDistance(Planet p){
		return Math.sqrt((this.xxPos - p.xxPos) * (this.xxPos - p.xxPos) + (this.yyPos - p.yyPos) * (this.yyPos - p.yyPos));
	}

	public double calcForceExertedBy(Planet p){
		double dist = calcDistance(p);
		return (grav * this.mass * p.mass) / (dist * dist);
	}

	public double calcForceExertedByX(Planet p){
		double force = calcForceExertedBy(p);
		double dist = calcDistance(p);
		double dx = Math.sqrt((this.xxPos - p.xxPos) * (this.xxPos - p.xxPos));
		if(this.xxPos - p.xxPos > 0){
			return -(force*dx)/dist;
		}
		return (force*dx)/dist;
	}

	public double calcForceExertedByY(Planet p){
		double force = calcForceExertedBy(p);
		double dist = calcDistance(p);
		double dy = Math.sqrt((this.yyPos - p.yyPos)*(this.yyPos - p.yyPos));
		if(this.yyPos - p.yyPos > 0){
			return -(force*dy)/dist;
		}
		return (force*dy)/dist;
	}

	public double calcNetForceExertedByX(Planet[] plist){
		double totalx = 0;
		for (Planet p : plist) {
			if(this.equals(p)){
				continue;
			} else {
				totalx = totalx + calcForceExertedByX(p);
			}
		}
		return totalx;
	}

	public double calcNetForceExertedByY(Planet[] plist){
		double totaly = 0;
		for (Planet p : plist) {
			if(this.equals(p)){
				continue;
			} else {
				totaly = totaly + calcForceExertedByY(p);
			}
		}
		return totaly;
	}

	public void update(double dt, double fX, double fY){
		double ax = fX/mass; 
		double ay = fY/mass; 
		xxVel = dt*ax + xxVel;
		yyVel = dt*ay + yyVel;
		xxPos = dt*xxVel + xxPos; 
		yyPos = dt*yyVel + yyPos; 
	}

	public void draw(){
		StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
	}

}






