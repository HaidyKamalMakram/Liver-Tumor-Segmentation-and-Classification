package app.ij.mlwithtensorflowlite;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3500);

                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
                Intent intent = new Intent(WelcomeActivity.this, IntroActivity.class);
                startActivity(intent);
            }
        });
        thread.start();
    }
}