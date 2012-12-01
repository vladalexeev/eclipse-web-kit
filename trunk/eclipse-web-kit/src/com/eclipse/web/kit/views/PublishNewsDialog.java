package com.eclipse.web.kit.views;

import java.io.IOException;

import org.eclipse.core.commands.operations.OperationStatus;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.eclipse.web.kit.overlay.ProjectPropertyStore;
import com.eclipse.web.kit.util.HtmlParser;
import com.eclipse.web.kit.util.HtmlSimpleElement;
import com.eclipse.web.kit.util.HtmlSimpleTag;
import com.eclipse.web.kit.util.HtmlSimpleText;

public class PublishNewsDialog extends Dialog {

	private ProjectPropertyStore store;
	private IFile file;
	
	private Shell shell;
	
	private Label labelNewsProfile;
	private Combo comboNewsProfile;
	
	private Label labelFileName;
	private Label labelFileNameValue;
	
	private Label labelAnchor;
	private Combo comboAnchor;
	
	private Label labelTitle;
	private Combo comboTitle;

	private Label labelCategory;
	private Combo comboCategory;
	
	private Label labelText;
	private Text textText;
	
	
	private Composite bottomPanel;
	private Button buttonPublish;
	private Button buttonCancel;

	
	public PublishNewsDialog(Shell parent, ProjectPropertyStore store, IFile file) {
		super(parent);
		this.store=store;
		this.file=file;
		createControls();
		fillDialogFields();
	}
	
	private void createControls() {
		Shell parent = getParent();
		shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE);
		shell.setText("Publish news");

		GridLayout gridLayout=new GridLayout(3, false);
		gridLayout.horizontalSpacing=10;
		gridLayout.verticalSpacing=5;
		gridLayout.marginBottom=10;
		gridLayout.marginTop=10;
		gridLayout.marginLeft=10;
		gridLayout.marginRight=10;
		shell.setLayout(gridLayout);

		//News feed profile
		labelNewsProfile=new Label(shell, SWT.NONE);
		labelNewsProfile.setText("News profile");
		
		GridData comboNewsProfileGD=new GridData(SWT.FILL,SWT.TOP, true, false);
		comboNewsProfileGD.horizontalSpan=2;
		//comboNewsProfileGD.horizontalAlignment=GridData.FILL;
		comboNewsProfileGD.widthHint=400;
		comboNewsProfile=new Combo(shell, SWT.READ_ONLY);
		comboNewsProfile.setLayoutData(comboNewsProfileGD);
		
		
		//File name for news
		labelFileName=new Label(shell, SWT.NONE);
		labelFileName.setText("File");
		
		GridData labelFileNameValueGD=new GridData();
		labelFileNameValueGD.horizontalSpan=2;
		labelFileNameValueGD.horizontalAlignment=GridData.FILL;
		labelFileNameValue=new Label(shell, SWT.NONE);
		labelFileNameValue.setLayoutData(labelFileNameValueGD);
		
		//Anchor
		labelAnchor=new Label(shell, SWT.NONE);
		labelAnchor.setText("Anchor");
		
		GridData comboAnchorGD=new GridData();
		comboAnchorGD.horizontalSpan=2;
		comboAnchorGD.horizontalAlignment=GridData.FILL;
		comboAnchor=new Combo(shell, SWT.SINGLE| SWT.BORDER);
		comboAnchor.setLayoutData(comboAnchorGD);

		//Title
		labelTitle=new Label(shell, SWT.NONE);
		labelTitle.setText("Title");
		
		GridData comboTitleGD=new GridData();
		comboTitleGD.horizontalSpan=2;
		comboTitleGD.horizontalAlignment=GridData.FILL;
		comboTitle=new Combo(shell, SWT.SINGLE| SWT.BORDER);
		comboTitle.setLayoutData(comboTitleGD);

		//Category
		labelCategory=new Label(shell, SWT.NONE);
		labelCategory.setText("Category");
		
