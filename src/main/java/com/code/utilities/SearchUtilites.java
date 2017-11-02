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
		try {
			 String folderLocation = "C:/Users/MateenAhmed/Desktop/logs/prod/invoices/";
			String testCase1 = "Method Name : {} updateInvoices";
//			String testCase2 = "76aa668e-b23a-4582-9a45-642820cfb4f0";
//			String testCase2 = "256d0e09-fb49-4f66-ba6c-0a0271e859c0";
//			String testCase2 = "014443e6-1f53-420d-8470-70d2011cfc02";
//			String testCase2 = "5f20695b-b93c-4ec1-bddc-6d73f0b2878c";
			String testCase2 = "250973b7-1c4b-4bd9-8b58-5328765ca511";
			String testCase3 = "\"state\":\"past_due\"";
			ArrayList<String> testCasesLst = new ArrayList<>();
			testCasesLst.add(testCase1);
			testCasesLst.add(testCase2);
			testCasesLst.add(testCase3);
			Map<String,ArrayList<Integer>> result = new HashMap<String,ArrayList<Integer>>();
			File folder = new File(folderLocation);
			if (folder.isDirectory() && folder.exists()) {
				File files[] = folder.listFiles();
				for (File inputFile : files) {
					fin = new FileReader(inputFile);
					bin = new BufferedReader(fin);
					int lineNumber = 1;
					int lineBuffer = 60;
					// System.out.println("testCasesLst:"+testCasesLst);
					searchHelper(inputFile.getName(),lineNumber, bin, testCasesLst, result, lineBuffer);
					// System.out.println("map:"+result);
					fin.close();
					bin.close();
				}
				if (!result.isEmpty()) {
					System.out.println("*********************************************");
					Iterator<String> itr = result.keySet().iterator();
					while(itr.hasNext()){
						String key = itr.next();
						System.out.println(key+""+result.get(key));
					}
					System.out.println("*********************************************");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	public static void searchHelper(String filename,int lineNumber, BufferedReader bin, ArrayList<String> testCasesLst, Map<String,ArrayList<Integer>> result,int lineBuffer) {
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
