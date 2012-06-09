package com.eclipse.web.kit.util.test;

import org.junit.Test;

import static org.junit.Assert.*;

import com.eclipse.web.kit.util.WildcardFilter;

public class TestWildcardFileNameFilter {
	
	@Test
	public void testSimpleFileName() {
		WildcardFilter filter=new WildcardFilter("*.html");
		assertTrue(filter.accept("test.html"));
		assertFalse(filter.accept("test.doc"));
		assertFalse(filter.accept("test/index.html"));
	}

	@Test
	public void testSimplePaths() {
		WildcardFilter filter=new WildcardFilter("news/*.html");
		assertTrue(filter.accept("news/index.html"));
		assertFalse(filter.accept("archive/index.html"));
	}
	
	
	@Test
	public void testComplexPaths() {
		WildcardFilter filter=new WildcardFilter("news/*/*.html");
		assertTrue(filter.accept("news/2010/index.html"));
		assertFalse(filter.accept("news/2010/01/index.html"));
	}

}
