package com.mchacks.qrtransfer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.mchacks.qrtransfer.util.CameraPreview;

import static com.mchacks.qrtransfer.util.CameraPreview.getCameraInstance;

public class ReceiveFileActivity extends AppCompatActivity {

    public static final int MEDIA_TYPE_IMAGE = 1;

    Camera mCamera = null;
    Camera.PictureCallback mPicture = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_file);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create an instance of Camera
        mCamera = getCameraInstance();
        mCamera.setDisplayOrientation(90);
        // Create our Preview view and set it as the content of our activity.
        CameraPreview mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.cameraPreview);
        preview.addView(mPreview);

        mPicture = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Bitmap image = BitmapFactory.decodeByteArray(data, 0, data.length);
//                BinaryBitmap binaryBitmap = BitmapProcessor.toBinaryBitmap(image);
//                QRCodeReader reader = new QRCodeReader();
//                try {
//                    Result result = reader.decode(binaryBitmap);
//                    String text = result.getText();
//
//                    Log.v("QRTransfer", text);
////                    TextView debugText = (TextView) findViewById(R.id.debugTextView);
////                    debugText.setText(text);
//                } catch (NotFoundException e) {
//                    e.printStackTrace();
//                } catch (ChecksumException e) {
//                    e.printStackTrace();
//                } catch (FormatException e) {
//                    e.printStackTrace();
//                }
            }
        };

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCamera.takePicture(null, null, mPicture);
//                // If the user can record a video, let them.  Otherwise display a message
//                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
//                    Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//                    if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
//                        startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
//                    }
//                } else {
//                    Snackbar.make(view, "Please enable video recording permissions!", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
//                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}


