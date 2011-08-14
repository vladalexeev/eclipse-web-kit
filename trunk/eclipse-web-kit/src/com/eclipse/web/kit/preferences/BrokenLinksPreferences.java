package com.eclipse.web.kit.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.eclipse.web.kit.Activator;
import com.eclipse.web.kit.preferences.editors.AddRemoveListFieldEditor;
import com.eclipse.web.kit.preferences.editors.StringValidator;

public class BrokenLinksPreferences extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {
	
	private StringValidator templateValidator=new StringValidator() {
		@Override
		public String validate(String value) {
			int starIndex=value.indexOf('*');
			if (starIndex<0) {
				return "String should contain '*'";
			}
			
			if (starIndex==0) {
				return "String should not start from '*'";
			}
			
			if (starIndex==value.length()-1) {
				return "String should not end to '*'";
			}
			
			return null;
		}
	};

	public BrokenLinksPreferences() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Broken links preferences");
	}

	@Override
	public void init(IWorkbench workbench) {
	}

	@Override
	protected void createFieldEditors() {
		AddRemoveListFieldEditor editor=new AddRemoveListFieldEditor(PreferenceConstants.P_BROKEN_LINK_TEMPLATES, "Broken link templates", getFieldEditorParent());
		editor.setValidator(templateValidator);
		addField(editor);
	}

}
