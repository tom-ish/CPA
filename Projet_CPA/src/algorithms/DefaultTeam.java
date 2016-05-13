package algorithms;

import java.util.ArrayList;
import java.util.Random;



/***************************************************************
 * TME 1: calcul de diamètre et de cercle couvrant minimum.    *
 *   - Trouver deux points les plus éloignés d'un ensemble de  *
 *     points donné en entrée.                                 *
 *   - Couvrir l'ensemble de poitns donné en entrée par un     *
 *     cercle de rayon minimum.                                *
 *                                                             *
 * class Circle:                                               *
 *   - Circle(Point c, int r) constructs a new circle          *
 *     centered at c with radius r.                            *
 *   - Point getCenter() returns the center point.             *
 *   - int getRadius() returns the circle radius.              *
 *                                                             *
 * class Line:                                                 *
 *   - Line(Point p, Point q) constructs a new line            *
 *     starting at p ending at q.                              *
 *   - Point getP() returns one of the two end points.         *
 *   - Point getQ() returns the other end point.               *
 ***************************************************************/
import supportGUI.Line;
import types.Point;
import types.Circle;

public class DefaultTeam {

	public static int cpt = 0;

	// calculDiametre: ArrayList<Point> --> Line
	//   renvoie une paire de points de la liste, de distance maximum.
	public Line calculDiametre(ArrayList<Point> points) {
		try {
			points = filtrageAklToussaint(points);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return calculDiametreNaif(points);
	}

	public static Point pointRandom(ArrayList<Point> points){
		Random r = new Random();
		Integer nb = r.nextInt();
		nb = Math.abs(r.nextInt() % points.size());

		return points.get(nb);
	}

	public Line calculDiametreNaif(ArrayList<Point> points) {
		if (points.size()<3) {
			return null;
		}

		Point ref = pointRandom(points);
		Point top = ref;
		Point bottom = ref;
		Point left = ref;
		Point right = ref;

		System.out.println("Coordonnées du points de départ: \n" + ref.toString());

		for(Point p : points) {
			if(p.x > right.x)
				right = p;
			else if(p.x < left.x)
				left = p;
			else if(p.y < top.y)
				top = p;
			else if(p.y > bottom.y)
				bottom = p;
		}

		System.out.println("Coordonnées des points");
		System.out.println(" ==> top: "+ top.toString());
		System.out.println(" ==> bottom: "+ bottom.toString());
		System.out.println(" ==> left: "+ left.toString());
		System.out.println(" ==> right: "+ right.toString());

		double distance_max = 0;
		Point extremePoints[] = new Point[4];
		extremePoints[0] = top;
		extremePoints[1] = bottom;
		extremePoints[2] = left;
		extremePoints[3] = right;

		Point extremeA = null;
		Point extremeB = null;

		for(int i = 0; i < 3; i++){
			System.out.println("i : " + i);
			if(((extremePoints[i].x - extremePoints[i+1].x)*(extremePoints[i].x - extremePoints[i+1].x)
					+ ((extremePoints[i].y - extremePoints[i+1].y)*(extremePoints[i].y - extremePoints[i+1].y))) > distance_max){
				distance_max = ((extremePoints[i].x - extremePoints[i+1].x)*(extremePoints[i].x - extremePoints[i+1].x)
						+ ((extremePoints[i].y - extremePoints[i+1].y)*(extremePoints[i].y - extremePoints[i+1].y)));
				extremeA = new Point(extremePoints[i].x, extremePoints[i].y);
				extremeB = new Point(extremePoints[i+1].x, extremePoints[i+1].y);
				System.out.println("distance Max = " + distance_max);
			}
		}

		switch(cpt){
		case 0:
			cpt++;
			return new Line(top.toIntPoint(), left.toIntPoint());
		case 1:
			cpt++;
			return new Line(left.toIntPoint(), bottom.toIntPoint());
		case 2:
			cpt++;
			return new Line(bottom.toIntPoint(), right.toIntPoint());
		case 3:
			cpt++;
			return new Line(right.toIntPoint(), top.toIntPoint());
		default:
			return new Line(extremeA.toIntPoint(), extremeB.toIntPoint());
		}

		//return new Line(extremeA, extremeB);
	}


	/********************************
	 **** FILTRAGE AKL-TOUSSAINT ****
	 ********************************/

	public static ArrayList<Point> filtrageAklToussaint(ArrayList<Point> points) throws Exception{
		if (points.size()<3) {
			throw new Exception("Size < 3");
		}

		Point ref = pointRandom(points);
		Point top = ref;
		Point bottom = ref;
		Point left = ref;
		Point right = ref;

		// System.out.println("Coordonnées du points de départ: \n" + ref.toString());

		for(Point p : points) {
			if(p.x > right.x)
				right = p;
			else if(p.x < left.x)
				left = p;
			else if(p.y < top.y)
				top = p;
			else if(p.y > bottom.y)
				bottom = p;
		}

		return filtrageAklToussaintCoordonneesBarycentriques(points, right, left, top, bottom);		
	}

	public static ArrayList<Point> filtrageAklToussaintHeron(ArrayList<Point> points, Point right, Point left, Point top, Point bottom){
		ArrayList<Point> rslt = new ArrayList<Point>(points);

		for(Point p : points) {
			double squareAire1 = heron(right, top, left);
			double squareAire2 = heron(right, bottom, left);
			if(heron(right,top,p) + heron(left,top,p) + heron(right,left,p) == squareAire1){
				points.remove(p);
			}
			else if(heron(right,bottom,p) + heron(left,bottom,p) + heron(right,left,p) == squareAire2){
				rslt.remove(p);
			}
			else {
				// Le point p n'est pas dans le quadrilatere
			}
		}
		return rslt;
	}

	/********************************
	 ******* FORMULE DE HERON 
	 * @throws Exception *******
	 ********************************/

	public static double heron(Point a, Point b, Point c){
		double A = ((a.x-b.x)*(a.x-b.x) + (a.y-b.y)*(a.y-b.y));
		double B = ((c.x-b.x)*(c.x-b.x) + (c.y-b.y)*(c.y-b.y));
		double C = ((a.x-c.x)*(a.x-c.x) + (a.y-c.y)*(a.y-c.y));

		// Triangle de cote ABC de sommet a,b,c
		double s = (A+B+C)/2;
		return Math.sqrt(s*(s-A)*(s-B)*(s-C));
	}


	/********************************
	 ****** PRODUIT VECTORIEL *******
	 ********************************/

	public static ArrayList<Point> filtrageAklToussaintProduitVectoriel(ArrayList<Point> points, Point right, Point left, Point top, Point bottom){
		ArrayList<Point> rslt = new ArrayList<Point>(points);

		for(Point p : points){
			if((signProduitVectoriel(right,top,p) == signProduitVectoriel(right,top,p)
					&& signProduitVectoriel(top,left,p) == signProduitVectoriel(top,left,right)
					&& signProduitVectoriel(right,left,p) == signProduitVectoriel(right,left,top))
					||
					(signProduitVectoriel(right,bottom,p) == signProduitVectoriel(right,bottom,p)
					&& signProduitVectoriel(bottom,left,p) == signProduitVectoriel(bottom,left,right)
					&& signProduitVectoriel(right,left,p) == signProduitVectoriel(right,left,bottom))
					)
				rslt.remove(p);
		}

		return rslt;
	}

	// Signe du produit vectoriel ab ^ ac
	public static Integer signProduitVectoriel(Point a, Point b, Point c){
		return signProduitVectoriel(a,b,a,c);
	}

	// Signe du produit vectoriel ab ^ cd
	public static Integer signProduitVectoriel(Point a, Point b, Point c, Point d){
		double x_ab = b.x - a.x;
		double y_ab = b.y - a.y;
		double x_cd = d.x - c.x;
		double y_cd = d.y - c.y;
		if(((x_ab*y_cd) - (y_ab*x_cd)) > 0)
			return 1;
		else if(((x_ab*y_cd) - (y_ab*x_cd)) < 0)
			return -1;
		else
			return 0;
	}

	/****************************************
	 ****** COORDONNEES BARYCENTRIQUE *******
	 ****************************************/

	public static ArrayList<Point> filtrageAklToussaintCoordonneesBarycentriques(ArrayList<Point> points, Point right, Point left, Point top, Point bottom){
		ArrayList<Point> rslt = new ArrayList<Point>(points);

		for(Point p : points) {
			double l1 = ((top.y-right.y)*(p.x-right.x)*+(right.x-top.x)*(p.y-right.y))/((top.y-right.y)*(left.x-right.x)+(right.x-top.x)*(left.y-right.y));
			double l2 = ((right.y-left.y)*(p.x-right.x)*+(left.x-right.x)*(p.y-right.y))/((top.y-right.y)*(left.x-right.x)+(right.x-top.x)*(left.y-right.y));
			double l3 = 1-l1-l2;
			if((l1 >= 0 && l1 <= 1) && (l2 >= 0 && l2 <= 1) && (l3 >= 0 && l3 <= 1))
				rslt.remove(p);
		}

		return rslt;
	}


	/**********************
	 *** CERCLE MINIMUM ***
	 **********************/

	private static boolean algoNaïf_filtrageAklToussaint = false;

	// calculCercleMin: ArrayList<Point> --> Circle
	//   renvoie un cercle couvrant tout point de la liste, de rayon minimum.
	public supportGUI.Circle calculCercleMin(ArrayList<java.awt.Point> points) {
		ArrayList<Point> myPoints = new ArrayList<Point>();
		for(java.awt.Point p : points){
			myPoints.add(new Point(p.getX(), p.getY()));
		}
		if(algoNaïf_filtrageAklToussaint){
			System.out.println("ALGO NAIF AVEC FILTRAGE AKL TOUSSAINT");
			try {
				ArrayList<Point> myPointsFiltrés = filtrageAklToussaint(myPoints);
				return algoNaïf(myPointsFiltrés).toIntCircle();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return welzl(myPoints).toIntCircle();
	}


	/**********************************
	 ******** ALGORITHME NAIF *********
	 **********************************/

	public static Circle algoNaïf(ArrayList<Point> points) {
		if (points.isEmpty()) {
			return null;
		}
		Circle c = null;
		Circle c_min = null;

		// Pour chaque couple de points (p,q) on construit le cercle
		// dont le centre est le milieu du segment PQ et de diametre PQ
		// => Cercle circonscrit trouve avec seulement 2 points
		for(Point p : points){
			for(Point q : points){
				c = new Circle(p, q);
				if(c.containsAll(points)){
					//System.out.println("Cercle circonscrit minimum à 2 points");
					return c;
				}
			}
		}

		//System.out.println("Cercle circonscrit minimum à 3 points");
		
		for(Point p : points){
			for(Point q : points){
				for(Point r : points){
						c = new Circle(p, q, r);
						if(c.containsAll(points)){
							//System.out.println("test "+c.getRadiusSquared()+" < "+c_min.getRadiusSquared());
							if(c_min == null || c.getRadiusSquared() < c_min.getRadiusSquared()){
								//System.out.println(c_min);
								c_min = c;
								//System.out.println("new circle min: " + c_min);
							}
						}
				}
			}
		}
		//System.out.println(c_min.containsAll(points));
		return c_min;
	}

	public boolean containsAll(Circle c, ArrayList<Point> points){
		for(Point p : points){
			//System.out.println(c.getCenter().distanceSquared(p)+"  >   "+c.getRadiusSquared());
			if(c.getCenter().distanceSquared(p) > c.getRadiusSquared())
				return false;
		}
		return true;
	}


	public Point getPointMax(Point p, ArrayList<Point> points){
		double distanceMax = 0;
		Point max = null;
		for(Point q : points){
			if((p.x-q.x)*(p.x-q.x)+(p.y-q.y)*(p.y-q.y) > distanceMax){
				distanceMax = (p.x-q.x)*(p.x-q.x)+(p.y-q.y)*(p.y-q.y);
				max = q;
			}
		}
		return max;
	}



	/***********************************
	 ******** ALGORITHME WELZL *********
	 ***********************************/

	public static Circle welzl(ArrayList<Point> points){
		Welzl w = new Welzl();
		return w.minidisk(points);
	}

}
