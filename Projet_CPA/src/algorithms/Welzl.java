package algorithms;

import java.util.ArrayList;

import types.Circle;
import types.Point;

public class Welzl {
	
	public Circle minidisk(ArrayList<Point> points){
		Circle c = b_minidisk(points, points.size(), new Point[3], 0);
		System.out.println(c);
		return c;
	}

	public Circle b_minidisk(ArrayList<Point> points, int index, Point[] boundary, int b){
		Circle c = buildMinidisk(boundary, b);
		for(int i = 0; i < index; i++){
			Point p = points.get(i);
			if(!c.contains(p) && b < 3){
				boundary[b] = p;
				c = b_minidisk(points, i, boundary, b+1);
			}
		}
		return c;
	}
	
	public Circle buildMinidisk(Point[] boundary, int b){
		Circle c = new Circle(new Point(0.,0.), 0.);
		if(b == 1){
			c = new Circle(boundary[0], 0.);
		}
		else if(b == 2){
			c = new Circle(boundary[0], boundary[1]);
		}
		else if(b == 3){
			c = new Circle(boundary[0], boundary[1], boundary[2]);
		}
		return c;
	}

}
