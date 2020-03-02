package com.cdac.qrcodescanner;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class ScannerBarcodeActivity extends AppCompatActivity {

    SurfaceView surfaceView;
    TextView textViewBarCodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    String intentData = "";
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_barcode);
        initComponents();
        sharedPref = this.getPreferences(MODE_PRIVATE);
    }

    private void initComponents() {
        textViewBarCodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
    }

    private void initialiseDetectorsAndSources() {
        Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setFacing(1)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                openCamera();
            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Log.d("emre","release");
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barCode = detections.getDetectedItems();
                Log.d("emre",barCode.toString()+"");
                if (barCode.size() > 0) {
                    setBarCode(barCode);
                }
            }
        });
    }

    private void openCamera(){
        try {
            if (ActivityCompat.checkSelfPermission(ScannerBarcodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                cameraSource.start(surfaceView.getHolder());
            } else {
                ActivityCompat.requestPermissions(ScannerBarcodeActivity.this, new
                        String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setBarCode(final SparseArray<Barcode> barCode){
        textViewBarCodeValue.post(new Runnable() {
            @Override
            public void run() {

                String TAG = "emreBarcode";
                intentData = barCode.valueAt(0).displayValue;
                textViewBarCodeValue.setText(intentData);
                copyToClipBoard(intentData);
                Log.d("emreValueFormat",barCode.valueAt(0).valueFormat+"");
                Log.d("emreDisplay",barCode.valueAt(0).displayValue+"");
                Log.d("emrerawValue",barCode.valueAt(0).rawValue+"");
                Log.d("emreEmail",barCode.valueAt(0).email+"");
                Log.d("emrePhone",barCode.valueAt(0).phone+"");
                Log.d("emreSMS",barCode.valueAt(0).sms+"");

                for (int index = 0; index < barCode.size(); index++) {
                    Barcode code = barCode.valueAt(index);
                    int type = barCode.valueAt(index).valueFormat;
                    switch (type) {
                        case Barcode.CONTACT_INFO:
                            Log.i(TAG, code.contactInfo.title);
                            break;
                        case Barcode.EMAIL:
                            Log.i(TAG, code.displayValue);
                            break;
                        case Barcode.ISBN:
                            Log.i(TAG, code.rawValue);
                            break;
                        case Barcode.PHONE:
                            Log.i(TAG, code.phone.number);
                            break;
                        case Barcode.PRODUCT:
                            Log.i(TAG, code.rawValue);
                            break;
                        case Barcode.SMS:
                            Log.i(TAG, code.sms.message);
                            break;
                        case Barcode.TEXT:
                            Log.i(TAG, code.displayValue);
                            break;
                        case Barcode.URL:
                            Log.i(TAG, "url: " + code.displayValue);
                            break;
                        case Barcode.WIFI:
                            Log.i(TAG, code.wifi.ssid);
                            break;
                        case Barcode.GEO:
                            Log.i(TAG, code.geoPoint + ":" + code.geoPoint.lat + ":" + code.geoPoint.lng);
                            break;
                        case Barcode.CALENDAR_EVENT:
                            Log.i(TAG, code.calendarEvent.description);
                            break;
                        case Barcode.DRIVER_LICENSE:
                            Log.i(TAG, code.driverLicense.licenseNumber);
                            break;
                        default:
                            Log.i(TAG, code.rawValue);
                            break;
                    }
                }

                if (!intentData.equals("No Barcode Detected")) {
                    Intent intent = new Intent(ScannerBarcodeActivity.this, MainActivity.class);
                    intent.putExtra("data",barCode.valueAt(0).displayValue);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }

    private void copyToClipBoard(String text){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("QR code Scanner", text);
        clipboard.setPrimaryClip(clip);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CAMERA_PERMISSION && grantResults.length>0){
            if (grantResults[0] == PackageManager.PERMISSION_DENIED)
                finish();
            else
                openCamera();
        }else
            finish();
    }
}