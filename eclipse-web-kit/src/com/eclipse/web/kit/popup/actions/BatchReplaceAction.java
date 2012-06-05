package com.eclipse.web.kit.popup.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.StringTokenizer;

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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.eclipse.web.kit.Activator;
import com.eclipse.web.kit.overlay.ProjectPropertyStore;
import com.eclipse.web.kit.preferences.PreferenceConstants;
import com.eclipse.web.kit.util.StringTransformer;
import com.eclipse.web.kit.util.StringTransformerOptions;
import com.eclipse.web.kit.util.SwtUtil;
import com.eclipse.web.kit.views.BatchReplaceDialog;
import com.eclipse.web.kit.views.BatchReplaceDialog.RESULT;
import com.eclipse.web.kit.views.BatchReplaceResultDialog;

public class BatchReplaceAction extends FolderPopupAction {

	private abstract class JobBatchCommand extends Job {
		private HashSet<String> fileExtensions;
		protected StringTransformerOptions options;
		private String textToFind;
		protected String textForReplace;
		
		protected String transformedTextToFind;
		
		protected int countFilesProcessed=0;
		protected int countFilesFound=0;
		protected int countFilesChanges=0;
		protected int countOccurences=0;
		protected int countErrors=0;
		
		private boolean recursive=false;
		
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
				if (m.getType()==IResource.FOLDER && recursive) {
					walkFolders(m);
				} else if (m.getType()==IResource.FILE) {
					IFile f=(IFile)m;
					if (fileExtensions.contains(f.getFileExtension())) {
						countFilesProcessed++;
						try {
							processFile(f);
						} catch (IOException e) {
							System.out.println("Error processing file "+f.getLocation().toOSString());
							countErrors++;
						}
					}
				}
			}
		}
		
		protected abstract void processFile(IFile ifile) throws CoreException, IOException;

		public JobBatchCommand(String name) {
			super(name);
		}
		
		protected String loadFile(IFile ifile) throws CoreException, IOException {
			String charset=ifile.getCharset();
			File file=ifile.getLocation().makeAbsolute().toFile();
			FileInputStream fis=new FileInputStream(file);
			InputStreamReader reader=new InputStreamReader(fis, charset);
			
			char[] buffer=new char[4096];
			StringBuffer result=new StringBuffer();
			int count;
			
			do {
				count=reader.read(buffer);
				if (count>0) {
					result.append(new String(buffer,0,count));
				}
			} while (count>=0);
			
			reader.close();
			
			return result.toString();
		}
		
		protected void saveFile(IFile ifile, String content) throws IOException, CoreException {
			String charset=ifile.getCharset();
			File file=ifile.getLocation().makeAbsolute().toFile();
			FileOutputStream fos=new FileOutputStream(file);
			OutputStreamWriter writer=new OutputStreamWriter(fos, charset);

			writer.write(content);
			
			writer.close();
		}


		@Override
		protected IStatus run(IProgressMonitor monitor) {
			StringTransformer tr=new StringTransformer();
			tr.setOptions(options);
			tr.setText(textToFind);
			tr.transformText();
			transformedTextToFind=tr.getTransformedText();
			
			if (options.isIgnoreNewLines()) {
				textForReplace='\n'+textForReplace+'\n';
			}
			
			try {
				walkFolders(folder);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					BatchReplaceResultDialog resultDialog=new BatchReplaceResultDialog(new Shell());
					resultDialog.setFilesProcessed(countFilesProcessed);
					resultDialog.setFilesFound(countFilesFound);
					resultDialog.setOccurencesFound(countOccurences);
					resultDialog.setFilesChanged(countFilesChanges);
					resultDialog.setErrorsCount(countErrors);
					resultDialog.open();
				}
			});
			
			try {
				folder.refreshLocal(IResource.DEPTH_INFINITE, null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
						
			return Status.OK_STATUS;
		}
		
	}
	
	private class JobJustFind extends JobBatchCommand {

		public JobJustFind(String name) {
			super(name);
		}

		@Override
		protected void processFile(IFile ifile) throws CoreException, IOException {
			String fileContent=loadFile(ifile);
			
			StringTransformer tr=new StringTransformer();
			tr.setOptions(options);
			tr.setText(fileContent);
			tr.transformText();
			String transformedContent=tr.getTransformedText();
			
			int index=0;
			boolean occurFound=false;
			
			while (index>=0) {
				index=transformedContent.indexOf(transformedTextToFind, index);
				if (index>=0) {
					index+=transformedTextToFind.length();
					occurFound=true;
					countOccurences++;
				}
			}
			
			if (occurFound) {
				countFilesFound++;
			}
		}
	}
	
	private class JobBatchReplace extends JobBatchCommand {

		public JobBatchReplace(String name) {
			super(name);
		}

		@Override
		protected void processFile(IFile ifile) throws CoreException, IOException {
			String fileContent=loadFile(ifile);
			
			StringTransformer tr=new StringTransformer();
			tr.setOptions(options);
			tr.setText(fileContent);
			tr.transformText();
			String transformedContent=tr.getTransformedText();
			int[] originalCharIndexes=tr.getOriginalCharIndexes();
			
			int prevIndex=0;
			int index=0;
			String resultText="";
			boolean occurFound=false;
			
			while (index>=0) {
				prevIndex=index;
				index=transformedContent.indexOf(transformedTextToFind, index);
				if (index>=0) {
					int beginIndex=originalCharIndexes[prevIndex];
					int endIndex= (index>0 ? originalCharIndexes[index-1]+1: 0);
					resultText+=fileContent.substring(beginIndex, endIndex)+textForReplace;
					
					index+=transformedTextToFind.length();
					occurFound=true;
					countOccurences++;
				}
			}
			
			int beginIndex=originalCharIndexes[prevIndex];
			resultText+=fileContent.substring(beginIndex);
			
			if (occurFound) {
				countFilesFound++;
			}
			
			saveFile(ifile, resultText);
			countFilesChanges++;
		}
		
	}
	
	@Override
	public void run(IAction action) {
		System.out.println("BatchReplaceAction");
		ProjectPropertyStore store=new ProjectPropertyStore(folder.getProject(), Activator.getDefault().getPreferenceStore(), 
				PreferenceConstants.PAGE_ID_BATCH_REPLACE);
		
		BatchReplaceDialog dialog=new BatchReplaceDialog(SwtUtil.getActiveWorkbenchWindow().getShell(), store);
		BatchReplaceDialog.RESULT result=dialog.open();
		if (result!=RESULT.NO_RESULT) {
			System.out.println("Execute batch replace");
						
			store.setValue(PreferenceConstants.P_BATCH_REPLACE_IGNORE_WHITESPACES, dialog.isResultIgnoreWhitespaces());
			store.setValue(PreferenceConstants.P_BATCH_REPLACE_IGNORE_LINEBREAKS, dialog.isResultIgnoreLinebreaks());
			store.setValue(PreferenceConstants.P_BATCH_REPLACE_RECURSIVE_SEARCH, dialog.isResultRecursiveSearch());
			store.setValue(PreferenceConstants.P_BATCH_REPLACE_FIND_TEXT, dialog.getResultFindText());
			store.setValue(PreferenceConstants.P_BATCH_REPLACE_REPLACE_TEXT, dialog.getResultReplaceText());
			store.setValue(PreferenceConstants.P_BATCH_REPLACE_EXTENSIONS, dialog.getResultExtensions());
			try {
				store.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			StringTransformerOptions options=new StringTransformerOptions();
			options.setIgnoreCarriageReturns(true);
			options.setIgnoreNewLines(dialog.isResultIgnoreLinebreaks());
			options.setIgnoreLeadingWhitespaces(dialog.isResultIgnoreWhitespaces());
			options.setIgnoreTrailingwhitespaces(dialog.isResultIgnoreWhitespaces());
			
			String strExtensions=dialog.getResultExtensions();
			HashSet<String> extensions=new HashSet<String>();
			StringTokenizer tk=new StringTokenizer(strExtensions, " ,;");
			while (tk.hasMoreTokens()) {
				String t=tk.nextToken();
				if (t.startsWith(".")) {
					t=t.substring(1);
				}
				extensions.add(t);
			}
			
			
			JobBatchCommand job;
			
			if (result==RESULT.JUST_FIND) {
				job=new JobJustFind("Just find");
			} else if (result==RESULT.REPLACE){
				job=new JobBatchReplace("Batch replace");
			} else {
				return;
			}
			
			job.fileExtensions=extensions;
			job.options=options;
			job.textToFind=dialog.getResultFindText();
			job.textForReplace=dialog.getResultReplaceText();
			job.recursive=dialog.isResultRecursiveSearch();
			job.schedule();
			
		} else {
			System.out.println("Canceled");
		}
	}

}
