package com.code.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SearchUtilites {

	public static void main(String[] args) throws IOException {
		search();
//		String line ="2018-01-10 16:57:32 DEBUG InvoiceDAOImpl:1109 - entered getInvoicePaymentsIds invoiceIds:'114ebba4-f198-4599-bda4-1a2deeb9b017','18ce00e4-01be-407c-bb7e-987da90e474e','1a058ff6-8805-4067-a51a-abaca662e099','1cff04c4-df37-4034-8828-a659cc3400df','1d5be404-2fc9-4e3a-92fe-c2be2a669910','1ebf9921-6c96-4356-af0c-1d0012beed95','25799274-df17-4b27-8186-1117e8c2c244','2b0e1fab-fbb9-4296-a3dd-d15d13546982','2c570a43-8cb0-4995-9ffe-b679feefe4e9','2c6dd1c0-81dd-46be-8c3a-34781061f28f','2fe3fa9f-d8e1-446a-b329-351ccced9f24','34a9da03-6ade-409e-a81f-9ac279085451','47908221-5f43-4c85-a83c-12eb7c92d6dc','4dd5c627-4618-46b9-80e0-ab7007eac18b','4e2d67a2-4032-449f-a947-a81c1520f66a','63614e29-9df3-4fd2-a5be-846c3817bb07','642f6208-e7b5-48c7-8a2d-e55d41e42ca1','656aabe7-f71e-4bd4-8e17-025727c48c44','69e052b0-39cf-4075-993e-d5f5b78cac0c','6fc08d19-5b6f-449f-85c1-9b1be380deda','7242a316-0a07-4635-bd0a-8d15d7ddf509','72e1619c-ecd4-4070-afc0-a7c7b3325ae6','73742590-c196-4d2a-a6fc-b74e5b206e8d','75c2c15d-953f-4008-a00c-75b19291f95d','75f00e5c-af78-41e4-a9dd-d612e4c57b75','789df904-dbd2-4ccb-b8f0-49aabaf6f3b5','7dc1d96a-9332-41d1-b923-36462258a78b','822c7f4f-e202-4efe-ab4d-666b78cafffd','82a68b02-49f5-439d-a8a3-15ae41810ade','8521e407-af11-4ada-b1e8-6234e086fe59','85226729-0f10-47aa-b353-562f43787a41','87374dc7-4fe4-4ed7-8778-e485de0347b8','8a35d94d-e9e6-486c-8ad0-deb0fa7505ce','8b70a1c6-2bec-4d3b-b3cd-f25160c5ad4f','8f2ef10e-72d1-4a02-8cbc-617159d0dbc3','90c46ae7-4365-4e42-af51-1140320c1c52','920c59f2-8899-4657-a7ef-5fcf0cebce65','94dbad5e-d90e-4b0b-aef3-3a6e4459bb83','996ac5c1-0a1d-41fa-aa8b-381edfe29cd5','9c4570fe-ad8a-49a9-a53f-b0cbdc7b4aac','a3207849-e3a2-4294-9e70-78e6708e6e84','b29f5574-fa4b-4129-82e1-55c6324d15de','b4c3a523-2fb3-4290-ae5d-35fac775910e','b73d834a-288e-4eff-972c-8c5720eabf39','b917068b-02c3-480f-985d-228810dde779','ba40290b-96b9-4f84-bb15-4105f123e530','ba902d7b-2a86-4221-b4fd-fd40fdde5ff0','bacbfa73-7650-4f82-a8ad-ba4a2593dfeb','bd44bda3-e742-49bc-bcec-93c865a3c424','c4bde85d-bc0c-44cd-8ffc-1b3698893af5','d0bf394c-64c8-436c-b3d9-a3181dc50ae0','dd259f3e-05f9-4405-8ef8-74c3c8e43d42','e16c885c-963a-471d-8302-47ef9ead3a3c','e182e046-ac0e-4ecb-ae64-ff3b340c3668','e5e7b2f9-a7bb-414a-9337-d9c4bc787c44','e7023650-0cbd-4fa6-ac54-72d16557ad92','e759ba12-7ee3-48ff-96fa-5501f3743674','ee39a617-4ca5-4708-9340-4c733470122a','f364db09-a84f-4431-b275-1563034d48e6','fa9dbf73-1db9-43a4-bbac-b7eba6456663','ff0a5f05-e43b-4127-9507-e03b626f526b'";
//		String testCase = "82a68b02-49f5-439d-a8a3-15ae41810ade";
//		String invalidTestCase = "','"+testCase+',';
//		if(line.contains(testCase) && line.contains(invalidTestCase)){
//			System.out.println("found");
//		}
//		System.out.println("done");
	}

	public static void search() throws IOException {
		FileReader fin = null;
		BufferedReader bin = null;
		long startTime = System.currentTimeMillis();
		try {
			int lineBuffer = 200;
			String invalidTestCase2 = "getInvoiceIds";
			String invalidTestCase3 = " - Entity: [";
			String invalidTestCase4 = "TRACE ConstraintTree:112 - Validating value [";
			String invalidTestCase5 = "payments:[";
			String invalidTestCase6 = "exited getInvoicePaymentsIds invoiceIds";
			String invalidTestCase7 = "entered getInvoicePaymentsIds invoiceIds";
			List<String> invalidTestCasesList = new ArrayList<String>();
			invalidTestCasesList.add(invalidTestCase2);
			invalidTestCasesList.add(invalidTestCase3);
			invalidTestCasesList.add(invalidTestCase4);
			invalidTestCasesList.add(invalidTestCase5);
			invalidTestCasesList.add(invalidTestCase6);
			invalidTestCasesList.add(invalidTestCase7);
			
			ArrayList<String> testCasesLst = new ArrayList<>();
			testCasesLst.add("013b8251-0b66-4bcc-becf-2853938002f9");
//			testCasesLst.add("Method Name : {} createPayment");
			String folderLocation = "C:/Users/MateenAhmed/Desktop/logs/prod/invoices/all_2/";
			Map<String,ArrayList<Integer>> result = new LinkedHashMap<String,ArrayList<Integer>>();
			File folder = new File(folderLocation);
			if (folder.isDirectory() && folder.exists()) {
				File files[] = folder.listFiles();
				for (File inputFile : files) {
					if(!inputFile.isFile() || inputFile.getName().contains("catalina")|| inputFile.getName().contains("host-manager")|| inputFile.getName().contains("localhost") || inputFile.getName().equals("localhost_access_log") || inputFile.getName().contains("manager")){
						continue;
					}
					fin = new FileReader(inputFile);
					bin = new BufferedReader(fin);
					int lineNumber = 1;
					nestedSearch(inputFile.getName(),lineNumber, bin, testCasesLst, result, lineBuffer, invalidTestCasesList);
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

	public static void nestedSearch(String filename,int lineNumber, BufferedReader bin, ArrayList<String> testCasesLst, Map<String,ArrayList<Integer>> result,int lineBuffer, List<String> invalidTestCasesList) {
		try {
			String line;
			Deque<String> deque = new LinkedList<String>(testCasesLst);
			int lineMatchInterval = 1;
			boolean matchFound=false;
			boolean continueOuter = false;
			while ((line = bin.readLine()) != null) {
				lineNumber++;
				if(!(line.trim().length()>0)){
					continue;
				}
				String testCase = deque.getFirst();
				String invalidTestCase = "','"+testCase+"','";
				continueOuter = false;
				if(invalidTestCasesList!=null && !invalidTestCasesList.isEmpty()){
					invalidTestCasesList.add(invalidTestCase);
					for(int invalidCtr =0 ; invalidCtr<invalidTestCasesList.size();invalidCtr++){
						if(line.contains(invalidTestCasesList.get(invalidCtr))){
							continueOuter = true;
							break;
						}
					}
				}
				if(continueOuter){
					continue;
				}
				if(lineMatchInterval>=lineBuffer){
					deque = new LinkedList<String>(testCasesLst);
					lineMatchInterval = 1;
					matchFound=false;
				}
				if(line.contains(testCase) ){
					deque.pop();
					matchFound=true;
					if(deque.size()==0 && lineMatchInterval<lineBuffer){
						if(result.get(filename)==null){
							ArrayList<Integer> lineNumbers = new ArrayList<Integer>();
							lineNumbers.add(lineNumber-1);
							result.put(filename,lineNumbers);
						}else{
							result.get(filename).add(lineNumber-1);
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
