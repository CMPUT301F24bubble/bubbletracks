package com.example.bubbletracksapp;
// https://reintech.io/blog/implementing-android-app-qr-code-scanner
// https://www.youtube.com/watch?v=mdpxaLwzPHg
// https://github.com/journeyapps/zxing-android-embedded/blob/master/sample/src/main/java/example/zxing/CustomScannerActivity.java

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

/**
 * This class is an activity that allows a user to scan a QR Code.
 * @author Gwen
 * @version 2.0
 */
public class QRScanner extends AppCompatActivity {

    private DecoratedBarcodeView barcodeScannerView;
    private static final int PERMISSION_REQUEST_CAMERA = 1;

    /**
     * Initializes the QR scanner and checks for camera permissions.
     * @param savedInstanceState Stores the state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_scanner);

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        barcodeScannerView = findViewById(R.id.barcode_scanner);

        if (checkCameraPermission()) {
            initQRCodeScanner();
        } else {
            requestCameraPermission();
        }
    }

    /**
     * Initializes the QR code scanner.
     */
    private void initQRCodeScanner() {
        barcodeScannerView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                if (result != null && result.getText() != null) {
                    String id = result.getText();
                    Intent intent = new Intent(QRScanner.this, EntrantViewActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    /**
     * Checks camera permissions
     */
    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Requests camera permissions from the user.
     */
    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
    }

    /**
     * Handles the result of the permission request.
     * @param requestCode The request code passed during the permission request.
     * @param permissions The requested permissions.
     * @param grantResults The grant results for the requested permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initQRCodeScanner();
            } else {
                Toast.makeText(this, "Camera permission is required to scan QR codes.", Toast.LENGTH_LONG).show();
                finish(); // Close the activity if permission is denied
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkCameraPermission()) {
            barcodeScannerView.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeScannerView.pause();
    }
}
