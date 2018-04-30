package com.winit.maidubai.utilities;


import android.util.Log;

import com.winit.maidubai.databaseaccess.DatabaseHelper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class LogUtils {

    public static boolean isLogEnable = true;

    public static String SERVICE_LOG_TAG = "Http Service ";
    public static String LOG_TAG = "MAI DUBAI";

    public static void errorLog(String tag, String msg) {
        if (isLogEnable)
            Log.e("" + tag, "" + msg);
    }

    public static void infoLog(String tag, String msg) {
        if (isLogEnable)
            Log.i(tag, msg);
    }

    public static void debug(String tag, String msg) {
        if (isLogEnable)
            Log.d(tag, msg);
    }

//    public static void printMessage(String msg) {
//        if (isLogEnable)
//            Log.d("", msg);
//    }


    public static void setLogEnable(boolean isEnable) {
        isLogEnable = isEnable;
    }

    /**
     * This method stores InputStream data into a file at specified location
     *
     * @param is
     */
    public static void convertResponseToFile(InputStream is) throws IOException {
        if(isLogEnable) {
            BufferedInputStream bis = new BufferedInputStream(is);
            FileOutputStream fos = new FileOutputStream("/sdcard/Response.xml");
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            byte byt[] = new byte[1024];
            int noBytes;
try {
    while ((noBytes = bis.read(byt)) != -1)
        bos.write(byt, 0, noBytes);

    bos.flush();
}catch (Exception e){
/*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
}finally {

    if(bos!=null)
        safeCloseBos(bos);
    if(fos!=null)
        safeCloseFos(fos);
    if(bis!=null)
        safeClose(bis);
}

        }
    }
    public static void safeClose(BufferedInputStream fis) {
        if (fis != null) {
            try {
                fis.close();
            } catch (IOException e) {
             /*e.printStackTrace(); */Log.d("This can never happen", e.getMessage());
            }
        }
    }
    public static void safeCloseInputStream(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
              /* e.printStackTrace();*/  throw new RuntimeException("This can never happen", e);
            }
        }
    }
    public static void safeCloseBos(BufferedOutputStream bos) {
        if (bos != null) {
            try {
                bos.close();
            } catch (IOException e) {
              /* e.printStackTrace();*/  throw new RuntimeException("This can never happen", e);
            }
        }
    }
    public static void safeCloseFos(FileOutputStream fos) {
        if (fos != null) {
            try {
                fos.close();
            } catch (IOException e) {
               /* e.printStackTrace();*/  throw new RuntimeException("This can never happen", e);
            }
        }
    }
    public static void safeCloseOutputStream(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
               /* e.printStackTrace();*/  throw new RuntimeException("This can never happen", e);
            }
        }
    }
    public static void exactDatabase(){
        if(LogUtils.isLogEnable){
            InputStream inputStream =null;
            OutputStream outputStream=null;
            try {
                inputStream = new FileInputStream(new File(DatabaseHelper.DATABASE_PATH));
               outputStream = new FileOutputStream("/sdcard/MaiDubai.sqlite");
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.flush();

            }catch (Exception e){
               /* e.printStackTrace();*/  throw new RuntimeException("This can never happen", e);
            }
            finally {
                {
                    if(outputStream!=null)
                        safeCloseOutputStream(outputStream);
                    if(inputStream!=null)
                        safeCloseInputStream(inputStream);

                }
            }
        }
    }

    /**
     * This method stores data in String into a file at specified location
     *
     * @param is
     */
    public static void convertRequestToFile(String is) throws IOException {
        FileOutputStream fos=null;
        if(isLogEnable) {
            try {
                fos = new FileOutputStream(new File("/sdcard/Request.xml"));
                fos.write(is.getBytes());
//                fos.close();
            }catch (Exception e){
              /* e.printStackTrace();*/  throw new RuntimeException("This can never happen", e);
            }finally {
                {
                    if(fos!=null)
                        safeCloseFos(fos);
                }
            }
        }
    }

    /**
     * This method will read data from the inputStream and return as StringBuffer
     *
     * @param inpStrData
     */
    public static StringBuffer getDataFromInputStream(InputStream inpStrData) {
        if (inpStrData != null) {
            try {
                BufferedReader brResp = new BufferedReader(new InputStreamReader(inpStrData));
                String stringTemporaryVariable;
                StringBuffer sbResp = new StringBuffer();

                //Converts response as a StringBuffer
                while ((stringTemporaryVariable = brResp.readLine()) != null)
                    sbResp.append(stringTemporaryVariable);

                brResp.close();
                inpStrData.close();

                return sbResp;
            } catch (Exception e) {
                LogUtils.errorLog("LogUtils", "Exception occurred in getDataFromInputStream(): " + e.toString());
            }
        }
        return null;
    }

}

