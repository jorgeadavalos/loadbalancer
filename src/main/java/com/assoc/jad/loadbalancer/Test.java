package com.assoc.jad.loadbalancer;

import java.io.FileInputStream;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Test {

	public static void main(String[] args) {
//		int[] wrkarr = {1, 2, 3, 4, 3, 6};
//		int[] wrkarr = {1, 2, 5, 3, 5};
//		int[] wrkarr = {1, 3, 2, 1};
//		int[] wrkarr = {1, 3, 2, 1};		//false
//		int[] wrkarr = {10, 1, 2, 3, 4, 5};	//true 
//		int[] wrkarr = {3, 5, 67, 98, 3};	//true 
		
		String jsonstr = readFile("/users/jorge/Downloads/test-36.json");
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonstr);
			jsonObj = (JSONObject) jsonObj.get("input");
			JSONArray jsonArray = (JSONArray) jsonObj.get("sequence");
			int[] wrkarr = toIntArray(jsonArray);
			long start = System.currentTimeMillis();
			boolean flag = solution(wrkarr);
			long len = System.currentTimeMillis() -start;
			System.out.println(""+len+" "+flag);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public static boolean solution(int[] sequence) {
	    int[] copyarr = new int[sequence.length-1];
	    for (int i=0;i<sequence.length;i++) {
	    	int x =0;
            for (int j=0;j<sequence.length;j++)
            	if (j!=i) copyarr[x++] = sequence[j];
            if (isAsc(copyarr)) return true;
	    }
	    return false;
	}
	public static boolean isAsc(int[] sequence) {	
		int len = sequence.length;
	    for (int i=0;i<len;i++) {
	    	if (i <len-1)
	    		if (sequence[i] <sequence[i+1]) continue;
            return false;
	    }
	    return true;
	}
	public static int[] toIntArray(JSONArray array) {
	    if(array==null)
	        return new int[0];
	    
	    int[] arr=new int[array.size()];
	    for(int i=0; i<arr.length; i++) {
	        Long item = (Long)array.get(i);
	        arr[i]= item.intValue();
	    }
	    return arr;
	}	
	public static String readFile(String filename) {
		byte[] bytes = new byte[4096];
		StringBuilder sb = new StringBuilder();
		FileInputStream fileInputStream = null;
		int len = -1;
		try {
			fileInputStream = new FileInputStream(filename);
			while ((len = fileInputStream.read(bytes)) != -1) {
				String wrkstr = new String(bytes,0,len);
				sb.append(wrkstr);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileInputStream != null) {
				try {fileInputStream.close();} catch (IOException e) {};
			}
		}
		return sb.toString();

	}
}
//for (int i=0;i<sequence.length;i++) System.out.print(sequence[i]);
