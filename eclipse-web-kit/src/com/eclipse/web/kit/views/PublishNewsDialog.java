package com.eclipse.web.kit.views;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.core.commands.operations.OperationStatus;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
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
import com.eclipse.web.kit.preferences.PreferenceConstants;
import com.eclipse.web.kit.util.html.Entities;
import com.eclipse.web.kit.util.html.parser.HtmlParser;
import com.eclipse.web.kit.util.html.parser.HtmlSimpleElement;
import com.eclipse.web.kit.util.html.parser.HtmlSimpleTag;
import com.eclipse.web.kit.util.html.parser.HtmlSimpleText;

public class PublishNewsDialog extends Dialog {

	private ProjectPropertyStore store;
	private IFile file;
	
	private Shell shell;
	
	private Label labelNewsProfile;
	private Combo comboNewsFeed;
	
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
		comboNewsFeed=new Combo(shell, SWT.READ_ONLY);
		comboNewsFeed.setLayoutData(comboNewsProfileGD);
		
		
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
		
		shell.addShellListener(new ShellListener() {
			@Override
			public void shellIconified(ShellEvent e) {
			}
			
			@Override
			public void shellDeiconified(ShellEvent e) {
			}
			
			@Override
			public void shellDeactivated(ShellEvent e) {
			}
			
			@Override
			public void shellClosed(ShellEvent e) {
				onShellClosed();
			}
			
			@Override
			public void shellActivated(ShellEvent e) {
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

			String strNewsFeeds=store.getString(PreferenceConstants.P_NEWS_FEEDS_DESCRIPTIONS);
			if (strNewsFeeds!=null) {
				String[] newsFeeds=strNewsFeeds.split("\0");
				for (String newsFeed:newsFeeds) {
					comboNewsFeed.add(newsFeed);
				}
				
				if (newsFeeds.length>0) {
					comboNewsFeed.select(0);
				}
			}
			
			HtmlSimpleElement[] fileElements=htmlParser.parse(file);
			
			String description=getDescription(fileElements);
			if (description!=null) {
				textText.setText(Entities.HTML40.unescape(description));
			}
			
			String title=getTitle(fileElements);
			if (title!=null) {
				comboTitle.setText(Entities.HTML40.unescape(title));
			}
			
			String strRecentTitles=store.getString(PreferenceConstants.P_NEWS_FEEDS_RECENT_TITLES);
			if (strRecentTitles!=null) {
				String[] recentTitles=strRecentTitles.split("\0");
				for (String t:recentTitles) {
					comboTitle.add(t);
				}
			}
			
			String strRecentCategories=store.getString(PreferenceConstants.P_NEWS_FEEDS_RECENT_CATEGORIES);
			if (strRecentCategories!=null) {
				String[] recentCategories=strRecentCategories.split("\0");
				for (String c:recentCategories) {
					comboCategory.add(c);
				}
			}
			
		} catch (IOException e) {
			ErrorDialog.openError(shell, "Error", "Error loading file",  
					new OperationStatus(IStatus.ERROR, "eclipse-web-kit", 0, "Error loading file", e));
			throw new RuntimeException(e);
		}
	}
	
	private void onShellClosed() {
		String currentTitle=comboTitle.getText();
		if (currentTitle!=null && currentTitle.trim().length()>0) {
			currentTitle=currentTitle.trim();			
			String[] recentTitles=comboTitle.getItems();
			ArrayList<String> newTitles=new ArrayList<String>(); 
			newTitles.add(currentTitle);
			for (String t:recentTitles) {
				if (!t.equals(currentTitle)) {
					newTitles.add(t);
				}
			}

			String paramTitles="";
			for (int i=0; i<newTitles.size() && i<10; i++) {
				if (i>0) {
					paramTitles+="\0";
				}
				paramTitles+=newTitles.get(i);
			}

			
			store.setValue(PreferenceConstants.P_NEWS_FEEDS_RECENT_TITLES, 
					paramTitles);
		}

		String currentCategory=comboCategory.getText();
		if (currentCategory!=null && currentCategory.trim().length()>0) {
			currentCategory=currentCategory.trim();			
			String[] recentCategories=comboCategory.getItems();
			ArrayList<String> newCategories=new ArrayList<String>(); 
			newCategories.add(currentCategory);
			for (String c:recentCategories) {
				if (!c.equals(currentCategory)) {
					newCategories.add(c);
				}
			}

			String paramCategories="";
			for (int i=0; i<newCategories.size() && i<10; i++) {
				if (i>0) {
					paramCategories+="\0";
				}
				paramCategories+=newCategories.get(i);
			}

			
			store.setValue(PreferenceConstants.P_NEWS_FEEDS_RECENT_CATEGORIES, 
					paramCategories);
		}

		try {
			store.save();
		} catch (IOException e) {
			ErrorDialog.openError(shell, "Warning", "Error saving property store",  
					new OperationStatus(IStatus.ERROR, "eclipse-web-kit", 0, "Error saving property store", e));
		}
	}
}
