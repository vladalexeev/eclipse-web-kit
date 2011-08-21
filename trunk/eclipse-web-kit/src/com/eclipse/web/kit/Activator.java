package com.eclipse.web.kit;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.eclipse.web.kit.overlay.FieldEditorOverlayPage;
import com.eclipse.web.kit.overlay.ProjectPropertyStore;
import com.eclipse.web.kit.preferences.PreferenceConstants;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "eclipse-web-kit"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	public static String getOverlayedPreferenceValue(IResource resource, QualifiedName qname) {
		IProject project;
		if (resource instanceof IProject) {
			project=(IProject) resource;
		} else {
			project=resource.getProject();
		}
		
		ProjectPropertyStore store=new ProjectPropertyStore(project, Activator.getDefault().getPreferenceStore(), qname.getQualifier());
		
		if (useProjectSettings(store, qname.getQualifier()) || qname.getLocalName().equals(PreferenceConstants.PAGE_ID_INTERNAL)) {
			return getProperty(store, qname);
		} else {
			return Activator.getDefault().getPreferenceStore().getString(qname.getLocalName());
		}
	}
	
	private static boolean useProjectSettings(ProjectPropertyStore store, String pageId) {
		String use = getProperty(store, new QualifiedName(pageId,FieldEditorOverlayPage.USEPROJECTSETTINGS));
		return "true".equals(use);
	}
	
	private static String getProperty(ProjectPropertyStore store, QualifiedName qname) {
		return store.getString(qname.getLocalName());
	}
}
