package com.eclipse.web.kit.util.test;

import org.junit.Test;

import static org.junit.Assert.*;

import com.eclipse.web.kit.util.Entities;

public class TestEntities {

	@Test
	public void testUnescape() {
		String str=Entities.HTML40.unescape("Johnson &amp; Johnson");
		assertEquals("Johnson & Johnson", str);
	}
	
	@Test
	public void testEscape() {
		String str=Entities.HTML40.escape("Johnson & Johnson");
		assertEquals("Johnson &amp; Johnson", str);
	}
}
