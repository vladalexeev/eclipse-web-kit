package com.eclipse.web.kit.newsfeed;

public class DefaultCategory {
	private String path;
	private String categoryName;
	
	public DefaultCategory() {
		
	}
	
	public DefaultCategory(String path, String categoryName) {
		this.path=path;
		this.categoryName=categoryName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
}
