package com.eclipse.web.kit.util;

import java.util.ArrayList;

public class StringTransformer {
	private StringTransformerOptions options;
	private String text;
	
	private String transformedText;
	private int[] originalCharIndexes;
		
	public StringTransformer() {
		
	}

	public StringTransformerOptions getOptions() {
		return options;
	}

	public void setOptions(StringTransformerOptions options) {
		this.options = options;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	private boolean isLeadingWhitespace(char[] charText, int index) {
		index--;
		while (index>0) {
			char c=charText[index];
			if (c=='\r' || c=='\n') {
				return true;
			} else if (c==' ' || c=='\t') {
				index--;
			} else {
				return false;
			}
		}
		
		return true;
	}
	
	private boolean isTrailingWhitespace(char[] charText, int index) {
		index++;
		while (index<charText.length) {
			char c=charText[index];
			if (c=='\r' || c=='\n') {
				return true;
			} else if (c==' ' || c=='\t') {
				index++;
			} else {
				return false;
			}
		}
		
		return true;
	}

	public void transformText() {
		ArrayList<Integer> originalIndexes=new ArrayList<Integer>();
		StringBuffer transformedBuffer=new StringBuffer();
		
		char[] charText=text.toCharArray();
		
		for (int i=0; i<text.length(); i++) {
			char c=text.charAt(i);
			if (c==' ' || c=='\t') {
				if (options.isIgnoreLeadingWhitespaces() && isLeadingWhitespace(charText, i)) {
					continue;
				} 
				
				if (options.isIgnoreTrailingwhitespaces() && isTrailingWhitespace(charText, i)) {
					continue;
				}
			} else if (c=='\r') {
				if (options.isIgnoreCarriageReturns()) {
					continue;
				}
			} else if (c=='\n') {
				if (options.isIgnoreNewLines()) {
					continue;
				}
			}
			
			transformedBuffer.append(c);
			originalIndexes.add(i);
		}
		
		transformedText=transformedBuffer.toString();
		
		originalCharIndexes=new int[originalIndexes.size()];
		for (int i=0; i<originalCharIndexes.length; i++) {
			originalCharIndexes[i]=originalIndexes.get(i);
		}
	}
	
	public String getTransformedText() {
		return transformedText;
	}
	
	public int[] getOriginalCharIndexes() {
		return originalCharIndexes;
	}
}
