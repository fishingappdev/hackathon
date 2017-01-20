package com.example.dmiadmin.hackathonapp.QRUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.example.dmiadmin.hackathonapp.R;
import com.google.zxing.WriterException;

import java.io.File;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

import static android.content.ContentValues.TAG;

/**
 * Created by Swati on 1/20/2017.
 */

public class QRGenerator {

    public void generateQRImage(Context context, String inputValue) {
        Bitmap bitmap;
        QRGEncoder qrgEncoder = new QRGEncoder(inputValue, null, QRGContents.Type.TEXT, (int) context.getResources().getDimension(R.dimen.QR_IMAGE_DIMEN));
        try {
            // Getting QR-Code as Bitmap
            bitmap = qrgEncoder.encodeAsBitmap();
            // Setting Bitmap to ImageView
            File file = new File(Environment.getExternalStorageDirectory() + "/" + context.getResources().getString(R.string.app_name));
            String savePath = file.getPath();
            QRGSaver.save(savePath, inputValue, bitmap, QRGContents.ImageType.IMAGE_JPEG);
        } catch (WriterException e) {
            Log.v(TAG, e.toString());
        }
    }
}
