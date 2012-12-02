package com.eclipse.web.kit.util.test;

import org.junit.Test;

import com.eclipse.web.kit.util.html.parser.HtmlParser;
import com.eclipse.web.kit.util.html.parser.HtmlSimpleComment;
import com.eclipse.web.kit.util.html.parser.HtmlSimpleElement;
import com.eclipse.web.kit.util.html.parser.HtmlSimpleTag;
import com.eclipse.web.kit.util.html.parser.HtmlSimpleText;

import static org.junit.Assert.*;

public class TestHtmlParser {

	@Test
	public void testSimpleHtml() {
		String testHtml="<html><a href=\"test.html\"></a></html>";
		HtmlParser parser=new HtmlParser();
		HtmlSimpleElement[] elems=parser.parse(testHtml);
		
		assertEquals(4, elems.length);
		assertTrue(elems[0] instanceof HtmlSimpleTag);
		assertEquals("html", ((HtmlSimpleTag)elems[0]).getTagName());
		assertEquals(0, ((HtmlSimpleTag)elems[0]).getAttributes().size());

		assertTrue(elems[1] instanceof HtmlSimpleTag);
		assertEquals("a", ((HtmlSimpleTag)elems[1]).getTagName());
		assertEquals(1, ((HtmlSimpleTag)elems[1]).getAttributes().size());
		assertEquals("test.html", ((HtmlSimpleTag)elems[1]).getAttributes().get("href"));
		
		assertTrue(elems[2] instanceof HtmlSimpleTag);
		assertEquals("/a", ((HtmlSimpleTag)elems[2]).getTagName());
		assertEquals(0, ((HtmlSimpleTag)elems[2]).getAttributes().size());
		
		assertTrue(elems[3] instanceof HtmlSimpleTag);
		assertEquals("/html", ((HtmlSimpleTag)elems[3]).getTagName());
		assertEquals(0, ((HtmlSimpleTag)elems[3]).getAttributes().size());
	}
	
	@Test
	public void testSimpleHtmlWithSpaces() {
		String testHtml="  <html> 123 <a   href  = \"test.html\" > qwe </a> 456 </html>";
		HtmlParser parser=new HtmlParser();
		HtmlSimpleElement[] elems=parser.parse(testHtml);
		
		assertEquals(8, elems.length);
		
		assertTrue(elems[0] instanceof HtmlSimpleText);
		assertEquals("  ", ((HtmlSimpleText)elems[0]).getText());
		
		assertTrue(elems[1] instanceof HtmlSimpleTag);
		assertEquals("html", ((HtmlSimpleTag)elems[1]).getTagName());
		assertEquals(0, ((HtmlSimpleTag)elems[1]).getAttributes().size());

		assertTrue(elems[2] instanceof HtmlSimpleText);
		assertEquals(" 123 ", ((HtmlSimpleText)elems[2]).getText());

		assertTrue(elems[3] instanceof HtmlSimpleTag);
		assertEquals("a", ((HtmlSimpleTag)elems[3]).getTagName());
		assertEquals(1, ((HtmlSimpleTag)elems[3]).getAttributes().size());
		assertEquals("test.html", ((HtmlSimpleTag)elems[3]).getAttributes().get("href"));
		
		assertTrue(elems[4] instanceof HtmlSimpleText);
		assertEquals(" qwe ", ((HtmlSimpleText)elems[4]).getText());
		
		assertTrue(elems[5] instanceof HtmlSimpleTag);
		assertEquals("/a", ((HtmlSimpleTag)elems[5]).getTagName());
		assertEquals(0, ((HtmlSimpleTag)elems[5]).getAttributes().size());

		assertTrue(elems[6] instanceof HtmlSimpleText);
		assertEquals(" 456 ", ((HtmlSimpleText)elems[6]).getText());

		assertTrue(elems[7] instanceof HtmlSimpleTag);
		assertEquals("/html", ((HtmlSimpleTag)elems[7]).getTagName());
		assertEquals(0, ((HtmlSimpleTag)elems[7]).getAttributes().size());
	}
	
	@Test
	public void testParseComment() {
		String testHtml="<html><!-- 123 --></html>";
		HtmlParser parser=new HtmlParser();
		HtmlSimpleElement[] elems=parser.parse(testHtml);
		
		assertEquals(3, elems.length);
		
		assertTrue(elems[0] instanceof HtmlSimpleTag);
		assertEquals("html", ((HtmlSimpleTag)elems[0]).getTagName());
		assertEquals(0, ((HtmlSimpleTag)elems[0]).getAttributes().size());

		assertTrue(elems[1] instanceof HtmlSimpleComment);
		assertEquals(" 123 ", ((HtmlSimpleComment)elems[1]).getComment());
		
		assertTrue(elems[2] instanceof HtmlSimpleTag);
		assertEquals("/html", ((HtmlSimpleTag)elems[2]).getTagName());
		assertEquals(0, ((HtmlSimpleTag)elems[2]).getAttributes().size());
	}
	
}
