package labex.feevale.br.oinc.utils;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

/**
 * Created by 0126128 on 05/03/2015.
 */
public class AudioUtils {

    public static final String CONGRATS_AUDIO = "achievement_conquest.mp3";
    public static final String FLAG_AUDIO = "bandeira_sound.mp3";
    public static final String CASH_AUDIO = "cash.mp3";

    private MediaPlayer player;

    public void playAudio(String audioPath, Activity activity){
        verifyAndCleanMediaPlayer();
        try {
            player.setLooping(false);
            AssetFileDescriptor descriptor = activity.getAssets().openFd("achievement_conquest.mp3");
            player.setDataSource(audioPath);
            descriptor.close();
            player.prepare();
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    player.start();
                }
            });
        }catch (IOException e){
            Log.e("MUSIC_ERROR", e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            Log.e("MUSIC_ERROR", e.getMessage());
            e.printStackTrace();
        }

    }

    public void playAudioFromInternalStorage(String audioName, Activity activity){
        verifyAndCleanMediaPlayer();
        try {
            player.setLooping(false);
            AssetFileDescriptor descriptor = activity.getAssets().openFd(audioName);
            player.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();
            player.prepare();
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    player.start();
                }
            });
        }catch (IOException e){
            Log.e("MUSIC_ERROR", e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            Log.e("MUSIC_ERROR", e.getMessage());
            e.printStackTrace();
        }
    }

    private void verifyAndCleanMediaPlayer(){
        if (player != null && player.isPlaying()) {
            player.stop();
            player.release();
        }
        player = new MediaPlayer();
    }
}
