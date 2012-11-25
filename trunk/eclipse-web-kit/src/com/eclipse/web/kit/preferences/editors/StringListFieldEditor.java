package com.eclipse.web.kit.preferences.editors;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;

public class StringListFieldEditor extends FieldEditor {

	/***
	 * default separator for list items.
	 */
	private static final String DEFAULT_SEPARATOR = "\0"; 

	/***
	 * vertical dialog units per char.
	 */
	private static final int VERTICAL_DIALOG_UNITS_PER_CHAR = 4;

	/***
	 * list height.
	 */
	private static final int LIST_HEIGHT_IN_CHARS = 10;

	/***
	 * list height in units.
	 */
	private static final int LIST_HEIGHT_IN_DLUS = LIST_HEIGHT_IN_CHARS * VERTICAL_DIALOG_UNITS_PER_CHAR;

	/***
	 * The list of items.
	 */
	List list;

	/***
	 * The top-level control for the field editor.
	 */
	private Composite parent;
	
	/**
	 * The main composite element of the field
	 */
	private Composite main;

	/***
	 * The button for adding the contents of the text field to the list.
	 */
	private Button buttonAdd;

	/***
	 * The button for removing the currently-selected list item.
	 */
	private Button buttonRemove;
	
	/**
	 * the button for changing the currently-selected list item.
	 */
	private Button buttonEdit;

	/***
	 * The string used to separate list items in a single String representation.
	 */
	private String separator = DEFAULT_SEPARATOR;

	private IInputValidator validator=null;
	
	private AddActionHandler addActionHandler=null;
	
	private boolean enabled=true;

	/***
	 * Creates a string field editor of unlimited width. Use the method <code>setTextLimit</code> to limit the text.
	 * @param name the name of the preference this field editor works on
	 * @param labelText the label text of the field editor
	 * @param parent the parent of the field editor's control
	 */
	public StringListFieldEditor(String name, String labelText, Composite parent) {
		super(name, labelText, parent);
	}

	/***
	 * Creates a string field editor of unlimited width. Use the method <code>setTextLimit</code> to limit the text.
	 * @param name the name of the preference this field editor works on
	 * @param labelText the label text of the field editor
	 * @param addButtonText text for the "add" button
	 * @param removeButtonText text for the "remove" buttom
	 * @param parent the parent of the field editor's control
	 */
	public StringListFieldEditor(String name, String labelText, String addButtonText, String removeButtonText, Composite parent) {
		super(name, labelText, parent);
		setAddButtonText(addButtonText);
		setRemoveButtonText(removeButtonText);
	}



	public IInputValidator getValidator() {
		return validator;
	}

	public void setValidator(IInputValidator validator) {
		this.validator = validator;
	}

	/***
	 * @see org.eclipse.jface.preference.FieldEditor#adjustForNumColumns(int)
	 */
	protected void adjustForNumColumns(int numColumns) {
		((GridData) this.parent.getLayoutData()).horizontalSpan = numColumns;
	}

