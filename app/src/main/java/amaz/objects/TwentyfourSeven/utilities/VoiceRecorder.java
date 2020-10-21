package amaz.objects.TwentyfourSeven.utilities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Handler;
import androidx.core.app.ActivityCompat;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import amaz.objects.TwentyfourSeven.BaseActivity;
import amaz.objects.TwentyfourSeven.listeners.OnRecordVoiceListener;

public class VoiceRecorder {

    private MediaRecorder recorder;
    private String filePath;
    private String fileName;

    private MediaPlayer player;
    private Handler handler;

    private Context context;
    private OnRecordVoiceListener listener;

    public VoiceRecorder(Context context, OnRecordVoiceListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void startRecording() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((BaseActivity) context, new String[]{Manifest.permission.RECORD_AUDIO}, Constants.RECORD_VOICE_PERMISSION);
        } else {
            if (context.getExternalCacheDir() != null) {
                filePath = context.getExternalCacheDir().getAbsolutePath();
                fileName = "record_" + (context.getExternalCacheDir().list().length + 1) + ".wav";
                filePath += "/" + fileName;

                Log.e("path", filePath);

                recorder = new MediaRecorder();
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                recorder.setOutputFile(filePath);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                try {
                    recorder.prepare();
                    listener.startTimer();
                    recorder.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public void stopRecording(boolean discard) {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
            if (!discard) {
                getDataFromRecord();
            } else {
                File file = new File(filePath);
                file.deleteOnExit();
            }
        }
    }

    private void getDataFromRecord() {
        Uri uri = Uri.parse(filePath);
        File recordFile = new File(filePath);
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(context, uri);
        String recordDurationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        String recordDateStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE);
        int recordDuration = Integer.parseInt(recordDurationStr);
        listener.saveRecord(recordFile, recordDuration, recordDateStr);
    }

    public void startPlaying(File recordFile, final int recordDuration) {
        if (player == null) {
            Log.e("new player", "new player");
            player = new MediaPlayer();
            handler = new Handler();
            try {
                player.setDataSource(recordFile.getPath());
                player.prepare();
                Log.e("start1", "start1");
                player.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            player.start();
        }

        ((BaseActivity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("runnable", "runnable");
                if (player != null) {

                    if (player.getCurrentPosition() != recordDuration) {

                        listener.updatePlayingUi(player.getCurrentPosition(), false);

                    } else {
                        listener.updatePlayingUi(player.getCurrentPosition(), true);
                        stopPlaying();
                        handler.removeCallbacks(this);

                    }
                    if (player != null && player.isPlaying()) {
                        handler.postDelayed(this, 100);
                    } else {
                        handler.removeCallbacks(this);
                    }
                } else {
                    handler.removeCallbacks(this);
                }

            }
        });


    }

    public void stopPlaying() {
        if (player != null) {
            player.release();
            player = null;
        }


    }

    public void pausePlaying() {
        if (player != null) {
            player.pause();
        }

    }

}
