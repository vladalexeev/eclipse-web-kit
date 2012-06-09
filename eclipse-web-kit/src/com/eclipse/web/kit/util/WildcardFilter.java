package com.eclipse.web.kit.util;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A filter that selects files, but not directories, based on one or more wildcards.
 * 
 * <code>?</code> matches a single character, excluding the path separator
 * <code>*</code> matches zero or more characters in a filename, excluding the path separator
 * <code>**</code> matches zero or more directories
 *
 * @author Gili Tzabari
 */
public final class WildcardFilter implements FileFilter {
	private final Pattern pattern;
	private String sourcePath;

	/**
	 * Creates a new WildcardFileFilter.
	 *
	 * @param path the path containing wildcards
	 */
	public WildcardFilter(String path) {
		this.sourcePath=path;
		
		final String slashPrefix = "(?<=^|/)";
		final String slashPostfix = "(?=/|$)";
		final String notStarPrefix = "(?<!\\*)";
		final String notStarPostfix = "(?!\\*)";

//		if (!path.contains("/")) {
//			// relative path
//			path = "**/" + path;
//		}
		Pattern wildcardTokens = Pattern.compile("(" + slashPrefix + Pattern.quote("**") + slashPostfix + ")|" +
				"(" + notStarPrefix + Pattern.quote("*") + notStarPostfix + ")|" +
				"(" + Pattern.quote("?") + ")");
		Matcher matcher = wildcardTokens.matcher(path);
		StringBuilder escaped = new StringBuilder();
		int endOfLastMatch = 0;
		while (matcher.find()) {
			escaped.append(path.substring(endOfLastMatch, matcher.start()));
			if (matcher.group(1) != null) {
				assert (matcher.group(2).equals("**"));
				escaped.append(".*");
			} else if (matcher.group(2) != null) {
				assert (matcher.group(2).equals("*"));
				escaped.append("[^/]*");
			} else if (matcher.group(3) != null) {
				assert (matcher.group(3).equals("?"));
				escaped.append(".");
			} else {
				throw new AssertionError("No groups matched: " + matcher);
			}
			endOfLastMatch = matcher.end();
		}
		escaped.append(path.substring(endOfLastMatch));
		this.pattern = Pattern.compile(escaped.toString());
	}

	@Override
	public boolean accept(File file) {
		return accept(file.getPath());
	}

	public boolean accept(String fileName) {
		return pattern.matcher(fileName.replace(File.separator, "/")).matches();
	}
	
	public String toString() {
		return "WildcardFilter["+sourcePath+"]";
	}
}