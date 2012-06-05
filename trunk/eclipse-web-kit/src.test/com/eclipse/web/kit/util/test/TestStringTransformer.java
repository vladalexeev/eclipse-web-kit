package com.eclipse.web.kit.util.test;

import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.*;

import com.eclipse.web.kit.util.StringTransformer;
import com.eclipse.web.kit.util.StringTransformerOptions;

public class TestStringTransformer {

	@Test
	public void testIgnoreNewLines() {
		StringTransformerOptions options=new StringTransformerOptions();
		options.setIgnoreCarriageReturns(true);
		options.setIgnoreNewLines(true);
		
		StringTransformer tr=new StringTransformer();
		tr.setOptions(options);
		tr.setText("  abc  \r\n 123");
		
		tr.transformText();
		
		assertEquals("  abc   123", tr.getTransformedText());
		assertTrue(Arrays.equals(tr.getOriginalCharIndexes(), 
				new int[]{0,1,2,3,4,5,6,9,10,11,12}));
	}
	
	@Test
	public void testIgnoreLeadingWhitespaces() {
		StringTransformerOptions options=new StringTransformerOptions();
		options.setIgnoreLeadingWhitespaces(true);
		
		StringTransformer tr=new StringTransformer();
		tr.setOptions(options);
		tr.setText("  abc  \r\n 123");
		
		tr.transformText();
		
		assertEquals("abc  \r\n123", tr.getTransformedText());
		assertTrue(Arrays.equals(tr.getOriginalCharIndexes(), 
				new int[]{2,3,4,5,6,7,8,10,11,12}));
	}
	
	@Test
	public void testIgnoreTrailingWhitespaces() {
		StringTransformerOptions options=new StringTransformerOptions();
		options.setIgnoreTrailingwhitespaces(true);
		
		StringTransformer tr=new StringTransformer();
		tr.setOptions(options);
		tr.setText("  abc  \r\n 123 ");
		
		tr.transformText();
		
		assertEquals("  abc\r\n 123", tr.getTransformedText());
		assertTrue(Arrays.equals(tr.getOriginalCharIndexes(), 
				new int[]{0,1,2,3,4,7,8,9,10,11,12}));
	}
	
	@Test
	public void testIgnoreLeadingTrailingWhitespaces() {
		StringTransformerOptions options=new StringTransformerOptions();
		options.setIgnoreLeadingWhitespaces(true);
		options.setIgnoreTrailingwhitespaces(true);
		
		StringTransformer tr=new StringTransformer();
		tr.setOptions(options);
		tr.setText("  abc  \r\n 123 ");
		
		tr.transformText();
		
		assertEquals("abc\r\n123", tr.getTransformedText());
		assertTrue(Arrays.equals(tr.getOriginalCharIndexes(), 
				new int[]{2,3,4,7,8,10,11,12}));
	}	
	
	@Test
	public void testIgnoreLeadingTrailingWhitespaces2() {
		StringTransformerOptions options=new StringTransformerOptions();
		options.setIgnoreLeadingWhitespaces(true);
		options.setIgnoreTrailingwhitespaces(true);
		
		StringTransformer tr=new StringTransformer();
		tr.setOptions(options);
		tr.setText("test\r\n  abc  \r\n 123 ");
		
		tr.transformText();
		
		assertEquals("test\r\nabc\r\n123", tr.getTransformedText());
		assertTrue(Arrays.equals(tr.getOriginalCharIndexes(), 
				new int[]{0,1,2,3,4,5,8,9,10,13,14,16,17,18}));
	}	
	
	@Test
	public void testIgnoreLeadingTrailingWhitespacesAndNewLines() {
		StringTransformerOptions options=new StringTransformerOptions();
		options.setIgnoreLeadingWhitespaces(true);
		options.setIgnoreTrailingwhitespaces(true);
		options.setIgnoreCarriageReturns(true);
		options.setIgnoreNewLines(true);
		
		StringTransformer tr=new StringTransformer();
		tr.setOptions(options);
		tr.setText("  abc  \r\n 123 ");
		
		tr.transformText();
		
		assertEquals("abc123", tr.getTransformedText());
		assertTrue(Arrays.equals(tr.getOriginalCharIndexes(), 
				new int[]{2,3,4,10,11,12}));
	}	
}
