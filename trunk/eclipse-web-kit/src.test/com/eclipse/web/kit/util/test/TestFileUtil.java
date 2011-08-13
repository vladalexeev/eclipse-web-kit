package com.eclipse.web.kit.util.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.eclipse.web.kit.util.FileUtil;

public class TestFileUtil {

	@Test
	public void testCreateRelativePath() {
		assertEquals("test.html", FileUtil.createRelativePath("C:/aaa/bbb/index.html", "C:/aaa/bbb/test.html"));
		assertEquals("../ccc/test.html", FileUtil.createRelativePath("C:/aaa/bbb/index.html", "C:/aaa/ccc/test.html"));
		assertEquals("../../ddd/eee/test.html", FileUtil.createRelativePath("C:/aaa/bbb/index.html", "C:/ddd/eee/test.html"));
		assertEquals("ccc/test.html", FileUtil.createRelativePath("C:/aaa/bbb/index.html", "C:/aaa/bbb/ccc/test.html"));
	}
	
	@Test
	public void testCreateAbsolutePath() {
		assertEquals("C:/aaa/bbb/test.html", FileUtil.createAbsolutePath("C:/aaa/bbb", "test.html"));
		assertEquals("C:/aaa/bbb/ccc/test.html", FileUtil.createAbsolutePath("C:/aaa/bbb", "ccc/test.html"));
		assertEquals("C:/aaa/ccc/test.html", FileUtil.createAbsolutePath("C:/aaa/bbb", "../ccc/test.html"));
		assertEquals("C:/ddd/eee/test.html", FileUtil.createAbsolutePath("C:/aaa/bbb", "../../ddd/eee/test.html"));
	}
}
