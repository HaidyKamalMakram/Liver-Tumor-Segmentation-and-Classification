package app.ij.mlwithtensorflowlite;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import app.ij.mlwithtensorflowlite.ml.Model;

public class DoctorActivity extends AppCompatActivity {

    TextView classTxt, percentagesTxt;
    ImageView imageView;
    Button chooseImgBtn;
    int imageSize = 224;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        classTxt = findViewById(R.id.classTxt);
        percentagesTxt = findViewById(R.id.percentagesTxt);
        imageView = findViewById(R.id.imageView);
        chooseImgBtn = findViewById(R.id.chooseImgBtn);

        chooseImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent();
                it.setAction(Intent.ACTION_GET_CONTENT);
                it.setType("image/*");
                startActivityForResult(it, 1000);
            }
        });
    }

    public void classifyImage(Bitmap image) {
        try {
            Model model = Model.newInstance(getApplicationContext());
            //ModelUnquant5 model = ModelUnquant5.newInstance(getApplicationContext());
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


            float[] percentages = outputFeature0.getFloatArray();

            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < percentages.length; i++) {
                if (percentages[i] > maxConfidence) {
                    maxConfidence = percentages[i];
                    maxPos = i;
                }
            }

            String[] classes = {"Class A", "Class B", "Class E", "Class 0"};
            classTxt.setText(classes[maxPos]);

            String s = "";
            for (int i = 0; i < classes.length; i++) {
                s += String.format("%s: %.1f%%\n", classes[i], percentages[i] * 100);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {
            if (resultCode == RESULT_OK && data != null) {
                try {
                    Bitmap bm = getBitmapFromUri(data.getData());
                    imageView.setImageBitmap(bm);
                    bm = Bitmap.createScaledBitmap(bm, imageSize, imageSize, false);
                    classifyImage(bm);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}