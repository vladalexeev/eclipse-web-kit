package com.eclipse.web.kit.content.assist;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

import com.eclipse.web.kit.Activator;
import com.eclipse.web.kit.overlay.ProjectPropertyStore;
import com.eclipse.web.kit.preferences.PreferenceConstants;
import com.eclipse.web.kit.util.css.CssClassExtractor;

public class CssClassContentAssistProcessor implements IContentAssistProcessor {
    
    private IProject getCurrentProject() {
        // Получаем текущий проект (например, из активного редактора)
        // Это может потребовать интеграции с API Eclipse для получения текущего контекста.
        return ResourcesPlugin.getWorkspace().getRoot().getProjects()[0]; 
    }
    
    private Set<String> getCssClassesFromProject() {
    	IProject project = this.getCurrentProject();
		ProjectPropertyStore store=new ProjectPropertyStore(
				project, 
				Activator.getDefault().getPreferenceStore(), 
				PreferenceConstants.PAGE_ID_CONTENT_ASSIST_CSS
		);
		String prop = store.getString(PreferenceConstants.P_CSS_CLASSES);
		HashSet<String> result = new HashSet<String>();
		if (prop != null) {
			String[] classes = prop.split("\0");
			for (String c: classes) {
				result.add(c);
			}
		}
		
		return result;
    }
    
    private Set<String> getCssClassesFromProjectFiles() {
    	IProject project = this.getCurrentProject();
		ProjectPropertyStore store=new ProjectPropertyStore(
				project, 
				Activator.getDefault().getPreferenceStore(), 
				PreferenceConstants.PAGE_ID_CONTENT_ASSIST_CSS
		);
		String prop = store.getString(PreferenceConstants.P_CSS_FILES);
		HashSet<String> result = new HashSet<String>();
		
		CssClassExtractor extractor = new CssClassExtractor();
		
		if (prop != null) {
			String[] fileNames = prop.split("\0");
			for (String fileName: fileNames) {
				IFile file = project.getFile(fileName);
				try {
					Set<String> fileClassNames = extractor.extractClassNames(file);
					result.addAll(fileClassNames);
				} catch (IOException e) {
					
				}
			}
		}
		
		return result;
    	
    }

    @Override
    public ICompletionProposal[] computeCompletionProposals(org.eclipse.jface.text.ITextViewer viewer, int offset) {
        String text = viewer.getDocument().get();
        int start = text.lastIndexOf("class=\"", offset) + 7;
        int end = text.indexOf("\"", start);

        if (start < 0 || end < 0 || offset < start || offset > end) {
            return new ICompletionProposal[0];
        }

        String currentText = text.substring(start, offset);
        java.util.List<ICompletionProposal> proposals = new java.util.ArrayList<>();
        
        Set<String> projectCssClasses = this.getCssClassesFromProject();
        Set<String> fileCssClasses = this.getCssClassesFromProjectFiles();

        HashSet<String> allClasses = new HashSet<String>();
        allClasses.addAll(projectCssClasses);
        allClasses.addAll(fileCssClasses);
        
        for (String cssClass : allClasses) {
            if (cssClass.startsWith(currentText)) {
                proposals.add(new org.eclipse.jface.text.contentassist.CompletionProposal(
                    cssClass, start, offset - start, cssClass.length()));
            }
        }

        return proposals.toArray(new ICompletionProposal[0]);
    }

    @Override
    public IContextInformation[] computeContextInformation(org.eclipse.jface.text.ITextViewer viewer, int offset) {
        return null;
    }

    @Override
    public char[] getCompletionProposalAutoActivationCharacters() {
        return new char[] { ' ' };
    }

    @Override
    public char[] getContextInformationAutoActivationCharacters() {
        return null;
    }

    @Override
    public String getErrorMessage() {
        return null;
    }

    @Override
    public IContextInformationValidator getContextInformationValidator() {
        return null;
    }
}