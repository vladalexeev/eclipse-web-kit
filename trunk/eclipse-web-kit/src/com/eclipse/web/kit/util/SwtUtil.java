package com.eclipse.web.kit.util;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class SwtUtil {

	public static IWorkbenchWindow getActiveWorkbenchWindow() {
		if (PlatformUI.getWorkbench()!=null) {
			return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		}
		
		return null;
	}
	
	public static Shell getActiveShell() {
		return getActiveWorkbenchWindow().getShell();
	}
	
	public static IProject getActiveProject() {
		//return ((IFileEditorInput)getActiveEditor().getEditorInput()).getFile().getProject();
		
		if (getActiveEditor()!=null) {
			return ((IFileEditorInput)getActiveEditor().getEditorInput()).getFile().getProject();
		}
		
		IProject project = null;
		ISelectionService selectionService = 
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService();

		ISelection selection = selectionService.getSelection();

		if(selection instanceof IStructuredSelection) {
			Object element = ((IStructuredSelection)selection).getFirstElement();

			if (element instanceof IResource) {
				project= ((IResource)element).getProject();
			} 
		}
		return project;
	}
	
	public static IEditorPart getActiveEditor() {
		if (getActiveWorkbenchWindow()!=null) {
			if (getActiveWorkbenchWindow().getActivePage()!=null) {
				return getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			}
		}
		
		return null;
	}
	
	public static String getActiveEditorFileName() {
		IEditorInput editorInput = getActiveEditor().getEditorInput();
		String documentFileName=null;
		if (editorInput instanceof IFileEditorInput) {
			IFileEditorInput fileEditorInput=(IFileEditorInput) editorInput;
			documentFileName=fileEditorInput.getFile().getLocation().toString();
		}
		
		return documentFileName;
	}
}
