package org.araport.jcvi.stock.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class FileUtils {

	public static String getSqlFileContents(String fileName) {
	    StringBuffer sb = new StringBuffer();
	    try {
	        Resource resource = new ClassPathResource(fileName);
	        DataInputStream in = new DataInputStream(resource.getInputStream());
	        BufferedReader br = new BufferedReader(new InputStreamReader(in));
	        String strLine;
	        while ((strLine = br.readLine()) != null) {
	            sb.append(" " + strLine);
	        }
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return sb.toString();
	}
	
}
