package com.eclipse.web.kit.views;


import java.util.HashMap;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.*;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.swt.SWT;

import com.eclipse.web.kit.Activator;
import com.eclipse.web.kit.preferences.PreferenceConstants;
import com.eclipse.web.kit.util.FileUtil;
import com.eclipse.web.kit.util.ImageInfo;
import com.eclipse.web.kit.util.ImageUtil;
import com.eclipse.web.kit.util.TemplateUtil;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class HtmlPaletteView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.eclipse.web.kit.views.HtmlPaletteView";
	
	public static final String ITEM_LINK="Link";
	public static final String ITEM_IMAGE="Image";
	public static final String ITEM_ZOOM_IMAGE="Zoom image";
	
	public static final String[][] ITEM_IMAGES=new String[][]{
		{ITEM_LINK, "icons/hyperlink.png"},
		{ITEM_IMAGE, "icons/picture.png"},
		{ITEM_ZOOM_IMAGE, "icons/zoom-picture.png"}
	};

	private TableViewer viewer;
	private Action clickAction;

	/*
	 * The content provider class is responsible for
	 * providing objects to the view. It can wrap
	 * existing objects in adapters or simply return
	 * objects as-is. These objects may be sensitive
	 * to the current input of the view, or ignore
	 * it and always show the same content 
	 * (like Task List, for example).
	 */
	 
	class ViewContentProvider implements IStructuredContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		public Object[] getElements(Object parent) {
			return new String[] {ITEM_LINK, ITEM_IMAGE, ITEM_ZOOM_IMAGE };
		}
	}
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}
		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}
		public Image getImage(Object obj) {
			String imageName=null;
			for (int i=0; i<ITEM_IMAGES.length; i++) {
				if (ITEM_IMAGES[i][0].equals(obj)) {
					imageName=ITEM_IMAGES[i][1];
				}
			}
			
			if (imageName!=null) {
				return Activator.getImageDescriptor(imageName).createImage();
			} else {
				return PlatformUI.getWorkbench().
						getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
			}
		}
	}
	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	public HtmlPaletteView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		//viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "eclipse-web-kit.viewer");
		makeActions();
		hookDoubleClickAction();
	}


	private void makeActions() {
		clickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				String item = (String) ((IStructuredSelection)selection).getFirstElement();
				
				IEditorPart editor=getActiveEditor();				
				if (editor!=null && editor instanceof ITextEditor) {				
					if (item.equals(ITEM_LINK)) {
						doActionLink();
					} else if (item.equals(ITEM_IMAGE)) {
						doActionImage();
					} else if (item.equals(ITEM_ZOOM_IMAGE)) {
						doActionZoomImage();
					}
				}
			}
		};
	}

	private void hookDoubleClickAction() {
		viewer.getControl().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				TableItem selectedTableItem=((Table)viewer.getControl()).getItem(new Point(e.x, e.y));
				if (selectedTableItem!=null) {
					clickAction.run();
				}
			}
		});
	}
	
	protected void showMessage(String message) {
		MessageDialog.openInformation(
			viewer.getControl().getShell(),
			"Html palette",
			message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	private IWorkbenchWindow getActiveWorkbenchWindow() {
		if (PlatformUI.getWorkbench()!=null) {
			return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		}
		
		return null;
	}
	
	private IEditorPart getActiveEditor() {
		if (getActiveWorkbenchWindow()!=null) {
			if (getActiveWorkbenchWindow().getActivePage()!=null) {
				return getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			}
		}
		
		return null;
	}
	
	private String getActiveEditorFileName() {
		IEditorInput editorInput = getActiveEditor().getEditorInput();
		String documentFileName=null;
		if (editorInput instanceof IFileEditorInput) {
			IFileEditorInput fileEditorInput=(IFileEditorInput) editorInput;
			documentFileName=fileEditorInput.getFile().getLocation().toString();
		}
		
		return documentFileName;
	}
	
	private String getActiveSelectionText() {
		IEditorPart editor=getActiveEditor();
		
		if (editor!=null && editor instanceof ITextEditor) {
			ITextEditor textEditor = (ITextEditor) editor;
	        ITextSelection sel = (ITextSelection) textEditor.getSelectionProvider().getSelection();
	        return sel.getText();
		}
		
		return null;
	}
	
	private void addTextToActiveEditor(String text) {
		IEditorPart editor=getActiveEditor();
		
		if (editor!=null && editor instanceof ITextEditor) {
			IEditorInput editorInput = editor.getEditorInput();
			ITextEditor textEditor = (ITextEditor) editor;
	        IDocument doc = textEditor.getDocumentProvider().getDocument(editorInput);
	        ITextSelection sel = (ITextSelection) textEditor.getSelectionProvider().getSelection();
	        
	        try {
        		doc.replace(sel.getOffset(), sel.getLength(), text);
        	} catch (BadLocationException e) {
        		e.printStackTrace();
        	}

	        TextSelection newSelection=new TextSelection(sel.getOffset()+text.length(), 0);
	        textEditor.getSelectionProvider().setSelection(newSelection);
	        
	        getActiveWorkbenchWindow().getActivePage().getActiveEditor().setFocus();
		}
	}
	
	

	
	private void doActionLink() {
		LinkDialog dialog=new LinkDialog(getActiveWorkbenchWindow().getShell());
		
		String selectionText=getActiveSelectionText();
		dialog.setText(selectionText);
		
		if (selectionText!=null && selectionText.startsWith("http://")) {
			dialog.setHyperlink(selectionText);
		}
		
		if (dialog.open()) {
			String resultText=dialog.getResultText();
			String resultHyperlink=dialog.getResultHyperlink();
			
			if (!resultHyperlink.startsWith("http://")) {
				String documentFileName=getActiveEditorFileName();
				resultHyperlink=FileUtil.createRelativePath(documentFileName, resultHyperlink);
			}
			
			if (resultText==null || resultText.length()==0) {
				resultText=resultHyperlink;
			}
			
			String template=Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_TEMPLATE_LINK);
			
			HashMap<String,String> params=new HashMap<String, String>();
			params.put("hyperlink", resultHyperlink);
			params.put("text", resultText);
			
			String text=TemplateUtil.applyParametersToString(template, params);
			
			addTextToActiveEditor(text);
		}
	}
	
	private void doActionImage() {
		String selectedImageFileName=FileUtil.selectImageFile(getActiveWorkbenchWindow().getShell());
		
		if (selectedImageFileName==null) {
			return;
		}

		String documentFileName=getActiveEditorFileName();
		String imageRelativePath=FileUtil.createRelativePath(documentFileName, selectedImageFileName);
		
		ImageInfo imageInfo=ImageUtil.getImageInfo(selectedImageFileName);
		
		String template=Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_TEMPLATE_IMAGE);
		
		HashMap<String, String> params=new HashMap<String, String>();
		params.put("imageFile", imageRelativePath);
		params.put("imageWidth", Integer.toString(imageInfo.getImageWidth()));
		params.put("imageHeight", Integer.toString(imageInfo.getImageHeight()));
		
		String text=TemplateUtil.applyParametersToString(template, params);
		addTextToActiveEditor(text);
	}
	
	private void doActionZoomImage() {
		ZoomImageDialog dialog=new ZoomImageDialog(getActiveWorkbenchWindow().getShell());
		
		if (dialog.open()) {
			String documentFileName=getActiveEditorFileName();
			String absoluteLargeImageFile=dialog.getResultLargeImageFile();
			String relativeLargeImageFile=FileUtil.createRelativePath(documentFileName, absoluteLargeImageFile);
			String largeImageFileName=FileUtil.getFileNameWithoutExt(absoluteLargeImageFile);
			String absoluteSmallImageFile=dialog.getResultSmallImageFile();
			String relativeSmallImageFile=FileUtil.createRelativePath(documentFileName, absoluteSmallImageFile);
			String imageName=dialog.getResultImageName();
			String imageDescription=dialog.getResultImageDescription();
			
			ImageInfo imageInfoLarge=ImageUtil.getImageInfo(absoluteLargeImageFile);
			ImageInfo imageInfoSmall=ImageUtil.getImageInfo(absoluteSmallImageFile);
			
			String template=Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_TEMPLATE_ZOOM_IMAGE);
			
			HashMap<String, String> params=new HashMap<String, String>();
			params.put("largeImageFileName", largeImageFileName);
			params.put("largeImageFile", relativeLargeImageFile);
			params.put("largeImageWidth", Integer.toString(imageInfoLarge.getImageWidth()));
			params.put("largeImageHeight", Integer.toString(imageInfoLarge.getImageHeight()));
			params.put("smallImageFile", relativeSmallImageFile);
			params.put("smallImageWidth", Integer.toString(imageInfoSmall.getImageWidth()));
			params.put("smallImageHeight", Integer.toString(imageInfoSmall.getImageHeight()));
			params.put("name", imageName);
			params.put("description", imageDescription);
			
			String text=TemplateUtil.applyParametersToString(template, params);
			
			addTextToActiveEditor(text);
		}
	}
	
	
}