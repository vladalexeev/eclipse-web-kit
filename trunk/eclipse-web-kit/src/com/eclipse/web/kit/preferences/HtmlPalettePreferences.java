package com.eclipse.web.kit.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import com.eclipse.web.kit.Activator;
import com.eclipse.web.kit.preferences.editors.MultilIneStringFieldEditor;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */

public class HtmlPalettePreferences
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	public HtmlPalettePreferences() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Html palette preferences");
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
		addField(new MultilIneStringFieldEditor(PreferenceConstants.P_TEMPLATE_LINK, "Link template", getFieldEditorParent()));
		addField(new MultilIneStringFieldEditor(PreferenceConstants.P_TEMPLATE_IMAGE, "Image template", getFieldEditorParent()));
		addField(new MultilIneStringFieldEditor(PreferenceConstants.P_TEMPLATE_ZOOM_IMAGE, "Zoom image template", getFieldEditorParent()));
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
}