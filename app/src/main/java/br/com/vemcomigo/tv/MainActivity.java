package br.com.vemcomigo.tv;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.net.Uri;
import android.widget.Toast;

public class MainActivity extends Activity {

    private WebView webView;

    private static final String SITE =
        "https://vem-comigo-tv.onrender.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(this);
        setContentView(webView);

        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);

        settings.setMixedContentMode(
            WebSettings.MIXED_CONTENT_NEVER_ALLOW
        );

        webView.setWebChromeClient(new WebChromeClient());

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(
                WebView view,
                WebResourceRequest request
            ) {
                Uri uri = request.getUrl();

                if ("vemcomigotv".equalsIgnoreCase(
                    uri.getScheme()
                )) {
                    if ("play".equalsIgnoreCase(
                        uri.getHost()
                    )) {
                        String videoUrl =
                            uri.getQueryParameter("url");

                        abrirPlayer(videoUrl);
                    }
                    return true;
                }

                return false;
            }
        });

        if (savedInstanceState == null) {
            webView.loadUrl(SITE);
        } else {
            webView.restoreState(savedInstanceState);
        }
    }

    private void abrirPlayer(String videoUrl) {

        if (videoUrl == null ||
            videoUrl.trim().isEmpty()) {
            Toast.makeText(
                this,
                "Endereço do vídeo não encontrado",
                Toast.LENGTH_LONG
            ).show();
            return;
        }

        Uri uri = Uri.parse(videoUrl);
        String scheme = uri.getScheme();

        if (!"http".equalsIgnoreCase(scheme) &&
            !"https".equalsIgnoreCase(scheme)) {
            Toast.makeText(
                this,
                "Formato de endereço não permitido",
                Toast.LENGTH_LONG
            ).show();
            return;
        }

        Intent intent = new Intent(
            this,
            PlayerActivity.class
        );

        intent.putExtra("video_url", videoUrl);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        webView.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }
}
