package br.com.vemcomigo.tv;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.media3.common.MediaItem;
import androidx.media3.common.MimeTypes;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

public class PlayerActivity extends Activity {

    private ExoPlayer player;
    private PlayerView playerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        playerView = new PlayerView(this);
        playerView.setUseController(true);
        playerView.setKeepScreenOn(true);

        setContentView(playerView);

        String videoUrl =
            getIntent().getStringExtra("video_url");

        if (videoUrl == null ||
            videoUrl.trim().isEmpty()) {
            Toast.makeText(
                this,
                "Vídeo não encontrado",
                Toast.LENGTH_LONG
            ).show();
            finish();
            return;
        }

        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        MediaItem.Builder mediaBuilder =
            new MediaItem.Builder()
                .setUri(Uri.parse(videoUrl));

        String path = Uri.parse(videoUrl).getPath();

        if (path != null) {
            String lower = path.toLowerCase();

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

    @Override
    protected void onStop() {
        super.onStop();

        if (player != null) {
            player.release();
            player = null;
        }
    }
}
