package com.code.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
			String testCase2 = "014443e6-1f53-420d-8470-70d2011cfc02";
			String testCase3 = "\"state\":\"past_due\"";
			ArrayList<String> testCasesLst = new ArrayList<>();
			testCasesLst.add(testCase1);
			testCasesLst.add(testCase2);
			testCasesLst.add(testCase3);
			Map<String, String> result = new HashMap<String, String>();
			File folder = new File(folderLocation);
			if (folder.isDirectory() && folder.exists()) {
				File files[] = folder.listFiles();
				for (File inputFile : files) {
					fin = new FileReader(inputFile);
					bin = new BufferedReader(fin);
					int lineNumber = 1;
					searchHelper(lineNumber, bin, testCasesLst, result);
					if (!result.isEmpty()) {
						System.out.println("*********************************************");
						System.out.println("Found in:" + inputFile.getName());
						Iterator<String> resultKeysIterator = result.keySet().iterator();
						while(resultKeysIterator.hasNext()){
							String key = resultKeysIterator.next();
							String value = result.get(key);
							System.out.println("Line Number:"+key+" Test Case:"+value);
						}
						System.out.println("*********************************************");
					}
					fin.close();
					bin.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
	

	public static void searchHelper(int lineNumber, BufferedReader bin, ArrayList<String> testCasesLst, Map<String, String> result) {
		try {
			if (testCasesLst.isEmpty()) {
				return;
			}
			String line;
			while ((line = bin.readLine()) != null) {
				if (testCasesLst.isEmpty()) {
					return;
				}
				if (testCase(lineNumber++, line, testCasesLst.get(0), result)) {
					testCasesLst.remove(0);
					if (testCasesLst.isEmpty()) {
						return;
					} else {
						searchHelper(lineNumber, bin, testCasesLst, result);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean testCase(int lineNumber, String input, String testCase, Map<String, String> result) {
		try {
			if (input.contains(testCase)) {
				result.put(lineNumber + "", testCase);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
