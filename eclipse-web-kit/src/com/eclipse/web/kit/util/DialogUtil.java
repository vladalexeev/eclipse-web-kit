package com.eclipse.web.kit.util;

import org.eclipse.core.commands.operations.OperationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;

public class DialogUtil {
	public static void showWarning(String message, Throwable t) {
		ErrorDialog.openError(null, "Warning", message,  
				new OperationStatus(IStatus.WARNING, "eclipse-web-kit", 0, message, t));
	}

	public static void showWarning(String message) {
		ErrorDialog.openError(null, "Warning", message, 
				new OperationStatus(IStatus.WARNING, "eclipse-web-kit", 0, null, null));
	}
	
	public static void showError(String message, Throwable t) {
		ErrorDialog.openError(null, "Error", message,  
				new OperationStatus(IStatus.ERROR, "eclipse-web-kit", 0, message, t));
	}
	
	public static void showMessage(String title, String message) {
		MessageDialog.openInformation(
				null,
				title,
				message);	
	}
}
