package com.eclipse.web.kit.preferences;

import com.eclipse.web.kit.Activator;
import com.eclipse.web.kit.overlay.FieldEditorPropertyPage;
import com.eclipse.web.kit.preferences.editors.StringListFieldEditor;

public class IgnorePreferences extends FieldEditorPropertyPage {

	public IgnorePreferences() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}
	
	@Override
	protected String getPageId() {
		return "com.eclipse.web.kit.preferences.IgnorePreferences";
	}

	@Override
	protected void createFieldEditors() {
		StringListFieldEditor ignoreEditor=new StringListFieldEditor(PreferenceConstants.P_BROKEN_LINK_IGNORE, "Ignored broken links", getFieldEditorParent());
		addField(ignoreEditor);
	}

}
