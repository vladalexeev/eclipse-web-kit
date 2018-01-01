package com.eclipse.web.kit.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import com.eclipse.web.kit.preferences.PreferenceConstants;

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
	
	private static String createRelativePath(List<String> basePath, List<String> targetFilePath) {
		int baseListMax=basePath.size();
		int targetListMax=targetFilePath.size()-1;
		
		String result="";
		
		int index=0;
		while (index<Math.min(baseListMax,targetListMax) &&
				basePath.get(index).equals(targetFilePath.get(index))) {
			index++;
		}

		while (index<Math.min(baseListMax,targetListMax)) {			
			result="../"+result+targetFilePath.get(index)+"/";
			index++;
		}
		
		while (index<baseListMax) {
			index++;
			result="../"+result;
		}
		
		while (index<targetListMax) {
			result+=targetFilePath.get(index)+"/";
			index++;
		}
		
		result+=targetFilePath.get(targetListMax);
		
		return result;
	}
	
	/**
	 * Create relative path from source file path and target file path
	 * @param baseFilePath source file path
	 * @param targetFilePath target file path
	 * @return
	 */
	public static String createRelativePath(String baseFilePath, String targetFilePath) {
//		File targetFile=new File(targetFilePath);
//		if (!targetFile.isAbsolute()) {
//			return targetFilePath;
//		}
		
		List<String> baseList=splitPathToTokens(baseFilePath);
		List<String> targetList=splitPathToTokens(targetFilePath);
		
		baseList.remove(baseList.size()-1);
		return createRelativePath(baseList, targetList);
		
//		int baseListMax=baseList.size()-1;
//		int targetListMax=targetList.size()-1;
//		
//		String result="";
//		
//		int index=0;
//		while (index<Math.min(baseListMax,targetListMax) &&
//				baseList.get(index).equals(targetList.get(index))) {
//			index++;
//		}
//
//		while (index<Math.min(baseListMax,targetListMax)) {			
//			result="../"+result+targetList.get(index)+"/";
//			index++;
//		}
//		
//		while (index<baseListMax) {
//			index++;
//			result="../"+result;
//		}
//		
//		while (index<targetListMax) {
//			result+=targetList.get(index)+"/";
//			index++;
//		}
//		
//		result+=targetList.get(targetListMax);
//		
//		return result;
	}
	
	/**
	 * Create realtive path using source directory path and target file path
	 * @param basePath source directory path
	 * @param targetFilePath target file path
	 * @return
	 */
	public static String createRelativePath2(String basePath, String targetFilePath) {
		File targetFile=new File(targetFilePath);
		if (!targetFile.isAbsolute()) {
			return targetFilePath;
		}
		
		List<String> baseList=splitPathToTokens(basePath);
		List<String> targetList=splitPathToTokens(targetFilePath);
		
		return createRelativePath(baseList, targetList);
	}
	
	public static String createAbsolutePath(String baseDirPath, String relativePath) {
		List<String> baseList=splitPathToTokens(baseDirPath);
		List<String> relativeList=splitPathToTokens(relativePath);
		
		for (String token:relativeList) {
			if (token.equals(".")) {
				continue;
			} else if (token.equals("..")) {
				if (baseList.size()>0) {
					baseList.remove(baseList.size()-1);
				}
			} else {
				baseList.add(token);
			}
		}
		
		String result="";
		for (String token:baseList) {
			if (result.length()>0) {
				result+="/";
			} 
			
			result+=token;
		}
		
		return result;
	}
	
	public static String createSiteAbsolutePath(String siteLocalPath, String basePath, String relativePath) {
		if (relativePath.startsWith("/")) {
			return relativePath;
		}
		String absolutePath=createAbsolutePath(basePath, relativePath);
		return "/"+createRelativePath2(siteLocalPath, absolutePath);
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
	
	public static String getFilePath(String fileName) {
		return new File(fileName).getParent();
	}
	
	public static String selectImageFile(Shell shell) {
		FileDialog fileDialog=new FileDialog(shell, SWT.OPEN);
		fileDialog.setFilterExtensions(new String[]{"*.jpg*;*.png;*.gif", "*.*"});
		fileDialog.setFilterNames(new String[]{"Images (jpg, png, gif)","All files"});
		try {
			String lastImagePath=SwtUtil.getActiveProject().getPersistentProperty(PreferenceConstants.Q_LAST_IMAGE_PATH);
			fileDialog.setFilterPath(lastImagePath);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		String selectedImageFileName=fileDialog.open();
		
		if (selectedImageFileName!=null) {
			try {
				SwtUtil.getActiveProject().setPersistentProperty(PreferenceConstants.Q_LAST_IMAGE_PATH, getFilePath(selectedImageFileName));
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		return selectedImageFileName;
	}
	
	public static String selectAnyFile(Shell shell) {
		FileDialog fileDialog=new FileDialog(shell, SWT.OPEN);
		fileDialog.setFilterExtensions(new String[]{"*.*"});
		fileDialog.setFilterNames(new String[]{"All files"});
		try {
			String lastLinkPath=SwtUtil.getActiveProject().getPersistentProperty(PreferenceConstants.Q_LAST_LINK_PATH);
			fileDialog.setFilterPath(lastLinkPath);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		String selectedFileName=fileDialog.open();
		
		if (selectedFileName!=null) {
			try {
				SwtUtil.getActiveProject().setPersistentProperty(PreferenceConstants.Q_LAST_LINK_PATH, getFilePath(selectedFileName));
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}	
		return selectedFileName;
	}
}
