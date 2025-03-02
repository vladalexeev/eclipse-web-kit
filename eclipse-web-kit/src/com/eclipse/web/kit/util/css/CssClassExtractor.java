package com.eclipse.web.kit.util.css;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;

import com.eclipse.web.kit.util.FileLoader;
import com.eclipse.web.kit.util.html.parser.HtmlSimpleElement;

public class CssClassExtractor {

    public Set<String> extractClassNames(String cssContent) {
        Set<String> classNames = new HashSet<>();
        
        // Регулярное выражение для поиска всех классов в CSS
        String regex = "(?:^|[\\s{]|[a-zA-Z0-9_-]+)\\.([a-zA-Z_][a-zA-Z0-9_-]*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(cssContent);
        
        // Поиск всех совпадений
        while (matcher.find()) {
            String className = matcher.group(1);
            classNames.add(className);
        }
        
        return classNames;
    }
    
	public Set<String> extractClassNames(IFile file) throws IOException {
		String filePath=file.getLocation().toOSString();
		String fileContent=FileLoader.loadFile(filePath);
		return this.extractClassNames(fileContent);
	}
}
