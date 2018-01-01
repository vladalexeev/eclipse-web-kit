package com.eclipse.web.kit.views;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
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

import com.eclipse.web.kit.newsfeed.DefaultCategory;
import com.eclipse.web.kit.newsfeed.NewsFeed;
import com.eclipse.web.kit.overlay.ProjectPropertyStore;
import com.eclipse.web.kit.preferences.PreferenceConstants;
import com.eclipse.web.kit.util.DialogUtil;
import com.eclipse.web.kit.util.FileLoader;
import com.eclipse.web.kit.util.FileUtil;
import com.eclipse.web.kit.util.SwtUtil;
import com.eclipse.web.kit.util.html.Entities;
import com.eclipse.web.kit.util.html.parser.HtmlParser;
import com.eclipse.web.kit.util.html.parser.HtmlSimpleElement;
import com.eclipse.web.kit.util.html.parser.HtmlSimpleTag;
import com.eclipse.web.kit.util.html.parser.HtmlSimpleText;

public class PublishNewsDialog extends Dialog {

	private ProjectPropertyStore store;
	private IFile file;
	private List<NewsFeed> feeds;
	
	private Shell shell;
	
	private Label labelNewsFeed;
	private Combo comboNewsFeed;
	
	private Label labelFileName;
	private Label labelFileNameValue;
	
	private Label labelAnchor;
	private Combo comboAnchor;
	
	private Label labelTitle;
	private Combo comboTitle;
	
	private Label labelImage;
	private Combo comboImage;

	private Label labelCategory;
	private Combo comboCategory;
	
	private Label labelText;
	private Text textText;
	
	
	private Composite bottomPanel;
	private Button buttonPublish;
	private Button buttonCancel;
	
	private boolean result=false;
	private String resultNewsFeed;
	private String resultAnchor;
	private String resultTitle;
	private String resultCategory;
	private String resultText;
	private String resultImage;
	
