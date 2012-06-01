package com.eclipse.web.kit.popup.actions;

import java.io.IOException;

import org.eclipse.jface.action.IAction;

import com.eclipse.web.kit.Activator;
import com.eclipse.web.kit.overlay.ProjectPropertyStore;
import com.eclipse.web.kit.preferences.PreferenceConstants;
import com.eclipse.web.kit.util.SwtUtil;
import com.eclipse.web.kit.views.BatchReplaceDialog;

public class BatchReplaceAction extends FolderPopupAction {

	@Override
	public void run(IAction action) {
		System.out.println("BatchReplaceAction");
		ProjectPropertyStore store=new ProjectPropertyStore(folder.getProject(), Activator.getDefault().getPreferenceStore(), 
				PreferenceConstants.PAGE_ID_BATCH_REPLACE);
		
		BatchReplaceDialog dialog=new BatchReplaceDialog(SwtUtil.getActiveWorkbenchWindow().getShell(), store);
		if (dialog.open()) {
			System.out.println("Execute batch replace");
						
			store.setValue(PreferenceConstants.P_BATCH_REPLACE_IGNORE_WHITESPACES, dialog.isResultIgnoreWhitespaces());
			store.setValue(PreferenceConstants.P_BATCH_REPLACE_RECURSIVE_SEARCH, dialog.isResultRecursiveSearch());
			store.setValue(PreferenceConstants.P_BATCH_REPLACE_FIND_TEXT, dialog.getResultFindText());
			store.setValue(PreferenceConstants.P_BATCH_REPLACE_REPLACE_TEXT, dialog.getResultReplaceText());
			try {
				store.save();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else {
			System.out.println("Canceled");
		}
	}

}
