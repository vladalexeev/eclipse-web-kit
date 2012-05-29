package com.eclipse.web.kit.popup.actions;

import org.eclipse.jface.action.IAction;

import com.eclipse.web.kit.util.SwtUtil;
import com.eclipse.web.kit.views.BatchReplaceDialog;

public class BatchReplaceAction extends FolderPopupAction {

	@Override
	public void run(IAction action) {
		System.out.println("BatchReplaceAction");
		BatchReplaceDialog dialog=new BatchReplaceDialog(SwtUtil.getActiveWorkbenchWindow().getShell());
		if (dialog.open()) {
			System.out.println("Execute batch replace");
		} else {
			System.out.println("Canceled");
		}
	}

}
