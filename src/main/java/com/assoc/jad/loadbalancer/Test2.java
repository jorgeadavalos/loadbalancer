package com.assoc.jad.loadbalancer;

import java.io.FileInputStream;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Test2 {

	public static void main(String[] args) {
//		int[] wrkarr = {1, 2, 3, 4, 3, 6};			//true
//		int[] wrkarr = {1, 2, 5, 3, 5};				//true
		int[] wrkarr = {3, 6, 5, 8, 10, 20, 15}; 	//false
//		int[] wrkarr = {1, 3, 2, 1};				//false
//		int[] wrkarr = {10, 1, 2, 3, 4, 5};			//true 
//		int[] wrkarr = {3, 5, 67, 98, 3};			//true 
//		int[] wrkarr = {1, 2, 3, 1};				//true 
//		int[] wrkarr = {1, 2, 1, 2};				//false 
		
		String jsonstr = readFile("/jadtemp/test-36.json");
		try {			
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(jsonstr);
			jsonObj = (JSONObject) jsonObj.get("input");
			JSONArray jsonArray = (JSONArray) jsonObj.get("sequence");
			//int[] wrkarr = toIntArray(jsonArray);
			
			long start = System.currentTimeMillis();
			boolean flag = solution(wrkarr);
			long len = System.currentTimeMillis() -start;
			System.out.println(""+len+" "+flag);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}
	public static boolean solution(int[] sequence) {
		final int BOUNDARY = -10000000;
        boolean flag = isAsc(sequence,BOUNDARY);
	    return flag;
	}
	public static boolean isAsc(int[] sequence,int BOUNDARY) {
		int len = sequence.length;
		int ctr = 0;
    	
	    for (int i=0;i<len;i++) {    		
	    	//System.out.println(" "+sequence[i]+" "+i);
	    	if (sequence[i] == BOUNDARY) {
	        	if (i==0 || i == len-1) continue;
	        	if (sequence[i-1] < sequence[i+1]) continue;
	        	ctr++;
	    	} else {
	        	if (i==len-1 || sequence[i+1] == BOUNDARY) continue;
	        	if (sequence[i] < sequence[i+1]) continue;
	        	ctr++;
	        	int ndx = checkAdjacents(sequence,i);
	        	sequence[ndx] = BOUNDARY;
	    	}
	    }
	    if (ctr > 1) return false;
	    return true;
	}
	private static int checkAdjacents(int[] sequence, int i) {
		int ndx = i;
		int len = sequence.length;
    	if (i > 0) {
        	if (sequence[i-1] >= sequence[i+1]) ndx = i+1;
    	}
		return ndx;
	}
	public static int[] isAsc2(int[] sequence,int[] output,int BOUNDARY) {
		int len = sequence.length;
	    for (int i=output[1];i<len;i++) {
	    	output[1] = i-1;

    		if (i<len-1 && sequence[i+1] == BOUNDARY) continue;
    		
	    	if (sequence[i] == BOUNDARY) {
	        	if (i==0 || i == len-1) continue;
	        	if (sequence[i-1] < sequence[i+1]) continue;
	        	output[0] = 0;
	            return output;
	    	} else {
	        	if (i==len-1) continue;
	        	if (sequence[i] < sequence[i+1]) continue;
	        	output[0] = 0;
	            return output;
	    	}
	    }
    	output[0] = 1; //true flag
	    return output;
	}
	public static boolean isAsc1(int[] sequence) {	
		int len = sequence.length;
	    for (int i=0;i<len;i++) {
	    	int prev = sequence[i];
	    	if (i>0) prev = sequence[i-1];
	    	int next = sequence[i];
	    	if (i<len-1) next = sequence[i+1];
	    	int current = sequence[i];
	    	
    		if (i<len-1 && next == -1) continue;
    		
	    	if (current == -1) {
	        	if (i==0 || i == len-1) continue;
	        	if (prev < next) continue;
	            return false;
	    	} else {
	        	if (i==len-1) continue;
	        	if (current < next) continue;
	            return false;
	    	}
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
