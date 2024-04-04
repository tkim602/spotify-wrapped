package com.example.spotifywrapped.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spotifywrapped.Entities.Account;
import com.example.spotifywrapped.R;
import com.example.spotifywrapped.SpotifyWrappedDatabase;
import com.example.spotifywrapped.SpotifyWrappedViewModel;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Response;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;

// testing branch

public class Homepage extends AppCompatActivity {

    private SpotifyWrappedViewModel spotifyWrappedViewModel;

    public static final String CLIENT_ID = "306fb5543abb4f23b00ae1a5d1d70886";
    public static final String REDIRECT_URI = "spotifywrapped://auth";

    public static final int AUTH_TOKEN_REQUEST_CODE = 0;
    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    private String mAccessToken;
    private Call mCall;

    private int accountID;

    private Account currAccount;
    private TextView tokenTextView, codeTextView, profileTextView, dataView, nameView;
    private EditText email;
    private EditText password;

    private ArrayList<Account> accountArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        // Initialize the views
        dataView = (TextView) findViewById(R.id.accounts_as_string);
        nameView = (TextView) findViewById(R.id.name_homepage);

        // Initialize the buttons
        Button displayDataBtn = (Button) findViewById(R.id.display_data);
        Button generateBtn = (Button) findViewById(R.id.generate_button);
        Button settingsBtn = (Button) findViewById(R.id.settings_button);
        Button llmBtn = (Button) findViewById(R.id.llm_button);
        Button galleryBtn = (Button) findViewById(R.id.gallery_button);

        displayDataBtn.setOnClickListener((v -> {
            String result = "";
            for (Account a : accountArrayList) {
                result += a.toString();
            }
            dataView.setText(result);
        }));


        System.out.println("hi, you're here now");


        SpotifyWrappedDatabase db = SpotifyWrappedDatabase.getInstance(this);
        spotifyWrappedViewModel = new ViewModelProvider(this).get(SpotifyWrappedViewModel.class);

        spotifyWrappedViewModel.getAllAccounts().observe(this, new Observer<List<Account>>() {
            @Override
            public void onChanged(List<Account> accounts) {
                accountArrayList.clear();
                for (Account a : accounts) {
                    accountArrayList.add(a);
                }

                Bundle bundle = getIntent().getExtras();
                accountID = bundle.getInt("accountID");

                for (Account a : accountArrayList) {
                    System.out.println(a.getAccountEmail());
                    if (a.getAccountID() == accountID) {
                        currAccount = a;
                        break;
                    }
                }

                String firstName = currAccount.getAccountName().split(" ")[0];
                nameView.setText(firstName + "!");
            }
        });
        generateBtn.setOnClickListener((v) -> {
            Bundle bundle = new Bundle();
            bundle.putInt("accountID", accountID);
            bundle.putString("accountToken", mAccessToken);
            Intent i = new Intent(getApplicationContext(), Generate.class);
            i.putExtras(bundle);
            startActivity(i);

        });
        settingsBtn.setOnClickListener((v) -> {
            Bundle bundle = new Bundle();
            bundle.putInt("accountID", accountID);
            Intent i = new Intent(getApplicationContext(), Settings.class);
            i.putExtras(bundle);
            startActivity(i);
        });
        llmBtn.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("accountID", accountID);
            bundle.putString("accountToken", currAccount.getAccountToken());
            Intent i = new Intent(getApplicationContext(), LLMActivity.class);
            i.putExtras(bundle);
            startActivity(i);
        });

        galleryBtn.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("accountID", accountID);
            bundle.putString("location", "home");
            bundle.putString("accountToken", mAccessToken);
            Intent i = new Intent(getApplicationContext(), Gallery.class);
            i.putExtras(bundle);
            startActivity(i);
        });

        getLoginInfo();

    }

    /**
     * Get token from Spotify
     * This method will open the Spotify login activity and get the token
     * What is token?
     * https://developer.spotify.com/documentation/general/guides/authorization-guide/
     */
    public void getLoginInfo() {
        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.TOKEN);
        AuthorizationClient.openLoginActivity(Homepage.this, AUTH_TOKEN_REQUEST_CODE, request);
    }

    /**
     * When the app leaves this activity to momentarily get a token/code, this function
     * fetches the result of that external activity to get the response from Spotify
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, data);
        mAccessToken = response.getAccessToken();
        currAccount.setAccountToken(mAccessToken);
        spotifyWrappedViewModel.updateAccount(currAccount);
    }

    /**
     * Get user profile
     * This method will get the user profile using the token
     */
    public void onGetUserProfileClicked() {
        if (mAccessToken == null) {
            Toast.makeText(this, "You need to get an access token first!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a request to get the user profile
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me")
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();

        cancelCall();
        mCall = mOkHttpClient.newCall(request);

        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("HTTP", "Failed to fetch data: " + e);
                Toast.makeText(Homepage.this, "Failed to fetch data, watch Logcat for more details",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final JSONObject jsonObject = new JSONObject(response.body().string());
                    setTextAsync(jsonObject.toString(3), profileTextView);
                } catch (JSONException e) {
                    Log.d("JSON", "Failed to parse data: " + e);
                    Toast.makeText(Homepage.this, "Failed to parse data, watch Logcat for more details",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Creates a UI thread to update a TextView in the background
     * Reduces UI latency and makes the system perform more consistently
     *
     * @param text     the text to set
     * @param textView TextView object to update
     */
    private void setTextAsync(final String text, TextView textView) {
        runOnUiThread(() -> textView.setText(text));
    }

    /**
     * Get authentication request
     *
     * @param type the type of the request
     * @return the authentication request
     */
    private AuthorizationRequest getAuthenticationRequest(AuthorizationResponse.Type type) {
        return new AuthorizationRequest.Builder(CLIENT_ID, type, getRedirectUri().toString())
                .setShowDialog(false)
                .setScopes(new String[]{"user-read-email", "ugc-image-upload", "user-read-playback-state", "user-modify-playback-state", "user-read-currently-playing", "app-remote-control", "streaming", "playlist-read-private", "playlist-read-collaborative", "playlist-modify-private", "playlist-modify-public", "user-follow-modify", "user-follow-read", "user-read-playback-position", "user-top-read", "user-read-recently-played", "user-library-modify", "user-library-read", "user-read-private"}) // <--- Change the scope of your requested token here
                .setCampaign("your-campaign-token")
                .build();
    }

    /**
     * Gets the redirect Uri for Spotify
     *
     * @return redirect Uri object
     */
    private Uri getRedirectUri() {
        return Uri.parse(REDIRECT_URI);
    }

    private void cancelCall() {
        if (mCall != null) {
            mCall.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        cancelCall();
        super.onDestroy();
    }
}