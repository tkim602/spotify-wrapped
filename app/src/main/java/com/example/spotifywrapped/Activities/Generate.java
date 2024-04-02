package com.example.spotifywrapped.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spotifywrapped.R;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Generate extends AppCompatActivity {
    private Call mCall;
    private String mAccessToken;
    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    public static final int AUTH_TOKEN_REQUEST_CODE = 0;
    public static final String CLIENT_ID = "306fb5543abb4f23b00ae1a5d1d70886";
    public static final String REDIRECT_URI = "spotifywrapped://auth";

    private ImageView imgButton;
    private String time_range;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generate);
        //Initialize the imageview
        imgButton = (ImageView) findViewById(R.id.cancel_img);
        //Initialize the buttons
        Button goBtn = (Button) findViewById(R.id.button_go);
        //Initialize the spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinner_generate);

        Bundle bundle = getIntent().getExtras();
        mAccessToken = bundle.getString("accountToken");
        //Set click listeners for "x" button
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Generate.this, Homepage.class);
                startActivity(intent);
            }
        });
        //Set click listener for spinner (drop down menu)
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();
                switch (item) {
                    case "Past Month":
                        time_range = "short_term";
                        break;
                    case "All Time":
                        time_range = "long_term";
                        break;
                    default:
                        time_range = "medium_term"; //just for testing purpose
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //set click listener for "Go" button
        goBtn.setOnClickListener((v) -> {
            onGoButtonClicked();
        });
    }


    //need database for the token in signup/login . java?
    //Below needs revision
    public void onGoButtonClicked() {
        Bundle bundle = new Bundle();
        bundle.putString("accountToken", mAccessToken);
        bundle.putString("timeFrame", time_range);
        Intent i = new Intent(Generate.this, TopSongs.class);
        i.putExtras(bundle);
        startActivity(i);
    }
}