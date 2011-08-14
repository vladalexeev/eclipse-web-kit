package com.eclipse.web.kit.preferences;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.eclipse.web.kit.Activator;
import com.eclipse.web.kit.preferences.editors.StringListFieldEditor;

public class BrokenLinksPreferences extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {
	
	private IInputValidator templateValidator=new IInputValidator() {
		
		@Override
		public String isValid(String newText) {
			int starIndex=newText.indexOf('*');
			if (starIndex<0) {
				return "String should contain '*'";
			}
			
			if (starIndex==0) {
				return "String should not start from '*'";
			}
			
			if (starIndex==newText.length()-1) {
				return "String should not end to '*'";
			}
			
			return null;
		}
	};
		
	public BrokenLinksPreferences() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		//setDescription("Broken links preferences");
	}

	@Override
	public void init(IWorkbench workbench) {
	}

	@Override
	protected void createFieldEditors() {
		StringListFieldEditor templatesEditor=new StringListFieldEditor(PreferenceConstants.P_BROKEN_LINK_TEMPLATES, "Broken link templates", getFieldEditorParent());
		templatesEditor.setValidator(templateValidator);
		addField(templatesEditor);
		
		StringListFieldEditor ignoreEditor=new StringListFieldEditor(PreferenceConstants.P_BROKEN_LINK_IGNORE, "Ignored broken links", getFieldEditorParent());
		addField(ignoreEditor);
	}

}
