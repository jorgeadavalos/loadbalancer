package com.assoc.jad.loadbalancer;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.assoc.jad.loadbalancer.tools.LoadBalancerStatic;

/**
 * determine connectivity for every application and every instance in the static hashMap of applications.
 * verify that the server is up and that the application is up.
 *  
 * @author jorge
 *
 */
public class InstancesHealthCheck implements Runnable {
	
	private static final Logger LOGGER = Logger.getLogger( InstancesHealthCheck.class.getName() );
	@Override
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			LoadBalancerStatic.applications.keySet().forEach(a -> instanceHealth(a));
			try {
				Thread.sleep(60000, 0);
			} catch (InterruptedException e) {
				LOGGER.log(Level.INFO,"InstancesHealthCheck brought down by interrupt ");
				System.out.println("InstancesHealthCheck brought down by interrupt ");
				return;
			}
		}
	}
	private Object instanceHealth(String application) {
		LoadBalancerStatic.applications.get(application).keySet().forEach(a -> healthCheck(a,application));
		return null;
	}
	private Object healthCheck(String instanceKey,String application) {
		
		String wrkString = instanceKey.replaceAll("http://", "");
		int ndx = wrkString.indexOf(":");
		if (ndx == -1) ndx = wrkString.indexOf("/");
		
		if (ndx == -1) ndx = wrkString.length();
		String host = wrkString.substring(0, ndx);
		
		HashMap<String,HashMap<String,String>> instances = LoadBalancerStatic.applications.get(application);
		HashMap<String,String> instanceApp = instances.get(instanceKey);
		instanceApp.put(LoadBalancerStatic.SERVERUP, pingServer(host).toString());
		instanceApp.put(LoadBalancerStatic.INSTANCEUP, pingInstance(instanceKey).toString());
		return null;
	}
	private Boolean pingInstance(String instanceKey) {
		byte[] bytes = new byte[4096];
		if (instanceKey.indexOf("index.xhtml") != -1) instanceKey = instanceKey.replaceFirst("index.xhtml", "heartBeat.xhtml") ;
		try {
			URL url = new URL(instanceKey);
			URLConnection urlC = url.openConnection();
			InputStream is = urlC.getInputStream();
			is.read(bytes);
		}
		catch (IOException e) {
			LOGGER.log(Level.FINE,"URL "+instanceKey+" "+e.getMessage());
			if (e.getClass().toString().indexOf("java.net.ConnectException") != -1) return false;
			return true;
		} 
		return true;
	}
	private Boolean pingServer(String host) {
		NetworkInterface networkInterface = null;
		try {
			InetAddress inetAddress = InetAddress.getByName(host);
			return inetAddress.isReachable(networkInterface,2,3000);
		} catch (Exception e) {
			LOGGER.info(() -> "invalid host "+host);
			return false;
		}
	}
}
