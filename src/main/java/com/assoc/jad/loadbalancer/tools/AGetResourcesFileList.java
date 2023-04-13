package com.assoc.jad.loadbalancer.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AGetResourcesFileList {
	private static final Log LOG = LogFactory.getLog(AGetResourcesFileList.class);

	protected List<String> getResourceFiles(String path1,String path2) {
	    List<String> filenames = new ArrayList<String>();
	     
        InputStream in = getResourceAsStream(path1);
        if (in == null)  in = getResourceAsStream(path2);
        if (in == null) return filenames;
        
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String resource;

        try {
			while ((resource = br.readLine()) != null) {
			    filenames.add(resource);
			}
		} catch (IOException e) {
			LOG.warn(e);
		}

	    return filenames;
	}
	private InputStream getResourceAsStream(String resource) {
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

	    final InputStream in = contextClassLoader.getResourceAsStream(resource);

	    return in == null ? getClass().getResourceAsStream(resource) : in;
	}
}
