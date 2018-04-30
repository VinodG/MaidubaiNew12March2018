package com.winit.maidubai.utilities;

import android.content.Context;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;

import com.winit.maidubai.common.AppConstants;
import com.winit.maidubai.dataobject.ProductImagesDO;
import com.winit.maidubai.webaccessLayer.HttpHelper;
import com.winit.maidubai.webaccessLayer.ServiceUrls;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtils
{
	static int count = 0;

	public static boolean isFileExists(String path){
		return (new File(path)).exists();
	}

	public static void writeFile(byte[] binary, String SdcardPath, String fileName){
		BufferedOutputStream bufferedOutputStream = null;
		try {
			File themeFile = new File(SdcardPath);
			if(!themeFile.exists())
			{
				new File(SdcardPath).mkdirs();
			}
			File file =new File(SdcardPath + fileName);
			if(file.exists())
			{
				file.delete();
			}
			bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
			bufferedOutputStream.write(binary);
		} catch (Exception e){
			/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
		}
		finally {
			try {
				if(bufferedOutputStream != null)
					bufferedOutputStream.close();
			}catch (Exception e){
			/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
			}
		}
	}

	public static String  getFileNameFromPath(String filePath)
	{
		String fileName = null;
		try
		{
			fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);

		} catch (Exception e)
		{
			/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
		}
		return fileName;
	}

	public static String SaveInputStreamAsFile(Context ctx,String SdcardPath, String source, String fileName) {
		FileOutputStream fOut=null;
		OutputStreamWriter myOutWriter=null;
		try {
			File myFile = new File(SdcardPath,"Themes.xml");

			myFile.createNewFile();

			fOut = new FileOutputStream(myFile);

			myOutWriter =

					new OutputStreamWriter(fOut);

			myOutWriter.append(source);

			myOutWriter.close();

			fOut.close();
		}
		catch (Exception e)
		{
		/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
		}
		finally {
			if(myOutWriter!=null)
				safeCloseOutputStreamWriter(myOutWriter);
			if(fOut!=null)
				safeCloseFileOutputStream(fOut);
		}

		return null;
	}

	public static int ordinalIndexOf(String str, String s, int n) {
		int pos = str.indexOf(s, 0);
		while (n-- > 0 && pos != -1)
			pos = str.indexOf(s, pos+1);
		return pos;
	}

	public static String getFileNameForProducts(String filePath) {
		int index = ordinalIndexOf(filePath,"/",2);
		return ((filePath.substring(index+1,filePath.length())).replace("/","_"));
	}

	public static void deleteDir(File file){
		String[]entries = file.list();
		for(String s: entries){
			File currentFile = new File(file.getPath(),s);
			currentFile.delete();
		}
	}

	public static boolean exractZip(InputStream inputStream,String location){
		FileOutputStream fout=null;
		boolean flag =false;
		try {
			File file = new File(location);
			if(!file.exists()) {
				file.mkdirs();
			}else{
				deleteDir(file);
				file.delete();
				file.mkdirs();
			}
			ZipInputStream zipInputStream = new ZipInputStream(inputStream);
			ZipEntry zipEntry;
			byte[] buffer = new byte[1024];
			while ((zipEntry = zipInputStream.getNextEntry()) != null) {
				if (zipEntry.isDirectory()) {
					File f = new File(location, zipEntry.getName());
					if (!f.isDirectory()) {
						f.mkdirs();
					}
				} else {
					 fout = new FileOutputStream(new File(location, zipEntry.getName()));
					int len;
					while ((len = zipInputStream.read(buffer)) != -1) {
						fout.write(buffer, 0, len);
					}
//					fout.close();
					if(fout!=null)
						safeCloseFileOutputStream(fout);
					zipInputStream.closeEntry();
				}

			}
			zipInputStream.close();
			flag=  true;
		}catch (Exception e){
			/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
		}finally {
  if(fout!=null)
  	safeCloseFileOutputStream(fout);
			return flag;
		}
	}

	public static void inputStream2File(InputStream inputStream, String fileName,String SdcardPath)
	{
		BufferedInputStream bis=null;
		FileOutputStream fos=null;
		BufferedOutputStream bos=null;
		try
		{
			File themeFile = new File(SdcardPath);
			if(!themeFile.exists())
			{
				new File(SdcardPath).mkdirs();
			}
			File file =new File(SdcardPath + fileName);
			if(file.exists())
			{
				file.delete();
			}

			bis = new BufferedInputStream(inputStream);
			fos = new FileOutputStream(SdcardPath+fileName);
			bos = new BufferedOutputStream(fos);
			byte byt[] = new byte[1024];
			int noBytes;
			while((noBytes=bis.read(byt)) != -1)
				bos.write(byt,0,noBytes);
			bos.flush();
//			bos.close();
//			fos.close();
//			bis.close();
		}
		catch (Exception e)
		{
			/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
		}
		finally {
			if(bos!=null)
				safeCloseBufferedOutputStream(bos);
			if(fos!=null)
				safeCloseFileOutputStream(fos);
			if(bis!=null)
				safeCloseBufferedInputStream(bis);
		}
	}

	public static InputStream getFileFromSDcard(String SDcardpath, String fileName)
	{
		InputStream is = null;
		FileInputStream fIn = null;
		BufferedReader myReader=null;
		try
		{
			File myFile = new File(SDcardpath,fileName);
			if(!myFile.exists())
			{
				myFile.createNewFile();
			}
			fIn = new FileInputStream(myFile);
			myReader = new BufferedReader(new InputStreamReader(fIn));
			String aDataRow = "";
			String aBuffer = "";
			while ((aDataRow = myReader.readLine()) != null) {
				aBuffer += aDataRow + "\n";
			}
//				txtData.setText(aBuffer);
			is = new ByteArrayInputStream(aBuffer.getBytes());
//			myReader.close();
		}
		catch (Exception e)
		{
			/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
		}finally{
           if(fIn!=null)
			   safeCloseFileInputStream(fIn);
           if(myReader!=null)
			   safeCloseBufferedReader(myReader);


			return is;
		}
	}
	public static void safeCloseFileInputStream(FileInputStream fin) {
		if (fin != null) {
			try {
				fin.close();
			} catch (IOException e) {
			/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
			}
		}
	}
	public static void safeCloseOutputStreamWriter(OutputStreamWriter fin) {
		if (fin != null) {
			try {
				fin.close();
			} catch (IOException e) {
				/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
			}
		}
	}
	public static void safeCloseInputStream(InputStream in) {
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
			}
		}
	}
	public static void safeCloseFileOutputStream(FileOutputStream fos) {
		if (fos != null) {
			try {
				fos.close();
			} catch (IOException e) {
				/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
			}
		}
	}
	public static void safeCloseBufferedOutputStream(BufferedOutputStream bos) {
		if (bos != null) {
			try {
				bos.close();
			} catch (IOException e) {
				/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
			}
		}
	}
	public static void safeCloseBufferedInputStream(BufferedInputStream bis) {
		if (bis != null) {
			try {
				bis.close();
			} catch (IOException e) {
				/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
			}
		}
	}
	public static void safeCloseBufferedReader(BufferedReader rd) {
		if (rd != null) {
			try {
				rd.close();
			} catch (IOException e) {
				/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
			}
		}
	}
	public static void updateProductImages(ArrayList<ProductImagesDO> productImagesDOs){
		InputStream inputStream=null;
		FileOutputStream fout=null;
		if(productImagesDOs != null){
			for(ProductImagesDO productImagesDO:productImagesDOs){
				try {
					File file = new File(AppConstants.APP_LOCATION+AppConstants.IMAGE_DIR+"/Data/ProductImages/"+productImagesDO.productCode);
					if(file.exists())
						file.delete();
					file.mkdirs();
					byte[] buffer = new byte[1024];
					inputStream = new HttpHelper().sendRequest( ServiceUrls.MAIN_URL.substring(0,ServiceUrls.MAIN_URL.length()-1) + productImagesDO.OriginalImage, ServiceUrls.METHOD_GET, null, null);
					fout = new FileOutputStream(new File(AppConstants.APP_LOCATION+AppConstants.IMAGE_DIR+productImagesDO.OriginalImage));
					int len;
					while ((len = inputStream.read(buffer)) != -1) {
						fout.write(buffer, 0, len);
					}
					fout.close();
				}catch (Exception ex){
					/*e.printStackTrace(); */Log.d("This can never happen", ex.getMessage());
				}finally {
					if(fout!=null)
						safeCloseFileOutputStream(fout);
					if(inputStream!=null)
						safeCloseInputStream(inputStream);
				}
			}
		}
	}



	private static void acquireWifi(Context context, PowerManager.WakeLock mWifiLock)
	{
		mWifiLock.acquire();
		Log.e("acquire", "DONE");
	}


	public static File getOutputImageFile(String folder)
	{

		File captureImagesStorageDir = new File(Environment.getExternalStorageDirectory()+"/ARMADA/"+folder);

		if (!captureImagesStorageDir.exists())
		{
			if (!captureImagesStorageDir.mkdirs())
			{
				return null;
			}
		}

		String timestamp = System.currentTimeMillis()+"";
		File imageFile = new File(captureImagesStorageDir.getPath() + File.separator
				+ "CAPTURE_" + timestamp + ".jpg");
		return imageFile;
	}
	public static File getOutputAudioFile(String folder)
	{

		File captureImagesStorageDir = new File(Environment.getExternalStorageDirectory()+"/ARMADA/"+folder);

		if (!captureImagesStorageDir.exists())
		{
			if (!captureImagesStorageDir.mkdirs())
			{
				return null;
			}
		}

		String timestamp = System.currentTimeMillis()+"";
		File imageFile = new File(captureImagesStorageDir.getPath() + File.separator
				+ "CAPTURE_" + timestamp + ".mp3");


		return imageFile;
	}
	public static File getOutputVideoFile(String folder)
	{

		File captureImagesStorageDir = new File(Environment.getExternalStorageDirectory()+"/ARMADA/"+folder);

		if (!captureImagesStorageDir.exists())
		{
			if (!captureImagesStorageDir.mkdirs())
			{
				return null;
			}
		}

		String timestamp = System.currentTimeMillis()+"";
		File imageFile = new File(captureImagesStorageDir.getPath() + File.separator
				+ "CAPTURE_" + timestamp + ".mp4");


		return imageFile;
	}
	public static File getApkFilePath(String folder)
	{

		File captureImagesStorageDir = new File(Environment.getExternalStorageDirectory()+"/ARMADA/"+folder);

		if (!captureImagesStorageDir.exists())
		{
			if (!captureImagesStorageDir.mkdirs())
			{
				return null;
			}
		}

		String timestamp = System.currentTimeMillis()+"";
		File imageFile = new File(captureImagesStorageDir.getPath() + File.separator
				+ "MaiDubai" + timestamp + ".apk");


		return imageFile;
	}

}
