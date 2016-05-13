package test;

import java.io.File;

import tools.Tools;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		File dest = new File("algoWelzl_timeResult.txt");
		File dest3 = new File("algoWelzl_centerList.txt");
		File src = new File("samples/");
		//Tools.putTimeCalculusInFile(dest, dest3, src, 0);

		File dest2 = new File("algoNaïf_timeResult.txt");
		File dest4 = new File("algoNaïf_centerList.txt");
		File src2 = new File("samples/");
		//Tools.putTimeCalculusInFile(dest2, dest4, src2, 1);
		//Tools.putCircleRadiusDifferenceInFile(dest2, dest);
		//Tools.getTableFormat(dest2, dest);
		Tools.getTableCenterPointFormat(dest4, dest3);
	}

}
