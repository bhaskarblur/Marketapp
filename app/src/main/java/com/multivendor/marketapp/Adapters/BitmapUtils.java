package com.multivendor.marketapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Locale;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Vishvendra.Singh@Brsoftech on 6/3/17.
 */

public class BitmapUtils {
    private static final String TAG = "BitmapUtils";
    private static ThreadPoolExecutor sThreadPool = new ThreadPoolExecutor(0, 2, 10, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>());
    private static Handler sUiHandler = new Handler(Looper.getMainLooper());

    private BitmapUtils() {
    }

    public static void getBitmapFromGallery(int requestCode, Context context, final Intent data, BitmapCallback callback) {
        final WeakReference<Context> contextWeakReference = new WeakReference<Context>(context);
        final WeakReference<BitmapCallback> callbackWeakReference = new WeakReference<BitmapCallback>(callback);

        sThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                Bitmap bm = null;
                Context ctx = contextWeakReference.get();
                if (ctx == null) {
                    Log.i(TAG, "run: " + "ctx is null");
                    return;
                }

                if (data == null) {
                    sUiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            BitmapCallback bitmapCallback = callbackWeakReference.get();
                            if (bitmapCallback != null) {
                                bitmapCallback.onBitmapFailed(requestCode);
                            } else {
                                Log.i(TAG, "run: " + "57: bitmap callback is null");
                            }
                        }
                    });
                    return;
                }

                try {
                    bm = MediaStore.Images.Media.getBitmap(ctx.getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ctx = contextWeakReference.get();
                if (ctx == null) {
                    Log.i(TAG, "run: " + "ctx is null");
                    return;
                }

                if (bm == null) {
                    sUiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            BitmapCallback bitmapCallback = callbackWeakReference.get();
                            if (bitmapCallback != null) {
                                bitmapCallback.onBitmapFailed(requestCode);
                            } else {
                                Log.i(TAG, "run: " + "84: bitmapCallback is null");
                            }
                        }
                    });
                    return;
                }

                File dir = ctx.getFilesDir();
                String fileName = String.format(Locale.getDefault(), "%d.jpg", System.currentTimeMillis());
                final File imageFile = new File(dir, fileName);
                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(imageFile);
                    bm.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
                    saveImageWithoutRotation(imageFile.getAbsolutePath());

                    sUiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            BitmapCallback bitmapCallback = callbackWeakReference.get();
                            if (bitmapCallback != null) {
                                bitmapCallback.onBitmapReady(requestCode, imageFile.getAbsolutePath());
                            } else {
                                Log.i(TAG, "run: " + "107: bitmap callback is null");
                            }
                        }
                    });

                } catch (IOException e) {
                    Log.i(TAG, "run: " + "io exception");
                    e.printStackTrace();
                } finally {
                    bm.recycle();
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        });
    }

    public static void getBitmapFromCamera(int requestCode, Context context, final String filePath, BitmapCallback callback) {
        final WeakReference<Context> contextWeakReference = new WeakReference<Context>(context);
        final WeakReference<BitmapCallback> callbackWeakReference = new WeakReference<BitmapCallback>(callback);

        sThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                Bitmap bm = null;
                Context ctx = contextWeakReference.get();
                if (ctx == null) {
                    Log.i(TAG, "run: " + "ctx is null");
                    return;
                }


                int rotation = 0;
                try {
                    rotation = getRotation(filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                bm = getBitmap(new File(filePath), 1024, 768);

                switch (rotation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        bm = rotateBitmap(bm, 90);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        bm = rotateBitmap(bm, 180);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        bm = rotateBitmap(bm, 270);
                        break;
                }
                ctx = contextWeakReference.get();
                if (ctx == null) {
                    Log.i(TAG, "run: " + "ctx is null");
                    return;
                }

                if (bm == null) {
                    sUiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            BitmapCallback bitmapCallback = callbackWeakReference.get();
                            if (bitmapCallback != null) {
                                bitmapCallback.onBitmapFailed(requestCode);
                            } else {
                                Log.i(TAG, "run: " + "84: bitmapCallback is null");
                            }
                        }
                    });
                    return;
                }

                File dir = ctx.getFilesDir();
                String fileName = String.format(Locale.getDefault(), "%d.jpg", System.currentTimeMillis());
                final File imageFile = new File(dir, fileName);
                FileOutputStream outputStream = null;
                try {
                    outputStream = new FileOutputStream(imageFile);
                    bm.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);

                    sUiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            BitmapCallback bitmapCallback = callbackWeakReference.get();
                            if (bitmapCallback != null) {
                                bitmapCallback.onBitmapReady(requestCode, imageFile.getAbsolutePath());
                            } else {
                                Log.i(TAG, "run: " + "107: bitmap callback is null");
                            }
                        }
                    });

                } catch (IOException e) {
                    Log.i(TAG, "run: " + "io exception");
                    e.printStackTrace();
                } finally {
                    bm.recycle();
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        });
    }

    public static void saveImageWithoutRotation(String filePath) throws IOException {
        int rotation = getRotation(filePath);
        Bitmap bitmap = getBitmap(new File(filePath), 1024, 768);
        Bitmap bitmapAfterRotation = null;
        switch (rotation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                bitmapAfterRotation = rotateBitmap(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                bitmapAfterRotation = rotateBitmap(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                bitmapAfterRotation = rotateBitmap(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:

            default:
                break;
        }

        if (bitmapAfterRotation != null) {
            bitmap.recycle();
        } else {
            bitmapAfterRotation = bitmap;
        }

        FileOutputStream outputStream = new FileOutputStream(filePath);
        bitmapAfterRotation.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
        outputStream.close();
        bitmapAfterRotation.recycle();
    }

    public static int getRotation(String filePath) throws IOException {
        ExifInterface ei = new ExifInterface(filePath);
        return ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
    }

    public static Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
        source.recycle();
        return bitmap;
    }

    public static Bitmap getBitmap(File file, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);

        options.inJustDecodeBounds = false;
        if (options.outWidth > width || options.outHeight > height) {
            options.inSampleSize = calculateInSampleSize(options, width, height);
        }
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public interface BitmapCallback {
        void onBitmapReady(int requestCode, String filePath);

        void onBitmapFailed(int requestCode);
    }
}