package com.eclipse.web.kit.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

public class BatchReplaceResultDialog extends Dialog {
	private Shell shell;
	
	private Label labelFilesProcessed;
	private Label labelFilesProcessedValue;
	private Label labelFilesFound;
	private Label labelFilesFoundValue;
	private Label labelOccurencesFound;
	private Label labelOccurencesFoundValue;
	
	private Label labelFilesChanged;
	private Label labelFilesChangedValue;
	private Label labelErrorsCount;
	private Label labelErrorsCountValue;
	
	private Composite bottomPanel;
	private Button buttonOk;

	public BatchReplaceResultDialog(Shell parent, int style) {
		super(parent, style);
		createControls();
	}

	public BatchReplaceResultDialog(Shell parent) {
		super(parent);
		createControls();
	}
	
	private void createControls() {
		Shell parent = getParent();
		shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE);
		shell.setText("Batch replace");
		
		GridLayout gridLayout=new GridLayout(2, false);
		gridLayout.horizontalSpacing=10;
		gridLayout.verticalSpacing=5;
		gridLayout.marginBottom=10;
		gridLayout.marginTop=10;
		gridLayout.marginLeft=10;
		gridLayout.marginRight=10;
		shell.setLayout(gridLayout);
		
		labelFilesProcessed=new Label(shell, SWT.NONE);
		labelFilesProcessed.setText("Files processed");
		labelFilesProcessedValue=new Label(shell, SWT.NONE);
		
		labelFilesFound=new Label(shell, SWT.NONE);
		labelFilesFound.setText("Files with searching text");
		labelFilesFoundValue=new Label(shell, SWT.NONE);
		
		labelOccurencesFound=new Label(shell, SWT.NONE);
		labelOccurencesFound.setText("Occurences found");
		labelOccurencesFoundValue=new Label(shell, SWT.NONE);
		
		labelFilesChanged=new Label(shell, SWT.NONE);
		labelFilesChanged.setText("Files changed");
		labelFilesChangedValue=new Label(shell, SWT.NONE);
		
		labelErrorsCount=new Label(shell, SWT.NONE);
		labelErrorsCount.setText("Errors");
		labelErrorsCountValue=new Label(shell, SWT.NONE);		
		
		GridData bottomGridData=new GridData(SWT.FILL, SWT.FILL, true, false);
		bottomGridData.horizontalSpan=3;
		bottomGridData.horizontalAlignment=SWT.RIGHT;
		bottomPanel=new Composite(shell, SWT.NONE);
		bottomPanel.setLayoutData(bottomGridData);
		RowLayout bottomLayout=new RowLayout();
		bottomLayout.pack=false;
		bottomLayout.spacing=5;
		bottomLayout.marginTop=10;
		bottomLayout.marginBottom=0;
		bottomPanel.setLayout(bottomLayout);
		
		buttonOk=new Button(bottomPanel, SWT.PUSH);
		buttonOk.setText("OK");
		buttonOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
			
		});
		
				
		shell.setDefaultButton(buttonOk);
	}
	
	public void open () {
		shell.pack();
		
		Monitor primary = shell.getMonitor();
		Rectangle bounds = primary.getBounds ();
		Rectangle rect = shell.getBounds ();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation (x, y);
		
		shell.open();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
	}
	
	public void setFilesProcessed(int count) {
		labelFilesProcessedValue.setText(Integer.toString(count));
	}
	
	public void setFilesFound(int count) {
		labelFilesFoundValue.setText(Integer.toString(count));
	}
	
	public void setOccurencesFound(int count) {
		labelOccurencesFoundValue.setText(Integer.toString(count));
	}
	
	public void setFilesChanged(int count) {
		labelFilesChangedValue.setText(Integer.toString(count));
	}
	
	public void setErrorsCount(int count) {
		labelErrorsCountValue.setText(Integer.toString(count));
	}
}
