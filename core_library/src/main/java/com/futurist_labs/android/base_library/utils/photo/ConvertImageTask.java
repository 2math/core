package com.futurist_labs.android.base_library.utils.photo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.media.ExifInterface;

import com.futurist_labs.android.base_library.utils.LogUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Galeen on 10/4/2017.
 */

public class ConvertImageTask extends AsyncTask<Void, ConvertImageTask.Response, ConvertImageTask.Response> {

    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private File file, newFile;

    private int CompressionRatio = 80; //You can change it by what ever ratio you want. in 0 to 100.

    private boolean shouldRotate = true, shouldCompress = true;

    private int imageSize;

    private ImageCompressorListener imageCompressorListener;

    public static void adjustImageOrientationAsync(String imagePath, File newFile, ConvertImageTask
            .ImageCompressorListener imageCompressorListener) {
        adjustImageOrientationAsync(imagePath, newFile, imageCompressorListener, 0);
    }

    public static void adjustImageOrientationAsync(String imagePath, File newFile, ConvertImageTask
            .ImageCompressorListener imageCompressorListener, int imageSize) {
        File imageFile = new File(imagePath);
        new ConvertImageTask(imageFile, newFile, imageCompressorListener, imageSize).execute();
    }

    public ConvertImageTask(File file, File newFile, ImageCompressorListener imageCompressorListener) {
        this.file = file;
        this.newFile = newFile;
        this.imageCompressorListener = imageCompressorListener;
    }

    public ConvertImageTask(File file, File newFile, ImageCompressorListener imageCompressorListener, int imageSize) {
        this.file = file;
        this.newFile = newFile;
        this.imageCompressorListener = imageCompressorListener;
        this.imageSize = imageSize;
        shouldCompress = imageSize > 0;
    }


    public void setRotation(boolean isRotate) {
        shouldRotate = isRotate;
    }

    public void setCompressionRatio(int Ratio) {
        CompressionRatio = Ratio;
    }

    public void setShouldCompress(final boolean shouldCompress) {
        this.shouldCompress = shouldCompress;
    }

    public void setImageCompressorListener(ImageCompressorListener imageCompressorListener) {
        this.imageCompressorListener = imageCompressorListener;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Response doInBackground(Void... params) {
        try {
            //***** Fatching file
            //*****Code for Orientation
            Matrix matrix = new Matrix();

            if (shouldRotate) {
                ExifInterface exif1 = new ExifInterface(file.getAbsolutePath());
                int orientation = exif1.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                LogUtils.d("EXIF", "Exif: " + orientation);
                int rotate = 0;
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotate = 270;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotate = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotate = 90;
                        break;
                }
                if (rotate != 0) {
                    matrix.postRotate(rotate);
                }
            }
//            else {
//                matrix.postRotate(0);
//            }

            try {
                Bitmap pickimg = null;
                if (shouldCompress) {
                    int file_size = Integer.parseInt(String.valueOf(file.length() / 1024));
                    LogUtils.d("ImageSize", "" + file_size);
                    int scale = 1;

                    if (file_size < imageSize) {
                        LogUtils.d("image size is good", "image size is less");
                    } else if (file_size < imageSize * 2) {
                        LogUtils.d("image size is * 2", "image size is heavy");
                        scale = 2;
                    } else if (file_size < imageSize * 3) {
                        LogUtils.d("image size is * 3", "image size is heavy");
                        scale = 2;
                    } else if (file_size < imageSize * 4) {
                        LogUtils.d("image size is * 4", "image size is heavy");
                        scale = 4;
                    } else {
                        LogUtils.d("image size > * 4", "image size is heavy");
                        scale = 4;
                    }

                    LogUtils.d("Scale", "Finaly Scaling with " + scale);
                    BitmapFactory.Options o2 = new BitmapFactory.Options();
                    o2.inSampleSize = scale;
                    pickimg = BitmapFactory.decodeFile(file.getAbsolutePath(), o2);
                    if (pickimg.getWidth() > 1280 || pickimg.getHeight() > 1000) {

                        int width = pickimg.getWidth();
                        int height = pickimg.getHeight();

                        while (width > 1280 || height > 700) {
                            width = (width * 90) / 100;
                            height = (height * 90) / 100;
                        }

                        pickimg = Bitmap.createScaledBitmap(pickimg, width, height, true);
                    }
//                else {
                    pickimg = Bitmap.createBitmap(pickimg, 0, 0, pickimg.getWidth(), pickimg.getHeight(), matrix, true); // rotating bitmap
//                }
                    pickimg.compress(Bitmap.CompressFormat.JPEG, CompressionRatio, baos);
                } else {
                    pickimg = BitmapFactory.decodeFile(file.getAbsolutePath());
                    if (shouldRotate) {
                        pickimg = Bitmap.createBitmap(pickimg, 0, 0, pickimg.getWidth(), pickimg.getHeight(), matrix, true); // rotating bitmap
                    }
                }
                File fileToSaveImage = newFile == null ? file : newFile;
                saveBitmap(fileToSaveImage.getPath(), pickimg);
                return new Response(pickimg, Uri.fromFile(fileToSaveImage));

            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;

            }

        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    protected void onPostExecute(Response result) {
        super.onPostExecute(result);
        if (result != null) {
            if (imageCompressorListener != null) {
                imageCompressorListener.onImageCompressed(result.bitmap, result.uri);
            }
        } else {
            if (imageCompressorListener != null) {
                imageCompressorListener.onError();
            }
        }
    }

    public static void saveBitmap(String filename, Bitmap bitmap) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filename);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
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
    }

    class Response {
        Bitmap bitmap;
        Uri uri;

        Response(Bitmap bitmap, Uri uri) {
            this.bitmap = bitmap;
            this.uri = uri;
        }
    }

    public interface ImageCompressorListener {
        void onImageCompressed(Bitmap bitmap, Uri uri);

        void onError();
    }
}