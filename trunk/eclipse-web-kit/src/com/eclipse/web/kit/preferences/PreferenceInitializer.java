package com.eclipse.web.kit.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.eclipse.web.kit.Activator;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		
		store.setDefault(PreferenceConstants.P_TEMPLATE_LINK, 
				"<a href=\"{hyperlink}\">{text}</a>");
		store.setDefault(PreferenceConstants.P_TEMPLATE_IMAGE, 
				"<img src=\"{imageFile}\" width=\"{imageWidth}\" height=\"{imageHeight}\" border=\"0\" alt=\"\" title=\"\" />");
		store.setDefault(PreferenceConstants.P_TEMPLATE_ZOOM_IMAGE, 
				"<a name=\"{largeImageFileName}\" href=\"javascript:showImage('{largeImageFile}', '{name}', {largeImageWidth}, {largeImageHeight}, '{description}')\">\n"+ 
				"<img src=\"{smallImageFile}\" width=\"{smallImageWidth}\" height=\"{smallImageHeight}\" border=\"0\" alt=\"{name}\" title=\"{name}\" /></a>");
		
		store.setDefault(PreferenceConstants.P_BROKEN_LINK_TEMPLATES, 
				" href=\"*\"\0 src=\"*\"\0 href=\"javascript:showImage('*'");
		store.setDefault(PreferenceConstants.P_BROKEN_LINK_FILE_EXTENSIONS, "htm, html");
		store.setDefault(PreferenceConstants.P_BROKEN_LINK_EXCLUSIONS, "");
		
		store.setDefault(PreferenceConstants.P_SITEMAP_BASE_URL, "");
		store.setDefault(PreferenceConstants.P_SITEMAP_EXTENSIONS, "htm, html");
		store.setDefault(PreferenceConstants.P_SITEMAP_INDEX_FILE, "index.html");
		store.setDefault(PreferenceConstants.P_SITEMAP_SITEMAP_FILE, "sitemap.xml");
		store.setDefault(PreferenceConstants.P_SITEMAP_REPLACE_INDEX_FILE, PreferenceConstants.V_SITEMAP_REPLACE_INDEX_ROOT_ONLY);
		store.setDefault(PreferenceConstants.P_SITEMAP_EXCLUSIONS, "");
	}

}
