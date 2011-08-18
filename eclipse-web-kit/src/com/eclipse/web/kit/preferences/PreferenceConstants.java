package com.eclipse.web.kit.preferences;

import org.eclipse.core.runtime.QualifiedName;

/**
 * Constant definitions for plug-in preferences
 */
public class PreferenceConstants {
	public static final String P_TEMPLATE_LINK="templateLink";
	public static final String P_TEMPLATE_IMAGE="templateImage";
	public static final String P_TEMPLATE_ZOOM_IMAGE="templateZoomImage";
	
	public static final String P_BROKEN_LINK_TEMPLATES="brokenLinkTemplates";
	public static final String P_BROKEN_LINK_IGNORE="brokenLinkIgnore";
	
	public static final String P_LAST_IMAGE_PATH="lastImagePath";
	public static final String P_LAST_LINK_PATH="lastLinkPath";
	
	public static final String PAGE_ID_WEB_KIT="com.eclipse.web.kit.preferences.WebKitPreferences";
	public static final String PAGE_ID_HTML_PALETTE="com.eclipse.web.kit.preferences.HtmlPalettePreferences";
	public static final String PAGE_ID_BROKEN_LINKS="com.eclipse.web.kit.preferences.BrokenLinksPreferences";
	public static final String PAGE_ID_IGNORE="com.eclipse.web.kit.preferences.IgnorePreferences";
	public static final String PAGE_ID_INTERNAL="com.eclipse.web.kit.preferences.InternalPreferences";
	
	public static final QualifiedName Q_TEMPLATE_LINK=new QualifiedName(PAGE_ID_HTML_PALETTE, P_TEMPLATE_LINK);
	public static final QualifiedName Q_TEMPLATE_IMAGE=new QualifiedName(PAGE_ID_HTML_PALETTE, P_TEMPLATE_IMAGE);
	public static final QualifiedName Q_TEMPLATE_ZOOM_IMAGE=new QualifiedName(PAGE_ID_HTML_PALETTE, P_TEMPLATE_ZOOM_IMAGE);
	
	public static final QualifiedName Q_BROKEN_LINK_TEMPLATES=new QualifiedName(PAGE_ID_BROKEN_LINKS, P_BROKEN_LINK_TEMPLATES);
	public static final QualifiedName Q_BROKEN_LINK_IGNORE=new QualifiedName(PAGE_ID_IGNORE, P_BROKEN_LINK_IGNORE);
	
	public static final QualifiedName Q_LAST_IMAGE_PATH=new QualifiedName(PAGE_ID_IGNORE, P_LAST_IMAGE_PATH);
	public static final QualifiedName Q_LAST_LINK_PATH=new QualifiedName(PAGE_ID_IGNORE, P_LAST_LINK_PATH);
	
	
	
}