	/***
	 * @see org.eclipse.jface.preference.FieldEditor#doFillIntoGrid (Composite, int)
	 */
	protected void doFillIntoGrid(Composite parent, int numColumns) {
		this.parent = parent;

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = numColumns;
		this.parent.setLayoutData(gd);

		Label label = getLabelControl(this.parent);
		GridData labelData = new GridData();
		labelData.horizontalSpan = numColumns;
		label.setLayoutData(labelData);
		
		main=new Composite(parent, SWT.NONE);
		GridLayout mainLayout=new GridLayout(2, false);
		main.setLayout(mainLayout);
		
		GridData mainData=new GridData(GridData.FILL_HORIZONTAL);
		mainData.horizontalSpan=numColumns;
		main.setLayoutData(mainData);
		

		this.list = new List(main, SWT.BORDER);

		// Create a grid data that takes up the extra space in the dialog and spans both columns.
		GridData listData = new GridData(GridData.FILL_HORIZONTAL);
		listData.heightHint = convertVerticalDLUsToPixels(this.list, LIST_HEIGHT_IN_DLUS);
		//listData.horizontalSpan = numColumns;

		this.list.setLayoutData(listData);
		this.list.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				selectionChanged();
			}
		});

		// Create a composite for the add and remove buttons and the input text field.
		Composite addRemoveGroup = new Composite(main, SWT.NONE);

		GridData addRemoveData = new GridData();//GridData.FILL_VERTICAL);
		addRemoveData.verticalAlignment=SWT.TOP;
		//addRemoveData.horizontalSpan = numColumns;
		addRemoveGroup.setLayoutData(addRemoveData);

		GridLayout addRemoveLayout = new GridLayout();
		addRemoveLayout.numColumns = 1;
		addRemoveLayout.marginHeight = 0;
		addRemoveLayout.marginWidth = 0;
		addRemoveGroup.setLayout(addRemoveLayout);

		// Create a composite for the add and remove buttons.
		Composite buttonGroup = new Composite(addRemoveGroup, SWT.NONE);
		buttonGroup.setLayoutData(new GridData());

		GridLayout buttonLayout = new GridLayout();
		buttonLayout.marginHeight = 0;
		buttonLayout.marginWidth = 0;
		buttonGroup.setLayout(buttonLayout);

		// Create the add button.
		this.buttonAdd = new Button(buttonGroup, SWT.NONE);
		this.buttonAdd.setText("Add..."); 
		this.buttonAdd.addSelectionListener(new SelectionAdapter() { 
			public void widgetSelected(SelectionEvent e) {
				doAdd();
			}
		});

		GridData addData = new GridData(GridData.FILL_HORIZONTAL);
		addData.widthHint = convertHorizontalDLUsToPixels(this.buttonAdd, IDialogConstants.BUTTON_WIDTH);
		this.buttonAdd.setLayoutData(addData);
		
		// Create the edit button.
		this.buttonEdit = new Button(buttonGroup, SWT.NONE);
		this.buttonEdit.setEnabled(false);
		this.buttonEdit.setText("Edit..."); 
		this.buttonEdit.addSelectionListener(new SelectionAdapter() { 
			public void widgetSelected(SelectionEvent e) {
				doEdit();
			}
		});

		GridData editData = new GridData(GridData.FILL_HORIZONTAL);
		editData.widthHint = convertHorizontalDLUsToPixels(this.buttonAdd, IDialogConstants.BUTTON_WIDTH);
		this.buttonEdit.setLayoutData(editData);

		// Create the remove button.
		this.buttonRemove = new Button(buttonGroup, SWT.NONE);
		this.buttonRemove.setEnabled(false);
		this.buttonRemove.setText("Remove");
		this.buttonRemove.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				doRemove();
			}
		});
		GridData removeData = new GridData(GridData.FILL_HORIZONTAL);
		removeData.widthHint = convertHorizontalDLUsToPixels(this.buttonRemove, IDialogConstants.BUTTON_WIDTH);
		this.buttonRemove.setLayoutData(removeData);
	}

	/***
	 * @see org.eclipse.jface.preference.FieldEditor#doLoad()
	 */
	protected void doLoad()	{
		String items = getPreferenceStore().getString(getPreferenceName());
		setList(items);
	}

	/***
	 * @see org.eclipse.jface.preference.FieldEditor#doLoadDefault()
	 */
	protected void doLoadDefault() {
		String items = getPreferenceStore().getDefaultString(getPreferenceName());
		setList(items);
	}

	/***
	 * Parses the string into separate list items and adds them to the list.
	 * @param items String to be splitted
	 */
	private void setList(String items) {
		String[] itemArray = parseString(items);
		this.list.setItems(itemArray);
	}

	/***
	 * @see org.eclipse.jface.preference.FieldEditor#doStore()
	 */
	protected void doStore() {
		String s = createListString(this.list.getItems());
		if (s != null) {

			getPreferenceStore().setValue(getPreferenceName(), s);
		}
	}

	/***
	 * @see org.eclipse.jface.preference.FieldEditor#getNumberOfControls()
	 */
	public int getNumberOfControls() {
		// The button composite and the text field.
		return 2;
	}

	/***
	 * Adds the string in the text field to the list.
	 */
	private void doAdd() {
		if (addActionHandler==null) {
			InputDialog dialog=new InputDialog(parent.getShell(), "Text input", "Enter text", "", validator);
			if (dialog.open()==Window.OK) {
				list.add(dialog.getValue());
			}
		} else {
			String str=addActionHandler.doAdd();
			if (str!=null) {
				list.add(str);
			}
		}
	}
	
	private void doEdit() {
		int selectedIndex=list.getSelectionIndex();
		String str=this.list.getItem(selectedIndex);
		
		InputDialog dialog=new InputDialog(parent.getShell(), "Text input", "Enter text", str, validator);
		if (dialog.open()==Window.OK) {
			list.setItem(selectedIndex, dialog.getValue());
		}
	}
	
	private void doRemove() {
		list.remove(list.getSelectionIndex());
		selectionChanged();
	}

	/***
	 * Sets the label for the button that adds the contents of the text field to the list.
	 * @param text "add" button text
	 */
	public void setAddButtonText(String text)
	{
		this.buttonAdd.setText(text);
	}

	/***
	 * Sets the label for the button that removes the selected item from the list.
	 * @param text "remove" button text
	 */
	public void setRemoveButtonText(String text)
	{
		this.buttonRemove.setText(text);
	}

	/***
	 * Sets the string that separates items in the list when the list is stored as a single String in the preference
	 * store.
	 * @param listSeparator token used as a delimiter when converting the array in a single string
	 */
	public void setSeparator(String listSeparator)
	{
		this.separator = listSeparator;
	}

	/***
	 * Creates the single String representation of the list that is stored in the preference store.
	 * @param items String array
	 * @return String created adding items elements separated by "separator"
	 */
	private String createListString(String[] items)
	{
		StringBuffer path = new StringBuffer();

		for (int i = 0; i < items.length; i++)
		{
			path.append(items[i]);
			path.append(this.separator);
		}
		return path.toString();
	}

	/***
	 * Parses the single String representation of the list into an array of list items.
	 * @param stringList String to be splitted in array
	 * @return String[] splitted string
	 */
	private String[] parseString(String stringList)
	{
		StringTokenizer st = new StringTokenizer(stringList, this.separator);
		java.util.List<String> v = new ArrayList<String>();
		while (st.hasMoreElements())
		{
			v.add((String) st.nextElement());
		}
		return (String[]) v.toArray(new String[v.size()]);
	}

	/***
	 * Sets the enablement of the remove button depending on the selection in the list.
	 */
	void selectionChanged()	{
		int index = this.list.getSelectionIndex();
		list.setEnabled(enabled);
		buttonAdd.setEnabled(enabled);
		buttonRemove.setEnabled(enabled && index >= 0);
		buttonEdit.setEnabled(enabled && index>=0);
	}

	@Override
	public void setEnabled(boolean enabled, Composite parent) {
		super.setEnabled(enabled, parent);
		
		this.enabled=enabled;
		selectionChanged();
	}
	
	public void setAddActionHandler(AddActionHandler handler) {
		this.addActionHandler=handler;
	}
}
