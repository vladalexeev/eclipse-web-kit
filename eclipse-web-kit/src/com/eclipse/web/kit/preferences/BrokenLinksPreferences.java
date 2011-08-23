package com.eclipse.web.kit.preferences;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.eclipse.web.kit.Activator;
import com.eclipse.web.kit.overlay.FieldEditorOverlayPage;
import com.eclipse.web.kit.preferences.editors.StringListFieldEditor;

public class BrokenLinksPreferences extends FieldEditorOverlayPage implements
		IWorkbenchPreferencePage {
	
	private IInputValidator templateValidator=new IInputValidator() {
		
		@Override
		public String isValid(String newText) {
			int starIndex=newText.indexOf('*');
			if (starIndex<0) {
				return "Template should contain '*'";
			}
			
			if (starIndex==0) {
				return "Template should not start from '*'";
			}
			
			if (starIndex==newText.length()-1) {
				return "Template should not end by '*'";
			}
			
			return null;
		}
	};
		
	public BrokenLinksPreferences() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	public void init(IWorkbench workbench) {
	}

	@Override
	protected void createFieldEditors() {
		addField(new StringFieldEditor(PreferenceConstants.P_BROKEN_LINK_FILE_EXTENSIONS, "File extensions", getFieldEditorParent()));
		
		StringListFieldEditor templatesEditor=new StringListFieldEditor(PreferenceConstants.P_BROKEN_LINK_TEMPLATES, "Templates for searching broken links", getFieldEditorParent());
		templatesEditor.setValidator(templateValidator);
		addField(templatesEditor);
	}

	@Override
	protected String getPageId() {
		return PreferenceConstants.PAGE_ID_BROKEN_LINKS;
	}

}
