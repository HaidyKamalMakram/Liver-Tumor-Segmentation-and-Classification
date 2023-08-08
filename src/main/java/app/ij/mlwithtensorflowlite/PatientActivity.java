package app.ij.mlwithtensorflowlite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PatientActivity extends AppCompatActivity {

    private Button insertDataBtn;
    private Button displayAllDataBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        Intent intent = getIntent();
        String getPassword = intent.getStringExtra("password");


        insertDataBtn = (Button) findViewById(R.id.insertDataBtn);
        insertDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TestCTActivity.class);
                intent.putExtra("password", getPassword);
                startActivity(intent);
            }
        });

        displayAllDataBtn = (Button) findViewById(R.id.displayAllDataBtn);
        displayAllDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GetSavedDataActivity.class);
                intent.putExtra("password", getPassword);
                startActivity(intent);
            }
        });
    }
}