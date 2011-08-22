package com.eclipse.web.kit.preferences;

import org.eclipse.core.runtime.QualifiedName;

/**
 * Constant definitions for plug-in preferences
 */
public class PreferenceConstants {
	public static final String PAGE_ID_WEB_KIT="com.eclipse.web.kit.preferences.webkit";
	public static final String PAGE_ID_HTML_PALETTE="com.eclipse.web.kit.preferences.htmlpalette";
	public static final String PAGE_ID_BROKEN_LINKS="com.eclipse.web.kit.preferences.brokenlinks";
	public static final String PAGE_ID_EXCLUSIONS="com.eclipse.web.kit.preferences.exclusions";
	public static final String PAGE_ID_INTERNAL="com.eclipse.web.kit.preferences.internal";
	public static final String PAGE_ID_SITEMAP="com.eclipse.web.kit.preferences.sitemap";
	
	
	public static final String P_TEMPLATE_LINK = PAGE_ID_HTML_PALETTE+".templateLink";
	public static final String P_TEMPLATE_IMAGE = PAGE_ID_HTML_PALETTE+".templateImage";
	public static final String P_TEMPLATE_ZOOM_IMAGE = PAGE_ID_HTML_PALETTE+".templateZoomImage";
	
	public static final String P_BROKEN_LINK_TEMPLATES = PAGE_ID_BROKEN_LINKS+".brokenLinkTemplates";
	public static final String P_BROKEN_LINK_EXCLUSIONS = PAGE_ID_EXCLUSIONS+".brokenLinkExclusions";
	
	public static final String P_SITEMAP_BASE_URL = PAGE_ID_SITEMAP+".baseUrl";
	public static final String P_SITEMAP_INDEX_FILE = PAGE_ID_SITEMAP+".indexFile";
	public static final String P_SITEMAP_EXTENSIONS = PAGE_ID_SITEMAP+".extensions";
	public static final String P_SITEMAP_REPLACE_INDEX_FILE = PAGE_ID_SITEMAP+".replaceIndexFile";
	public static final String P_SITEMAP_SITEMAP_FILE = PAGE_ID_SITEMAP+".sitemapFile";
	
	public static final String V_SITEMAP_REPLACE_INDEX_ROOT_ONLY="rootOnly";
	public static final String V_SITEMAP_REPLACE_INDEX_ALWAYS="always";
	public static final String V_SITEMAP_REPLACE_INDEX_NEVER="never";
	
	public static final String V_SITEMAP_DATE_FORMAT_DATE="date";
	public static final String V_SITEMAP_DATE_FORMAT_DATETIME="datetime";
	
	
	public static final String P_LAST_IMAGE_PATH=PAGE_ID_INTERNAL+".lastImagePath";
	public static final String P_LAST_LINK_PATH=PAGE_ID_INTERNAL+".lastLinkPath";
	
	
	
	public static final QualifiedName Q_TEMPLATE_LINK=new QualifiedName(PAGE_ID_HTML_PALETTE, P_TEMPLATE_LINK);
	public static final QualifiedName Q_TEMPLATE_IMAGE=new QualifiedName(PAGE_ID_HTML_PALETTE, P_TEMPLATE_IMAGE);
	public static final QualifiedName Q_TEMPLATE_ZOOM_IMAGE=new QualifiedName(PAGE_ID_HTML_PALETTE, P_TEMPLATE_ZOOM_IMAGE);
	
	public static final QualifiedName Q_BROKEN_LINK_TEMPLATES=new QualifiedName(PAGE_ID_BROKEN_LINKS, P_BROKEN_LINK_TEMPLATES);
	public static final QualifiedName Q_BROKEN_LINK_EXCLUSIONS=new QualifiedName(PAGE_ID_EXCLUSIONS, P_BROKEN_LINK_EXCLUSIONS);
	
	public static final QualifiedName Q_LAST_IMAGE_PATH=new QualifiedName(PAGE_ID_EXCLUSIONS, P_LAST_IMAGE_PATH);
	public static final QualifiedName Q_LAST_LINK_PATH=new QualifiedName(PAGE_ID_EXCLUSIONS, P_LAST_LINK_PATH);
	
	public static final QualifiedName Q_SITEMAP_BASE_URL=new QualifiedName(PAGE_ID_SITEMAP, P_SITEMAP_BASE_URL);
	public static final QualifiedName Q_SITEMAP_INDEX_FILE=new QualifiedName(PAGE_ID_SITEMAP, P_SITEMAP_INDEX_FILE);
	public static final QualifiedName Q_SITEMAP_EXTENSIONS=new QualifiedName(PAGE_ID_SITEMAP, P_SITEMAP_EXTENSIONS);
	public static final QualifiedName Q_SITEMAP_REPLACE_INDEX_FILE=new QualifiedName(PAGE_ID_SITEMAP, P_SITEMAP_REPLACE_INDEX_FILE);
	public static final QualifiedName Q_SITEMAP_SITEMAP_FILE=new QualifiedName(PAGE_ID_SITEMAP, P_SITEMAP_SITEMAP_FILE);
}
