package com.assoc.jad.loadbalancer.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * load resources from jar. This depends on the type of application ie. tomcat or standalone.
 * <ul> 
 * <li><b>from application container</b> "example.jar!file.txt" get a specific resource and access the jar file name.<br/>
 * Collect all the jar entries for sub-directory.
 * <li><b>from stand alone application</b>.  Get System.getProperty("sun.java.command") .it till have the jar name.
 * <li><b>eclipse or Maven package</b>. The command "jar -t -f filename.jar will give you the jar file structure.
 * 
 * </ul>
 * @author xFact
 *
 */
public class ConfigureXMLTemplates extends AGetResourcesFileList implements Runnable {
	private static Log LOG = LogFactory.getLog(ConfigureXMLTemplates.class);
	private final String JARPREFIX = "resources/loadbalancer/";
	private final String JARPREFIX2 = "loadbalancer/";
	
	private String folder;
	private boolean templatesLoaded = false;

	public ConfigureXMLTemplates(String folder) {
		this.folder = folder;
	}

	@Override
	public void run() {
		List<String> filenames = getResourceFiles(JARPREFIX,JARPREFIX2);
		
		if (filenames.size() > 0) loadTemplatesFiles(filenames);
		else WebAppClassLoader();
	}
	private void loadTemplatesFiles(List<String> filenames) {
		for (String filename : filenames) {
			URL url = getClass().getClassLoader().getResource(folder+"/"+filename);
			URI uri;
			try {
				uri = url.toURI();
				File file = new File(uri);
				setXmlTemplateFiles(file);
			} catch (URISyntaxException e) {
				LOG.warn("invalid URL="+url);
				LOG.warn(e);
			}
		}
		templatesLoaded = true;
	}
	private void WebAppClassLoader() {
		ArrayList<File> files = bldJarFileWithResources();
		
		if (files.size() > 0) this.templatesLoaded = true;
		else  {
			LOG.warn("during configuration::resource files were not found");
		}
		for (File file : files) {
			setXmlTemplateFiles(file);
		}
	}
	private ArrayList<File>  bldJarFileWithResources() {
		ArrayList<File> files = new ArrayList<File>();

		URL url = getClass().getClassLoader().getResource(JARPREFIX);
		if (url == null)  url = getClass().getClassLoader().getResource(JARPREFIX2);
		String jarFileLocation = url.getFile();
		int ndx1 = jarFileLocation.indexOf("!");
		if (url == null || ndx1 == -1) {
			String standAlone = System.getProperty("sun.java.command");
			if (standAlone != null && standAlone.endsWith(".jar")) {
				files = checkStandAlone(standAlone);
				return files;
			}
		}
		int ndx2 = jarFileLocation.indexOf("/");
		String jarname = jarFileLocation.substring(++ndx2,ndx1);
		return bldFileArray(jarname);

	}
	private ArrayList<File> checkStandAlone(String jarName) {
		
		return bldFileArray(jarName);
	}
	@SuppressWarnings("resource")
	private ArrayList<File> bldFileArray(String jarName) {
		ArrayList<File> files = new ArrayList<File>();
		FileOutputStream fos = null;
		InputStream is = null;
		try {
			JarFile jarFile = new JarFile(jarName);
			Enumeration<JarEntry> jarEntries = jarFile.entries();
			while ( jarEntries.hasMoreElements()) {
				JarEntry jarEntry = jarEntries.nextElement();
				if (jarEntry.getName().indexOf(JARPREFIX2) == -1) continue;
				if (jarEntry.isDirectory()) continue;
				
			      File file = File.createTempFile(jarEntry.getName(), ".xml");
			      fos = new FileOutputStream(file);
				  is = jarFile.getInputStream(jarEntry);
				  byte[] xmlTemplate = new byte[is.available()];
			      while (is.read(xmlTemplate) != -1) {
					  fos.write(xmlTemplate);
			      }
			      files.add(file);
			      fos.close();
			      is.close();
			} 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) try { is.close();} catch (IOException e) {}
			if (fos != null) try { fos.close();} catch (IOException e) {}
		}
		return files;
	}

	private void setXmlTemplateFiles(File file) {
		
		//LoadBalancerStatic.xmlTemplateFiles.put("templateMessageKey", file);
	}
	public boolean isTemplatesLoaded() {
		return templatesLoaded;
	}
	public void setTemplatesLoaded(boolean templatesLoaded) {
		this.templatesLoaded = templatesLoaded;
	}
}
