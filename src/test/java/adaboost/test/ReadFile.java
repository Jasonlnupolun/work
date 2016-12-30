package adaboost.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

//Class for reading input file 
public class ReadFile {

	// Total No Of Iterations in the program
	public static int T = 0;
	// Total No. Of Examples in the program
	public static int n = 0;
	// ArrayList for storing n real numbers
	public static ArrayList<Double> X = new ArrayList<>();
	// ArrayList for storing n real numbers which can be either +1 or -1
	public static ArrayList<Integer> Y = new ArrayList<>();
	// ArrayList for storing non negative numbers that sum up to 1
	public static ArrayList<Double> Prob_List = new ArrayList<>();
	// A small real number - Epsilon value used in the program
	//public static Double epsilon;

	public ReadFile(String file1) throws FileNotFoundException {
		String path = System.getProperty("user.dir")
				+ (ReadFile.class.getPackage() == null ? "" : "\\"
						+ "\\src\\"
						+ ReadFile.class.getPackage().getName()
								.replace('.', '\\'));

		@SuppressWarnings("resource")
		Scanner S = new Scanner(new File(path + "\\" + file1));
		String temp = S.nextLine();
		StringTokenizer token = new StringTokenizer(temp, " ");

		
		T = Integer.parseInt(token.nextToken());
		
		n = Integer.parseInt(token.nextToken());
		
		//epsilon = Double.parseDouble(token.nextToken());
		temp = "";

		// Reading input file with all the data
		temp = S.nextLine();
		StringTokenizer token1 = new StringTokenizer(temp, " ");
		while (token1.hasMoreTokens()) {
			X.add(Double.parseDouble(token1.nextToken()));
		}
		temp = "";
		temp = S.nextLine();
		StringTokenizer token2 = new StringTokenizer(temp, " ");
		while (token2.hasMoreTokens()) {
			Y.add(Integer.parseInt(token2.nextToken()));
		}

		temp = "";
		temp = S.nextLine();
		StringTokenizer token3 = new StringTokenizer(temp, " ");
		while (token3.hasMoreTokens()) {
			Prob_List.add(Double.parseDouble(token3.nextToken()));
		}

	}

}