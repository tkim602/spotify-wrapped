package com.example.spotifywrapped.Activities;

import android.content.Intent;
import android.os.Bundle;
import com.example.spotifywrapped.R;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Handler;
public class login_load extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_load);
        Bundle bundle = getIntent().getExtras();
        bundle.putInt("accountID", bundle.getInt("accountID"));
        Intent i = new Intent(login_load.this, Homepage.class);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                i.putExtras(bundle);
                startActivity(i);
                finish();
            }
        }, 2000);
    }
}
