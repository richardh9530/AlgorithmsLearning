package org.study.references.nlp.lda.main;

/**Class for Lda model
 * @author yangliu
 * @blog http://blog.csdn.net/yangliuy
 * @mail yangliuyx@gmail.com
 */
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.study.references.nlp.lda.com.FileUtil;
import org.study.references.nlp.lda.conf.PathConfig;

public class LdaModel {
	
	int [][] doc;//word index array
	int V, K, M;//vocabulary size, topic number, document number
	int [][] z;//topic label array
	float alpha; //doc-topic dirichlet prior parameter 
	float beta; //topic-word dirichlet prior parameter
	int [][] nmk;//given document m, count times of topic k. M*K
	int [][] nkt;//given topic k, count times of term t. K*V
	int [] nmkSum;//Sum for each row in nmk M篇文档，每篇文档在K个主题下word的计数之和
	int [] nktSum;//Sum for each row in nkt
	double [][] phi;//Parameters for topic-word distribution K*V
	double [][] theta;//Parameters for doc-topic distribution M*K
	int iterations;//Times of iterations
	int saveStep;//The number of iterations between two saving
	int beginSaveIters;//Begin save model at this iteration
	
	public LdaModel(LdaGibbsSampling.modelparameters modelparam) {
		// TODO Auto-generated constructor stub
		alpha = modelparam.alpha;
		beta = modelparam.beta;
		iterations = modelparam.iteration;
		K = modelparam.topicNum;  // 主题数目
		saveStep = modelparam.saveStep;
		beginSaveIters = modelparam.beginSaveIters;
	}

	/**
	 * 初始化模型
	 * @param docSet  文档集合
	 */
	void initializeModel(Documents docSet) {
		// TODO Auto-generated method stub
		M = docSet.docs.size();  // 文档数量
		V = docSet.termToIndexMap.size();  // 词的数量
		nmk = new int [M][K];  // M篇文档，对应每一篇文档在每个主题（一共K个主题）下的计数
		nkt = new int[K][V];
		nmkSum = new int[M];  // M篇文档
		nktSum = new int[K];  // K个主题，每个主题的词（term)的计数
		phi = new double[K][V];
		theta = new double[M][K];
		
		//initialize documents index array
		// 二维数组，保存所有文档所有词的索引，每一行代表一篇文档，
		// 行中的每一个值表示词在termToIndexMap中的索引
		doc = new int[M][];
		for(int m = 0; m < M; m++){
			//Notice the limit of memory
			int N = docSet.docs.get(m).docWords.length;  // 第m篇文档词的数量
			doc[m] = new int[N];
			for(int n = 0; n < N; n++){
				doc[m][n] = docSet.docs.get(m).docWords[n];
			}
		}
		
		//initialize topic lable z for each word
		// M 篇文档 每篇文档N_m个词，每个词对应一个主题
		z = new int[M][];
		for(int m = 0; m < M; m++){
			int N = docSet.docs.get(m).docWords.length;
			z[m] = new int[N];
			for(int n = 0; n < N; n++){
				int initTopic = (int)(Math.random() * K);// From 0 to K - 1  随机分配一个主题
				z[m][n] = initTopic;
				//number of words in doc m assigned to topic initTopic add 1
				nmk[m][initTopic]++;
				//number of terms doc[m][n] assigned to topic initTopic add 1
				nkt[initTopic][doc[m][n]]++;  // 每个主题下 term的数量
				// total number of words assigned to topic initTopic add 1
				nktSum[initTopic]++;
			}
			 // total number of words in document m is N
			nmkSum[m] = N;
		}
	}

	/**
	 * 模型的推理
	 * @param docSet  文档集合
	 * @throws IOException
	 */
	public void inferenceModel(Documents docSet) throws IOException {
		// TODO Auto-generated method stub
		if(iterations < saveStep + beginSaveIters){
			System.err.println("Error: the number of iterations should be larger than " + (saveStep + beginSaveIters));
			System.exit(0);
		}
		for(int i = 0; i < iterations; i++){  // 迭代次数
			System.out.println("Iteration " + i);
			if((i >= beginSaveIters) && (((i - beginSaveIters) % saveStep) == 0)){ // 保存模型
				//Saving the model
				System.out.println("Saving model at iteration " + i +" ... ");
				//Firstly update parameters
				updateEstimatedParameters();  // 更新参数 theta和phi
				//Secondly print model variables
				saveIteratedModel(i, docSet);
			}
			
			//Use Gibbs Sampling to update z[][]
			for(int m = 0; m < M; m++){
				int N = docSet.docs.get(m).docWords.length;
				for(int n = 0; n < N; n++){
					// Sample from p(z_i|z_-i, w)
					int newTopic = sampleTopicZ(m, n);
					z[m][n] = newTopic;
				}
			}
		}
	}
	
	private void updateEstimatedParameters() {
		/*
		 * 以下词分布phi的参数
		 * K个主题
		 * 对每个主题，V个term中每个term属于该主题的概率
		 * K*V维
		 * */
		for(int k = 0; k < K; k++){
			for(int t = 0; t < V; t++){
				phi[k][t] = (nkt[k][t] + beta) / (nktSum[k] + V * beta);
			}
		}

		/*
		 * 主题分布theta：m篇文档，每篇文档属于K个主题中每个主题的概率
		 * M*K维
		 * */
		for(int m = 0; m < M; m++){
			for(int k = 0; k < K; k++){
				theta[m][k] = (nmk[m][k] + alpha) / (nmkSum[m] + K * alpha);
			}
		}
	}

