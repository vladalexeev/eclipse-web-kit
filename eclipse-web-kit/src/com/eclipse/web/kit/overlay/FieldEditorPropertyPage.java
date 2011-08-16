package com.eclipse.web.kit.overlay;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchPropertyPage;

public abstract class FieldEditorPropertyPage extends FieldEditorPreferencePage implements IWorkbenchPropertyPage {

	/*** Name of resource property for the selection of workbench or project settings ***/
	public static final String USEPROJECTSETTINGS = "useProjectSettings"; //$NON-NLS-1$

	private static final String TRUE = "true"; //$NON-NLS-1$

	// Stores all created field editors
	private List<FieldEditor> editors = new ArrayList<FieldEditor>();

	// Stores owning element of properties
	private IAdaptable element;

	// Overlay preference store for property pages
	private IPreferenceStore overlayStore;

	// Cache for page id
	private String pageId;

	/**
	 * Constructor
	 * @param style - layout style
	 */
	public FieldEditorPropertyPage(int style) {
		super(style);
	}

	/**
	 * Constructor
	 * @param title - title string
	 * @param style - layout style
	 */
	public FieldEditorPropertyPage(String title, int style) {
		super(title, style);
	}

	/**
	 * Returns the id of the current preference page as defined in plugin.xml
	 * Subclasses must implement. 
	 * 
	 * @return - the qualifier
	 */
	protected abstract String getPageId();

	/**
	 * Receives the object that owns the properties shown in this property page.
	 * @see org.eclipse.ui.IWorkbenchPropertyPage#setElement(org.eclipse.core.runtime.IAdaptable)
	 */
	public void setElement(IAdaptable element) {
		this.element = element;
	}

	/**
	 * Delivers the object that owns the properties shown in this property page.
	 * @see org.eclipse.ui.IWorkbenchPropertyPage#getElement()
	 */
	public IAdaptable getElement() {
		return element;
	}

	/** 
	 * We override the addField method. This allows us to store each field editor added by subclasses
	 * in a list for later processing.
	 * 
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#addField(org.eclipse.jface.preference.FieldEditor)
	 */
	protected void addField(FieldEditor editor) {
		editors.add(editor);
		super.addField(editor);
	}

	/**
	 *  We override the createControl method. 
	 * In case of property pages we create a new PropertyStore as local preference store.
	 * After all control have been create, we enable/disable these controls.
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#createControl()
	 */
	public void createControl(Composite parent) {
		// Special treatment for property pages
		// Cache the page id
		pageId = getPageId();
		// Create an overlay preference store and fill it with properties
		overlayStore =
				new PropertyStore(
						(IResource) getElement(),
						super.getPreferenceStore(),
						pageId);
		// Set overlay store as current preference store
		super.createControl(parent);
		// Update state of all subclass controls
		updateFieldEditors();
	}

	/** 
	 * We override the createContents method. 
	 * In case of property pages we insert two radio buttons at the top of the page.
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents(Composite parent) {
		return super.createContents(parent);
	}

	/**
	 * Returns in case of property pages the overlay store, 
	 * in case of preference pages the standard preference store
	 * @see org.eclipse.jface.preference.PreferencePage#getPreferenceStore()
	 */
	public IPreferenceStore getPreferenceStore() {
		return overlayStore;
	}

	/*
	 * Enables or disables the field editors and buttons of this page
	 */
	private void updateFieldEditors() {
		updateFieldEditors(true);
	}

	/**
	 * Enables or disables the field editors and buttons of this page
	 * Subclasses may override.
	 * @param enabled - true if enabled
	 */
	protected void updateFieldEditors(boolean enabled) {
		Composite parent = getFieldEditorParent();
		Iterator<FieldEditor> it = editors.iterator();
		while (it.hasNext()) {
			FieldEditor editor = (FieldEditor) it.next();
			editor.setEnabled(enabled, parent);
		}
	}

	/** 
	 * We override the performOk method. In case of property pages we copy the values in the 
	 * overlay store into the property values of the selected project.
	 * We also save the state of the radio buttons.
	 * 
	 * @see org.eclipse.jface.preference.IPreferencePage#performOk()
	 */
	public boolean performOk() {
		boolean result = super.performOk();
		if (result) {
			// Save state of radiobuttons in project properties
			IResource resource = (IResource) getElement();
			try {
				String value = TRUE;
				resource.setPersistentProperty(
						new QualifiedName(pageId, USEPROJECTSETTINGS),
						value);
			} catch (CoreException e) {
			}
		}
		return result;
	}

	/**
	 * We override the performDefaults method. In case of property pages we
	 * switch back to the workspace settings and disable the field editors.
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
	 */
	protected void performDefaults() {
		updateFieldEditors();
		super.performDefaults();
	}
}
