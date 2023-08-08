package app.ij.mlwithtensorflowlite;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SelectedDataActivity extends AppCompatActivity {

    TextView test_ct_back;
    String getPassword;
    private UserModel userModel;
    private EditText etdate, etclass;
    private ImageView imageView;
    private Database dbase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_data);

        Intent intent = getIntent();
        getPassword = intent.getStringExtra("password");
        userModel = (UserModel) intent.getSerializableExtra("user");

        dbase = new Database(this);

        etdate = (EditText) findViewById(R.id.etdate);
        etclass = (EditText) findViewById(R.id.etclass);
        imageView = (ImageView) findViewById(R.id.loadedImg);
        test_ct_back = (TextView) findViewById(R.id.test_ct_back);

        etdate.setText(userModel.getDate());
        etclass.setText(userModel.getClassImg());

        byte[] recordImage = userModel.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(recordImage, 0, recordImage.length);

        imageView.setImageBitmap(bitmap);

        test_ct_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}