package com.java.kanke.utils.kmeans;

import java.util.HashSet;
import java.util.Random;

/*k-means算法基本步骤：
（1） 从 n个数据对象任意选择 k 个对象作为初始聚类中心；
（2） 根据每个聚类对象的均值（中心对象），计算每个对象与这些中心对象的距离；并根据最小距离重新对相应对象进行划分；
（3） 重新计算每个（有变化）聚类的均值（中心对象）；
（4） 计算标准测度函数，当满足一定条件，如函数收敛时，则算法终止；如果条件不满足则回到步骤（2），不断重复直到标准测度函数开始收敛为止。（一般都采用均方差作为标准测度函数。）*/
public class Kmeans {
	public Kmeans() {
	}

	/*
     * Input double[numIns][numAtt] features, int K
     * Output double[K][numAtt] clusterCenters, int[numIns] clusterIndex
     *
     * clusterCenters[k] should store the kth cluster center
     * clusterIndex[i] should store the cluster index which the ith sample belongs to
     */
	public void train(double[][] features, int K, double[][] clusterCenters, int[] clusterIndex) {
		int numIns = features.length;
		if (numIns == 0) return;
		int numAtt = features[0].length;

		kill_missing_data(features, numIns, numAtt);

		//随机初始化中心点
		Random random = new Random();
		HashSet<Integer> checker = new HashSet<Integer>();
		for (int i = 0; i < K; ++i) {
			int center = 0;
			do {
				center = random.nextInt(numIns);
			} while (checker.contains(center));
			checker.add(center);

			clusterCenters[i] = features[center].clone();
		}
		for (int i = 0; i < numIns; ++i) {
			clusterIndex[i] = -1;
		}

		//迭代更新
		boolean flag;
		while (true) {
			flag = false;

			//记录数组，辅助计算新中心点
			double[][] temp = new double[K][numAtt];
			int[] counter = new int[K];
			for (int i = 0; i < K; ++i) {
				for (int j = 0; j < numAtt; ++j) {
					temp[i][j] = 0;
				}
				counter[i] = 0;
			}

			for (int i = 0; i < numIns; ++i) {
				int index = closestCluster(features[i], clusterCenters, K);

				//记录辅助值
				counter[index]++;
				for (int j = 0; j < numAtt; ++j) {
					temp[index][j] += features[i][j];
				}

				if (index != clusterIndex[i]) {
					//更新簇标号
					flag = true;
					clusterIndex[i] = index;
				}
			}

			if (flag) {
				//用辅助值更新蔟均值
				for (int i = 0; i < K; ++i) {
					for (int j = 0; j < numAtt; ++j) {
						clusterCenters[i][j] = temp[i][j] / counter[i];
					}
				}
			} else {
				//已稳定，迭代结束
				break;
			}
		}

		return;
	}

	private int closestCluster(double[] feature, double[][] clusterCenters, int K) {
		int cluster = -1;
		double min = -1;

		for (int i = 0; i < K; ++i) {
			double dist = distance(feature, clusterCenters[i]);
			if (min < 0 || dist < min) {
				cluster = i;
				min = dist;
			}
		}
		return cluster;
	}

	//计算两个样本间的欧式距离
	private double distance(double[] a, double[] b) {
		if (a.length != b.length) return 0;

		int length = a.length;
		if (length == 0) return 0;

		double result = 0;
		for (int i = 0; i < length; ++i) {
			result += (a[i] - b[i]) * (a[i] - b[i]);
		}
		return result;
	}

	//缺失值简单处理为平均值
	private void kill_missing_data(double[][] features, int numIns, int numAtt) {
		double[] defaults = new double[numAtt];

		for (int i = 0; i < numAtt; ++i) {
			int count = 0;
			double total = 0;
			for (int j = 0; j < numIns; ++j) {
				if (!Double.isNaN(features[j][i])) {
					count++;
					total += features[j][i];
				}
			}
			defaults[i] = total / count;
		}

		//代换
		for (int i = 0; i < numIns; ++i) {
			for (int j = 0; j < numAtt; ++j) {
				if (Double.isNaN(features[i][j])) {
					features[i][j] = defaults[j];
				}
			}
		}
		return;
	}
}