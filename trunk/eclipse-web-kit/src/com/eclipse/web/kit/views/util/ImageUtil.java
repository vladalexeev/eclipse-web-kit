package com.eclipse.web.kit.views.util;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;

public class ImageUtil {

	public static ImageInfo getImageInfo(String imageFileName) {
		ImageLoader imageLoader=new ImageLoader();
		ImageData[] imageData=imageLoader.load(imageFileName);
		
		ImageInfo result=new ImageInfo();
		result.setImageFileName(imageFileName);
		result.setImageWidth(imageData[0].width);
		result.setImageHeight(imageData[0].height);
		return result;
	}
}
