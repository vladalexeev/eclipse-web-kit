package com.eclipse.web.kit.util;

import java.util.ArrayList;
import java.util.HashMap;

public class HtmlParser {
	
	enum ParserState {
		SEARCH_TAG_START,
		SEARCH_TAG_END,
		SEARCH_ATTR_NAME_START,
		SEARCH_ATTR_NAME_END,
		SEARCH_ATTR_EQUALS,
		SEARCH_ATTR_VALUE_START,
		SEARCH_ATTR_VALUE_END_COMMA,
		SEARCH_ATTR_VALUE_END_SPACE,
	}
	
	private ParserState state;
	
	private ArrayList<HtmlSimpleTag> currentTags=new ArrayList<HtmlSimpleTag>();
	
	private String currentTag=null;
	private HashMap<String,String> currentAttributes=null;
	private String currentAttrName=null;
	private String currentAttrValue=null;
	
	private void processCurrentChar(char c) {
		switch (state) {
		case SEARCH_TAG_START:
			processCurrentChat_SearchTagStart(c);
			break;
		case SEARCH_TAG_END:
			processCurrentChar_SearchTagEnd(c);
			break;
		case SEARCH_ATTR_NAME_START:
			processCurrentChar_SearchAttrNameStart(c);
			break;
		case SEARCH_ATTR_NAME_END:
			processCurrentChar_SearchAttrNameEnd(c);
			break;
		case SEARCH_ATTR_EQUALS:
			processCurrentChar_SearchAttrEquals(c);
			break;
		case SEARCH_ATTR_VALUE_START:
			processCurrentChar_SearchAttrValueStart(c);
			break;
		case SEARCH_ATTR_VALUE_END_COMMA:
			processCurrentChar_SearchAttrValueEndComma(c);
			break;
		case SEARCH_ATTR_VALUE_END_SPACE:
			processCurrentChar_SearchAttrValueEndSpace(c);
			break;
		default:
			throw new RuntimeException("Unknown state "+state);
		}
	}
	
	private void processCurrentChat_SearchTagStart(char c) {
		if (c=='<') {
			state=ParserState.SEARCH_TAG_END;
			currentTag="";
		}
	}
	
	private void processCurrentChar_SearchTagEnd(char c) {
		if (c==' ' || c=='\r' || c=='\n' || c=='\t') {
			state=ParserState.SEARCH_ATTR_NAME_START;
		} else if (c=='>') {
			storeCurrentTag();
			state=ParserState.SEARCH_TAG_START;
		} else {
			currentTag+=c;
		}
	}
	
	private void processCurrentChar_SearchAttrNameStart(char c) {
		if (c==' ' || c=='\r' || c=='\n' || c=='\t') {
			return;
		} else if (c=='>') {
			storeCurrentTag();
			state=ParserState.SEARCH_TAG_START;
		} else {
			currentAttrName=""+c;
			state=ParserState.SEARCH_ATTR_NAME_END;
		}
	}
	
	private void processCurrentChar_SearchAttrNameEnd(char c) {
		if (c==' ' || c=='\r' || c=='\n' || c=='\t') {
			state=ParserState.SEARCH_ATTR_EQUALS;
		} else if (c=='=') {
			state=ParserState.SEARCH_ATTR_VALUE_START;
		} else {
			currentAttrName+=c;
		}
	}
	
	private void processCurrentChar_SearchAttrEquals(char c) {
		if (c==' ' || c=='\r' || c=='\n' || c=='\t') {
			return;
		} else if (c=='=') {
			state=ParserState.SEARCH_ATTR_VALUE_START;
		} else {
			storeCurrentAttr();
			currentAttrName=""+c;
			state=ParserState.SEARCH_ATTR_NAME_END;
		}
	}
	
	private void processCurrentChar_SearchAttrValueStart(char c) {
		if (c==' ' || c=='\r' || c=='\n' || c=='\t') {
			return;
		} else if (c=='"') {
			currentAttrValue="";
			state=ParserState.SEARCH_ATTR_VALUE_END_COMMA;
		} else {
			currentAttrValue=""+c;
			state=ParserState.SEARCH_ATTR_VALUE_END_SPACE;
		}
	}
	
	private void processCurrentChar_SearchAttrValueEndComma(char c) {
		if (c=='"') {
			storeCurrentAttr();
			state=ParserState.SEARCH_ATTR_NAME_START;
		} else {
			currentAttrValue+=c;
		}
	}
	
	private void processCurrentChar_SearchAttrValueEndSpace(char c) {
		if (c==' ' || c=='\r' || c=='\n' || c=='\t') {
			storeCurrentAttr();
			state=ParserState.SEARCH_ATTR_NAME_START;
		} else if (c=='>') {
			storeCurrentAttr();
			storeCurrentTag();
			state=ParserState.SEARCH_TAG_START;
		}
	}
	
	private void storeCurrentTag() {
		if (currentTag!=null) {
			HtmlSimpleTag tag=new HtmlSimpleTag();
			tag.setTagName(currentTag);
			if (currentAttributes==null) {
				tag.setAttributes(new HashMap<String, String>());
			} else {
				tag.setAttributes(currentAttributes);
			}
			
			currentTags.add(tag);
			
			currentTag=null;
			currentAttributes=null;
			currentAttrName=null;
			currentAttrValue=null;
		}
	}
	
	private void storeCurrentAttr() {
		if (currentAttrName!=null) {		
			if (currentAttrValue==null) {
				currentAttrValue="";
			}
			
			if (currentAttributes==null) {
				currentAttributes=new HashMap<String, String>();
			}
			
			currentAttributes.put(currentAttrName, currentAttrValue);
			currentAttrName=null;
			currentAttrValue=null;
		}
	}

	public HtmlSimpleTag[] parse(String html) {
		state=ParserState.SEARCH_TAG_START;

		currentTag=null;
		currentAttributes=null;
		currentAttrName=null;
		currentAttrValue=null;
		
		currentTags=new ArrayList<HtmlSimpleTag>();

		for (int index=0; index<html.length(); index++) {
			char c=html.charAt(index);
			processCurrentChar(c);
		}
		
		return currentTags.toArray(new HtmlSimpleTag[currentTags.size()]);
	}
}
