package com.eclipse.web.kit.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class WebKitPreferences extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	public WebKitPreferences() {
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

}
