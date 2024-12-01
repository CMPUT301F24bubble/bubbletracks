package com.example.bubbletracksapp;
// https://reintech.io/blog/implementing-android-app-qr-code-scanner
// https://www.youtube.com/watch?v=mdpxaLwzPHg
// https://github.com/journeyapps/zxing-android-embedded/blob/master/sample/src/main/java/example/zxing/CustomScannerActivity.java
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

/**
 * this class is an activity that allows a user to scan a QR Code
 * @author Gwen
 * @version 2.0
 */
public class QRScanner extends AppCompatActivity {

    private DecoratedBarcodeView barcodeScannerView;

    /**
     * initializes the qr scanner and ask for permission based on API version
     * @param savedInstanceState stores the state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_scanner);

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        barcodeScannerView = findViewById(R.id.barcode_scanner);
        barcodeScannerView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                // Handle the scanned QR code result
                if (result != null && result.getText() != null) {
                    String id = result.getText();
                    Intent intent = new Intent(QRScanner.this, EntrantViewActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                    finish(); // Close the scanner activity
                }
            }
        });}

    @Override
    protected void onResume() {
        super.onResume();
        barcodeScannerView.resume(); // Resume scanner
    }
    @Override
    protected void onPause() {
        super.onPause();
        barcodeScannerView.pause(); // Pause scanner
    }
}