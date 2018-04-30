package com.winit.maidubai.utilities;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore.Images;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class BitmapUtilsLatLang {



	static void test(Bitmap bitmap, String url) throws FileNotFoundException {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
		FileOutputStream fo=null;

		try {
			File photo = new File(url);

			photo.createNewFile();
			 fo = new FileOutputStream(photo);
			fo.write(bytes.toByteArray());
			fo.close();
		} catch (IOException e) {
			/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
		}
		finally{
			if(fo!=null)
				safeCloseFileOutputStream(fo);

		}

	}

	public static void safeCloseFileOutputStream(FileOutputStream writer) {
		if (writer != null) {
			try {
				writer.close();
			} catch (IOException e) {
				/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
			}
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
	//====================== newly added merchandiser==============

	 public static String saveVerifySignature(Bitmap bitmap, String filePath)
	 {
			File file = new File (filePath);
		 FileOutputStream out=null;
			try
			{
				
		       out = new FileOutputStream(file);
		       bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
		       out.flush();
		       out.close();
			} 
			catch (Exception e) 
			{
		    /*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
			}
			finally {
				if(out!=null)
					safeCloseFileOutputStream(out);

				return file.getAbsolutePath();
			}
	 }




	public static Bitmap processBitmap(Bitmap bmpOrig) {
		int width = 2;
		Bitmap bmpProcessed = Bitmap
				.createBitmap(100, 120, bmpOrig.getConfig());
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		Canvas canvas = new Canvas(bmpProcessed);

		Rect rectSrc = new Rect(0, 0, bmpOrig.getWidth(), bmpOrig.getHeight());
		Rect rectDest = new Rect(0, 0, 100, 120);

		paint.setAntiAlias(true);
		// canvas.drawRect(0, 0, 100, 120, paint);
		canvas.drawBitmap(bmpOrig, rectSrc, rectDest, paint);

		if (bmpOrig != null && !bmpOrig.isRecycled()) {
			WeakReference<Bitmap> weakBitmap = new WeakReference<Bitmap>(
					bmpOrig);
			if (weakBitmap.get() != null && !weakBitmap.get().isRecycled()) {
				weakBitmap.get().recycle();
			}
		}

		// paint.setColor(Color.WHITE);
		// paint.setTextSize(15);
		// paint.setColor(Color.BLACK);
		// canvas.drawText("IMG", 30, 115, paint);

		return bmpProcessed;
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,
			float roundPxRadius) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = roundPxRadius;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	public static Bitmap resizeBitmap(Bitmap bitmapOrg, int newWidth,
			int newHeight) {
		Bitmap resizedBitmap = null;
		try {

			int width = bitmapOrg.getWidth();
			int height = bitmapOrg.getHeight();

			/**
			 * calculate the scale - in this case = 0.4f
			 */
			float scaleWidth = ((float) newWidth) / width;
			float scaleHeight = ((float) newHeight) / height;

			/**
			 * createa matrix for the manipulation
			 */
			Matrix matrix = new Matrix();
			/**
			 * resize the bit map
			 */
			matrix.postScale(scaleWidth, scaleHeight);
			/**
			 * rotate the Bitmap
			 */
			resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, width, height,
					matrix, true);

		} catch (Exception e) {   throw new RuntimeException("This can never happen", e);
		}

		return resizedBitmap;
	}

	public static Bitmap resizeWithoutBitmap(Bitmap bitmapOrg, int newWidth,
			int newHeight) {
		Bitmap resizedBitmap = null;
		try {

			int width = bitmapOrg.getWidth();
			int height = bitmapOrg.getHeight();

			/**
			 * calculate the scale - in this case = 0.4f
			 */
			float scaleWidth = ((float) newWidth) / width;
			float scaleHeight = ((float) newHeight) / height;

			/**
			 * createa matrix for the manipulation
			 */
			Matrix matrix = new Matrix();
			/**
			 * resize the bit map
			 */
			matrix.postScale(scaleWidth, scaleHeight);
			/**
			 * rotate the Bitmap
			 */
			resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, width, height,
					matrix, true);

		} catch (Exception e) {  throw new RuntimeException("This can never happen", e);
		}

		return resizedBitmap;
	}

	public static Bitmap getResizedBmp(Bitmap bitmap, float width, float height) {

		float bmpHieght = 600;
		float bmpWidth = 1024;
		Log.i("bmpHieght ", bmpHieght + " bmpWidth " + bmpWidth);

		int scaledWidth = 0;
		int scaledHeight = 0;
		Bitmap scaledBitmap = null;
		// if(bmpWidth/width > bmpHieght/ height)
		// {
		scaledWidth = (int) width;
		scaledHeight = (int) (bmpHieght * width / bmpWidth);
		scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth,
				scaledHeight, true);
		// }
		// else
		// {
		// scaledWidth = (int)(bmpWidth * height/ bmpHieght);
		// scaledHeight = (int)height;
		// scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth,
		// scaledHeight, true);
		// }

		return scaledBitmap;
	}

	public static Bitmap decodeSampledBitmapFromResource(File f, int reqWidth,
			int reqHeight) {
		FileInputStream fis=null;
		FileInputStream fis1=null;
		try {
			// First decode with inJustDecodeBounds=true to check dimensions
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;

			BitmapFactory.decodeStream(fis1=new FileInputStream(f), null, options);

			// Calculate inSampleSize
			//options.inSampleSize = 1;

			options.inJustDecodeBounds = false;
			if(fis1!=null)
				safeCloseFileInputStream(fis1);
			options.inSampleSize = ScalingUtilities.calculateSampleSize(options.outWidth, options.outHeight, reqWidth,reqHeight, ScalingUtilities.ScalingLogic.CROP);
			Bitmap tmpBitmapc = BitmapFactory.decodeStream(fis=new FileInputStream(f), null, options);


			//tmpBitmap = getResizedBitmap(tmpBitmap, reqWidth, reqHeight);
			Bitmap tmpBitmap = ScalingUtilities.createScaledBitmap(tmpBitmapc, reqWidth,reqHeight, ScalingUtilities.ScalingLogic.CROP);
			tmpBitmapc.recycle();

			float rotation = rotationForImage(null, Uri.fromFile(f));
			if (rotation != 0f) {
				Matrix matrix = new Matrix();
				matrix.preRotate(rotation);
				tmpBitmap = Bitmap.createBitmap(tmpBitmap, 0, 0,
						tmpBitmap.getWidth(), tmpBitmap.getHeight(), matrix,
						true);
			}
			return tmpBitmap;
		} catch (FileNotFoundException e) {
			/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
		}
		finally {

			if(fis!=null)
				safeCloseFileInputStream(fis);
			return null;
		}
	}

	public static Bitmap getResizedBitmap(Bitmap bitmap, float width,
			float height) {
		if (bitmap != null) {

		}
		float bmpHieght = bitmap.getHeight();
		float bmpWidth = bitmap.getWidth();

		Bitmap scaledBitmap = null;
		if (bmpHieght < height && bmpWidth < width) {
			return bitmap;
		}

		scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int)width,(int)height, true);

		/*int scaledWidth = 0;
		int scaledHeight = 0;

		if (bmpWidth / width < bmpHieght / height) {
			scaledWidth = convertPixelToDp((int) (bmpWidth * height / bmpHieght));
			scaledHeight = convertPixelToDp((int) height);
			scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth,
					scaledHeight, true);
		} else {
			scaledWidth = convertPixelToDp((int) width);
			scaledHeight = convertPixelToDp((int) (bmpHieght * width / bmpWidth));
			scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth,
					scaledHeight, true);
		}*/
		return scaledBitmap;
	}



	public static Bitmap getResizedBitmapNew(Bitmap bitmap, float newWidth,
										  float newHeight) {
		Bitmap scaledBitmap = null;

		Bitmap bitmapOrg = bitmap;

		int width = bitmapOrg.getWidth();

		int height = bitmapOrg.getHeight();

		// calculate the scale - in this case = 0.4f

		float scaleWidth = ((float) newWidth) / width;

		float scaleHeight = ((float) newHeight) / height;

		Matrix matrix = new Matrix();

		matrix.postScale(scaleWidth, scaleHeight);

		// this will create image with new size
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0,width, height, matrix, true);


		return scaledBitmap;
	}

	public static float rotationForImage(Context context, Uri uri) {
		try {
			if (context != null && uri.getScheme().equals("content")) {
				String[] projection = { Images.ImageColumns.ORIENTATION };
				Cursor c = context.getContentResolver().query(uri, projection,
						null, null, null);
				if (c.moveToFirst()) {
					return c.getInt(0);
				}
			} else if (uri.getScheme().equals("file")) {
				try {
					ExifInterface exif = new ExifInterface(uri.getPath());
					int rotation = (int) exifOrientationToDegrees(exif
							.getAttributeInt(ExifInterface.TAG_ORIENTATION,
									ExifInterface.ORIENTATION_NORMAL));
					return rotation;
				} catch (IOException e) {
					/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
				}
			}
		} catch (Exception e) {
			/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
		}

		return 0f;
	}

	public static int convertPixelToDp(int px) {
		return (int) (px * (160 / 160f));
	}

	private static float exifOrientationToDegrees(int exifOrientation) {
		if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
			return 90;
		} else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
			return 180;
		} else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
			return 270;
		}
		return 0;
	}
}