	public PublishNewsDialog(Shell parent, ProjectPropertyStore store, IFile file, List<NewsFeed> feeds) {
		super(parent);
		this.store=store;
		this.file=file;
		this.feeds=feeds;
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
		labelNewsFeed=new Label(shell, SWT.NONE);
		labelNewsFeed.setText("News profile");
		
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
		
		//Image
		labelImage=new Label(shell, SWT.NONE);
		labelImage.setText("Image");
		
		GridData comboImageGD=new GridData();
		comboImageGD.horizontalSpan=2;
		comboImageGD.horizontalAlignment=GridData.FILL;
		comboImage=new Combo(shell, SWT.SINGLE| SWT.BORDER);
		comboImage.setLayoutData(comboImageGD);

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
		//buttonPublish.setEnabled(false);
		buttonPublish.setText("Publish");
		buttonPublish.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result=true;
				resultNewsFeed=comboNewsFeed.getText();
				resultAnchor=comboAnchor.getText();
				resultTitle=comboTitle.getText();
				resultCategory=comboCategory.getText();
				resultText=textText.getText();
				resultImage=comboImage.getText();
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
	
	private String extractValueByPatters(String fileContent, String[] patterns) {
		for (String pattern:patterns) {
			pattern=pattern.trim();
			int pIndex=pattern.indexOf("{{}}");
			if (pIndex<=0) {
				continue;
			}
			
			String patternBegin=pattern.substring(0, pIndex);
			String patternEnd=pattern.substring(pIndex+4);
			if (patternEnd.length()==0) {
				continue;
			}

			int indexBegin=fileContent.indexOf(patternBegin);
			if (indexBegin<0) {
				continue;
			}
			
			int indexEnd=fileContent.indexOf(patternEnd, indexBegin+patternBegin.length()-1);
			if (indexEnd<0) {
				continue;
			}
			
			String value=fileContent.substring(indexBegin+patternBegin.length(), indexEnd);
			if (value.length()>0) {
				return value;
			}
		}
		
		return null;
	}
	
	private String getDescription(HtmlSimpleElement[] elems, String fileContent) {
		String str=store.getString(PreferenceConstants.P_NEWS_FEEDS_DESCRIPTION_PATTERNS);
		String[] descriptionPatters=new String[]{};
		if (str!=null) {
			descriptionPatters=str.split("\0");
		}
		
		String patternValue=extractValueByPatters(fileContent, descriptionPatters);
		if (patternValue!=null) {
			return patternValue;
		}
		
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
	
	private String getTitle(HtmlSimpleElement[] elems, String fileContent) {
		String str=store.getString(PreferenceConstants.P_NEWS_FEEDS_TITLE_PATTERNS);
		String[] titlePatters=new String[]{};
		if (str!=null) {
			titlePatters=str.split("\0");
		}
		
		String patternValue=extractValueByPatters(fileContent, titlePatters);
		if (patternValue!=null) {
			return patternValue;
		}		
		
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
	
	private String[] getImages(HtmlSimpleElement[] elems, String fileContent) {
		ArrayList<String> imageFiles=new ArrayList<String>();
		
		String str=store.getString(PreferenceConstants.P_NEWS_FEEDS_IMAGE_PATTERNS);
		String[] imagePatterns=new String[]{};
		if (str!=null) {
			imagePatterns=str.split("\0");
		}
		
		String patternValue=extractValueByPatters(fileContent, imagePatterns);
		if (patternValue!=null) {
			imageFiles.add(patternValue);
		}
		
		for (HtmlSimpleElement e:elems) {
			if (e instanceof HtmlSimpleTag) {
				HtmlSimpleTag tag=(HtmlSimpleTag) e;
				if (tag.getTagName().equalsIgnoreCase("img")) {
					String srcAttr=tag.getAttribute("src");
					if (srcAttr!=null) {
						imageFiles.add(srcAttr);
					}
				} 
			} 
		}
		
		ArrayList<String> result=new ArrayList<String>();
		IProject project=SwtUtil.getActiveProject();
		String projectPath=project.getLocation().toString();
		for (String imageFile:imageFiles) {
			result.add(FileUtil.createSiteAbsolutePath(projectPath, file.getParent().getLocation().toString(), imageFile));
		}
		
		
		return result.toArray(new String[result.size()]);
	}
	
	private void fillDialogFields() {
		HtmlParser htmlParser=new HtmlParser();
		try {
			labelFileNameValue.setText(file.getFullPath().toString());

			for (int i=0; i<feeds.size(); i++) {
				NewsFeed f=feeds.get(i);
				comboNewsFeed.add(f.getFeedFileName());
				
				if (f.getDefaultFolder()!=null) {
					IFolder folder=file.getProject().getFolder(f.getDefaultFolder());
					if (file.getLocation().toOSString().startsWith(folder.getLocation().toOSString())) {
						comboNewsFeed.select(i);
					}
				}
			}
			
			if (comboNewsFeed.getSelectionIndex()<0) {
				comboNewsFeed.select(0);
			}
			
			NewsFeed newsFeed=feeds.get(comboNewsFeed.getSelectionIndex());
			
			String fileContent=FileLoader.loadFile(file.getLocation().toOSString());
						
			HtmlSimpleElement[] fileElements=htmlParser.parse(fileContent);
			
			String description=getDescription(fileElements, fileContent);
			if (description!=null) {
				textText.setText(Entities.HTML40.unescape(Entities.HTML40.unescape(description)));
			}
			
			String title=getTitle(fileElements, fileContent);
			if (title!=null) {
				comboTitle.setText(Entities.HTML40.unescape(Entities.HTML40.unescape(title)));
			}
			
			String strRecentTitles=store.getString(PreferenceConstants.P_NEWS_FEEDS_RECENT_TITLES);
			if (strRecentTitles!=null) {
				String[] recentTitles=strRecentTitles.split("\0");
				for (String t:recentTitles) {
					comboTitle.add(t);
				}
			}
			
			String[] imageFiles=getImages(fileElements, fileContent);
			if (imageFiles!=null && imageFiles.length>0) {
				comboImage.setText(Entities.HTML40.unescape(imageFiles[0]));
				
				for (String imageFile:imageFiles) {
					comboImage.add(imageFile);
				}
			}
			
			String strRecentCategories=store.getString(PreferenceConstants.P_NEWS_FEEDS_RECENT_CATEGORIES);
			if (strRecentCategories!=null) {
				String[] recentCategories=strRecentCategories.split("\0");
				for (String c:recentCategories) {
					comboCategory.add(c);
				}
			}
			
			for (DefaultCategory dc:newsFeed.getDefaultCategories()) {
				String filePath=file.getProjectRelativePath().toPortableString();
				if (filePath.startsWith(dc.getPath())) {
					comboCategory.setText(dc.getCategoryName());
				}
			}			
			
			for (HtmlSimpleElement elem:fileElements) {
				if (elem instanceof HtmlSimpleTag) {
					HtmlSimpleTag tag=(HtmlSimpleTag) elem;
					if (tag.getTagName().equals("a") && tag.getAttribute("name")!=null) {
						comboAnchor.add(tag.getAttribute("name"));
					}
				}
			}
			
		} catch (IOException e) {
			DialogUtil.showError("Error loading file", e);
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
			DialogUtil.showError("Error saving property store", e);
		}
	}
	
	public boolean hasResult() {
		return result;
	}

	public String getResultNewsFeed() {
		return resultNewsFeed;
	}

	public String getResultAnchor() {
		return resultAnchor;
	}

	public String getResultTitle() {
		return resultTitle;
	}

	public String getResultCategory() {
		return resultCategory;
	}

	public String getResultText() {
		return resultText;
	}
	
	public String getResultImage() {
		return resultImage;
	}
}
