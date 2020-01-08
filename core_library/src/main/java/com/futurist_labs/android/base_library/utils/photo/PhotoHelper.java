package com.futurist_labs.android.base_library.utils.photo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.futurist_labs.android.base_library.R;
import com.futurist_labs.android.base_library.model.BaseLibraryConfiguration;
import com.futurist_labs.android.base_library.utils.LogUtils;
import com.futurist_labs.android.base_library.utils.RippleOnClickListener;
import com.futurist_labs.android.base_library.utils.files.FileHelper;
import com.futurist_labs.android.base_library.utils.files.FileUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;


/**
 * Created by Galeen on 12.7.2017 г..
 */

public class PhotoHelper {
    private static final int GET_IMAGE = 421;
    private static final int GET_VIDEO = 423;
    private static final int REQUEST_IMAGE_CAPTURE = 422;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 11;
    private AlertDialog photoDialog, permissionsDialog;
    private ImageView imageView;
    private View clickView, cameraView, fileView;
    private Activity activity;
    private Fragment fragment = null;
    private int resDialog, idBtnImage, idBtnPhoto, idBtnCancel, resMsgNoCameraPermission, resMsgNoDiskPermission,
            resErrorImage;
    private boolean isComingForTakePicture = false, isComingFromAskForPermission = false;
    private Uri dataUri;
    private File avatarFile = null;
    private PhotoEvents callback;
    private boolean resultAsBase64String = false;//null is bitmap, true file, false base64
    private boolean resizeSavedFileToImageViewSize = false;
    private int imageSize = 1000; // in Bytes
    private int imageMaxWidth = 1280, imageMaxHeight = 1000;
    private View[] clickViews;

//    @Override
//    protected void finalize() throws Throwable {
//        Log.e("PhotoHelper","finalize()");
//        super.finalize();
//    }

    /**
     * @param imageView            if is set the result will be set on it
     * @param clickView
     * @param activity
     * @param resultAsBase64String
     * @param callback
     */
    public PhotoHelper(ImageView imageView, View clickView, Activity activity, Boolean resultAsBase64String,
                       PhotoEvents callback) {
        this.imageView = imageView;
        this.clickView = clickView;
        this.activity = activity;
        this.callback = callback;
        this.resultAsBase64String = resultAsBase64String;
        setListeners();
    }

    public PhotoHelper(ImageView imageView, View[] clickViews, Activity activity, Boolean resultAsBase64String,
                       PhotoEvents callback) {
        this.imageView = imageView;
        this.clickViews = clickViews;
        this.activity = activity;
        this.callback = callback;
        this.resultAsBase64String = resultAsBase64String;
        setListeners();
    }

    public PhotoHelper(ImageView imageView, View cameraView, View fileView, Activity activity, Boolean resultAsBase64String,
                       PhotoEvents callback) {
        this.imageView = imageView;
        this.cameraView = cameraView;
        this.fileView = fileView;
        this.activity = activity;
        this.callback = callback;
        this.resultAsBase64String = resultAsBase64String;
        setListeners();
    }

    // TODO: 6/21/2018 change to builder

    /**
     * This method must be called right after the constructor
     */
    public void setResources(int resDialog, int idBtnImage, int idBtnPhoto, int idBtnCancel, int
            resMsgNoCameraPermission, int resMsgNoDiskPermission, int resErrorImage) {
        this.resDialog = resDialog;
        this.idBtnImage = idBtnImage;
        this.idBtnPhoto = idBtnPhoto;
        this.idBtnCancel = idBtnCancel;
        this.resMsgNoCameraPermission = resMsgNoCameraPermission;
        this.resMsgNoDiskPermission = resMsgNoDiskPermission;
        this.resErrorImage = resErrorImage;
    }

    /**
     * This method must be called right after the constructor
     */
    public void setResources(int resMsgNoCameraPermission, int resMsgNoDiskPermission,
                             int resErrorImage) {
        this.resMsgNoCameraPermission = resMsgNoCameraPermission;
        this.resMsgNoDiskPermission = resMsgNoDiskPermission;
        this.resErrorImage = resErrorImage;
    }

