package br.com.vemcomigo.tv;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.media3.common.MediaItem;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.Player;
import androidx.media3.common.PlaybackException;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.AspectRatioFrameLayout;
import androidx.media3.ui.PlayerView;

public class PlayerActivity extends Activity {

    private ExoPlayer player;
    private PlayerView playerView;
    private TextView modeButton;

    private int mode = 0;

    private final String[] modeNames = {
        "AJUSTAR", "ZOOM", "ESTICAR"
    };

    private final int[] resizeModes = {
        AspectRatioFrameLayout.RESIZE_MODE_FIT,
        AspectRatioFrameLayout.RESIZE_MODE_ZOOM,
        AspectRatioFrameLayout.RESIZE_MODE_FILL
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        );

        hideSystemUI();

        FrameLayout layout = new FrameLayout(this);
        layout.setBackgroundColor(0xFF000000);

        playerView = new PlayerView(this);
        playerView.setUseController(true);
        playerView.setKeepScreenOn(true);
        playerView.setResizeMode(
            AspectRatioFrameLayout.RESIZE_MODE_FIT
        );

        layout.addView(
            playerView,
            new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        );

        modeButton = new TextView(this);
        modeButton.setText("AJUSTAR");
        modeButton.setTextColor(0xFFFFFFFF);
        modeButton.setTextSize(12);
        modeButton.setGravity(Gravity.CENTER);
        modeButton.setPadding(24, 16, 24, 16);
        modeButton.setBackgroundColor(0xBB182033);

        FrameLayout.LayoutParams buttonParams =
            new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.TOP | Gravity.END
            );

        buttonParams.setMargins(16, 16, 20, 16);

        layout.addView(modeButton, buttonParams);

        modeButton.setOnClickListener(v -> {
            mode = (mode + 1) % resizeModes.length;
            playerView.setResizeMode(resizeModes[mode]);
            modeButton.setText(modeNames[mode]);

            Toast.makeText(
                this,
                "Imagem: " + modeNames[mode],
                Toast.LENGTH_SHORT
            ).show();
        });

        setContentView(layout);

        String videoUrl = getIntent()
            .getStringExtra("video_url");

        if (videoUrl == null ||
            videoUrl.trim().isEmpty()) {
            showError("Endereço do vídeo não encontrado");
            return;
        }

        Uri uri = Uri.parse(videoUrl);
        String scheme = uri.getScheme();

        if (!"http".equalsIgnoreCase(scheme) &&
            !"https".equalsIgnoreCase(scheme)) {
            showError("Endereço de vídeo não permitido");
            return;
        }

        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        player.addListener(new Player.Listener() {
            @Override
            public void onPlayerError(PlaybackException error) {
                Toast.makeText(
                    PlayerActivity.this,
                    "Erro ao reproduzir: " + error.getErrorCodeName(),
                    Toast.LENGTH_LONG
                ).show();
            }
        });

        MediaItem.Builder mediaBuilder =
            new MediaItem.Builder().setUri(uri);

        String path = uri.getPath();

        if (path != null) {
            String lower = path.toLowerCase(
                java.util.Locale.ROOT
            );

            if (lower.endsWith(".m3u8")) {
                mediaBuilder.setMimeType(
                    MimeTypes.APPLICATION_M3U8
                );
            } else if (lower.endsWith(".mpd")) {
                mediaBuilder.setMimeType(
                    MimeTypes.APPLICATION_MPD
                );
            } else if (lower.endsWith(".ts")) {
                mediaBuilder.setMimeType(
                    MimeTypes.VIDEO_MP2T
                );
            }
        }

        player.setMediaItem(mediaBuilder.build());
        player.prepare();
        player.play();
    }

    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
            View.SYSTEM_UI_FLAG_FULLSCREEN |
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );
    }

    private void showError(String message) {
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_LONG
        ).show();
        finish();
    }

    @Override
    protected void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            hideSystemUI();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (player != null) {
            player.release();
            player = null;
        }
    }
}
