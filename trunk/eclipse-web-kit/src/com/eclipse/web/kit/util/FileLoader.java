package com.eclipse.web.kit.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileLoader {

	public static String loadFile(String fileName) throws IOException {
		InputStream is=new FileInputStream(fileName);
		InputStreamReader reader=new InputStreamReader(is);
		
		StringBuffer sb=new StringBuffer();
		char[] buffer=new char[1024];
		int count=0;
		
		do {
			count=reader.read(buffer);
			if (count>0) {
				sb.append(buffer,0,count);
			}
		} while (count>=0);
		
		reader.close();
		
		return sb.toString();
	}
}