    /**
     * Assign multiple views to have a click listener and show start dialog
     * @param clickViews all views
     */
    public void setClickViews(View[] clickViews) {
        this.clickViews = clickViews;
        setListeners();
    }

    private ProgressDialog progressDialog;

    /**
     * Next methods must be called from the presenterCallback/fragment
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == GET_IMAGE) {
            dataUri = data.getData();
            loadPhoto();
//            if (dataUri == null) return;
//            ConvertImageTask.adjustImageOrientationAsync(
//                    dataUri.getPath(), avatarFile,
//                    new ConvertImageTask.ImageCompressorListener() {
//                        @Override
//                        public void onImageCompressed(final Bitmap bitmap, final Uri uri) {
//                            dataUri = uri;
//                            loadPhoto();
//                        }
//
//                        @Override
//                        public void onError() {
//                            loadPhoto();
//                        }
//                    });
        } else if (requestCode == GET_VIDEO) {
            dataUri = data.getData();
            uploadFile();
        } else if (requestCode == REQUEST_IMAGE_CAPTURE) {//resultCode == Activity.RESULT_OK &&
            if (avatarFile != null) {
                dataUri = getUriFromFile(activity, avatarFile);//Uri.fromFile(avatarFile);//
//                loadPhoto();
                progressDialog = new ProgressDialog(activity);
                progressDialog.setMessage(activity.getString(R.string.msg_loading));
                progressDialog.setCancelable(false);
                progressDialog.show();
                ConvertImageTask.adjustImageOrientationAsync(
                        avatarFile.getPath(), avatarFile,
                        new ConvertImageTask.ImageCompressorListener() {
                            @Override
                            public void onImageCompressed(final Bitmap bitmap, final Uri uri) {
                                dataUri = uri;
                                loadPhoto();
                            }

                            @Override
                            public void onError() {
                                loadPhoto();
                            }
                        }, imageSize, imageMaxWidth, imageMaxHeight);

//                avatarFile = null;
            }
        }
    }

    private static Uri getUriFromFile(Activity ctx, File photoFile) {
        return FileProvider.getUriForFile(ctx,
                GenericFileProvider.NAME,
                photoFile);
    }

    public boolean onResumeCheckIfIsForMe() {
        if (isComingFromAskForPermission) {
            isComingFromAskForPermission = false;
            return true;
        }
        return false;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (isComingForTakePicture) {
                        isComingForTakePicture = false;
                        takeImage();
                    } else {
                        pickImage();
                    }
                } else {
                    showPermissionExplanationDialog();
                }
            }
        }
    }

    public void destroy() {
        dismissDialog();
    }

    /**
     * End
     */

    /**
     * If calling it from fragment ,pass it here so onRequestPermissionsResult and onActivityResult
     * are received properly
     *
     * @param fragment the fragment
     */
    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public View lastClickedView = null;

