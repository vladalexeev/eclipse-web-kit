package com.eclipse.web.kit.preferences;

import org.eclipse.core.runtime.QualifiedName;

/**
 * Constant definitions for plug-in preferences
 */
public class PreferenceConstants {
	public static final String P_TEMPLATE_LINK="com.eclipse.web.kit.preferences.templateLink";
	public static final String P_TEMPLATE_IMAGE="com.eclipse.web.kit.preferences.templateImage";
	public static final String P_TEMPLATE_ZOOM_IMAGE="com.eclipse.web.kit.preferences.templateZoomImage";
	
	public static final String P_BROKEN_LINK_TEMPLATES="com.eclipse.web.kit.preferences.brokenLinkTemplates";
	public static final String P_BROKEN_LINK_EXCLUSIONS="com.eclipse.web.kit.preferences.brokenLinkExclusions";
	
	public static final String P_LAST_IMAGE_PATH="com.eclipse.web.kit.preferences.lastImagePath";
	public static final String P_LAST_LINK_PATH="com.eclipse.web.kit.preferences.lastLinkPath";
	
	public static final String PAGE_ID_WEB_KIT="com.eclipse.web.kit.preferences.webkit";
	public static final String PAGE_ID_HTML_PALETTE="com.eclipse.web.kit.preferences.htmlpalette";
	public static final String PAGE_ID_BROKEN_LINKS="com.eclipse.web.kit.preferences.brokenlinks";
	public static final String PAGE_ID_EXCLUSIONS="com.eclipse.web.kit.preferences.exclusions";
	public static final String PAGE_ID_INTERNAL="com.eclipse.web.kit.preferences.internal";
	
	public static final QualifiedName Q_TEMPLATE_LINK=new QualifiedName(PAGE_ID_HTML_PALETTE, P_TEMPLATE_LINK);
	public static final QualifiedName Q_TEMPLATE_IMAGE=new QualifiedName(PAGE_ID_HTML_PALETTE, P_TEMPLATE_IMAGE);
	public static final QualifiedName Q_TEMPLATE_ZOOM_IMAGE=new QualifiedName(PAGE_ID_HTML_PALETTE, P_TEMPLATE_ZOOM_IMAGE);
	
	public static final QualifiedName Q_BROKEN_LINK_TEMPLATES=new QualifiedName(PAGE_ID_BROKEN_LINKS, P_BROKEN_LINK_TEMPLATES);
	public static final QualifiedName Q_BROKEN_LINK_EXCLUSIONS=new QualifiedName(PAGE_ID_EXCLUSIONS, P_BROKEN_LINK_EXCLUSIONS);
	
	public static final QualifiedName Q_LAST_IMAGE_PATH=new QualifiedName(PAGE_ID_EXCLUSIONS, P_LAST_IMAGE_PATH);
	public static final QualifiedName Q_LAST_LINK_PATH=new QualifiedName(PAGE_ID_EXCLUSIONS, P_LAST_LINK_PATH);
	
	
	
}
