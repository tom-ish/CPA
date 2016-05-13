package types;

import java.util.ArrayList;

public class Circle {
	
	public Point center;
	public double radiusSquared;
	
	public Circle(Point c, double r){
		this.center = c;
		this.radiusSquared = r;
	}

	public supportGUI.Circle toIntCircle(){
		int radius = (int) Math.ceil(Math.sqrt(radiusSquared));
		// System.out.println("CERCLE FINAL:\n\tradius = "+radius + " centre: " + this.center.toIntPoint());
		return new supportGUI.Circle(this.center.toIntPoint(), radius);
	}

	public Circle(Point a, Point b){
		this.center = new Point((a.x+b.x)/2, (a.y+b.y)/2);
		this.radiusSquared = this.center.distanceSquared(a);
	}
	
	public Circle(Point A, Point B, Point C){
		Point I = new Point((A.x+B.x)/2,(A.y+B.y)/2);//milieu AB
		Point J = new Point((B.x+C.x)/2,(B.y+C.y)/2);//milieu BC
		this.center = systeme2equation(B.x-A.x, 
				B.y-A.y, 
				I.x*B.x-I.x*A.x+I.y*B.y-I.y*A.y, 
				C.x-B.x,
				C.y-B.y,
				J.x*C.x-J.x*B.x+J.y*C.y-J.y*B.y);
		//System.out.println(this.center.distanceSquared(C)+" avec A:"+A+" B:"+B+" C:"+C);
		this.radiusSquared = this.center.distanceSquared(C);
	}
	
	private Point systeme2equation(double a, double b, double c, double d, double e, double f){

		/*	si on a un systempe a 2 equations de la forme :
		 *  
		 *  ax + by = c
		 *  dx + ey = f
		 * 
		 * alors on a x = (ce-fb)/(ae-db)
		 * 			  y	= (af-dc)/(ae-db)
		 * 			  avec (ae-db) != 0
		 * */
		double x = 0, y = 0;
		if(a*e-d*b != 0){
			x = (c*e-f*b)/(a*e-d*b);
			y = (a*f-d*c)/(a*e-d*b);
		}
		
		return new Point(x,y);

	}
	
	public ArrayList<Point> getPointsOnCircle(ArrayList<Point> points){
		ArrayList<Point> rslt = new ArrayList<Point>();
		for(Point p : points)
			if(p.distanceSquared(this.center) == this.radiusSquared)
				rslt.add(p);
		return rslt;
	}
	
	public boolean containsAll(ArrayList<Point> points){
		for(Point p : points){
			if(this.center.distanceSquared(p) > this.getRadiusSquared())
				return false;
		}
		return true;
	}
	
	public boolean contains(Point point){
		if(this.center.distanceSquared(point) > this.getRadiusSquared())
			return false;
		return true;
	}
	
	public Point getCenter(){ return this.center; }
	
	public double getRadiusSquared(){ return this.radiusSquared; }
	
	public void setRadiusSquared(double d){ this.radiusSquared = d; }
	
	public String toString(){
		return center.toString() + "\tradiusSquared: " + radiusSquared + "\tradius: " + Math.sqrt(radiusSquared);
	}
}
