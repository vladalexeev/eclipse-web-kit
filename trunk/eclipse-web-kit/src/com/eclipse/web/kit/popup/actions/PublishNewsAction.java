package com.eclipse.web.kit.popup.actions;

import org.eclipse.jface.action.IAction;

import com.eclipse.web.kit.Activator;
import com.eclipse.web.kit.overlay.ProjectPropertyStore;
import com.eclipse.web.kit.preferences.PreferenceConstants;
import com.eclipse.web.kit.util.SwtUtil;
import com.eclipse.web.kit.views.PublishNewsDialog;

public class PublishNewsAction extends FilePopupAction {

	public PublishNewsAction() {
	}

	@Override
	public void run(IAction action) {
		System.out.println("PublishNewsAction");
		ProjectPropertyStore store=new ProjectPropertyStore(file.getProject(), Activator.getDefault().getPreferenceStore(), 
				PreferenceConstants.PAGE_ID_NEWS_FEEDS);
		
		PublishNewsDialog dialog=new PublishNewsDialog(SwtUtil.getActiveWorkbenchWindow().getShell(), store, file);
		dialog.open();

	}

}
