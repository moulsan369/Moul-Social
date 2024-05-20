package com.ntu.moulsocial;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GitHubSignInActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "YOUR_GITHUB_CLIENT_ID";
    private static final String REDIRECT_URI = "YOUR_REDIRECT_URI";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebView webView = new WebView(this);
        setContentView(webView);

        // Enable JavaScript only if absolutely necessary
        webView.getSettings().setJavaScriptEnabled(true);

        // Set a WebViewClient to handle redirects
        webView.setWebViewClient(new SecureWebViewClient());

        // Load the GitHub OAuth authorization URL
        String authUrl = "https://github.com/login/oauth/authorize" +
                "?client_id=" + CLIENT_ID +
                "&redirect_uri=" + REDIRECT_URI +
                "&scope=read:user,user:email";

        webView.loadUrl(authUrl);
    }

    private class SecureWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            if (url.startsWith(REDIRECT_URI)) {
                Uri uri = Uri.parse(url);
                String code = uri.getQueryParameter("code");

                Intent resultIntent = new Intent();
                resultIntent.putExtra("code", code);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url.startsWith("https://github.com/") || url.startsWith(REDIRECT_URI)) {
                return false;
            }

            return true;
        }
    }
}
