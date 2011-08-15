package com.eclipse.web.kit.preferences;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.eclipse.web.kit.overlay.FieldEditorOverlayPage;

public class WebKitPreferences extends FieldEditorOverlayPage implements
		IWorkbenchPreferencePage {

	public WebKitPreferences() {
		super(GRID);
	}
	
	public WebKitPreferences(int style) {
		super(style);
	}

	public WebKitPreferences(String title, int style) {
		super(title, style);
	}

	public WebKitPreferences(String title, ImageDescriptor image, int style) {
		super(title, image, style);
	}

	@Override
	public void init(IWorkbench workbench) {
	}

	@Override
	protected void createFieldEditors() {
	}

	@Override
	protected String getPageId() {
		return PreferenceConstants.PAGE_ID_WEB_KIT;
	}

}
