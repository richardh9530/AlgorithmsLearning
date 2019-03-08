package org.study.references.nlp.lda.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.study.references.nlp.lda.com.FileUtil;
import org.study.references.nlp.lda.conf.ConstantConfig;
import org.study.references.nlp.lda.conf.PathConfig;

/**Liu Yang's implementation of Gibbs Sampling of LDA
 * @author yangliu
 * @blog http://blog.csdn.net/yangliuy
 * @mail yangliuyx@gmail.com
 */

public class LdaGibbsSampling {
	/*
	 即设定alpha和beta的值为0.5和0.1，
	  Topic数目为10，迭代100次，
	  从第80次开始保存模型结果，每10次保存一次。
	 */
	public static class modelparameters {
		float alpha = 0.5f; //usual value is 50 / K
		float beta = 0.1f;//usual value is 0.1
		int topicNum = 100;
		int iteration = 100;
		int saveStep = 10;
		int beginSaveIters = 50;
	}
	
	/**Get parameters from configuring file. If the 
	 * configuring file has value in it, use the value.
	 * Else the default value in program will be used
	 * @param ldaparameters： LDA模型参数
	 * @param parameterFile：参数文件
	 * @return void
	 */
	private static void getParametersFromFile(modelparameters ldaparameters,
			String parameterFile) {
		// TODO Auto-generated method stub
		ArrayList<String> paramLines = new ArrayList<String>();
		FileUtil.readLines(parameterFile, paramLines);
		for(String line : paramLines){
			String[] lineParts = line.split("\t");
			switch(parameters.valueOf(lineParts[0])){
			case alpha:
				ldaparameters.alpha = Float.valueOf(lineParts[1]);
				break;
			case beta:
				ldaparameters.beta = Float.valueOf(lineParts[1]);
				break;
			case topicNum:
				ldaparameters.topicNum = Integer.valueOf(lineParts[1]);
				break;
			case iteration:
				ldaparameters.iteration = Integer.valueOf(lineParts[1]);
				break;
			case saveStep:
				ldaparameters.saveStep = Integer.valueOf(lineParts[1]);
				break;
			case beginSaveIters:
				ldaparameters.beginSaveIters = Integer.valueOf(lineParts[1]);
				break;
			}
		}
	}
	
	public enum parameters{
		alpha, beta, topicNum, iteration, saveStep, beginSaveIters;
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String originalDocsPath = PathConfig.ldaDocsPath;  // 文档所在文件夹的路径
		String resultPath = PathConfig.LdaResultsPath;  // 模型结果保存路径
		String parameterFile= ConstantConfig.LDAPARAMETERFILE;  // 参数保存路径
		
		modelparameters ldaparameters = new modelparameters();  // 模型参数
		getParametersFromFile(ldaparameters, parameterFile);  // 从文件中读取模型参数
		Documents docSet = new Documents();  // 语料的定义
		docSet.readDocs(originalDocsPath);  // 从文件夹下读取文件
		System.out.println("wordMap size " + docSet.termToIndexMap.size());  // 词典中词的数量

		FileUtil.mkdir(new File(resultPath));  // 模型结果保存路径

		LdaModel model = new LdaModel(ldaparameters); // 模型
		System.out.println("1 Initialize the model ...");
		model.initializeModel(docSet);  // 模型初始化，给每篇文档的每个单词随机赋一个topic

		System.out.println("2 Learning and Saving the model ...");
		model.inferenceModel(docSet);

		System.out.println("3 Output the final model ...");
		// iter计数最大为99，所以最后一次手动输出
		model.saveIteratedModel(ldaparameters.iteration, docSet);
		System.out.println("Done!");
	}
}
