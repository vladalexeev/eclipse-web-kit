package com.eclipse.web.kit.preferences;

import com.eclipse.web.kit.Activator;
import com.eclipse.web.kit.overlay.FieldEditorPropertyPage;
import com.eclipse.web.kit.preferences.editors.StringListFieldEditor;


public class CssContentAssistPrefences extends FieldEditorPropertyPage {

	public CssContentAssistPrefences() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}
	
	@Override
	protected String getPageId() {
		return PreferenceConstants.PAGE_ID_CONTENT_ASSIST_CSS;
	}

	@Override
	protected void createFieldEditors() {
		addField(new StringListFieldEditor(PreferenceConstants.P_CSS_FILES, "CSS files", getFieldEditorParent()));
		addField(new StringListFieldEditor(PreferenceConstants.P_CSS_CLASSES, "CSS classes", getFieldEditorParent()));
	}

}
