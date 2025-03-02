package com.eclipse.web.kit.preferences;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Shell;

import com.eclipse.web.kit.Activator;
import com.eclipse.web.kit.overlay.FieldEditorPropertyPage;
import com.eclipse.web.kit.preferences.editors.AddActionHandler;
import com.eclipse.web.kit.preferences.editors.StringListFieldEditor;
import com.eclipse.web.kit.util.FileUtil;
import com.eclipse.web.kit.util.SwtUtil;


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
		StringListFieldEditor cssFilesFieldEditor=
				new StringListFieldEditor(PreferenceConstants.P_CSS_FILES, "CSS files", getFieldEditorParent());
		cssFilesFieldEditor.setAddActionHandler(new AddActionHandler() {
			@Override
			public String doAdd() {
				String fileName=FileUtil.selectAnyFile(new Shell());
				IProject project=SwtUtil.getActiveProject();
				return FileUtil.createRelativePath2(project.getLocation().toString(), fileName);
			}
		});
		
		addField(cssFilesFieldEditor);
		
		addField(new StringListFieldEditor(PreferenceConstants.P_CSS_CLASSES, "CSS classes", getFieldEditorParent()));
	}

}
