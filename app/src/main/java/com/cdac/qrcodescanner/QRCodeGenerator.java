package com.cdac.qrcodescanner;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class QRCodeGenerator extends AppCompatActivity {

    ImageView imageView;
    Button button;
    EditText editText;
    String EditTextValue;
    Thread thread;
    public final static int QRcodeWidth = 500;
    Bitmap bitmap;
    String name = "Emre OLGUN";
    String username = "mrolgun";
    String email = "emre@genel.com";
    String phone = "+90(506) 944 44 97";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_generator);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView = findViewById(R.id.imageView);

        new MyTask().execute();


    }

    public class MyTask extends AsyncTask<String, String, String> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(QRCodeGenerator.this);
            progressDialog.setMessage("Lütfen Bekleyiniz");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            /*EditTextValue = "{\n" +
                    "    \"id\": 1,\n" +
                    "    \"name\":  \"" + name + "\"" + ",\n" +
                    "    \"username\": \"" + username + "\"" + ",\n" +
                    "    \"email\": \"" + email + "\"" + ",\n" +
                    "\"address\": {\n" +
                    "      \"street\": \"Bizim cad.\",\n" +
                    "      \"suite\": \"Kardelen Apt.\",\n" +
                    "      \"city\": \"Ankara\",\n" +
                    "      \"zipcode\": \"06280\",\n" +
                    "      \"geo\": {\n" +
                    "        \"lat\": \"29.4572\",\n" +
                    "        \"lng\": \"-164.2990\"\n" +
                    "      }\n" +
                    "    },\n" +
                    "\"phone\": \"" + phone + "\"" + "\n" +
                    "}";*/
            EditTextValue = "Emre OLGUN\n" +
                    "mrolgun\n" +
                    "emreolgun944@gmail.com";

            try {
                bitmap = TextToImageEncode(EditTextValue);
            } catch (WriterException e) {
                e.printStackTrace();
            }
            return "finish";
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            imageView.setImageBitmap(bitmap);
            Log.d("emre","3");
            //Start other Activity or do whatever you want
        }
    }


    Bitmap TextToImageEncode(String Value) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.QRCodeBlackColor) : getResources().getColor(R.color.QRCodeWhiteColor);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            this.finish();
        }
        return true;
    }
}
