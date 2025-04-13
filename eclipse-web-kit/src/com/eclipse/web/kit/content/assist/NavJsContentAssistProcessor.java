package com.eclipse.web.kit.content.assist;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

public class NavJsContentAssistProcessor implements IContentAssistProcessor {
    
    @Override
    public ICompletionProposal[] computeCompletionProposals(org.eclipse.jface.text.ITextViewer viewer, int offset) {
        // Проверяем, что файл имеет расширение .nav.js
        IFile currentFile = getCurrentFile(viewer);
        
        if (currentFile == null || !currentFile.getName().endsWith(".nav.js")) {
            return new ICompletionProposal[0];
        }
        
        String text = viewer.getDocument().get();
        int start = text.lastIndexOf("new LocalNavigationItem(\"", offset) + "new LocalNavigationItem(\"".length();
        int end = text.indexOf("\"", start);

        if (start < 0 || end < 0 || offset < start || offset > end) {
            return new ICompletionProposal[0];
        }

        String currentText = text.substring(start, offset);
        List<ICompletionProposal> proposals = new ArrayList<>();
        
        String[] files = getFilesInCurrentDirectory(currentFile);
        
        for (String fileName : files) {
            if (fileName.startsWith(currentText)) {
                // Создаем предложение, которое заменит ВЕСЬ текст до закрывающей кавычки
                proposals.add(new org.eclipse.jface.text.contentassist.CompletionProposal(
                    fileName, // Вставляем имя файла
                    start,          // Начальная позиция замены
                    end - start,    // Длина заменяемого текста (до кавычки)
                    fileName.length() + 1 // Курсор после имени файла (перед кавычкой)
                ));
            }
        }

        return proposals.toArray(new ICompletionProposal[0]);
    }
    
    private IFile getCurrentFile(org.eclipse.jface.text.ITextViewer viewer) {
    	IWorkbench workbench = PlatformUI.getWorkbench();
    	IWorkbenchPage page = workbench
                .getWorkbenchWindows()[0] // Первое окно
                .getActivePage();
    	IFile file = page.getActiveEditor().getEditorInput().getAdapter(IFile.class);
    	return file;
    }
    
    private String[] getFilesInCurrentDirectory(IFile currentFile) {
        List<String> fileNames = new ArrayList<>();
        
        try {
            IPath location = currentFile.getLocation();
            if (location != null) {
                File parentDir = location.toFile().getParentFile();
                if (parentDir != null && parentDir.exists()) {
                    File[] files = parentDir.listFiles();
                    if (files != null) {
                        for (File file : files) {
                            if (file.isFile()) {
                                fileNames.add(file.getName());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            // ignore
        }
        
        return fileNames.toArray(new String[0]);
    }

    @Override
    public IContextInformation[] computeContextInformation(org.eclipse.jface.text.ITextViewer viewer, int offset) {
        return null;
    }

    @Override
    public char[] getCompletionProposalAutoActivationCharacters() {
        return new char[] { '"' };
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