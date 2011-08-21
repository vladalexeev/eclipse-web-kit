package com.eclipse.web.kit.preferences;

import com.eclipse.web.kit.Activator;
import com.eclipse.web.kit.overlay.FieldEditorPropertyPage;
import com.eclipse.web.kit.preferences.editors.StringListFieldEditor;

public class ExclusionsPreferences extends FieldEditorPropertyPage {

	public ExclusionsPreferences() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}
	
	@Override
	protected String getPageId() {
		return PreferenceConstants.PAGE_ID_EXCLUSIONS;
	}

	@Override
	protected void createFieldEditors() {
		StringListFieldEditor ignoreEditor=new StringListFieldEditor(PreferenceConstants.P_BROKEN_LINK_EXCLUSIONS, "Broken links exclusions", getFieldEditorParent());
		addField(ignoreEditor);
	}

}
