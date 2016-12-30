package adaboost.test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class AdaBoosting {
	public static void main(String[] args) throws FileNotFoundException {
		// application logic here
		// Reading the input file from the reading class
		new ReadFile("adaboost-5.txt");
		ArrayList<Double> flist = new ArrayList<>();
		double bound = 1.0;
		// Loop runs through all number of examples
		for (int i = 0; i < ReadFile.n; i++) {
			flist.add(0.0);
		}

		System.out.println("Binary Adaboosting: ");
		// Looping through the entire input file to read the data during each iteration
		for (int l = 0; l < ReadFile.T; l++) {
			// -------------

			System.out.println("------------------------------------------------------------------------------------");
			System.out.println("--------------------Iteration:" + (l + 1)+ "-------------------------------------------------");
			double minerr = 1.0;
			int x_pos = 0;
			boolean flag = true;
			double alpha = 0.0;
			double z = 0;
			for (int i = 0; i < ReadFile.n - 1; i++) {
				double Temp_Error = 0.0;
				for (int j = 0; j <= i; j++) {
					if (ReadFile.Y.get(j) == -1) {
						Temp_Error = Temp_Error + ReadFile.Prob_List.get(j);
					}
				}
				for (int j = i + 1; j < ReadFile.n; j++) {
					if (ReadFile.Y.get(j) == 1) {
						Temp_Error = Temp_Error + ReadFile.Prob_List.get(j);
					}
				}
				if (minerr > Temp_Error) {
					minerr = Temp_Error;
					x_pos = i;
					flag = true;
				}
			}
			for (int i = 0; i < ReadFile.n - 1; i++) {
				double Temp_Error = 0.0;
				for (int j = 0; j <= i; j++) {
					if (ReadFile.Y.get(j) == 1) {
						Temp_Error = Temp_Error + ReadFile.Prob_List.get(j);
					}
				}
				for (int j = i + 1; j < ReadFile.n; j++) {
					if (ReadFile.Y.get(j) == -1) {
						Temp_Error = Temp_Error + ReadFile.Prob_List.get(j);
					}
				}
				if (minerr > Temp_Error) {
					minerr = Temp_Error;
					x_pos = i;
					flag = false;
				}

			}

			alpha = 0.5 * Math.log((1 - minerr) / minerr);

			if (flag == true) {
				System.out.println(" 1.The selected weak classifier is: x<"
						+ (ReadFile.X.get(x_pos) + ReadFile.X.get(x_pos + 1))
						/ 2);
			} else {
				System.out.println("1.The selected weak classifier is: x>"
						+ (ReadFile.X.get(x_pos) + ReadFile.X.get(x_pos + 1))
						/ 2);

			}
			System.out.println("2.The Error of ht:" + minerr);
			System.out.println("3.The weight of ht: " + alpha);

			// ---------------------------Classifier
			// h(x)---------------------------------------------//

			ArrayList<Integer> h = new ArrayList<>();
			if (flag == true) {
				for (int i = 0; i <= x_pos; i++) {
					h.add(1);
				}
				for (int i = x_pos + 1; i < ReadFile.n; i++) {
					h.add(-1);
				}
			}

			if (flag == false) {
				for (int i = 0; i <= x_pos; i++) {
					h.add(-1);
				}
				for (int i = x_pos + 1; i < ReadFile.n; i++) {
					h.add(1);
				}
			}

			// -------------------Set Pre normalized Probabilities calculations------------------------------------/
			for (int i = 0; i < ReadFile.n; i++) {
				double tempP = 0.0;
				if (ReadFile.Y.get(i) == h.get(i)) {
					tempP = ReadFile.Prob_List.get(i) * Math.exp(-alpha);
					ReadFile.Prob_List.set(i, tempP);
				} else {
					tempP = ReadFile.Prob_List.get(i) * Math.exp(alpha);
					ReadFile.Prob_List.set(i, tempP);
				}
			}
			// --------------------------------------------Calculation Of Z---------------------------------------------
			for (int i = 0; i < ReadFile.n; i++) // calculate z
			{
				z = z + ReadFile.Prob_List.get(i);
			}
			System.out.println("4.The probabilites normalisation factor: "+ z);
			// -----------------------------------Calculate New Normalized Probabilities-------------------------------------------------------////////////
			for (int i = 0; i < ReadFile.n; i++) // calculate new p
			{
				double newp = ReadFile.Prob_List.get(i) / z;
				ReadFile.Prob_List.set(i, newp);
			}
			System.out.println("5.The probabilities after normalization: ");
			for (int i = 0; i < ReadFile.n; i++) // calculate new p
			{
				System.out.print(ReadFile.Prob_List.get(i) + " ");
			}
			System.out.println("");

			// -------------Calculate f(x)-----------------------------
			double tempf = 0.0;
			for (int i = 0; i < ReadFile.n; i++) {
				tempf = 0.0;
				tempf = flist.get(i) + (alpha * h.get(i));
				flist.set(i, tempf);
			}
			System.out.println("6.The Boosted Classifier: ");
			for (int i = 0; i < ReadFile.n; i++) // calculate new p
			{
				System.out.print(flist.get(i) + " ");

			}
			System.out.println("");
			// ---------------------------Calculation Of Boosted Error Et----------------------------
			int err_count = 0;
			for (int i = 0; i < ReadFile.n; i++) {
				if (flist.get(i) > 0) {
					if (ReadFile.Y.get(i) == -1) {
						err_count = err_count + 1;
					}
				} else {
					if (ReadFile.Y.get(i) == 1) {
						err_count = err_count + 1;
					}
				}
			}

			double boostedErr = (double) err_count / (double) ReadFile.n;
			System.out.println("7.The Error of Boosted Classifier: "+ boostedErr);
			// ---------------------------Calculation Of Bound----------------
			bound = bound * z;
			System.out.println("8.The Bound on Et: " + bound);
			// ----------------------------------------------------------------------------------
		}

	}

}