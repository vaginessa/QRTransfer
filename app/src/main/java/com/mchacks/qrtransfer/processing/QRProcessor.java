package com.mchacks.qrtransfer.processing;
import com.mchacks.qrtransfer.util.Constants;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.File;
import java.util.LinkedList;


public class QRProcessor implements QRInterface {

    public static BitMatrix generateQRCodeBitMatrx(String myCodeText) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        int size = Constants.qrDimension;
        return qrCodeWriter.encode(myCodeText, BarcodeFormat.QR_CODE, size, size);
    }

    public static Bitmap bitMatrixToBitmap(BitMatrix b)
    {
        int width = b.getWidth();
        Bitmap bmp = Bitmap.createBitmap(width, width, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < width; y++) {
                bmp.setPixel(y, x, b.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }

    /**
     * Encode a file as QR code images.
     *
     * @param file a file that we want to convert to QR codes
     * @return a linked list of QR code images
     */
    @Override
    public LinkedList<Image> fileToQrCodes(File file) {
        return null;
    }

    /**
     * Given a video file, scan for QR codes and return the decoded file.
     *
     * @param videoFile a file that is a video
     * @return the decoded file
     */
    @Override
    public File videoToFile(File videoFile) {
        return null;
    }
}
