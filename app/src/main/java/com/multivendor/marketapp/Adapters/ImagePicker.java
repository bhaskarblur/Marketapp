package com.multivendor.marketapp.Adapters;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.multivendor.marketapp.R;

import java.io.File;

import static android.app.Activity.RESULT_OK;

/**
 * <h1>ImagePicker, helps to pick images from gallery and camera</h1>
 * <p>
 * This class is helper class which helps choosing images from camera and gallery.
 * For now it works with the help of {@link BitmapUtils} class. So use this class you must
 * have to include {@code BitmapUtils} class.
 * </p>
 * <p>
 * To use this class create an instance of class with your activity or fragment.
 * Then feedback result of class {@link androidx.appcompat.app.AppCompatActivity# onActivityResult(int, int, Intent)}
 * or fragment {@link Fragment#onActivityResult}
 * into {@link #onActivityResult(int, int, Intent)} of {@code ImagePicker}
 * </p>
 * <b>Sample code</b>
 * <pre>
 * {@code
 * ImagePicker imagePicker = new ImagePicker(this);
 * imagePicker.onActivityResult(requestCode, resultCode, data);
 * }
 * </pre>
 *
 * @author Sanjay Kumar
 * @version 1.0
 * @since 09.12.2021
 */

public class ImagePicker implements BitmapUtils.BitmapCallback {
    private static final String CAMERA_PICTURE_FILE = "camera_image.jpg";

    private static final int INTENT_GALLERY = 1023;
    private static final int INTENT_CAMERA = 1022;

    private int requestCode = 70;
    private Activity mActivity;
    private Fragment mFragment;
    private String mCameraImagePath = null;
    private boolean isFragment = false;
    private BitmapUtils.BitmapCallback mBitmapCallback = null;
    private String gallery, camera;

    /**
     * <h1>Constructor of class with activity</h1>
     *
     * @param activity instance of activity
     */
    public ImagePicker(Activity activity) {
        init(activity);
    }

    /**
     * <h1>Constructor of class with fragment</h1>
     *
     * @param fragment instance of fragment
     */
    public ImagePicker(Fragment fragment) {
        mFragment = fragment;
        isFragment = true;
        init(fragment.getActivity());
    }

    private void init(Activity activity) {
        mActivity = activity;
        mCameraImagePath = new File(mActivity.getFilesDir(), CAMERA_PICTURE_FILE).getAbsolutePath();
    }

    public void setBitmapCallback(BitmapUtils.BitmapCallback bitmapCallback) {
        this.mBitmapCallback = bitmapCallback;
    }

    public void pickImage(int requestCode) {
        this.requestCode = requestCode;
        pickImage();
    }

    public void pickImage() {
        gallery = mActivity.getString(R.string.gallery_label);
        camera = mActivity.getString(R.string.camera_label);

        final String[] options = {gallery, camera};
        new AlertDialog.Builder(mActivity).setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        galleryIntent(mActivity);
                        break;
                    case 1:
                        cameraIntent(mActivity);
                        break;
                }
            }
        }).setTitle(mActivity.getString(R.string.pick_image_lable)).show();
    }

    public void galleryIntent(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            if (isFragment) {
                mFragment.startActivityForResult(intent, INTENT_GALLERY);
            } else {
                activity.startActivityForResult(intent, INTENT_GALLERY);
            }
        } else {
            if (mBitmapCallback != null) {
                mBitmapCallback.onBitmapFailed(requestCode);
            }
        }
    }

    public void cameraIntent(Activity activity) {
        Uri fileUri = FileProvider.getUriForFile(activity, activity.getPackageName(), new File(mCameraImagePath));
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        if (intent.resolveActivity(mActivity.getPackageManager()) != null) {
            if (isFragment) {
                mFragment.startActivityForResult(intent, INTENT_CAMERA);
            } else {
                activity.startActivityForResult(intent, INTENT_CAMERA);
            }
        } else {
            mBitmapCallback.onBitmapFailed(requestCode);
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        BitmapUtils.getBitmapFromGallery(requestCode, mActivity, data, this);
    }

    private void onCapturedImageResult(String filePath) {
        BitmapUtils.getBitmapFromCamera(requestCode, mActivity, filePath, this);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == INTENT_GALLERY) {
            onSelectFromGalleryResult(data);
        } else if (requestCode == INTENT_CAMERA) {
            onCapturedImageResult(mCameraImagePath);
        }
    }

    @Override
    public void onBitmapReady(int requestCode, String filePath) {
        if (mBitmapCallback != null) {
            mBitmapCallback.onBitmapReady(requestCode, filePath);
        }
    }

    @Override
    public void onBitmapFailed(int requestCode) {
        if (mBitmapCallback != null) {
            mBitmapCallback.onBitmapFailed(requestCode);
        }
    }
}
