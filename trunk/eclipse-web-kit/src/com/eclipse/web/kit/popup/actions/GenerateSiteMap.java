package com.eclipse.web.kit.popup.actions;

import java.io.File;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
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
import org.eclipse.ui.PlatformUI;

import com.eclipse.web.kit.Activator;
import com.eclipse.web.kit.preferences.PreferenceConstants;
import com.eclipse.web.kit.util.DialogUtil;
import com.eclipse.web.kit.util.FileUtil;
import com.eclipse.web.kit.util.StringUtil;
import com.eclipse.web.kit.util.SwtUtil;
import com.eclipse.web.kit.util.WildcardFilter;

public class GenerateSiteMap extends ProjectPopupAction {
	
	private class JobGenerateSiteMap extends Job {
		private URL siteUrl;
		private String indexFileName;
		private int replaceIndexRule=ROOT;
		private HashSet<String> fileExtensions;
		private String sitemapFileName;
		private SimpleDateFormat dateFormat;
		
		private HashSet<String> sitemapExlusions;
		private ArrayList<WildcardFilter> sitemapExclusionPatters=new ArrayList<WildcardFilter>();
		
		private String projectPath;
		private PrintWriter writer;
		private int count=0;
		
		private static final int ROOT=1;
		private static final int ALWAYS=2;
		private static final int NEVER=3;

		public JobGenerateSiteMap(String name) {
			super(name);
		}
		
		private boolean isExcludedPath(String relativePath) {
			relativePath.replace('\\', '/');
			if (sitemapExlusions.contains(relativePath)) {
				return true;
			}
			
			for (WildcardFilter f:sitemapExclusionPatters) {
				if (f.accept(relativePath)) {
					return true;
				}
			}
			
			return false;
		}
		
		private void walkFolders(IResource resource) throws CoreException, MalformedURLException {
			IResource[] members;
			if (resource.getType()==IResource.PROJECT) {
				members=((IProject) resource).members();
			} else if (resource.getType()==IResource.FOLDER) {
				members=((IFolder) resource).members();
			} else {
				return;
			}
			
			if (members==null || members.length==0) {
				return;
			}
			
			for (IResource m:members) {
				if (m.getType()==IResource.FOLDER) {
					walkFolders(m);
				} else if (m.getType()==IResource.FILE) {
					IFile f=(IFile)m;
					if (fileExtensions.contains(f.getFileExtension())) {
						String relativePath=FileUtil.createRelativePath2(projectPath, f.getLocation().toString());
						if (isExcludedPath(relativePath)) {
							continue;
						}
						
						URL url=new URL(siteUrl,relativePath);
						if (f.getName().equals(indexFileName)) {
							if (resource==project && (replaceIndexRule==ROOT || replaceIndexRule==ALWAYS)) {
								url=new URL(siteUrl,"/");
							} else if (replaceIndexRule==ALWAYS) {
								url=new URL(siteUrl,FileUtil.createRelativePath2(projectPath, resource.getLocation().toString())+"/");
							}
						}
						
						writer.println("<url><loc>"+url+"</loc><lastmod>"+
								dateFormat.format(new Date(f.getLocalTimeStamp()))+"</lastmod></url>");
						count++;
					}
				}
			}
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			try {
				siteUrl=new URL(Activator.getOverlayedPreferenceValue(project, PreferenceConstants.Q_SITEMAP_BASE_URL));
				indexFileName=Activator.getOverlayedPreferenceValue(project, PreferenceConstants.Q_SITEMAP_INDEX_FILE);
				sitemapFileName=Activator.getOverlayedPreferenceValue(project, PreferenceConstants.Q_SITEMAP_SITEMAP_FILE);

				dateFormat=new SimpleDateFormat("yyyy-MM-dd");

				String strReplaceRule=Activator.getOverlayedPreferenceValue(project, PreferenceConstants.Q_SITEMAP_REPLACE_INDEX_FILE);
				if (PreferenceConstants.V_SITEMAP_REPLACE_INDEX_ROOT_ONLY.equals(strReplaceRule)) {
					replaceIndexRule=ROOT;
				} else if (PreferenceConstants.V_SITEMAP_REPLACE_INDEX_ALWAYS.equals(strReplaceRule)) {
					replaceIndexRule=ALWAYS;
				} else if (PreferenceConstants.V_SITEMAP_REPLACE_INDEX_NEVER.equals(strReplaceRule)) {
					replaceIndexRule=NEVER;
				}

				String[] arrExtensions=StringUtil.splitStringByCommas(Activator.getOverlayedPreferenceValue(project, PreferenceConstants.Q_SITEMAP_EXTENSIONS));
				fileExtensions=new HashSet<String>();
				for (String e:arrExtensions) {
					fileExtensions.add(e);
				}
				
				String strExclusions=Activator.getOverlayedPreferenceValue(project, PreferenceConstants.Q_SITEMAP_EXCLUSIONS);
				sitemapExlusions=new HashSet<String>(); 
				sitemapExclusionPatters=new ArrayList<WildcardFilter>();
				if (strExclusions!=null && strExclusions.length()>0) {
					String[] arr=strExclusions.split("\0");
					for (String e:arr) {
						e=e.replace('\\', '/');
						if (e.indexOf('*')>=0 || e.indexOf('?')>=0) {
							sitemapExclusionPatters.add(new WildcardFilter(e));
						} else {
							sitemapExlusions.add(e);
						}
					}
				}
				
				
				
				projectPath=project.getLocation().toString();

				writer=new PrintWriter(new File(project.getLocation().toString(),sitemapFileName));
				writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
				writer.println("<urlset xmlns=\"http://www.google.com/schemas/sitemap/0.84\">");
				
				walkFolders(project);
				
				writer.println("</urlset>");
				writer.close();
				
				Display.getDefault().asyncExec(new Runnable() {
					@Override
					public void run() {
						DialogUtil.showMessage(
								"Sitemap generate result",
								"Sitemap generated with "+count+" URLs");
					}
				});				
			} catch (Exception e) {
				e.printStackTrace();
			} 
						
			return Status.OK_STATUS;
		}
		
	}
	
	public GenerateSiteMap() {
	}

	@Override
	public void run(IAction action) {
		String siteUrl=Activator.getOverlayedPreferenceValue(project, PreferenceConstants.Q_SITEMAP_BASE_URL);
		if (siteUrl==null || siteUrl.length()==0) {
			MessageDialog.openInformation(SwtUtil.getActiveShell(), "Generate sitemap error", "Site URL does not specified in project properties");
			return;
		}
		
		JobGenerateSiteMap job=new JobGenerateSiteMap("Ganerating sitemap");
		job.schedule();
		PlatformUI.getWorkbench().getProgressService().showInDialog(
				SwtUtil.getActiveWorkbenchWindow().getShell(), job);
	}
}
