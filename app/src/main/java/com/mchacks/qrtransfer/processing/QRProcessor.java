package com.mchacks.qrtransfer.processing;
import com.google.common.io.Files;
import com.mchacks.qrtransfer.util.Constants;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;


public class QRProcessor  {

    public static BitMatrix generateQRCodeBitMatrix(String myCodeText) throws WriterException {
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
     * @return a linked list of QR code BitMatrices
     */
    public static LinkedList<BitMatrix> fileToQrCodes(File file) {
        String encoded_string = parse_file(file);

        LinkedList<BitMatrix> file_code = new LinkedList<BitMatrix>();
        int str_len = encoded_string.length();
        for (int i = 0; i < str_len; i += (Constants.byteDensity + 1))
        {
            int end = i + Constants.byteDensity;
            if (str_len <= end)
            {
                end = str_len - 1;
            }
            if(i >= end){
                break;
            }

            String sub = encoded_string.substring(i, end);
            try {
                BitMatrix bmp = QRProcessor.generateQRCodeBitMatrix(sub);
                file_code.add(bmp);
            } catch (WriterException e)
            {
                e.printStackTrace();
            }
        }
        return file_code;
    }

    /**
     * Given a video file, scan for QR codes and return the decoded file.
     *
     * @param videoFile a file that is a video
     * @return the decoded file
     */
    public File videoToFile(File videoFile) {
        return null;
    }

    //Parse a file into an encoded String of bytes.
    public static String parse_file(File f) {
        try {
            byte[] file_bytes = Files.toByteArray(f);
            String encoded_string = new String(file_bytes, "ISO-8859-1");
            return encoded_string;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    //Parse a String into an array of bytes.
    public static byte[] parse_string(String s)
    {
        byte[] data;
        try{
            data = s.getBytes("ISO-8859-1");
            return data;
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
