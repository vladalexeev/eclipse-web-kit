package com.eclipse.web.kit.preferences;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Shell;

import com.eclipse.web.kit.Activator;
import com.eclipse.web.kit.overlay.FieldEditorPropertyPage;
import com.eclipse.web.kit.preferences.editors.AddActionHandler;
import com.eclipse.web.kit.preferences.editors.StringListFieldEditor;
import com.eclipse.web.kit.util.FileUtil;
import com.eclipse.web.kit.util.SwtUtil;

public class NewsFeedsPreferences extends FieldEditorPropertyPage {

	public NewsFeedsPreferences() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}
	
	@Override
	protected String getPageId() {
		return PreferenceConstants.PAGE_ID_NEWS_FEEDS;
	}

	@Override
	protected void createFieldEditors() {
		StringListFieldEditor descFieldEditor=
				new StringListFieldEditor(PreferenceConstants.P_NEWS_FEEDS_DESCRIPTIONS, "News feeds descriptions", getFieldEditorParent());
		descFieldEditor.setAddActionHandler(new AddActionHandler() {
			@Override
			public String doAdd() {
				String fileName=FileUtil.selectAnyFile(new Shell());
				IProject project=SwtUtil.getActiveProject();
				return FileUtil.createRelativePath2(project.getLocation().toString(), fileName);
			}
		});
		
		addField(descFieldEditor);
		
		
		addField(new StringListFieldEditor(PreferenceConstants.P_NEWS_FEEDS_TITLE_PATTERNS, "News title patterns", getFieldEditorParent()));
		addField(new StringListFieldEditor(PreferenceConstants.P_NEWS_FEEDS_DESCRIPTION_PATTERNS, "News text patterns", getFieldEditorParent()));
	}

}
