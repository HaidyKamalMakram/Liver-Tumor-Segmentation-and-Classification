package app.ij.mlwithtensorflowlite;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import app.ij.mlwithtensorflowlite.ml.Model;

public class TestCTActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int SELECT_PICTURE = 100;
    TextView result, percentagesTxt, test_ct_back;
    int imageSize = 224;
    Bitmap bm;
    String getPassword;
    private EditText etdate;
    private Database dbase;
    private Button btnOpenGallery, btnSaveImage;
    private ImageView imgView;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_ct);

        btnOpenGallery = findViewById(R.id.btnSelectImage);
        btnSaveImage = findViewById(R.id.btnSavaData);
        imgView = findViewById(R.id.imgView);
        result = findViewById(R.id.result);
        percentagesTxt = findViewById(R.id.percentagesTxt);
        test_ct_back = findViewById(R.id.test_ct_back);
        etdate = findViewById(R.id.etdate);

        btnOpenGallery.setOnClickListener(this);

        btnSaveImage.setOnClickListener(this);

        dbase = new Database(this);

        Intent intent = getIntent();
        getPassword = intent.getStringExtra("password");

        test_ct_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void classifyImage(Bitmap image) {
        try {
            Model model = Model.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            // get 1D array of 224 * 224 pixels in image
            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());

            // iterate over pixels and extract R, G, and B values. Add to bytebuffer.
            int pixel = 0;
            for (int i = 0; i < imageSize; i++) {
                for (int j = 0; j < imageSize; j++) {
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            String[] classes = {"Class A", "Class B", "Class E", "Class 0"};
            result.setText(classes[maxPos]);

            String s = "";
            for (int i = 0; i < classes.length; i++) {
                s += String.format("%s: %.1f%%\n", classes[i], confidences[i] * 100);
            }
            percentagesTxt.setText(s);


            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    // Choose an image from Gallery
    void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageUri = data.getData();

                if (null != selectedImageUri) {
                    imgView.setImageURI(selectedImageUri);
                    try {
                        bm = getBitmapFromUri(data.getData());
                        imgView.setImageBitmap(bm);

                        bm = Bitmap.createScaledBitmap(bm, imageSize, imageSize, false);
                        classifyImage(bm);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void saveData() {

        String resultTxt = result.getText().toString();
        String etdateTxt = etdate.getText().toString();

        if (resultTxt.equals("") || etdateTxt.equals("")) {
            Toast.makeText(TestCTActivity.this, "Fill All Fields", Toast.LENGTH_SHORT).show();
        } else {
            dbase.addUser(getPassword, result.getText().toString(), etdate.getText().toString(), bm);
            etdate.setText("");
            imgView.setImageBitmap(null);
            result.setText("");
            percentagesTxt.setText("");
            Toast.makeText(TestCTActivity.this, "Saved Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClick(View v) {
        if (v == btnOpenGallery)
            openImageChooser();

        if (v == btnSaveImage) {
            saveData();
        }
    }
}