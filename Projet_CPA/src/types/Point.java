package types;

public class Point {
	
	public double x;
	public double y;
	
	public Point(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public java.awt.Point toIntPoint(){
		int x, y;
		if(this.x%1.0 < 0.5) x = (int) Math.floor(this.x);
		else x = (int) Math.ceil(this.x);
		if(this.y%1.0 < 0.5) y = (int) Math.floor(this.y);
		else y = (int) Math.ceil(this.y);
		return new java.awt.Point(x, y);
	}
	
	public double distanceSquared(Point p){
		return ((this.x - p.x)*(this.x - p.x) + (this.y - p.y)*(this.y - p.y));
	}

	public String toString(){
		return "["+this.x+","+this.y+"]";
	}
	
	public boolean equals(Point p){
		if(this.x == p.x && this.y == p.y)
			return true;
		return false;
	}
}
