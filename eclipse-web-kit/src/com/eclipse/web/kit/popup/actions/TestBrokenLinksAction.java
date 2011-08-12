package com.eclipse.web.kit.popup.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class TestBrokenLinksAction implements IObjectActionDelegate {

	private IProject project;
	
	private class TestBrokenLinksJob extends Job {

		public TestBrokenLinksJob(String name) {
			super(name);
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			monitor.beginTask("", 100);
			
			IFile iFile=project.getFile("index.html");
			System.out.println(iFile);
			
			try {
				IMarker marker=iFile.createMarker(IMarker.PROBLEM);
				marker.setAttribute(IMarker.MESSAGE, "Broken link in "+iFile.getFullPath().toOSString());
				marker.setAttribute(IMarker.CHAR_START, 5);
				marker.setAttribute(IMarker.CHAR_END, 10);
				
			} catch (CoreException e1) {
				e1.printStackTrace();
			}
			
			for (int i=0; i<100; i++) {
				if (monitor.isCanceled()) {
					return Status.CANCEL_STATUS;
				}
				
				monitor.worked(1);
				monitor.setTaskName(project.getName()+" - "+i+"%");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}
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
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		StructuredSelection sel=(StructuredSelection)targetPart.getSite().getSelectionProvider().getSelection();
		project=(IProject) sel.getFirstElement();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
//		MessageDialog.openInformation(
//			shell,
//			"Eclipse-web-kit",
//			"Test broken links ... was executed.");
		
		System.out.println(project);
		TestBrokenLinksJob job=new TestBrokenLinksJob("Test broken links");
		job.schedule();
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

}
