package com.example.bubbletracksapp;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


/**
 * Hold ability to create QR code
 * Organizer use
 */
public class QRGenerator {

    public Bitmap generateQRCode(String url) throws WriterException {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(url, BarcodeFormat.QR_CODE, 512, 512);
        Bitmap bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.RGB_565);

        for (int x = 0; x < 500; x++) {
            for (int y = 0; y < 500; y++) {
                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return bitmap;
    }
}