		GridData comboCategoryGD=new GridData();
		comboCategoryGD.horizontalSpan=2;
		comboCategoryGD.horizontalAlignment=GridData.FILL;
		comboCategory=new Combo(shell, SWT.SINGLE| SWT.BORDER);
		comboCategory.setLayoutData(comboCategoryGD);
		
		//Text		
		GridData labelTextGD=new GridData();
		labelTextGD.horizontalSpan=3;
		labelTextGD.horizontalAlignment=GridData.FILL;
		
		labelText=new Label(shell, SWT.NONE);
		labelText.setText("Text");
		labelText.setLayoutData(labelTextGD);
		
		GridData textTextGD=new GridData(SWT.FILL, SWT.FILL, true, true);
		textTextGD.horizontalSpan=3;
		textTextGD.horizontalAlignment=GridData.FILL;
		textTextGD.widthHint=600;
		textTextGD.heightHint=200;
		textText=new Text(shell, SWT.MULTI| SWT.BORDER | SWT.WRAP);
		textText.setLayoutData(textTextGD);
		
		
		//Bottom panel
		GridData bottomGD=new GridData();
		bottomGD.horizontalSpan=3;
		bottomGD.horizontalAlignment=SWT.RIGHT;
		bottomPanel=new Composite(shell, SWT.NONE);
		bottomPanel.setLayoutData(bottomGD);
		RowLayout bottomLayout=new RowLayout();
		bottomLayout.pack=false;
		bottomLayout.spacing=5;
		bottomLayout.marginTop=10;
		bottomLayout.marginBottom=0;
		bottomPanel.setLayout(bottomLayout);
		
		buttonPublish=new Button(bottomPanel, SWT.PUSH);
		buttonPublish.setEnabled(false);
		buttonPublish.setText("Publish");
		buttonPublish.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
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
				
		shell.setDefaultButton(buttonPublish);

		shell.pack();
		
		Monitor primary = shell.getMonitor();
		Rectangle bounds = primary.getBounds ();
		Rectangle rect = shell.getBounds ();
		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;
		shell.setLocation (x, y);
		
	}
	
	public void open () {
		shell.open();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
	}
	
	private String getDescription(HtmlSimpleElement[] elems) {
		for (HtmlSimpleElement e:elems) {
			if (e instanceof HtmlSimpleTag) {
				HtmlSimpleTag tag=(HtmlSimpleTag) e;
				if (tag.getTagName().equalsIgnoreCase("meta") && 
						tag.getAttributes().get("name")!=null && 
						tag.getAttributes().get("name").equals("description")) {
					return tag.getAttributes().get("content");
				}
			}
		}
		
		return null;
	}
	
	private String getTitle(HtmlSimpleElement[] elems) {
		String result=null;
		for (HtmlSimpleElement e:elems) {
			if (e instanceof HtmlSimpleTag) {
				HtmlSimpleTag tag=(HtmlSimpleTag) e;
				if (tag.getTagName().equalsIgnoreCase("title")) {
					result="";
				} else if (tag.getTagName().equalsIgnoreCase("/title")) {
					return result;
				}
			} else if (e instanceof HtmlSimpleText) {
				if (result!=null) {
					HtmlSimpleText text=(HtmlSimpleText)e;
					result+=text.getText();
				}
			}
		}
		
		return result;
	}
	
	private void fillDialogFields() {
		HtmlParser htmlParser=new HtmlParser();
		try {
			labelFileNameValue.setText(file.getFullPath().toString());
			
			HtmlSimpleElement[] fileElements=htmlParser.parse(file);
			
			String description=getDescription(fileElements);
			if (description!=null) {
				textText.setText(description);
			}
			
			String title=getTitle(fileElements);
			if (title!=null) {
				comboTitle.setText(title);
			}
			
		} catch (IOException e) {
			ErrorDialog.openError(shell, "Error", "Error loading file",  
					new OperationStatus(IStatus.ERROR, "eclipse-web-kit", 0, "Error loading file", e));
			throw new RuntimeException(e);
		}
	}
}
