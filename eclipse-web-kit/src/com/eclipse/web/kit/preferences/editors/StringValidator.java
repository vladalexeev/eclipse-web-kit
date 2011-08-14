package com.eclipse.web.kit.preferences.editors;

public interface StringValidator {
	/**
	 * @param value source string
	 * @return null if string is valid or error message otherwise.
	 */
	public String validate(String value);
}
