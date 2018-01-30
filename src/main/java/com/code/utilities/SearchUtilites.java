package com.code.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class SearchUtilites {

	public static void main(String[] args) throws IOException {
		search();
	}

	public static void search() throws IOException {
		FileReader fin = null;
		BufferedReader bin = null;
		long startTime = System.currentTimeMillis();
		try {
			int lineBuffer = 200;
			ArrayList<String> testCasesLst = new ArrayList<>();
			testCasesLst.add("dd94013c-6e6d-4091-980c-730d9c82659b");
//			testCasesLst.add("Method Name : {} createPayment");
			String folderLocation = "C:/Users/MateenAhmed/Desktop/logs/prod/invoices/nov/";
			Map<String,ArrayList<Integer>> result = new HashMap<String,ArrayList<Integer>>();
			File folder = new File(folderLocation);
			if (folder.isDirectory() && folder.exists()) {
				File files[] = folder.listFiles();
				for (File inputFile : files) {
					if(!inputFile.isFile()){
						continue;
					}
					fin = new FileReader(inputFile);
					bin = new BufferedReader(fin);
					int lineNumber = 1;
					nestedSearch(inputFile.getName(),lineNumber, bin, testCasesLst, result, lineBuffer);
					fin.close();
					bin.close();
				}
				if (!result.isEmpty()) {
					System.out.println("*********************************************");
					Iterator<String> itr = result.keySet().iterator();
					System.out.println(testCasesLst);
					while(itr.hasNext()){
						String key = itr.next();
						System.out.println(key);
						System.out.println(result.get(key));
					}
					System.out.println("*********************************************");
				}
			}
			
			long endTime = System.currentTimeMillis();
			System.out.println("total time in mills");
			System.out.println(endTime - startTime);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	public static void nestedSearch(String filename,int lineNumber, BufferedReader bin, ArrayList<String> testCasesLst, Map<String,ArrayList<Integer>> result,int lineBuffer) {
		try {
			String line;
			Deque<String> deque = new LinkedList<String>(testCasesLst);
			int lineMatchInterval = 1;
			boolean matchFound=false;
			while ((line = bin.readLine()) != null) {
				lineNumber++;
				if(lineMatchInterval>=lineBuffer){
					deque = new LinkedList<String>(testCasesLst);
					lineMatchInterval = 1;
					matchFound=false;
				}
				String testCase = deque.getFirst();
				if(line.contains(testCase)){
					deque.pop();
					matchFound=true;
					if(deque.size()==0 && lineMatchInterval<lineBuffer){
						if(result.get(filename)==null){
							ArrayList<Integer> lineNumbers = new ArrayList<Integer>();
							lineNumbers.add(lineNumber);
							result.put(filename,lineNumbers);
						}else{
							result.get(filename).add(lineNumber);
						}
						deque = new LinkedList<String>(testCasesLst);
						matchFound=false;
						lineMatchInterval = 1;
					}
				}
				if(matchFound){
					lineMatchInterval++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(filename);
			System.out.println(lineNumber);
		}
	}
}
