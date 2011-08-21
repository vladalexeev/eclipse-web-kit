package com.eclipse.web.kit.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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

import com.eclipse.web.kit.util.FileUtil;

public class LinkDialog extends Dialog {
	private boolean result=false;
	private String resultText;
	private String resultHyperlink;
	
	Shell shell;
	private Label labelText;
	private Label labelHyperlink;
	private Text textText;
	private Text textHyperlink;
	
	private Composite bottomPanel;
	private Button buttonOk;
	private Button buttonCancel;
	private Button buttonBrowse;

	public LinkDialog (Shell parent, int style) {
		super (parent, style);
		createControls();
	}
	
	public LinkDialog (Shell parent) {
		this (parent, 0); // your default style bits go here (not the Shell's style bits)
		createControls();		
	}
	
	private void createControls() {
		Shell parent = getParent();
		shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText("Add link");
		
		GridLayout gridLayout=new GridLayout(3, false);
		gridLayout.horizontalSpacing=10;
		gridLayout.verticalSpacing=5;
		gridLayout.marginBottom=10;
		gridLayout.marginTop=10;
		gridLayout.marginLeft=10;
		gridLayout.marginRight=10;
		shell.setLayout(gridLayout);
		
		labelText=new Label(shell, SWT.NONE);
		labelText.setText("Text");
		
		GridData textGridData=new GridData();
		textGridData.horizontalSpan=2;
		textGridData.horizontalAlignment=GridData.FILL;
		textText=new Text(shell, SWT.SINGLE| SWT.BORDER);
		textText.setLayoutData(textGridData);
		
		labelHyperlink=new Label(shell, SWT.NONE);
		labelHyperlink.setText("Hyperlink");
		
		GridData hyperlinkGridData=new GridData();
		hyperlinkGridData.horizontalAlignment=GridData.FILL;
		hyperlinkGridData.widthHint=300;
		textHyperlink=new Text(shell, SWT.SINGLE| SWT.BORDER);
		textHyperlink.setLayoutData(hyperlinkGridData);
		
		buttonBrowse=new Button(shell, SWT.PUSH);
		buttonBrowse.setText("Browse...");
		buttonBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doSelectFile();
			}
		});

		GridData bottomGridData=new GridData();
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
		buttonOk.setEnabled(false);
		buttonOk.setText("OK");
		buttonOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result=true;
				resultText=textText.getText();
				resultHyperlink=textHyperlink.getText();
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
		
		textHyperlink.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				buttonOk.setEnabled(textHyperlink.getText()!=null && textHyperlink.getText().length()>0);
			}
		});
		
		shell.setDefaultButton(buttonOk);

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
	
	public void setText(String text) {
		textText.setText(text);
	}
	
	public String getResultText() {
		return resultText;
	}
	
	public void setHyperlink(String text) {
		textHyperlink.setText(text);
	}
	
	public String getResultHyperlink() {
		return resultHyperlink;
	}
	
	private void doSelectFile() {
		String selectedFileName=FileUtil.selectAnyFile(shell);
		if (selectedFileName!=null) {
			textHyperlink.setText(selectedFileName);
		}
	}
}
