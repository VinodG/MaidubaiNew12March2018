package com.winit.maidubai.common;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;

import com.winit.maidubai.MaiDubaiApplication;


/**
 * Creating Application Level Storage Directories
 * eg: storing images,caching data etc
 * */
public class FilesStorage 
{
	public static String SDCARD_ROOT;
	public static  String ROOT_DIR;
	public  static  String IMAGE_CACHE_DIR ="";
	public  static  String TEMP_DIR;

	
	public static void CreateStorageDirs(Context context) 
	{
		 // Ensure that the directories exist.
		if(Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_REMOVED))
			SDCARD_ROOT = context.getFilesDir().toString()+"/";
		else
			SDCARD_ROOT = Environment.getExternalStorageDirectory().toString() + "/"; 
				
		IMAGE_CACHE_DIR   	 = ROOT_DIR  + "ImageCache/";
		TEMP_DIR   	     	 = ROOT_DIR + "Temp/";
		new File(ROOT_DIR).mkdirs();
		new File(IMAGE_CACHE_DIR).mkdirs();
	}

	public static String getRootDirector() {
		if (Environment.getExternalStorageState().equalsIgnoreCase(
				Environment.MEDIA_REMOVED))
			SDCARD_ROOT = MaiDubaiApplication.mContext.getFilesDir().toString()
					+ "/";
		else if(MaiDubaiApplication.mContext.getExternalCacheDir()!=null)
			SDCARD_ROOT = MaiDubaiApplication.mContext.getExternalCacheDir()
					.toString() + "/";
		else
			SDCARD_ROOT = Environment.getExternalStorageDirectory()
			.toString() + "/";
		ROOT_DIR = SDCARD_ROOT + "MaiDubai/";
		return ROOT_DIR;
	}

	public static String getTempDownload() {
		if (Environment.getExternalStorageState().equalsIgnoreCase(
				Environment.MEDIA_REMOVED))
			SDCARD_ROOT = MaiDubaiApplication.mContext.getFilesDir().toString()
					+ "/";
		else
			SDCARD_ROOT = MaiDubaiApplication.mContext.getExternalCacheDir()
					.toString() + "/";
		ROOT_DIR = SDCARD_ROOT + "MaiDubai/";
		TEMP_DIR = ROOT_DIR + "TempDownload/";
		return TEMP_DIR;
	}

	public static String getImageCacheDirectory() {
		IMAGE_CACHE_DIR = getRootDirector() + "ImageCache/";
		return IMAGE_CACHE_DIR;
	}

	public static String getHttpCacheDirectory() {
		IMAGE_CACHE_DIR = getRootDirector() + "Http/";
		return IMAGE_CACHE_DIR;
	}
}
