package com.ntu.moulsocial;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GithubAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN_GITHUB = 1234;

    FirebaseAuth auth;
    GoogleSignInClient googleSignInClient;

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
                        try {
                            GoogleSignInAccount signInAccount = accountTask.getResult(ApiException.class);
                            AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
                            auth.signInWithCredential(authCredential).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    auth = FirebaseAuth.getInstance();
                                    updateUI(auth.getCurrentUser());
                                } else {
                                    Toast.makeText(LoginActivity.this, "Failed to sign in: " + task.getException(), Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            });
                        } catch (ApiException e) {
                            e.printStackTrace();
                            updateUI(null);
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d(TAG, "LoginActivity onCreate");

        FirebaseApp.initializeApp(this);

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(LoginActivity.this, options);

        auth = FirebaseAuth.getInstance();
        
        auth.signOut();
        googleSignInClient.signOut();

        ImageButton googleSignInButton = findViewById(R.id.img_btn_google);
        googleSignInButton.setOnClickListener(view -> {
            Intent intent = googleSignInClient.getSignInIntent();
            activityResultLauncher.launch(intent);
        });

        ImageButton githubSignInButton = findViewById(R.id.img_btn_github);
        githubSignInButton.setOnClickListener(view -> signInWithGitHub());

    }

    private void signInWithGitHub() {
        Intent intent = new Intent(LoginActivity.this, GitHubSignInActivity.class);
        startActivityForResult(intent, RC_SIGN_IN_GITHUB);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN_GITHUB && resultCode == RESULT_OK) {
            String code = data.getStringExtra("code");
            if (code != null) {
                exchangeCodeForToken(code);
            } else {
                updateUI(null);
            }
        }
    }

    private void exchangeCodeForToken(String code) {
        OkHttpClient client = new OkHttpClient();

        String clientId = getString(R.string.github_client_id);
        String clientSecret = getString(R.string.github_client_secret);

        String url = "https://github.com/login/oauth/access_token" +
                "?client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&code=" + code;

        Request request = new Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "Token exchange failed", e);
                runOnUiThread(() -> updateUI(null));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    String token = null;
                    try {
                        token = new JSONObject(responseBody).getString("access_token");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    AuthCredential credential = GithubAuthProvider.getCredential(token);
                    auth.signInWithCredential(credential)
                            .addOnCompleteListener(LoginActivity.this, task -> {
                                if (task.isSuccessful()) {
                                    updateUI(auth.getCurrentUser());
                                } else {
                                    Toast.makeText(LoginActivity.this, "Authentication failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            });
                } else {
                    Log.e(TAG, "Token exchange failed with code " + response.code());
                    runOnUiThread(() -> updateUI(null));
                }
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Log.d(TAG, "Redirecting to MainActivity");
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Log.d(TAG, "User is not authenticated");
            Toast.makeText(LoginActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
        }
    }
}
