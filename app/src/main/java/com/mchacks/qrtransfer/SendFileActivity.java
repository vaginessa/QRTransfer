package com.mchacks.qrtransfer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.mchacks.qrtransfer.processing.BitmapProcessor;
import com.mchacks.qrtransfer.processing.QRProcessor;
import com.mchacks.qrtransfer.util.Constants;
import com.nononsenseapps.filepicker.FilePickerActivity;


import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;


public class SendFileActivity extends AppCompatActivity {

    private static final int FILE_CODE = 0;

    LinkedList<BitMatrix> bm = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_file);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // This always works
                Intent i = new Intent(view.getContext(), FilePickerActivity.class);
                // This works if you defined the intent filter
                // Intent i = new Intent(Intent.ACTION_GET_CONTENT);

                // Set these depending on your use case. These are the defaults.
                i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
                i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
                i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);

                // Configure initial directory by specifying a String.
                // You could specify a String like "/storage/emulated/0/", but that can
                // dangerous. Always use Android's API calls to get paths to the SD-card or
                // internal memory.
                i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());

                startActivityForResult(i, FILE_CODE);


//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        FloatingActionButton playSlideshowButton = (FloatingActionButton) findViewById(R.id.playSlideshowButton);
        playSlideshowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    playSlideShow();
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILE_CODE && resultCode == Activity.RESULT_OK) {
            if (data.getBooleanExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false)) {
                // For JellyBean and above
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ClipData clip = data.getClipData();

                    if (clip != null) {
                        for (int i = 0; i < clip.getItemCount(); i++) {
                            Uri uri = clip.getItemAt(i).getUri();
                            // Do something with the URI
                            handleLoadedFileUri(uri);
                        }
                    }
                    // For Ice Cream Sandwich
                } else {
                    ArrayList<String> paths = data.getStringArrayListExtra
                            (FilePickerActivity.EXTRA_PATHS);

                    if (paths != null) {
                        for (String path : paths) {
                            Uri uri = Uri.parse(path);
                            // Do something with the URI
                            handleLoadedFileUri(uri);
                        }
                    }
                }

            } else {
                Uri uri = data.getData();
                // Do something with the URI
                handleLoadedFileUri(uri);
            }
        }
    }

    /**
     * After the file URI has been chosen, do what happens next
     *
     * @param uri
     */
    void handleLoadedFileUri(Uri uri) {
        File f1 = new File(uri.getPath());
        this.bm = QRProcessor.fileToQrCodes(f1);
    }

    /**
     * Play the file slideshow.
     *
     * @throws WriterException
     */
    void playSlideShow() throws WriterException {
        if (this.bm != null) {
            final ImageView qrCodeImageView = (ImageView) findViewById(R.id.imageView);
            final TextView statusText = (TextView) findViewById(R.id.statusText);

            Handler[] handlers = new Handler[this.bm.size()];

            final int totalImages = this.bm.size();
            for (int i = 0; i < this.bm.size(); i++) {
                handlers[i] = new Handler();
                final Bitmap qrCode = QRProcessor.bitMatrixToBitmap(this.bm.get(i));
                final int current = i + 1;
                handlers[i].postDelayed(new Runnable() {
                    public void run() {
                        // Set the status text
                        statusText.setText(String.format("%d/%d", current, totalImages));
                        // Set the
                        qrCodeImageView.setImageBitmap(qrCode);
                    }
                }, i *3000);
            }
        }
    }

}