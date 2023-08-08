package app.ij.mlwithtensorflowlite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SigninActivity extends AppCompatActivity {

    Button submitBtn;
    EditText password, email;
    Database MyDB;
    RadioGroup radioGroup;
    TextView account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        account = (TextView) findViewById(R.id.account);

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });

        MyDB = new Database(this);

        // Uncheck or reset the radio buttons initially
        //radioGroup.clearCheck();

        submitBtn = (Button) findViewById(R.id.submit);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                String passwordTxt = password.getText().toString();
                RadioButton radioButton = (RadioButton) radioGroup.findViewById(selectedId);
                String emailTxt = email.getText().toString();
                String stateTxt = radioButton.getText().toString();

                if (emailTxt.equals("") || passwordTxt.equals("") || selectedId == -1) {
                    Toast.makeText(SigninActivity.this, "Please, Fill All Fields", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean result = MyDB.checkUser(emailTxt, passwordTxt, stateTxt);

                    if (result == false) {
                        Toast.makeText(SigninActivity.this, "User does not exists.\n Kindly Register", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SigninActivity.this, "LogIn Successfully!", Toast.LENGTH_SHORT).show();

                        if (stateTxt.equals("Doctor")) {
                            Intent intent = new Intent(getApplicationContext(), DoctorActivity.class);
                            intent.putExtra("password", passwordTxt);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getApplicationContext(), PatientActivity.class);
                            intent.putExtra("password", passwordTxt);
                            startActivity(intent);
                        }
                    }
                }
            }
        });
    }
}