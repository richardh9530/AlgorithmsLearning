package liuyang.nlp.lda.main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import liuyang.nlp.lda.com.FileUtil;
import liuyang.nlp.lda.com.Stopwords;

/**Class for corpus which consists of M documents
 * @author yangliu
 * @blog http://blog.csdn.net/yangliuy
 * @mail yangliuyx@gmail.com
 */

public class Documents {
	
	ArrayList<Document> docs; 
	Map<String, Integer> termToIndexMap;
	ArrayList<String> indexToTermMap;
	Map<String,Integer> termCountMap;
	
	public Documents(){
		docs = new ArrayList<Document>();
		termToIndexMap = new HashMap<String, Integer>();
		indexToTermMap = new ArrayList<String>();
		termCountMap = new HashMap<String, Integer>();
	}

	/**
	 * 读取文件夹下的每一篇文档
	 * @param docsPath：文件夹路径
	 */
	public void readDocs(String docsPath){
		for(File docFile : new File(docsPath).listFiles()){
			Document doc = new Document(docFile.getAbsolutePath(), termToIndexMap, indexToTermMap, termCountMap);
			docs.add(doc);
		}
	}
	/**
	 * 每个文件代表一篇文档
	 */
	public static class Document {	
		private String docName;
		int[] docWords;

		/**
		 * 构造函数
		 * @param docName  文件的绝对路径
		 * @param termToIndexMap 词到索引的映射
		 * @param indexToTermMap 索引到词的映射
		 * @param termCountMap 词的计数的映射
		 */
		public Document(String docName, Map<String, Integer> termToIndexMap,
						ArrayList<String> indexToTermMap, Map<String, Integer> termCountMap){
			this.docName = docName;
			//Read file and initialize word index array
			ArrayList<String> docLines = new ArrayList<String>();
			ArrayList<String> words = new ArrayList<String>();
			FileUtil.readLines(docName, docLines);
			for(String line : docLines){
				FileUtil.tokenizeAndLowerCase(line, words);
			}
			//Remove stop words and noise words
			for(int i = 0; i < words.size(); i++){
				if(Stopwords.isStopword(words.get(i)) || isNoiseWord(words.get(i))){
					words.remove(i);
					i--;
				}
			}
			//Transfer word to index
			this.docWords = new int[words.size()];
			for(int i = 0; i < words.size(); i++){
				String word = words.get(i);
				if(!termToIndexMap.containsKey(word)){ // 原来的Map中不含有该词，需要添加
					int newIndex = termToIndexMap.size();  // 词到索引的映射，返回size
					termToIndexMap.put(word, newIndex);  // 加到最后
					indexToTermMap.add(word); // index的顺序就是索引，所以这里的映射实际上是ArrayList
					termCountMap.put(word, new Integer(1));  // 统计词频
					docWords[i] = newIndex;
				} else {
					docWords[i] = termToIndexMap.get(word);  // 返回索引
					termCountMap.put(word, termCountMap.get(word) + 1);  // 统计词频
				}
			}
			words.clear(); // 释放内存
		}
		
		public boolean isNoiseWord(String string) {
			// TODO Auto-generated method stub
			string = string.toLowerCase().trim();
			Pattern MY_PATTERN = Pattern.compile(".*[a-zA-Z]+.*");
			Matcher m = MY_PATTERN.matcher(string);
			// filter @xxx and URL
			if(string.matches(".*www\\..*") || string.matches(".*\\.com.*") || 
					string.matches(".*http:.*") )
				return true;
			if (!m.matches()) {
				return true;
			} else
				return false;
		}
		
	}
}
