package tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import algorithms.DefaultTeam;
import supportGUI.Circle;
import types.Point;

public class Tools {

	static String folder = "samples/";

	public static ArrayList<ArrayList<Point>> getInputFiles(File directory) {
		String[] inputFiles = directory.list();
		ArrayList<ArrayList<Point>> res = new ArrayList<>();


		for(String filename : inputFiles){
			BufferedReader input;
			try {
				input = new BufferedReader(
						new java.io.InputStreamReader(new FileInputStream(folder+filename)));

				try {
					ArrayList<Point> pts = new ArrayList<>();
					String line;
					while ((line = input.readLine()) != null) { 
						String[] coordinates = line.split("\\s+");
						pts.add(new Point(Integer.parseInt(coordinates[0]), 
								Integer.parseInt(coordinates[1])));
					}
					res.add(pts);
					input.close();
				}catch (IOException e) { 
					System.err.println("Exception: interrupted I/O.");
				}

			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		return res;
	}

	public static ArrayList<ArrayList<Point>> getFilteredInputFiles(ArrayList<ArrayList<Point>> lpts){
		ArrayList<ArrayList<Point>> res = new ArrayList<>();
		for(ArrayList<Point> lp : lpts){
			try {
				res.add(DefaultTeam.filtrageAklToussaint(lp));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return res;
	}

	public static void putTimeCalculusInFile(File dest, File dest2, File src, int choice){

		ArrayList<Long> timeList = new ArrayList<>();
		ArrayList<Integer> radiusList = new ArrayList<>();
		ArrayList<java.awt.Point> centerList = new ArrayList<>();
		ArrayList<ArrayList<Point>> lp = Tools.getInputFiles(src);
		ArrayList<ArrayList<Point>> lf = Tools.getFilteredInputFiles(lp);

		int j=0;
		long t = System.currentTimeMillis();
		long tps = System.currentTimeMillis();
		for(ArrayList<Point> ll : lf){
			System.out.println(" ===== \titeration n°: " + j++);
			Circle c = null;
			if(choice == 0)
				c = DefaultTeam.welzl(ll).toIntCircle();
			else
				c = DefaultTeam.algoNaïf(ll).toIntCircle();
			timeList.add(System.currentTimeMillis() - t);
			radiusList.add(c.getRadius());
			centerList.add(c.getCenter());
			//System.out.println("temps fichier "+ i++ +":"+(System.currentTimeMillis() - t)); //affiche le temps pour chaque fichier
			t = System.currentTimeMillis();
		}
		timeList.add(System.currentTimeMillis()-tps);

		try
		{
			FileWriter fw = new FileWriter (dest);

			for (int i=0; i<radiusList.size(); i++)
			{
				fw.write (String.valueOf (timeList.get(i)));
				fw.write ("\t");
				if(i<timeList.size()-1)
					fw.write (String.valueOf (radiusList.get(i)));
				//fw.write ("\t");
				//fw.write ("[" + centerList.get(i).x + ";" + centerList.get(i).y + "]");
				fw.write ("\r\n");
			}

			fw.close();
			
			FileWriter fw2 = new FileWriter (dest2);
			
			for (int i=0; i<centerList.size(); i++)
			{
				fw2.write ("[" + centerList.get(i).x + ";" + centerList.get(i).y + "]");
				fw2.write ("\r\n");
			}

			fw2.close();
		}
		catch (IOException exception)
		{
			System.out.println ("Erreur lors de la lecture : " + exception.getMessage());
		}
		System.out.println("Time calculus done !");
	}

	public static int[] getCircleRadiusFromFile(File src){
		int[] var = null;
		int numLigne = 0; 
		String ligne; 
		
		
		// lecture du fichier texte 
		try { 
			InputStream ips = new FileInputStream(src); 
			InputStreamReader ipsr = new InputStreamReader(ips); 
			BufferedReader br = new BufferedReader(ipsr);
			
			var = new int[1664]; 

			while ((ligne = br.readLine()) != null) 
			{ 
				numLigne++; 
				//chaine += ligne + "\n"; 
				int ind = ligne.indexOf("\t"); 
				var[numLigne -1] = Integer.parseInt(ligne.substring(ind+1, ligne.length())); 
			}

			br.close(); 
		} 
		catch (Exception e) { 
			System.out.println("ERROR" + e.toString()); 
		}
		return var;
	}
	
	public static int[] getTimeExecutionFromFile(File src){
		int[] var = null;
		int numLigne = 0; 
		String ligne; 
		
		
		// lecture du fichier texte 
		try { 
			InputStream ips = new FileInputStream(src); 
			InputStreamReader ipsr = new InputStreamReader(ips); 
			BufferedReader br = new BufferedReader(ipsr);
			
			var = new int[1664]; 

			while ((ligne = br.readLine()) != null) 
			{ 
				numLigne++; 
				//chaine += ligne + "\n"; 
				int ind = ligne.indexOf("\t"); 
				var[numLigne -1] = Integer.parseInt(ligne.substring(0, ind));
			}

			br.close(); 
		} 
		catch (Exception e) { 
			System.out.println("ERROR " + e.toString()); 
		}
		return var;
	}

	public static void putCircleRadiusDifferenceInFile(File naïf, File welzl){
		int[] circleNaïf = Tools.getCircleRadiusFromFile(naïf);
		int[] circleWelzl = Tools.getCircleRadiusFromFile(welzl);

		try{    
			// Ouvrir le fichier 
			// (en append si tu veux écrire à la suite du fichier s'il existe)  
			FileWriter fw = new FileWriter("circleRadiusDifference.txt", true); 

			for(int i = 0; i < circleNaïf.length; i++){
				fw.append(Integer.toString(circleWelzl[i] - circleNaïf[i]));
				fw.append("\n");
			}
			// Fermer le fichier 
			fw.close();
		}catch(IOException e){    
			// On écrit dans le flux d'erreur (et non dans la sortie standard)    
			// un message d'erreur 
			System.err.println("Erreur, écriture impossible\n");  
		} 
	}
	
	public static void getTableFormat(File naïf, File welzl){
		int[] circleNaïf = Tools.getCircleRadiusFromFile(naïf);
		int[] circleWelzl = Tools.getCircleRadiusFromFile(welzl);
		
		int[] timeNaïf = Tools.getTimeExecutionFromFile(naïf);
		int[] timeWelzl = Tools.getTimeExecutionFromFile(welzl);

		try{    
			// Ouvrir le fichier 
			// (en append si tu veux écrire à la suite du fichier s'il existe)  
			FileWriter fw = new FileWriter("tableFormat.txt", true); 

			for(int i = 0; i < circleNaïf.length; i++){
				//System.out.println(i + "\t" + circleWelzl[i] + "\t" + circleNaïf[i] + "\t" + timeNaïf[i] + "\t" + timeWelzl[i]);
				fw.append("     ");
				fw.append(Integer.toString(i+1));
				fw.append(" & ");
				fw.append(Integer.toString(timeNaïf[i]));
				fw.append(" & ");
				fw.append(Integer.toString(circleNaïf[i]));
				fw.append(" & ");
				fw.append(Integer.toString(timeWelzl[i]));
				fw.append(" & ");
				fw.append(Integer.toString(circleWelzl[i]));
				fw.append(" \\\\ \\hline");
				fw.append("\n");
			}
			// Fermer le fichier 
			fw.close();
		}catch(IOException e){    
			// On écrit dans le flux d'erreur (et non dans la sortie standard)    
			// un message d'erreur 
			System.err.println("Erreur, écriture impossible\n");  
		} 
	}
	
	public static String[] getCenterPointFromFile(File src){
		String[] var = null;
		int numLigne = 0; 
		String ligne; 
		
		
		// lecture du fichier texte 
		try { 
			InputStream ips = new FileInputStream(src); 
			InputStreamReader ipsr = new InputStreamReader(ips); 
			BufferedReader br = new BufferedReader(ipsr);
			
			var = new String[1664]; 

			while ((ligne = br.readLine()) != null) 
			{ 
				numLigne++; 
				//chaine += ligne + "\n"; 
				var[numLigne -1] = ligne.substring(0, ligne.length());
			}

			br.close(); 
		} 
		catch (Exception e) { 
			System.out.println("ERROR " + e.toString()); 
		}
		return var;
	}


	public static void getTableCenterPointFormat(File naïf, File welzl){
		String[] centerNaïf = Tools.getCenterPointFromFile(naïf);
		String[] centerWelzl = Tools.getCenterPointFromFile(welzl);

		try{    
			// Ouvrir le fichier 
			// (en append si tu veux écrire à la suite du fichier s'il existe)  
			FileWriter fw = new FileWriter("tableCenterPointFormat.txt", true); 

			for(int i = 0; i < centerNaïf.length; i++){
				fw.append("     ");
				fw.append(Integer.toString(i+1));
				fw.append(" & ");
				fw.append(centerNaïf[i]);
				fw.append(" & ");
				fw.append(centerWelzl[i]);
				fw.append(" \\\\ \\hline");
				fw.append("\n");
			}
			// Fermer le fichier 
			fw.close();
		}catch(IOException e){    
			// On écrit dans le flux d'erreur (et non dans la sortie standard)    
			// un message d'erreur 
			System.err.println("Erreur, écriture impossible\n");  
		} 
	}
}
