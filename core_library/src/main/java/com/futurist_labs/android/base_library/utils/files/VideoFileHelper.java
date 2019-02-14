package com.futurist_labs.android.base_library.utils.files;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Galeen on 2/14/2019.
 */
public class VideoFileHelper {

    /**
     * API for creating thumbnail from Video
     *
     * @param filePath - video file path
     * @param type     - size MediaStore.Images.Thumbnails.MINI_KIND or MICRO_KIND
     * @return thumbnail bitmap
     */
    public static Bitmap createThumbnailFromPath(String filePath, int type) {
        return ThumbnailUtils.createVideoThumbnail(filePath, type);
    }

    /**
     * API for generating Thumbnail from particular time frame
     *
     * @param filePath      - video file path
     * @param timeInSeconds - thumbnail to generate at time
     * @return thumbnail bitmap
     */
    public static Bitmap createThumbnailAtTime(String filePath, int timeInSeconds) {
        return createThumbnailAtTime(filePath, (long)(timeInSeconds * 1000));
    }

    /**
     * API for generating Thumbnail from particular time frame
     *
     * @param filePath      - video file path
     * @param timeInMills - thumbnail to generate at time
     * @return thumbnail bitmap
     */
    public static Bitmap createThumbnailAtTime(String filePath, long timeInMills) {
        MediaMetadataRetriever mMMR = new MediaMetadataRetriever();
        mMMR.setDataSource(filePath);
        //api time unit is microseconds
        return mMMR.getFrameAtTime(timeInMills * 1000, MediaMetadataRetriever.OPTION_CLOSEST);
    }

    /**
     * Get duration of video
     * @param context context
     * @param videoFile the file
     * @return 0 on error or the duration in mills
     */
    public static long getDuration(Context context, File videoFile){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//use one of overloaded setDataSource() functions to set your data source
        retriever.setDataSource(context, Uri.fromFile(videoFile));
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        retriever.release();
        try {
            return Long.parseLong(time);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }
    /**
     * Get duration of video
     * @param context context
     * @param videoFile the file
     * @return Float[Width, Height, Rotation] or null on error. Rotation is 0,90,180,270
     */
    public static Float[] getProps(Context context, File videoFile){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//use one of overloaded setDataSource() functions to set your data source
        retriever.setDataSource(context, Uri.fromFile(videoFile));
        String w = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
        String h = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
        String r = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);
        retriever.release();
        try {
            Float[] res = new Float[3];
            res[0] = Float.parseFloat(w);
            res[1] = Float.parseFloat(h);
            res[2] = Float.parseFloat(r);
            return res;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * @param url           local path
     * @param timeInSeconds 1 for first thumbnail
     * @return the frame
     * @throws Throwable on Exception
     */
    public static Bitmap retriveVideoFrameFromVideo(String url, int timeInSeconds)
            throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 15) {
                mediaMetadataRetriever.setDataSource(url, new HashMap<String, String>());
            } else {
                mediaMetadataRetriever.setDataSource(url);
            }
            bitmap = mediaMetadataRetriever.getFrameAtTime(timeInSeconds, MediaMetadataRetriever.OPTION_CLOSEST);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String videoPath)"
                            + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }
}
