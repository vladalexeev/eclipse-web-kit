package com.eclipse.web.kit.popup.actions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.eclipse.web.kit.Activator;
import com.eclipse.web.kit.preferences.PreferenceConstants;
import com.eclipse.web.kit.util.FileLoader;
import com.eclipse.web.kit.util.FileUtil;
import com.eclipse.web.kit.util.LinkExtractor;
import com.eclipse.web.kit.util.LinkInfo;
import com.eclipse.web.kit.util.LinkPattern;
import com.eclipse.web.kit.util.StringUtil;
import com.eclipse.web.kit.util.WildcardFilter;

public class TestBrokenLinksAction extends ProjectPopupAction {

	private class TestBrokenLinksJob extends Job {
		private LinkPattern[] linkPatterns;
		private HashSet<String> ignoredLinks;
		private ArrayList<WildcardFilter> ignoredPatters;
		private HashSet<String> fileExtensions;
		private int count;

		public TestBrokenLinksJob(String name) {
			super(name);
		}
		
		private boolean isIgnoredPath(String path) {
			path=path.replace('\\', '/');
			
			if (ignoredLinks.contains(path)) {
				return true;
			}
			
			for (WildcardFilter f:ignoredPatters) {
				if (f.accept(path)) {
					return true;
				}
			}
			
			return false;
		}
		
		private void processSubdirectories(IProgressMonitor monitor, IProject project, String directory) {
			String projectFolder=project.getLocation().toOSString();
			File directoryFile=new File(projectFolder,directory);
			String directoryAbsolutePath=directoryFile.toString();
			String[] childFileNames=directoryFile.list();
			
			for (String childFileName:childFileNames) {
				File targetFile=new File(directoryFile,childFileName);
				File relativeTargetFile=new File(directory, childFileName);
				if (targetFile.isDirectory()) {
					monitor.setTaskName(relativeTargetFile.toString());
					processSubdirectories(monitor, project, relativeTargetFile.toString());
				} else {
					String ext="";
					int index=childFileName.indexOf('.');
					if (index>=0) {
						ext=childFileName.substring(index+1);
					}
					
					if (fileExtensions.contains(ext)) {
						try {
							String fileContent=FileLoader.loadFile(targetFile.toString());
							List<LinkInfo> fileLinks=LinkExtractor.extractLinks(fileContent, linkPatterns);
							for (LinkInfo link:fileLinks) {
								if (link.getLinkFile()==null || link.getLinkFile().startsWith("http://") ||
									link.getLinkFile().startsWith("https://") || link.getLinkFile().startsWith("mailto:") ||
									link.getLinkFile().startsWith("javascript:")) {
									continue;
								}
								
								File linkedFile=new File(FileUtil.createAbsolutePath(directoryAbsolutePath, link.getLinkFile()));
								String projectRelativeFileName=FileUtil.createRelativePath2(projectFolder, linkedFile.toString());
								if (!linkedFile.exists() && !isIgnoredPath(projectRelativeFileName)) {
									IFile iFile=project.getFile(relativeTargetFile.toString());
									try {
										IMarker marker=iFile.createMarker("eclipse-web-kit.brokenlink");
										marker.setAttribute(IMarker.MESSAGE, "Broken link - "+link.getLinkFile()+" == "+projectRelativeFileName);
										marker.setAttribute(IMarker.CHAR_START, link.getBeginIndex());
										marker.setAttribute(IMarker.CHAR_END, link.getEndIndex());
										marker.setAttribute(IMarker.LINE_NUMBER, link.getLineNumber());
										count++;
									} catch (CoreException e1) {
										e1.printStackTrace();
									}									
								}
							}
						} catch (IOException e) {
							e.printStackTrace();
						}						
					}
				}
			}
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			monitor.beginTask("", IProgressMonitor.UNKNOWN);

			try {
				project.deleteMarkers("eclipse-web-kit.brokenlink", true, IResource.DEPTH_INFINITE);
			} catch (CoreException e) {
				e.printStackTrace();
			}
			
			String patterns=Activator.getOverlayedPreferenceValue(project, PreferenceConstants.Q_BROKEN_LINK_TEMPLATES);
			String[] patternStrs=new String[]{};
			if (patterns!=null) {
				patternStrs=patterns.split("\0");
			}
			
			linkPatterns=new LinkPattern[patternStrs.length];
			for (int i=0; i<patternStrs.length; i++) {
				String p=patternStrs[i];
				int index=p.indexOf('*');
				String prefix=p.substring(0,index);
				String postfix=p.substring(index+1);
				linkPatterns[i]=new LinkPattern(prefix, postfix);
			}
						
			fileExtensions=new HashSet<String>();
			String[] arrExtensions=StringUtil.splitStringByCommas(Activator.getOverlayedPreferenceValue(project, PreferenceConstants.Q_BROKEN_LINK_FILE_EXTENSIONS));
			for (String e:arrExtensions) {
				fileExtensions.add(e);
			}

			ignoredLinks=new HashSet<String>();
			ignoredPatters=new ArrayList<WildcardFilter>();
			String ignoredValue=Activator.getOverlayedPreferenceValue(project, PreferenceConstants.Q_BROKEN_LINK_EXCLUSIONS);			
			if (ignoredValue!=null) {
				String[] ignoredStrs=ignoredValue.split("\0");			

				for (String str:ignoredStrs) {
					str=str.replace('\\', '/');
					if (str.indexOf('*')>=0 || str.indexOf('?')>=0) {
						ignoredPatters.add(new WildcardFilter(str));
					} else {
						ignoredLinks.add(str);
					}
				}
			}
			
			processSubdirectories(monitor, project, "");
			
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					MessageDialog.openInformation(
							new Shell(),
							"Search broken links result",
							count+" broken links found");						
				}
			});
						
			return Status.OK_STATUS;
		}
		
	}
	
	/**
	 * Constructor for Action1.
	 */
	public TestBrokenLinksAction() {
		super();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		TestBrokenLinksJob job=new TestBrokenLinksJob("Test broken links");
		job.schedule();
	}

}
