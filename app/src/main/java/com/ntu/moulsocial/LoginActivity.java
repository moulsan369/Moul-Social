package com.ntu.moulsocial;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN_GOOGLE = 9001;

    private GoogleSignInClient googleSignInClient;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    private String CLIENT_ID;
    private String CLIENT_SECRET;
    private String REDIRECT_URI;
    private static final String AUTH_URL = "https://github.com/login/oauth/authorize";
    private static final String TOKEN_URL = "https://github.com/login/oauth/access_token";
    private static final String USER_URL = "https://api.github.com/user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        CLIENT_ID = getString(R.string.github_client_id);
        CLIENT_SECRET = getString(R.string.github_client_secret);
        REDIRECT_URI = getString(R.string.github_redirect_uri);

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, options);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        ImageButton googleSignInButton = findViewById(R.id.img_btn_google);
        googleSignInButton.setOnClickListener(view -> signInWithGoogle());

        ImageButton githubSignInButton = findViewById(R.id.img_btn_github);
        githubSignInButton.setOnClickListener(view -> signInWithGitHub());
    }

    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN_GOOGLE);
    }

    private void signInWithGitHub() {
        String authUrl = AUTH_URL + "?client_id=" + CLIENT_ID + "&redirect_uri=" + REDIRECT_URI + "&scope=user:email";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(authUrl));
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(REDIRECT_URI)) {
            String code = uri.getQueryParameter("code");
            if (code != null) {
                new GitHubTokenTask().execute(code);
            } else if (uri.getQueryParameter("error") != null) {
                Toast.makeText(this, "GitHub login failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class GitHubTokenTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... codes) {
            try {
                URL url = new URL(TOKEN_URL + "?client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&code=" + codes[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Accept", "application/json");

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                connection.disconnect();

                JSONObject json = new JSONObject(content.toString());
                return json.getString("access_token");
            } catch (Exception e) {
                Log.e(TAG, "GitHub token request failed", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String accessToken) {
            if (accessToken != null) {
                new GitHubUserTask().execute(accessToken);
            } else {
                Toast.makeText(LoginActivity.this, "GitHub login failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class GitHubUserTask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... tokens) {
            try {
                URL url = new URL(USER_URL + "?access_token=" + tokens[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                connection.disconnect();

                return new JSONObject(content.toString());
            } catch (Exception e) {
                Log.e(TAG, "GitHub user request failed", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject user) {
            if (user != null) {
                String userId = user.optString("id", null);
                String userName = user.optString("name", "Unknown");
                String userEmail = user.optString("email", "No email");
                String userPhotoUrl = user.optString("avatar_url", null);

                saveUserToFirebase(userId, userName, userEmail, userPhotoUrl);
                saveUserToSharedPreferences(userId, userName, userEmail, userPhotoUrl);
                updateUI();
            } else {
                Toast.makeText(LoginActivity.this, "GitHub login failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveUserToFirebase(String userId, String userName, String userEmail, String userPhotoUrl) {
        User user = new User(userId, userName, userEmail, userPhotoUrl);
        databaseReference.child(userId).setValue(user);
    }

    private void saveUserToSharedPreferences(String userId, String userName, String userEmail, String userPhotoUrl) {
        SharedPreferences.Editor editor = getSharedPreferences("user_prefs", MODE_PRIVATE).edit();
        editor.putString("user_id", userId);
        editor.putString("user_name", userName);
        editor.putString("user_email", userEmail);
        editor.putString("user_photo_url", userPhotoUrl);
        editor.apply();
    }

    private void updateUI() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    handleSignInResult(account);
                } else {
                    Toast.makeText(this, "Failed to sign in with Google", Toast.LENGTH_SHORT).show();
                }
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(this, "Failed to sign in with Google", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void handleSignInResult(GoogleSignInAccount account) {
        String userId = account.getId();
        String userName = account.getDisplayName();
        String userEmail = account.getEmail();
        String userPhotoUrl = account.getPhotoUrl() != null ? account.getPhotoUrl().toString() : null;

        // Authenticate with Firebase using Google Account
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                saveUserToFirebase(userId, userName, userEmail, userPhotoUrl);
                saveUserToSharedPreferences(userId, userName, userEmail, userPhotoUrl);
                updateUI();
            } else {
                Toast.makeText(LoginActivity.this, "Firebase authentication failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        googleSignInClient.signOut();
    }
}
