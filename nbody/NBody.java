public class NBody{
		


	public static double readRadius(String fileName){
		In in = new In(fileName);
        int first = in.readInt();
        double second = in.readDouble();
		return second;
	}

	public static Planet[] readPlanets(String fileName){
		In in = new In(fileName);
		int first = in.readInt();
        double second = in.readDouble();
		Planet[] list = new Planet[first];
        for(int i = 0; i < first; i++){
        	Planet p = new Planet(
        		in.readDouble(),
        		in.readDouble(),
        		in.readDouble(),
        		in.readDouble(),
        		in.readDouble(),
        		in.readString()
        		);
        	list[i] = p;
        }
        return list;
	}	
	
	public static void main(String[] args) {
		double T = Double.parseDouble(args[0]);
		double dt = Double.parseDouble(args[1]);
		String filename = args[2];
		double radius = readRadius(filename);
		Planet[] plist = readPlanets(filename);

		StdDraw.setScale(radius, -radius);
		StdDraw.picture(0, 0, "images/starfield.jpg");

		for(Planet p : plist){
			p.draw();
		}

		StdDraw.enableDoubleBuffering();
		double time = 0;

		double[] xForces = new double[plist.length];
		double[] yForces = new double[plist.length];

		while (time<T){

			for(int i = 0; i<plist.length; i++){
				xForces[i] = plist[i].calcNetForceExertedByX(plist);
				yForces[i] = plist[i].calcNetForceExertedByY(plist);
			}

			for(int i = 0; i < plist.length; i++){
				plist[i].update(dt, xForces[i], yForces[i]);
			}			

			StdDraw.picture(0, 0, "images/starfield.jpg");

			for(Planet p : plist){
				p.draw();
			}

			StdDraw.show();		
        	StdDraw.pause(10);
			time+=dt;
		} 

		StdOut.printf("%d\n", plist.length);
		StdOut.printf("%.2e\n", radius);
		for (int i = 0; i < plist.length; i += 1) {
    		StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n", plist[i].xxPos, plist[i].yyPos, plist[i].xxVel, plist[i].yyVel, plist[i].mass, plist[i].imgFileName);
		}

	}


}
