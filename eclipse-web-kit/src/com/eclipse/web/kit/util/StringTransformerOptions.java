package com.eclipse.web.kit.util;

public class StringTransformerOptions {
	private boolean ignoreLeadingWhitespaces=false;
	private boolean ignoreTrailingwhitespaces=false;
	private boolean ignoreCarriageReturns=false;
	private boolean ignoreNewLines=false;
	
	public StringTransformerOptions() {
	}

	public boolean isIgnoreLeadingWhitespaces() {
		return ignoreLeadingWhitespaces;
	}

	public void setIgnoreLeadingWhitespaces(boolean ignoreLeadingWhitespaces) {
		this.ignoreLeadingWhitespaces = ignoreLeadingWhitespaces;
	}

	public boolean isIgnoreTrailingwhitespaces() {
		return ignoreTrailingwhitespaces;
	}

	public void setIgnoreTrailingwhitespaces(boolean ignoreTrailingwhitespaces) {
		this.ignoreTrailingwhitespaces = ignoreTrailingwhitespaces;
	}

	public boolean isIgnoreCarriageReturns() {
		return ignoreCarriageReturns;
	}

	public void setIgnoreCarriageReturns(boolean ignoreCarriageReturns) {
		this.ignoreCarriageReturns = ignoreCarriageReturns;
	}

	public boolean isIgnoreNewLines() {
		return ignoreNewLines;
	}

	public void setIgnoreNewLines(boolean ignoreNewLines) {
		this.ignoreNewLines = ignoreNewLines;
	}
}