    /**
     * Set click listener on this view to show start dialog
     * @param clickView any View
     */
    public void setClickToShowStartDialog(View clickView) {
        if (clickView == null) return;
        clickView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                lastClickedView = view1;
                if (photoDialog != null) {
                    photoDialog.dismiss();
                }
                photoDialog = new AlertDialog.Builder(activity).create();
                View view = activity.getLayoutInflater().inflate(resDialog, null);
                photoDialog.setView(view);
                view.findViewById(idBtnImage).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismissDialog();
                        pickImage();
                    }
                });
                view.findViewById(idBtnPhoto).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismissDialog();
                        takeImage();
                    }
                });
                view.findViewById(idBtnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismissDialog();
                    }
                });
                if (callback != null) {
                    callback.onDialogInflated(view);
                }

                photoDialog.show();
            }
        });
    }

    public void dismissDialog() {
        if (photoDialog != null) {
            photoDialog.dismiss();
        }
    }

    private void loadPhoto() {
        if (dataUri != null) {
            if (isJustSelectFile()) {//this is just select file option
                uploadFile();
                hideProgress();
                return;
            }
            int width = imageView.getWidth();
            int height = imageView.getHeight();
            RequestCreator rc;
//            Picasso.with(activity).setLoggingEnabled(true);
            rc = Picasso.with(activity).load(dataUri);


            rc.resize(width, height).centerInside()
                    .error(resErrorImage)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            uploadFile();
                            avatarFile = null;
                            hideProgress();
                        }

                        @Override
                        public void onError() {
//                            if (avatarFile == null) {
//                                avatarFile = FileUtils.getFile(activity, dataUri);
//                            }
                            if (avatarFile != null) {
                                Picasso.with(activity).load(avatarFile).into(imageView, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        uploadFile();
//                                        avatarFile = null;
                                        hideProgress();
                                    }

                                    @Override
                                    public void onError() {
                                        avatarFile = null;
                                        hideProgress();
                                    }
                                });
                            } else {
                                hideProgress();
                            }
                        }
                    });
        } else {
            hideProgress();
        }
    }

    private void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    /**
     * type ocl to fast get new setOnClickListener, rr/rc/rs to set ripple
     */
    private void setListeners() {
        if (clickView != null) {
            setClickToShowStartDialog(clickView);
        }

        if(clickViews != null){
            for (View view : clickViews) {
                setClickToShowStartDialog(view);
            }
        }

        if (cameraView != null) {
            cameraView.setOnClickListener(new RippleOnClickListener() {
                @Override
                public void onClickListener(View clickedView) {
                    takeImage();
                }
            });
        }

        if (fileView != null) {
            fileView.setOnClickListener(new RippleOnClickListener() {
                @Override
                public void onClickListener(View clickedView) {
                    pickImage();
                }
            });
        }
    }

    private void takeImage() {
//        if (isJustSelectFile()) {
//            activity.startActivityForResult(createGetVideoIntent(), GET_VIDEO);
//        } else
        if (ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            avatarFile = dispatchTakePictureIntent(activity, fragment, REQUEST_IMAGE_CAPTURE);
        } else {
            isComingForTakePicture = true;
            requestPermissions();
        }
    }

    private void pickImage() {
        isComingForTakePicture = false;
        if (ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            avatarFile = null;
            if (fragment == null) {
                activity.startActivityForResult(createGetImageIntent(), GET_IMAGE);
            } else {
                fragment.startActivityForResult(createGetImageIntent(), GET_IMAGE);
            }
        } else {
            requestPermissions();
        }
    }

    private void showPermissionExplanationDialog() {
        if (permissionsDialog == null) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
//            dialog.setMessage(R.string.no_read_write_permission);
            dialog.setPositiveButton(R.string.photo_permissions_btn_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    requestPermissions();
                }
            });
            dialog.setNegativeButton(R.string.photo_permissions_btn_no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    finish();
                }
            });
            dialog.setCancelable(false);
            permissionsDialog = dialog.create();
        }
        if (isComingForTakePicture) {
            permissionsDialog.setMessage(activity.getString(resMsgNoCameraPermission));
        } else {
            permissionsDialog.setMessage(activity.getString(resMsgNoDiskPermission));
        }
        if (!permissionsDialog.isShowing()) {
            permissionsDialog.show();
        }
    }

    private void requestPermissions() {
        isComingFromAskForPermission = true;
        if (fragment != null) {
            fragment.requestPermissions(new String[] { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE },
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE },
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
    }

    public void setResizeSavedFileToImageViewSize(boolean resizeSavedFileToImageViewSize) {
        this.resizeSavedFileToImageViewSize = resizeSavedFileToImageViewSize;
    }

    private String fileName = null, dir = null;
    private boolean inLocalStorage = true;

    public void setInLocalStorage(boolean inLocalStorage) {
        this.inLocalStorage = inLocalStorage;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public int getImageSize() {
        return imageSize;
    }

    public void setImageSize(int imageSize) {
        this.imageSize = imageSize;
    }

    public int getImageMaxWidth() {
        return imageMaxWidth;
    }

    public void setImageMaxWidth(int imageMaxWidth) {
        this.imageMaxWidth = imageMaxWidth;
    }

    public int getImageMaxHeight() {
        return imageMaxHeight;
    }

    public void setImageMaxHeight(int imageMaxHeight) {
        this.imageMaxHeight = imageMaxHeight;
    }

    private void uploadFile() {
        if (callback == null) {
            return;
        }
        if (ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            if (dataUri == null) {
                return;
            }
            boolean fileExist = false;
            File file = avatarFile;
            if (avatarFile != null) {
                fileExist = avatarFile.exists();
            } else {
                file = FileUtils.getFile(activity, dataUri);
                fileExist = file != null && file.exists();// || FileUtils.isDocsUri(dataUri);
            }
            if (fileExist) {//is not the error image
                if (isJustSelectFile()) {
                    callback.onFileReadyToUpload(avatarFile != null ? avatarFile : file, null);
                } else if (resultAsBase64String) {
                    Bitmap bm = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    callback.onFileReadyToUpload(null, parseImageToBase64(bm));
                } else {
                    FileHelper fileHelper = new FileHelper();
                    File newFile = new File(fileHelper.saveImageToFile(
                            ((BitmapDrawable) imageView.getDrawable()).getBitmap(),
                            resizeSavedFileToImageViewSize ? imageView.getWidth() : 0,//0 no resizing
                            imageView.getHeight(),
                            inLocalStorage, fileName, dir));
                    // delete file since is new one returned
                    if (avatarFile != null && avatarFile.exists()) {
                        avatarFile.delete();
                    }
                    callback.onFileReadyToUpload(newFile, null);
                }
            }
        } else {
            showPermissionExplanationDialog();
        }
    }

    private boolean isJustSelectFile() {
        return imageView == null || imageView.getVisibility() == View.GONE;
    }

    public static String parseImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }


    public static Intent createGetImageIntent() {
        // Implicitly allow the user to select a particular kind of data
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT < 19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        }
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.setType("image/*");
        return intent;
    }

    public static Intent createGetVideoIntent() {
        // Implicitly allow the user to select a particular kind of data
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT < 19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        }
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("video/*");
        return intent;
    }

    public static File dispatchTakePictureIntent(Activity activity, Fragment fragment, int REQUEST_IMAGE_CAPTURE) {
        if (checkIfCameraExist(activity)) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile(activity, false);
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    Log.e("dispatchTakePictureInt", ex.toString());
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri uri = FileProvider.getUriForFile(activity,
                            GenericFileProvider.NAME,
                            photoFile);
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//                    takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
//                            Uri.fromFile(photoFile));

                    if (fragment == null) {
                        activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    } else {
                        fragment.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                    return photoFile;
                }
            }
        }
        return null;
    }


    public static boolean checkIfCameraExist(Context ctx) {
        PackageManager pm = ctx.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public static File createImageFile(Context ctx, boolean inExternalMemory) throws IOException {
        // Create an image file NAME
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
//        String imageFileName = "avatar";
        File storageDir;
        if (inExternalMemory) {
            storageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES),
                    BaseLibraryConfiguration.getInstance().getPublicFileFolder() != null
                            ? BaseLibraryConfiguration.getInstance().getPublicFileFolder()
                            : GenericFileProvider.FOLDER_NAME);
        } else {
            storageDir = ctx.getFilesDir();
        }
        LogUtils.i(storageDir.getName(), storageDir.mkdirs() + "");
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
        File image = new File(storageDir, imageFileName + ".jpg");

        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    public interface PhotoEvents {
        void onFileReadyToUpload(File fileToUpload, String base64);

        void onDialogInflated(View view);
    }
}
