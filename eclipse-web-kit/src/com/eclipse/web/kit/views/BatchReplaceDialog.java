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

import com.eclipse.web.kit.overlay.ProjectPropertyStore;
import com.eclipse.web.kit.preferences.PreferenceConstants;

public class BatchReplaceDialog extends Dialog {
	
	public enum RESULT {
		NO_RESULT,
		JUST_FIND,
		REPLACE
	}

	private Shell shell;
	
	private Label labelFindText;
	private Label labelReplaceText;
	private Label labelExtensions;
	
	private Text textFind;
	private Text textReplace;
	private Text textExtensions;
	
	private Button checkboxIgnoreWhitespaces;
	private Button checkboxRecursiveSearch;
	private Button checkboxIgnoreLinebreaks;
	
	private Composite bottomPanel;
	private Button buttonJustFind;
	private Button buttonReplace;
	private Button buttonCancel;
	
	private RESULT result=RESULT.NO_RESULT;
	private boolean resultIgnoreWhitespaces;
	private boolean resultRecursiveSearch;
	private boolean resultIgnoreLinebreaks;
	private String resultFindText;
	private String resultReplaceText;
	private String resultExtensions;
	
	private ProjectPropertyStore store;
	
	public BatchReplaceDialog(Shell parent, int style, ProjectPropertyStore store) {
		super(parent, style);
		this.store=store;
		createControls();
	}

	public BatchReplaceDialog(Shell parent, ProjectPropertyStore store) {
		super(parent);
		this.store=store;
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
		textFind.setText(store.getString(PreferenceConstants.P_BATCH_REPLACE_FIND_TEXT));
		textFind.setLayoutData(textFindGridData);

		labelReplaceText=new Label(shell, SWT.NONE);
		labelReplaceText.setText("Text to replace");
		//labelReplaceText.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false));
		
		GridData textReplaceGridData=new GridData(SWT.FILL, SWT.FILL, true, true);
		textReplaceGridData.widthHint=400;
		textReplaceGridData.heightHint=100;
		textReplace=new Text(shell, SWT.MULTI| SWT.BORDER);
		textReplace.setText(store.getString(PreferenceConstants.P_BATCH_REPLACE_REPLACE_TEXT));
		textReplace.setLayoutData(textReplaceGridData);
		
		labelExtensions=new Label(shell, SWT.NONE);
		labelExtensions.setText("Extensions");

		GridData textExtensionsGridData=new GridData(SWT.FILL, SWT.FILL, true, false);
		textExtensionsGridData.widthHint=400;
		textExtensions=new Text(shell, SWT.SINGLE| SWT.BORDER);
		textExtensions.setText(store.getString(PreferenceConstants.P_BATCH_REPLACE_EXTENSIONS));
		textExtensions.setLayoutData(textExtensionsGridData);
		
		checkboxIgnoreWhitespaces=new Button(shell, SWT.CHECK);
		checkboxIgnoreWhitespaces.setText("Ignore leading and trailing whitespaces while searching");
		checkboxIgnoreWhitespaces.setSelection(store.getBoolean(PreferenceConstants.P_BATCH_REPLACE_IGNORE_WHITESPACES));
		
		checkboxIgnoreLinebreaks=new Button(shell, SWT.CHECK);
		checkboxIgnoreLinebreaks.setText("Ignore line breaks while searching");
		checkboxIgnoreLinebreaks.setSelection(store.getBoolean(PreferenceConstants.P_BATCH_REPLACE_IGNORE_LINEBREAKS));
		
		checkboxRecursiveSearch=new Button(shell, SWT.CHECK);
		checkboxRecursiveSearch.setText("Recursive search");
		checkboxRecursiveSearch.setSelection(store.getBoolean(PreferenceConstants.P_BATCH_REPLACE_RECURSIVE_SEARCH));		
		
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
				result=RESULT.JUST_FIND;
				fillResultValues();
				shell.close();
			}
			
		});
		
		buttonReplace=new Button(bottomPanel, SWT.PUSH);
		//buttonOk.setEnabled(false);
		buttonReplace.setText("Replace");
		buttonReplace.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result=RESULT.REPLACE;
				fillResultValues();
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
	
	public RESULT open () {
		shell.open();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		
		return result;
	}
	
	private void fillResultValues() {
		resultIgnoreWhitespaces=checkboxIgnoreWhitespaces.getSelection();
		resultRecursiveSearch=checkboxRecursiveSearch.getSelection();
		resultIgnoreLinebreaks=checkboxIgnoreLinebreaks.getSelection();
		resultFindText=textFind.getText();
		resultReplaceText=textReplace.getText();
		resultExtensions=textExtensions.getText();
	}

	public boolean isResultIgnoreWhitespaces() {
		return resultIgnoreWhitespaces;
	}

	public boolean isResultIgnoreLinebreaks() {
		return resultIgnoreLinebreaks;
	}
	
	public boolean isResultRecursiveSearch() {
		return resultRecursiveSearch;
	}

	public String getResultFindText() {
		return resultFindText;
	}

	public String getResultReplaceText() {
		return resultReplaceText;
	}
	
	public String getResultExtensions() {
		return resultExtensions;
	}
}
