package com.eclipse.web.kit.util.test;

import org.junit.Test;

import com.eclipse.web.kit.util.HtmlParser;
import com.eclipse.web.kit.util.HtmlSimpleTag;

import static org.junit.Assert.*;

public class TestHtmlParser {

	@Test
	public void testSimpleHtml() {
		String testHtml="<html><a href=\"test.html\"></a></html>";
		HtmlParser parser=new HtmlParser();
		HtmlSimpleTag[] tags=parser.parse(testHtml);
		
		assertEquals(4, tags.length);
		assertEquals("html", tags[0].getTagName());
		assertEquals(0, tags[0].getAttributes().size());

		assertEquals("a", tags[1].getTagName());
		assertEquals(1, tags[1].getAttributes().size());
		assertEquals("test.html", tags[1].getAttributes().get("href"));
		
		assertEquals("/a", tags[2].getTagName());
		assertEquals(0, tags[2].getAttributes().size());
		
		assertEquals("/html", tags[3].getTagName());
		assertEquals(0, tags[3].getAttributes().size());
	}
	
	@Test
	public void testSimpleHtmlWithSpaces() {
		String testHtml="  <html>  <a   href  = \"test.html\" >qqq  </a>  </html>";
		HtmlParser parser=new HtmlParser();
		HtmlSimpleTag[] tags=parser.parse(testHtml);
		
		assertEquals(4, tags.length);
		assertEquals("html", tags[0].getTagName());
		assertEquals(0, tags[0].getAttributes().size());

		assertEquals("a", tags[1].getTagName());
		assertEquals(1, tags[1].getAttributes().size());
		assertEquals("test.html", tags[1].getAttributes().get("href"));
		
		assertEquals("/a", tags[2].getTagName());
		assertEquals(0, tags[2].getAttributes().size());
		
		assertEquals("/html", tags[3].getTagName());
		assertEquals(0, tags[3].getAttributes().size());
	}
}
