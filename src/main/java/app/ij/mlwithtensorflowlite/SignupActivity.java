package app.ij.mlwithtensorflowlite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    EditText name, age, password, email, phoneNumber;
    Button submit;
    Database MyDB;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = (EditText) findViewById(R.id.name);
        age = (EditText) findViewById(R.id.age);
        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email);
        phoneNumber = (EditText) findViewById(R.id.phonenumber);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        submit = (Button) findViewById(R.id.submit);

        MyDB = new Database(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) radioGroup.findViewById(selectedId);
                String stateTxt = radioButton.getText().toString();
                String nameTxt = name.getText().toString();
                String ageTxt = age.getText().toString();
                String passwordTxt = password.getText().toString();
                String emailTxt = email.getText().toString();
                String phoneNumberTxt = phoneNumber.getText().toString();

                if (passwordTxt.equals("") || nameTxt.equals("") || ageTxt.equals("") || emailTxt.equals("") || stateTxt.equals("") || phoneNumberTxt.equals("") || selectedId == -1) {
                    Toast.makeText(SignupActivity.this, "Please, Fill all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean result = MyDB.checkUser(emailTxt, passwordTxt, stateTxt);
                    if (result == false) {
                        Boolean res = MyDB.insertData(nameTxt, ageTxt, passwordTxt, emailTxt, stateTxt, phoneNumberTxt);

                        if (res == true) {
                            Toast.makeText(SignupActivity.this, "Register Successfully!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(SignupActivity.this, "Registration Failed!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SignupActivity.this, "User already exists.\n Please Login", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}


