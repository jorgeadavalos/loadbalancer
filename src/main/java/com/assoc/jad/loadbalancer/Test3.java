package com.assoc.jad.loadbalancer;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.IntStream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Test3 {

	public static void main(String[] args) {
//		int[] wrkarr = {1, 2, 3, 4, 3, 6};
//		int[] wrkarr = {1, 2, 5, 3, 5};
//		int[] wrkarr = {3, 6, 5, 8, 10, 20, 15}; 	//false
//		int[] wrkarr = {1, 3, 2, 1};				//false
//		int[] wrkarr = {10, 1, 2, 3, 4, 5};			//true 
//		int[] wrkarr = {3, 5, 67, 98, 3};			//true 
//		int[] wrkarr = {1, 2, 3, 1};				//true 

		String jsonstr = readFile("/jadtemp/test-36.json");
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
			e.printStackTrace();
		}

	}
	public static boolean solution(int[] sequence) {
		int len = sequence.length;
		final Integer BOUNDARY = -10000000;
	    return isSorted(sequence, 0,len,0);
	}
		public static boolean isSorted(int[] sequence, int index,int len,int ctr) {
			System.out.println(" "+index+" "+ctr);
		    if (index > len-1) {
		        return true;
		    } else if (sequence[index] >= sequence[index + 1]) {
		    	if (++ctr > 1) return false;
		        return isSorted(sequence, index + 1,len,ctr);
		    } else {
		        return isSorted(sequence, index + 1,len,ctr);
		    }
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
	public static ArrayList<Integer> toIntArrayList(JSONArray array) {
	    if(array==null)
	        return new ArrayList<Integer>();
	    
	    int len = array.size();
	    ArrayList<Integer> arr=new ArrayList<>(len);
	    for(int i=0; i<len; i++) {
	        Long item = (Long)array.get(i);
	        arr.add(item.intValue());
	    }
	    return arr;
	}	
	private static String readFile(String filename) {
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
