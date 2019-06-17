package com.futurist_labs.android.base_library.utils.files;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;

import com.futurist_labs.android.base_library.model.BaseLibraryConfiguration;
import com.futurist_labs.android.base_library.utils.LogUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Galeen on 9.2.2016 г..
 */
public class FileHelper {
    private final static String TAG = FileHelper.class.getName();

    boolean mExternalStorageAvailable = false;
    boolean mExternalStorageWriteable = false;

    public static void saveImageToFileAsync(Bitmap myBitmap, int width, int height, boolean localStorage, String name, String dirName, Callback callback) {
        new SasveImageOperation(myBitmap, width, height, localStorage, name, dirName, callback).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public interface Callback {
        void onResult(String filePath);
    }

    public FileHelper() {

    }

    public String saveImageToFile(Bitmap myBitmap, int width, int height, String name, String dirName) {
        return saveImageToFile(myBitmap, width, height, false, name, dirName);
    }


    public String saveImageToFile(Bitmap myBitmap, int width, int height, boolean localStorage, String name, String dirName) {
        FileOutputStream out = null;
        try {
            if (!localStorage)
                checkSD();
            if (dirName == null) {
                dirName = "images";
            }
            File dir;
            if (mExternalStorageAvailable && mExternalStorageWriteable) {
                dir = new File(BaseLibraryConfiguration.getInstance().getApplication().getExternalFilesDir(null), dirName);
            } else
                dir = new File(BaseLibraryConfiguration.getInstance().getApplication().getFilesDir() + "/" + dirName);
//            if (dir.isDirectory()) {
//                String[] children = dir.list();
//                for (int i = 0; i < children.length; i++) {
//                    new File(dir, children[i]).delete();
//                }
//            }
            File file;
            dir.mkdirs();
            if (name == null) name = "avatar.png";
            file = new File(dir, name);//System.currentTimeMillis() +
//            if (!file.exists())
//                file.createNewFile();
            out = new FileOutputStream(file);
            if (width <= 0 || height <= 0) {
                myBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            } else {
                Bitmap bitmap = Bitmap.createScaledBitmap(myBitmap, width, height, false);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            }
            out.flush();
            LogUtils.d("File Saved", "File : " + file.toString());
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }


    private void checkSD() {
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Something else is wrong. It may be one of many other states, but all we need
            //  to know is we can neither read nor write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
    }

    public void copyFiles(File sourceLocation, File targetLocation){

        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(sourceLocation);
            out = new FileOutputStream(targetLocation);
            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static class SasveImageOperation extends AsyncTask<Void, Void, String> {
        private Bitmap myBitmap;
        private int width, height;
        private boolean localStorage;
        private String name;
        private Callback callback;
        private String dirName;

        public SasveImageOperation(Bitmap myBitmap, int width, int height, boolean localStorage, String name, String dirName, Callback callback) {
            this.myBitmap = myBitmap;
            this.width = width;
            this.height = height;
            this.localStorage = localStorage;
            this.name = name;
            this.callback = callback;
            this.dirName = dirName;
        }

        @Override
        protected String doInBackground(Void... voids) {
            return new FileHelper().saveImageToFile(myBitmap, width, height, localStorage, name, dirName);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (callback != null) {
                callback.onResult(s);
            }
        }
    }

    public static ArrayList<String> ReadFile(File file) {
        String line = null;
        ArrayList<String> lines = new ArrayList<>();

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
//                stringBuilder.append(line + System.getProperty("line.separator"));
            }
            fileInputStream.close();
//            line = stringBuilder.toString();

            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            LogUtils.e(TAG, ex.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static String ReadFileAsString(File file, boolean isFormatted) {
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
                if(isFormatted){
                    stringBuilder.append(System.getProperty("line.separator"));
                }
            }
            fileInputStream.close();

            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            LogUtils.e(TAG, ex.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
