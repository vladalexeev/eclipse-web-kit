package com.eclipse.web.kit.popup.actions;

import org.eclipse.core.resources.IFolder;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public abstract class FolderPopupAction implements IObjectActionDelegate {

	protected IFolder folder;
	
	/**
	 * Constructor for Action1.
	 */
	public FolderPopupAction() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		StructuredSelection sel=(StructuredSelection)targetPart.getSite().getSelectionProvider().getSelection();
		folder=(IFolder) sel.getFirstElement();	
	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

}
