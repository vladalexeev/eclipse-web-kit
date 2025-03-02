package com.eclipse.web.kit.util.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import com.eclipse.web.kit.util.css.CssClassExtractor;

public class TestCssClassExtractor {


    @Test
    public void testSingleClass() {
        CssClassExtractor extractor = new CssClassExtractor();
        String cssContent = ".my-class { color: red; }";
        Set<String> classes = extractor.extractClassNames(cssContent);
        
        assertEquals(1, classes.size());
        assertTrue(classes.contains("my-class"));
    }

    @Test
    public void testMultipleClasses() {
        CssClassExtractor extractor = new CssClassExtractor();
        String cssContent = ".class1 .class2 { color: red; } .class3 { font-size: 12px; }";
        Set<String> classes = extractor.extractClassNames(cssContent);
        
        assertEquals(3, classes.size());
        assertTrue(classes.contains("class1"));
        assertTrue(classes.contains("class2"));
        assertTrue(classes.contains("class3"));
    }

    @Test
    public void testClassWithTag() {
        CssClassExtractor extractor = new CssClassExtractor();
        String cssContent = ".class2 { color: red; } div.class1 { font-size: 12px; }";
        Set<String> classes = extractor.extractClassNames(cssContent);
        
        assertEquals(2, classes.size());
        assertTrue(classes.contains("class1"));
        assertTrue(classes.contains("class2"));
    }

    @Test
    public void testDuplicateClasses() {
        CssClassExtractor extractor = new CssClassExtractor();
        String cssContent = ".class1 { color: red; } .class1 { font-size: 12px; }";
        Set<String> classes = extractor.extractClassNames(cssContent);
        
        assertEquals(1, classes.size());
        assertTrue(classes.contains("class1"));
    }

    @Test
    public void testComplexCase() {
        CssClassExtractor extractor = new CssClassExtractor();
        String cssContent = "div.class1 .class2 { color: red; } #id .class3 { margin: 0; } .class1 { font-size: 12px; }";
        Set<String> classes = extractor.extractClassNames(cssContent);
        
        assertEquals(3, classes.size());
        assertTrue(classes.contains("class1"));
        assertTrue(classes.contains("class2"));
        assertTrue(classes.contains("class3"));
    }

    @Test
    public void testEmptyInput() {
        CssClassExtractor extractor = new CssClassExtractor();
        String cssContent = "";
        Set<String> classes = extractor.extractClassNames(cssContent);
        
        assertTrue(classes.isEmpty());
    }
}
