package es.guiguegon.gallerymodule.helpers;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import es.guiguegon.gallerymodule.model.GalleryMedia;
import es.guiguegon.gallerymodule.utils.FileUtils;

/**
 * Created by guiguegon on 23/10/2015.
 */
public class CameraHelper {

    private static final int REQUEST_CODE_CAMERA = 15;
    private static final String MIME_TYPE_IMAGE = "image/jpeg";
    private static final String MIME_TYPE_VIDEO = "video/mp4";
    private static CameraHelper mInstance;
    private final String TAG = "[" + this.getClass().getSimpleName() + "]";
    private Uri mediaUri;
    private String mimeType;
    private Context context;

    private CameraHelper() {
    }

    public static CameraHelper getInstance() {
        if (mInstance == null) {
            mInstance = new CameraHelper();
        }
        return mInstance;
    }

    public void onCreate(Context context) {
        this.context = context;
    }

    public void onDestroy() {
        this.context = null;
    }

    public void dispatchGetPictureIntent(Activity activity) {
        try {
            Intent cameraIntent = new Intent();
            cameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            ContentValues values = new ContentValues(1);
            mimeType = MIME_TYPE_IMAGE;
            values.put(MediaStore.Images.Media.MIME_TYPE, mimeType);
            mediaUri = Uri.fromFile(FileUtils.getOutputMediaFile(FileUtils.MEDIA_TYPE_IMAGE));
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mediaUri);
            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            activity.startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);
        } catch (Exception e) {
            Log.e(TAG, "[dispatchGetPictureIntent]", e);
        }
    }

    public void dispatchGetVideoIntent(Activity activity) {
        try {
            Intent cameraIntent = new Intent();
            cameraIntent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
            ContentValues values = new ContentValues(1);
            mimeType = MIME_TYPE_VIDEO;
            values.put(MediaStore.Images.Media.MIME_TYPE, mimeType);
            mediaUri = Uri.fromFile(FileUtils.getOutputMediaFile(FileUtils.MEDIA_TYPE_VIDEO));
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mediaUri);
            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            activity.startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);
        } catch (Exception e) {
            Log.e(TAG, "[dispatchGetPictureIntent]", e);
        }
    }

    public GalleryMedia onGetPictureIntentResults(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == REQUEST_CODE_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                galleryAddPic(mediaUri);
                return new GalleryMedia().setMediaUri(mediaUri.getPath()).setMimeType(mimeType);
            }
        }
        return null;
    }

    private void galleryAddPic(Uri mediaUri) {
        if (context != null) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(mediaUri);
            context.sendBroadcast(mediaScanIntent);
        }
    }
}