	/**
	 * 采样
	 * @param m: 第m篇文档
	 * @param n：第n个词
	 * @return
	 */
	private int sampleTopicZ(int m, int n) {
		// TODO Auto-generated method stub
		// Sample from p(z_i|z_-i, w) using Gibbs upde rule
		
		//Remove topic label for w_{m,n}
		int oldTopic = z[m][n];
		nmk[m][oldTopic]--;
		nkt[oldTopic][doc[m][n]]--;
		nmkSum[m]--;
		nktSum[oldTopic]--;
		
		//Compute p(z_i = k|z_-i, w)
		double [] p = new double[K];
		for(int k = 0; k < K; k++){
			p[k] = (nkt[k][doc[m][n]] + beta) / (nktSum[k] + V * beta) * (nmk[m][k] + alpha) / (nmkSum[m] + K * alpha);
		}
		
		//Sample a new topic label for w_{m, n} like roulette 轮盘赌选择法
		//Compute cumulated probability for p
		for(int k = 1; k < K; k++){
			p[k] += p[k - 1];
		}
		double u = Math.random() * p[K - 1]; //p[] is unnormalised
		int newTopic;
		for(newTopic = 0; newTopic < K; newTopic++){
			if(u < p[newTopic]){
				break;
			}
		}
		
		//Add new topic label for w_{m, n}
		nmk[m][newTopic]++;
		nkt[newTopic][doc[m][n]]++;
		nmkSum[m]++;
		nktSum[newTopic]++;
		return newTopic;
	}

	/**
	 * 保存模型参数
	 * @param iters  迭代次数
	 * @param docSet  文档集合
	 * @throws IOException
	 */
	public void saveIteratedModel(int iters, Documents docSet) throws IOException {
		//lda.params lda.phi lda.theta lda.tassign lda.twords
		//lda.params
		String resPath = PathConfig.LdaResultsPath;
		String modelName = "lda_" + iters;
		ArrayList<String> lines = new ArrayList<String>();
		lines.add("alpha = " + alpha);
		lines.add("beta = " + beta);
		lines.add("topicNum = " + K);
		lines.add("docNum = " + M);
		lines.add("termNum = " + V);
		lines.add("iterations = " + iterations);
		lines.add("saveStep = " + saveStep);
		lines.add("beginSaveIters = " + beginSaveIters);
		FileUtil.writeLines(resPath + modelName + ".params", lines);
		
		//lda.phi K*V 参数phi
		BufferedWriter writer = new BufferedWriter(new FileWriter(resPath + modelName + ".phi"));		
		for (int i = 0; i < K; i++){
			for (int j = 0; j < V; j++){
				writer.write(phi[i][j] + "\t");
			}
			writer.write("\n");
		}
		writer.close();
		
		//lda.theta M*K  参数theta
		writer = new BufferedWriter(new FileWriter(resPath + modelName + ".theta"));
		for(int i = 0; i < M; i++){
			for(int j = 0; j < K; j++){
				writer.write(theta[i][j] + "\t");
			}
			writer.write("\n");
		}
		writer.close();
		
		//lda.tassign  主题指派
		/*
		 * 对M篇文档，每篇文档有N_m个词
		 * :左边代表term_index, 右边代表topic_index
		 */
		writer = new BufferedWriter(new FileWriter(resPath + modelName + ".tassign"));
		for(int m = 0; m < M; m++){
			for(int n = 0; n < doc[m].length; n++){
				writer.write(doc[m][n] + ":" + z[m][n] + "\t");
			}
			writer.write("\n");
		}
		writer.close();
		
		//lda.twords phi[][] K*V
		writer = new BufferedWriter(new FileWriter(resPath + modelName + ".twords"));
		int topNum = 20; //Find the top 20 topic words in each topic
		for(int i = 0; i < K; i++){
			List<Integer> tWordsIndexArray = new ArrayList<Integer>();  // 主题词分布的下标（索引）
			double phi_sum = 0;
			for(int j = 0; j < V; j++){
				tWordsIndexArray.add(new Integer(j));
				phi_sum += phi[i][j];
			}

			// System.out.println("第"+i+"个phi求和："+phi_sum);
			/*
			 * phi表示词分布，phi是K*V维
			 *也就是横坐标代表第k个主题下的词分布，
			 * 即各个term属于该主题的概率
			 * */
			// 验证了之前的想法，每个主题的对应的所有term之和为1

			Collections.sort(tWordsIndexArray, new LdaModel.TwordsComparable(phi[i]));
			System.out.println(phi[i]);
			writer.write("topic " + i + "\t:\t");
			for(int t = 0; t < topNum; t++){
				writer.write(docSet.indexToTermMap.get(tWordsIndexArray.get(t)) + " " + phi[i][tWordsIndexArray.get(t)] + "\t");
			}
			writer.write("\n");
		}
		writer.close();
	}
	
	public class TwordsComparable implements Comparator<Integer> {
		/**
		 * 排序接口，对每个主题的词分布按照概率大小排序
		 */
		public double [] sortProb; // Store probability of each word in topic k
		
		public TwordsComparable (double[] sortProb){
			this.sortProb = sortProb;
		}

		@Override
		public int compare(Integer o1, Integer o2) {
			/*
			* 主题下的词分布
			* */
			//Sort topic word index according to the probability of each word in topic k
			if(sortProb[o1] > sortProb[o2]) return -1;
			else if(sortProb[o1] < sortProb[o2]) return 1;
			else return 0;
		}
	}
}
