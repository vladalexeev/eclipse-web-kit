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
import org.eclipse.swt.widgets.Text;

public class BatchReplaceDialog extends Dialog {

	private Shell shell;
	
	private Label labelFindText;
	private Label labelReplaceText;
	
	private Text textFind;
	private Text textReplace;
	
	private Button checkboxIgnoreWhitespaces;
	
	private Composite bottomPanel;
	private Button buttonJustFind;
	private Button buttonReplace;
	private Button buttonCancel;	
	
	private boolean result=false;
	
	public BatchReplaceDialog(Shell parent, int style) {
		super(parent, style);
		createControls();
	}

	public BatchReplaceDialog(Shell parent) {
		super(parent);
		createControls();
	}
	
	private void createControls() {
		Shell parent = getParent();
		shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE);
		shell.setText("Batch replace");
		
		GridLayout gridLayout=new GridLayout(1, false);
		gridLayout.horizontalSpacing=10;
		gridLayout.verticalSpacing=5;
		gridLayout.marginBottom=10;
		gridLayout.marginTop=10;
		gridLayout.marginLeft=10;
		gridLayout.marginRight=10;
		shell.setLayout(gridLayout);
		
		labelFindText=new Label(shell, SWT.NONE);
		labelFindText.setText("Text to find");
		//labelFindText.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false));
		
		GridData textFindGridData=new GridData(SWT.FILL, SWT.FILL, true, true);
		textFindGridData.widthHint=400;
		textFindGridData.heightHint=100;
		textFind=new Text(shell, SWT.MULTI| SWT.BORDER);
		textFind.setLayoutData(textFindGridData);

		labelReplaceText=new Label(shell, SWT.NONE);
		labelReplaceText.setText("Text to replace");
		//labelReplaceText.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false));
		
		GridData textReplaceGridData=new GridData(SWT.FILL, SWT.FILL, true, true);
		textReplaceGridData.widthHint=400;
		textReplaceGridData.heightHint=100;
		textReplace=new Text(shell, SWT.MULTI| SWT.BORDER);
		textReplace.setLayoutData(textReplaceGridData);
		
		checkboxIgnoreWhitespaces=new Button(shell, SWT.CHECK);
		checkboxIgnoreWhitespaces.setText("Ignore leading and trailing whitespaces");
		
		
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
		
		buttonJustFind=new Button(bottomPanel, SWT.PUSH);
		//buttonOk.setEnabled(false);
		buttonJustFind.setText("Just find");
		buttonJustFind.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result=true;
				shell.close();
			}
			
		});
		
		buttonReplace=new Button(bottomPanel, SWT.PUSH);
		//buttonOk.setEnabled(false);
		buttonReplace.setText("Replace");
		buttonReplace.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result=true;
				shell.close();
			}
			
		});
		
		
		buttonCancel=new Button(bottomPanel, SWT.PUSH);
		buttonCancel.setText("Cancel");
		buttonCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
				
		shell.setDefaultButton(buttonJustFind);

		shell.pack();
		
		Monitor primary = shell.getMonitor();
		Rectangle bounds = primary.getBounds ();
		Rectangle rect = shell.getBounds ();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation (x, y);
	}
	
	public boolean open () {
		shell.open();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		
		return result;
	}	
}
