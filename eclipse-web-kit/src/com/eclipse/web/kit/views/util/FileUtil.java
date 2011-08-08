package com.eclipse.web.kit.views.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class FileUtil {
	private static List<String> splitPathToTokens(String path) {
		ArrayList<String> result=new ArrayList<String>();
		
		String str="";
		for (int i=0; i<path.length(); i++) {
			char c=path.charAt(i);
			if (c=='/' || c=='\\') {
				if (str.length()>0) {
					result.add(str);
				}
				str="";
			} else {
				str+=c;
			}
		}
		
		if (str.length()>0) {
			result.add(str);
		}
		
		return result;
	}
	
	public static String createRelativePath(String baseFilePath, String targetFilePath) {
		File targetFile=new File(targetFilePath);
		if (!targetFile.isAbsolute()) {
			return targetFilePath;
		}
		
		List<String> baseList=splitPathToTokens(baseFilePath);
		List<String> targetList=splitPathToTokens(targetFilePath);
		int baseListMax=baseList.size()-1;
		int targetListMax=targetList.size()-1;
		
		String result="";
		
		int index=0;
		while (index<Math.min(baseListMax,targetListMax) &&
				baseList.get(index).equals(targetList.get(index))) {
			index++;
		}

		while (index<Math.min(baseListMax,targetListMax)) {			
			result="../"+result+targetList.get(index)+"/";
			index++;
		}
		
		while (index<baseListMax) {
			index++;
			result="../"+result;
		}
		
		while (index<targetListMax) {
			result+=targetList.get(index)+"/";
			index++;
		}
		
		result+=targetList.get(targetListMax);
		
		return result;
	}
	
	public static String getFileExt(String filePath) {
		File f=new File(filePath);
		String fileName=f.getName();
		int lastPoint=fileName.lastIndexOf(".");
		if (lastPoint<=0) {
			return null;
		} else {
			return fileName.substring(lastPoint+1);
		}
	}
	
	public static String getFileNameWithoutExt(String filePath) {
		File f=new File(filePath);
		String ext=getFileExt(filePath);
		
		if (ext!=null) {
			return f.getName().substring(0, f.getName().length()-ext.length()-1);
		} else {
			return f.getName();
		}
	}
	
	public static String selectImageFile(Shell shell) {
		FileDialog fileDialog=new FileDialog(shell, SWT.OPEN);
		fileDialog.setFilterExtensions(new String[]{"*.jpg*;.png;*.gif", "*.*"});
		fileDialog.setFilterNames(new String[]{"Images (jpg, png, gif)","All files"});
		String selectedImageFileName=fileDialog.open();
		return selectedImageFileName;
	}
	
	public static String selectAnyFile(Shell shell) {
		FileDialog fileDialog=new FileDialog(shell, SWT.OPEN);
		fileDialog.setFilterExtensions(new String[]{"*.*"});
		fileDialog.setFilterNames(new String[]{"All files"});
		String selectedFileName=fileDialog.open();
		return selectedFileName;
	}
}
