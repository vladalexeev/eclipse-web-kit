package com.eclipse.web.kit.overlay;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;

public class ProjectPropertyStore extends PreferenceStore {
	
	private IPreferenceStore workbenchStore;
	private String fileName;

	public ProjectPropertyStore(IProject project, IPreferenceStore workbenchStore, String pageId) {
		super(makeFileName(project, pageId));
		this.workbenchStore=workbenchStore;
		this.fileName=makeFileName(project, pageId);
		
		try {
			File file=new File(fileName);
			if (file.exists()) {
				load();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	 
	private static String makeFileName(IProject project, String pageId) {
		return new File(project.getLocation().toOSString(),".eclipse-web-kit/."+pageId).toString();
	}

	@Override
	public void addPropertyChangeListener(IPropertyChangeListener listener) {
		super.addPropertyChangeListener(listener);
		workbenchStore.addPropertyChangeListener(listener);
	}
	
	@Override
	public void removePropertyChangeListener(IPropertyChangeListener listener) {
		super.removePropertyChangeListener(listener);
		workbenchStore.removePropertyChangeListener(listener);
	}
	
	@Override
	public void firePropertyChangeEvent(String name, Object oldValue,
			Object newValue) {
		super.firePropertyChangeEvent(name, oldValue, newValue);
	}	

	@Override
	public boolean contains(String name) {
		return super.contains(name) || workbenchStore.contains(name);
	}

	@Override
	public boolean getBoolean(String name) {
		if (super.contains(name)) {
			return super.getBoolean(name);
		} else {
			return workbenchStore.getBoolean(name);
		}
	}

	@Override
	public boolean getDefaultBoolean(String name) {
		return workbenchStore.getDefaultBoolean(name);
	}

	@Override
	public double getDefaultDouble(String name) {
		return workbenchStore.getDefaultDouble(name);
	}

	@Override
	public float getDefaultFloat(String name) {
		return workbenchStore.getDefaultFloat(name);
	}

	@Override
	public int getDefaultInt(String name) {
		return workbenchStore.getDefaultInt(name);
	}

	@Override
	public long getDefaultLong(String name) {
		return workbenchStore.getDefaultLong(name);
	}

	@Override
	public String getDefaultString(String name) {
		return workbenchStore.getDefaultString(name);
	}

	@Override
	public double getDouble(String name) {
		if (super.contains(name)) {
			return super.getDouble(name);
		} else {
			return workbenchStore.getDouble(name);
		}
	}

	@Override
	public float getFloat(String name) {
		if (super.contains(name)) {
			return super.getFloat(name);
		} else {
			return workbenchStore.getFloat(name);
		}
	}

	@Override
	public int getInt(String name) {
		if (super.contains(name)) {
			return super.getInt(name);
		} else {
			return workbenchStore.getInt(name);
		}
	}

	@Override
	public long getLong(String name) {
		if (super.contains(name)) {
			return super.getLong(name);
		} else {
			return workbenchStore.getLong(name);
		}
	}

	@Override
	public String getString(String name) {
		if (super.contains(name)) {
			return super.getString(name);
		} else {
			return workbenchStore.getString(name);
		}
	}

	@Override
	public boolean isDefault(String name) {
		return workbenchStore.isDefault(name);
	}

	@Override
	public void setDefault(String name, double value) {
		workbenchStore.setDefault(name, value);
	}

	@Override
	public void setDefault(String name, float value) {
		workbenchStore.setDefault(name, value);
	}

	@Override
	public void setDefault(String name, int value) {
		workbenchStore.setDefault(name, value);
	}

	@Override
	public void setDefault(String name, long value) {
		workbenchStore.setDefault(name, value);
	}

	@Override
	public void setDefault(String name, String value) {
		workbenchStore.setDefault(name, value);
	}

	@Override
	public void setDefault(String name, boolean value) {
		workbenchStore.setDefault(name, value);
	}

	@Override
	public void save() throws IOException {
		File targetFile=new File(fileName);
		if (!targetFile.getParentFile().exists()) {
			targetFile.getParentFile().mkdirs();
		}
		
		super.save();
	}
	
	
}
