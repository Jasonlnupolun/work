package kmeans;

import org.encog.ml.bayesian.BayesianNetwork;
import org.encog.ml.bayesian.training.BayesianInit;
import org.encog.ml.bayesian.training.TrainBayesian;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLDataSet;

public class SimpleK2 {

	public static final double DATA[][] = {
		{ 1, 0, 0 }, // case 1
		{ 1, 1, 1 }, // case 2
		{ 0, 0, 1 }, // case 3
		{ 1, 1, 1 }, // case 4
		{ 0, 0, 0 }, // case 5
		{ 0, 1, 1 }, // case 6
		{ 1, 1, 1 }, // case 7
		{ 0, 0, 0 }, // case 8
		{ 1, 1, 1 }, // case 9
		{ 0, 0, 0 }, // case 10		
	};

	public static void main(String[] args) {
		//String[] labels = { "available", "not" };
		
		MLDataSet data = new BasicMLDataSet(DATA,null);
		BayesianNetwork network = new BayesianNetwork();
		network.createEvent("x1");
		network.createEvent("x2");
		network.createEvent("x3");
		network.finalizeStructure();
		
		TrainBayesian train = new TrainBayesian(network,data,10);
		train.setInitNetwork(BayesianInit.InitEmpty);
		train.iteration();
		
		double p = network.performQuery("P(+x2|+x1)");// 0.71
		System.out.println("x2 probability : " + network.getEvent("x2").getTable().findLine(1, new int[] {1}));
		System.out.println("Calculated P(+x2|+x1): " + p);
		System.out.println("Final network structure: " + network.toString());
		
		//EncogDirectoryPersistence.saveObject(new File("d:\\test.eg"), network);
	}
}