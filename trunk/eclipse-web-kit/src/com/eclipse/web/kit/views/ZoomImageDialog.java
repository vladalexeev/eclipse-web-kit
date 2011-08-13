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

public class ZoomImageDialog extends Dialog {
	private boolean result;
	private String resultLargeImageFile;
	private String resultSmallImageFile;
	private String resultImageName;
	private String resultImageDescription;
	
	Shell shell;
	
	private Label labelLargeImageFile;
	private Label labelSmallImageFile;
	private Label labelImageName;
	private Label labelImageDescription;
	
	private Text textLargeImageFile;
	private Text textSmallImageFile;
	private Text textImageName;
	private Text textImageDescription;
	
	private Button buttonBrowseLargeImage;
	private Button buttonBrowseSmallImage;
	private Button buttonOk;
	private Button buttonCancel;
	
	private Composite bottomPanel;

	public ZoomImageDialog(Shell parent, int style) {
		super(parent, style);
		createControls();
	}

	public ZoomImageDialog(Shell parent) {
		super(parent);
		createControls();
	}

	private void createControls() {
		Shell parent = getParent();
		shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText("Add zoom image");
		
		GridLayout gridLayout=new GridLayout(3, false);
		gridLayout.horizontalSpacing=10;
		gridLayout.verticalSpacing=5;
		gridLayout.marginBottom=10;
		gridLayout.marginTop=10;
		gridLayout.marginLeft=10;
		gridLayout.marginRight=10;
		shell.setLayout(gridLayout);
		
		labelLargeImageFile=new Label(shell, SWT.NONE);
		labelLargeImageFile.setText("Large image file");

		GridData textLargeImageFileGridData=new GridData();
		textLargeImageFileGridData.widthHint=300;
		textLargeImageFile=new Text(shell, SWT.SINGLE| SWT.BORDER);
		textLargeImageFile.setLayoutData(textLargeImageFileGridData);
		
		buttonBrowseLargeImage=new Button(shell, SWT.PUSH);
		buttonBrowseLargeImage.setText("Browse...");
		buttonBrowseLargeImage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doSelectLargeFile();
			}
		});
		
		labelSmallImageFile=new Label(shell, SWT.NONE);
		labelSmallImageFile.setText("Small image file");
		
		GridData textSmallImageFileGridData=new GridData();
		textSmallImageFileGridData.widthHint=300;
		textSmallImageFile=new Text(shell, SWT.SINGLE| SWT.BORDER);
		textSmallImageFile.setLayoutData(textSmallImageFileGridData);
		
		buttonBrowseSmallImage=new Button(shell, SWT.PUSH);
		buttonBrowseSmallImage.setText("Browse...");
		buttonBrowseSmallImage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doSelectSmallFile();
			}
		});
		
		
		labelImageName=new Label(shell, SWT.NONE);
		labelImageName.setText("Image name");
		
		GridData textImageNameGridData=new GridData();
		textImageNameGridData.horizontalSpan=2;
		textImageNameGridData.horizontalAlignment=GridData.FILL;
		textImageName=new Text(shell, SWT.SINGLE| SWT.BORDER);
		textImageName.setLayoutData(textImageNameGridData);
		
		labelImageDescription=new Label(shell, SWT.NONE);
		labelImageDescription.setText("Image description");
		
		GridData textImageDescriptionGridData=new GridData();
		textImageDescriptionGridData.horizontalSpan=2;
		textImageDescriptionGridData.horizontalAlignment=GridData.FILL;
		textImageDescription=new Text(shell, SWT.SINGLE| SWT.BORDER);
		textImageDescription.setLayoutData(textImageNameGridData);
		
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
		buttonOk.setText("OK");
		buttonOk.setEnabled(false);
		buttonOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result=true;
				resultLargeImageFile=textLargeImageFile.getText();
				resultSmallImageFile=textSmallImageFile.getText();
				resultImageName=textImageName.getText();
				resultImageDescription=textImageDescription.getText();
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
		
		shell.setDefaultButton(buttonOk);
		
		textLargeImageFile.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				updateControls();
			}
		});
		
		textSmallImageFile.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				updateControls();
			}
		});

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

	public boolean isResult() {
		return result;
	}

	public String getResultLargeImageFile() {
		return resultLargeImageFile;
	}

	public String getResultSmallImageFile() {
		return resultSmallImageFile;
	}

	public String getResultImageName() {
		return resultImageName;
	}

	public String getResultImageDescription() {
		return resultImageDescription;
	}
	
	private void doSelectLargeFile() {
		String imageFileName=FileUtil.selectImageFile(shell);
		if (imageFileName==null) {
			textLargeImageFile.setText("");
		} else {
			textLargeImageFile.setText(imageFileName);
			
			String fileExt=FileUtil.getFileExt(imageFileName);
			if (fileExt!=null) {
				String trimmedPath=imageFileName.substring(0, imageFileName.length()-fileExt.length()-1);
				String smallImageFileName=trimmedPath+"-small."+fileExt;
				textSmallImageFile.setText(smallImageFileName);
			}
		}
	}
	
	private void doSelectSmallFile() {
		String imageFileName=FileUtil.selectImageFile(shell);
		if (imageFileName==null) {
			textSmallImageFile.setText("");
		} else {
			textSmallImageFile.setText(imageFileName);
		}
	}
	
	private void updateControls() {
		buttonOk.setEnabled(
				(textLargeImageFile.getText()!=null && textLargeImageFile.getText().length()>0) &&
				(textSmallImageFile.getText()!=null && textSmallImageFile.getText().length()>0));
	}
}
