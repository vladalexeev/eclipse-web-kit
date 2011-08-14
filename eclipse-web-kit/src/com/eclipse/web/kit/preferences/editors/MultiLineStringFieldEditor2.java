package com.eclipse.web.kit.preferences.editors;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class MultiLineStringFieldEditor2 extends FieldEditor {
	
	private Text textField;
	
	/**
     * Cached valid state.
     */
    private boolean isValid;

    /**
     * Old text value.
     * @since 3.4 this field is protected.
     */
    protected String oldValue;
    
    

	public MultiLineStringFieldEditor2() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MultiLineStringFieldEditor2(String name, String labelText,
			Composite parent) {
		super(name, labelText, parent);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void adjustForNumColumns(int numColumns) {
		GridData gd = (GridData) textField.getLayoutData();
        gd.horizontalSpan = numColumns;// - 1;
        // We only grab excess space if we have to
        // If another field editor has more columns then
        // we assume it is setting the width.
        gd.grabExcessHorizontalSpace = gd.horizontalSpan == 1;
	}

	@Override
	protected void doFillIntoGrid(Composite parent, int numColumns) {
		getLabelControl(parent);

        textField = getTextControl(parent);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = numColumns;// - 1;
        //gd.horizontalAlignment = GridData.FILL;
        //gd.grabExcessHorizontalSpace = true;
        gd.heightHint=60;
        //gd.widthHint=500;
        textField.setLayoutData(gd);
	}

	@Override
	protected void doLoad() {
		if (textField != null) {
            String value = getPreferenceStore().getString(getPreferenceName());
            textField.setText(value);
            oldValue = value;
        }
	}

	@Override
	protected void doLoadDefault() {
		if (textField != null) {
            String value = getPreferenceStore().getDefaultString(getPreferenceName());
            textField.setText(value);
        }
        valueChanged();
	}

	@Override
	protected void doStore() {
		getPreferenceStore().setValue(getPreferenceName(), textField.getText());
	}

	@Override
	public int getNumberOfControls() {
		return 2;
	}

	protected void valueChanged() {
        setPresentsDefaultValue(false);
        boolean oldState = isValid;
        refreshValidState();

        if (isValid != oldState) {
			fireStateChanged(IS_VALID, oldState, isValid);
		}

        String newValue = textField.getText();
        if (!newValue.equals(oldValue)) {
            fireValueChanged(VALUE, oldValue, newValue);
            oldValue = newValue;
        }
    }
	
	 public Text getTextControl(Composite parent) {
	        if (textField == null) {
	            textField = new Text(parent,
	            		SWT.MULTI
	                    | SWT.BORDER
	                    | SWT.H_SCROLL
	                    | SWT.V_SCROLL);
	            textField.setFont(parent.getFont());
	            
	            textField.addDisposeListener(new DisposeListener() {
	                public void widgetDisposed(DisposeEvent event) {
	                    textField = null;
	                }
	            });
	        } else {
	            checkParent(textField, parent);
	        }
	        return textField;
	    }
}
