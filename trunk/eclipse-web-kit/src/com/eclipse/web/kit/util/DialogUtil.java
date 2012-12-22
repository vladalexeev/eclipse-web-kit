package com.eclipse.web.kit.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.operations.OperationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;

import com.eclipse.web.kit.Activator;

public class DialogUtil {
	public static void showWarning(Throwable t) {
		showErrorStackTrace(IStatus.WARNING, "Warning", null, t);
	}

	public static void showWarning(String message, Throwable t) {
		showErrorStackTrace(IStatus.WARNING, "Warning", message, t);
	}

	public static void showWarning(String message) {
		ErrorDialog.openError(null, "Warning", message, 
				new OperationStatus(IStatus.WARNING, "eclipse-web-kit", 0, null, null));
	}
	
	public static void showError(Throwable t) {
		showErrorStackTrace(IStatus.ERROR, "Error", null, t);
	}

	public static void showError(String message, Throwable t) {
		showErrorStackTrace(IStatus.ERROR, "Error", message, t);
	}

	private static void showErrorStackTrace(int errorStatus, String title, String message, Throwable t) {
		StringWriter sw = new StringWriter();
	    PrintWriter pw = new PrintWriter(sw);
	    t.printStackTrace(pw);

	    final String trace = sw.toString(); // stack trace as a string

	    // Temp holder of child statuses
	    List<Status> childStatuses = new ArrayList<Status>();

	    // Split output by OS-independend new-line
	    for (String line : trace.split(System.getProperty("line.separator"))) {
	        // build & add status
	        childStatuses.add(new Status(errorStatus, Activator.PLUGIN_ID, line.replaceAll("\t", "    ")));
	    }

	    MultiStatus ms = new MultiStatus(Activator.PLUGIN_ID, errorStatus,
	            childStatuses.toArray(new Status[] {}), // convert to array of statuses
	            t.getLocalizedMessage(), null);

	    ErrorDialog.openError(null, title, message, ms);				
	}
	
	public static void showMessage(String title, String message) {
		MessageDialog.openInformation(
				null,
				title,
				message);	
	}
}
