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
		addField(new StringListFieldEditor(PreferenceConstants.P_BROKEN_LINK_EXCLUSIONS, "Broken links exclusions", getFieldEditorParent()));
		addField(new StringListFieldEditor(PreferenceConstants.P_SITEMAP_EXCLUSIONS, "Sitemap exclusions", getFieldEditorParent()));
	}

}
