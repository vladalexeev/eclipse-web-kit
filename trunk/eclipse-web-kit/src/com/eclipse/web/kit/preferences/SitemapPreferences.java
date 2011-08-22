package com.eclipse.web.kit.preferences;

import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;

import com.eclipse.web.kit.Activator;
import com.eclipse.web.kit.overlay.FieldEditorPropertyPage;

public class SitemapPreferences extends FieldEditorPropertyPage {

	public SitemapPreferences() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	@Override
	protected String getPageId() {
		return PreferenceConstants.PAGE_ID_SITEMAP;
	}

	@Override
	protected void createFieldEditors() {
		addField(new StringFieldEditor(PreferenceConstants.P_SITEMAP_BASE_URL, "Site url", getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_SITEMAP_SITEMAP_FILE, "Sitemap file name", getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_SITEMAP_EXTENSIONS, "File extensions", getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_SITEMAP_INDEX_FILE, "Index file name", getFieldEditorParent()));
		addField(new ComboFieldEditor(PreferenceConstants.P_SITEMAP_REPLACE_INDEX_FILE, "Replace index file", 
				new String[][]{
					{"Root only", PreferenceConstants.V_SITEMAP_REPLACE_INDEX_ROOT_ONLY},
					{"Always", PreferenceConstants.V_SITEMAP_REPLACE_INDEX_ALWAYS},
					{"Never", PreferenceConstants.V_SITEMAP_REPLACE_INDEX_NEVER}
				}, getFieldEditorParent()));
		addField(new ComboFieldEditor(PreferenceConstants.P_SITEMAP_DATE_FORMAT, "Date format", 
				new String[][]{
					{"Date", PreferenceConstants.V_SITEMAP_DATE_FORMAT_DATE},
					{"Date and time", PreferenceConstants.V_SITEMAP_DATE_FORMAT_DATETIME}
				},getFieldEditorParent()));
	}

}
