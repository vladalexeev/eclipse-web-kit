package com.eclipse.web.kit.util.html.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.core.resources.IFile;

import com.eclipse.web.kit.util.FileLoader;

public class HtmlParser {
	
	enum ParserState {
		SEARCH_TAG_START,
		SEARCH_TAG_END,
		SEARCH_COMMENT_END,
		SEARCH_ATTR_NAME_START,
		SEARCH_ATTR_NAME_END,
		SEARCH_ATTR_EQUALS,
		SEARCH_ATTR_VALUE_START,
		SEARCH_ATTR_VALUE_END_COMMA,
		SEARCH_ATTR_VALUE_END_SPACE,
	}
	
	private ParserState state;
	
	private ArrayList<HtmlSimpleElement> currentTags=new ArrayList<HtmlSimpleElement>();
	
	private String currentTag=null;
	private boolean currentTagAutoClose=false;
	private HashMap<String,String> currentAttributes=null;
	private String currentAttrName=null;
	private String currentAttrValue=null;
	private String currentText=null;
	
	private void processCurrentChar(char c) {
		switch (state) {
		case SEARCH_TAG_START:
			processCurrentChat_SearchTagStart(c);
			break;
		case SEARCH_TAG_END:
			processCurrentChar_SearchTagEnd(c);
			break;
		case SEARCH_COMMENT_END:
			processCurrentChar_SearchCommentEnd(c);
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
			if (currentText!=null && currentText.length()>0) {
				currentTags.add(new HtmlSimpleText(currentText));
				currentText=null;
			}
			
			state=ParserState.SEARCH_TAG_END;
			currentTag="";
		} else {
			if (currentText==null) {
				currentText=""+c;
			} else {
				currentText+=c;
			}
		}
	}
	
	private void processCurrentChar_SearchTagEnd(char c) {
		if (c==' ' || c=='\r' || c=='\n' || c=='\t') {
			if (currentTag.equals("!--")) {
				currentText=""+c;
				state=ParserState.SEARCH_COMMENT_END;
			} else {
				state=ParserState.SEARCH_ATTR_NAME_START;
			}
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
		} else if (c=='>') {
			if (currentAttrName.equals("/")) {
				currentTagAutoClose=true;
			} else {
				currentAttrValue="";
				storeCurrentAttr();
			}
			
			storeCurrentTag();
			state=ParserState.SEARCH_TAG_START;
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
	
	private void processCurrentChar_SearchCommentEnd(char c) {
		currentText+=c;
		if (currentText.endsWith("-->")) {
			currentTags.add(new HtmlSimpleComment(currentText.substring(0,currentText.length()-3)));
			currentTag=null;
			currentText=null;
			state=ParserState.SEARCH_TAG_START;
		}
	}
	
	private void storeCurrentTag() {
		if (currentTag!=null) {
			HtmlSimpleTag tag=new HtmlSimpleTag();
			tag.setTagName(currentTag.toLowerCase());
			tag.setAutoClose(currentTagAutoClose);
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
			currentTagAutoClose=false;
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
			
			currentAttributes.put(currentAttrName.toLowerCase(), currentAttrValue);
			currentAttrName=null;
			currentAttrValue=null;
		}
	}

	public HtmlSimpleElement[] parse(String html) {
		state=ParserState.SEARCH_TAG_START;

		currentTag=null;
		currentAttributes=null;
		currentAttrName=null;
		currentAttrValue=null;
		
		currentTags=new ArrayList<HtmlSimpleElement>();

		for (int index=0; index<html.length(); index++) {
			char c=html.charAt(index);
			processCurrentChar(c);
		}
		
		if (state==ParserState.SEARCH_TAG_START && currentText!=null && currentText.length()>0) {
			currentTags.add(new HtmlSimpleText(currentText));
		}
		
		return currentTags.toArray(new HtmlSimpleElement[currentTags.size()]);
	}
	
	public HtmlSimpleElement[] parse(IFile file) throws IOException {
		String filePath=file.getLocation().toOSString();
		String fileContent=FileLoader.loadFile(filePath);
		return parse(fileContent);
	}
}
