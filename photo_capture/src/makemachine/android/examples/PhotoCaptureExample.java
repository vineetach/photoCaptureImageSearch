package makemachine.android.examples;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class PhotoCaptureExample extends Activity 
{
    protected ImageView mPhotoImage;
    protected EditText mPostingEditText;
    protected String mImageFilePath;
    private View mRemoveImageButton;
    private View mCameraButton;

    protected boolean _taken;

    protected static final String PHOTO_TAKEN = "photo_taken";
    
    ArrayList<String> mSelectedSources = new ArrayList<String>();

    private static final int LOAD_IMAGE_FROM_GALLERY_REQUEST_CODE = 1;
    private static final int TAKE_PICTURE_FROM_CAMERA_REQUEST_CODE = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        
        setContentView(R.layout.social_posting_ui);

        mPhotoImage = ( ImageView ) findViewById( R.id.image );
        mPostingEditText = ( EditText ) findViewById( R.id.postingEditText );

        mCameraButton = findViewById(R.id.camera_button);
        mCameraButton.setOnClickListener(new CameraButtonClickHandler());

        mRemoveImageButton = findViewById(R.id.removeImageButton);
        mRemoveImageButton.setOnClickListener(new RemoveImageButtonClickHandler());

        Button shareButton = ( Button ) findViewById( R.id.shareButton );
        shareButton.setOnClickListener( new ShareButtonClickHandler() );

        mImageFilePath = Environment.getExternalStorageDirectory() + "/images/make_machine_example.jpg";
        
        View source1_container = findViewById(R.id.source1_container);
        SourceContainerClickHandler source1ClickHandler = new SourceContainerClickHandler();
        source1ClickHandler.sourceTickViewId = R.id.source1_tick_icon;
        source1ClickHandler.sourceImageViewId = R.id.source1_icon;
        source1_container.setOnClickListener(source1ClickHandler);
        setGrayCaleToIcon(R.id.source1_icon);
        
        View source2_container = findViewById(R.id.source2_container);
        SourceContainerClickHandler source2ClickHandler = new SourceContainerClickHandler();
        source2ClickHandler.sourceTickViewId = R.id.source2_tick_icon;
        source2ClickHandler.sourceImageViewId = R.id.source2_icon;
        source2_container.setOnClickListener(source2ClickHandler);
        setGrayCaleToIcon(R.id.source2_icon);
        
        View source3_container = findViewById(R.id.source3_container);
        SourceContainerClickHandler source3ClickHandler = new SourceContainerClickHandler();
        source3ClickHandler.sourceTickViewId = R.id.source3_tick_icon;
        source3ClickHandler.sourceImageViewId = R.id.source3_icon;
        source3_container.setOnClickListener(source3ClickHandler);
        setGrayCaleToIcon(R.id.source3_icon);
    }

    public class CameraButtonClickHandler implements View.OnClickListener {
        public void onClick( View view ) {
            showDialog();
        }
    }

    public class ShareButtonClickHandler implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
        }
    }
    
    public class SourceContainerClickHandler implements View.OnClickListener {
        String sourceName;
        int sourceTickViewId;
        int sourceImageViewId;
        
        public void onClick( View view ) {
            View tickView = findViewById(sourceTickViewId);
            if(mSelectedSources.contains(sourceName)) {
                mSelectedSources.remove(sourceName);
                tickView.setVisibility(View.GONE);
                
                ImageView iconView = (ImageView)findViewById(sourceImageViewId);
                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(0);
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                iconView.setColorFilter(filter);
                view.setBackgroundResource(R.color.app_page_background_color);

            } else {
                tickView.setVisibility(View.VISIBLE);
                mSelectedSources.add(sourceName);
                
                ImageView iconView = (ImageView)findViewById(sourceImageViewId);
                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(1);
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                iconView.setColorFilter(filter);
                view.setBackgroundResource(R.color.transparent_blue_overlay);
            }
        }
    }
    
    private void setGrayCaleToIcon(int sourceImageViewId) {
        ImageView iconView = (ImageView)findViewById(sourceImageViewId);
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        iconView.setColorFilter(filter);
    }

    public class RemoveImageButtonClickHandler implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            mPhotoImage.setImageDrawable(null);
            mRemoveImageButton.setVisibility(View.INVISIBLE);
            mCameraButton.setVisibility(View.VISIBLE);
        }

    }

    protected void startCameraActivity() {
        if(!isThereAnAppAndCameraToTakePictures()) {
            return;
        }
        
        File file = new File( mImageFilePath );
        file.getParentFile().mkdirs();
        try {
            file.createNewFile();
        } catch (IOException e) {
        }
        Uri outputFileUri = Uri.fromFile( file );

        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE );
        intent.putExtra( MediaStore.EXTRA_OUTPUT, outputFileUri );

        startActivityForResult( intent, TAKE_PICTURE_FROM_CAMERA_REQUEST_CODE );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
        if(requestCode == TAKE_PICTURE_FROM_CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            onPhotoTaken();
        } else if(requestCode == LOAD_IMAGE_FROM_GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            onPhotoSelectedFromGallery(data);
        }
    }

    protected void onPhotoTaken()
    {
        try {
            _taken = true;

            Bitmap unscaledBitmap = ScalingUtilities.decodeFile(mImageFilePath, mPhotoImage.getWidth(), mPhotoImage.getHeight(), ScalingUtilities.ScalingLogic.FIT);
            Bitmap scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, mPhotoImage.getWidth(), mPhotoImage.getHeight(), ScalingUtilities.ScalingLogic.FIT);
            unscaledBitmap.recycle();
            mPhotoImage.setImageBitmap(scaledBitmap);

        } catch (OutOfMemoryError e) {
            Toast.makeText(this, "You ran out of memory", Toast.LENGTH_LONG).show();
        }

        mRemoveImageButton.setVisibility(View.VISIBLE);
        mCameraButton.setVisibility(View.GONE);
    }

    private void startGalleryActivity() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, LOAD_IMAGE_FROM_GALLERY_REQUEST_CODE);
    }
    
    private void onPhotoSelectedFromGallery(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        
        try {
            _taken = true;

            Bitmap unscaledBitmap = ScalingUtilities.decodeFile(picturePath, mPhotoImage.getWidth(), mPhotoImage.getHeight(), ScalingUtilities.ScalingLogic.FIT);
            Bitmap scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, mPhotoImage.getWidth(), mPhotoImage.getHeight(), ScalingUtilities.ScalingLogic.FIT);
            unscaledBitmap.recycle();
            mPhotoImage.setImageBitmap(scaledBitmap);

        } catch (OutOfMemoryError e) {
            Toast.makeText(this, "You ran out of memory", Toast.LENGTH_LONG).show();
        }

        mRemoveImageButton.setVisibility(View.VISIBLE);
        mCameraButton.setVisibility(View.GONE);
    }

    @Override 
    protected void onRestoreInstanceState( Bundle savedInstanceState) {
        Log.i( "MakeMachine", "onRestoreInstanceState()");
        if( savedInstanceState.getBoolean( PhotoCaptureExample.PHOTO_TAKEN ) ) {
            onPhotoTaken();
        }
    }

    @Override
    protected void onSaveInstanceState( Bundle outState ) {
        outState.putBoolean( PhotoCaptureExample.PHOTO_TAKEN, _taken );
    }

    private void showDialog() {
        final CharSequence[] actions = new CharSequence[2];
        actions[0] = "Take Picture";
        actions[1] = "Load Picture From Library";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(actions, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if(actions[item].equals("Take Picture")) {
                    startCameraActivity();
                } else if(actions[item].equals("Load Picture From Library")) {
                    startGalleryActivity();
                }
            }
        });

        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(true);
        alert.show();
    }
    
    private boolean isThereAnAppAndCameraToTakePictures()
    {
        boolean hasCamera = true;
        boolean hasCameraApp = true;
        PackageManager packageManager = getPackageManager();
        if(packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) == false){
            hasCamera = false;
        }
        
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY); {
            if(list.size() <= 0) {
                hasCameraApp = false;
            }
        }
        
        return (hasCamera && hasCameraApp);
    }
    
}