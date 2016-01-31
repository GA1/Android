package com.ga1.ronquito;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.os.Bundle;
import android.media.MediaRecorder;
import android.media.MediaPlayer;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Iterator;


public class RonQuitoMainActivity extends Activity {

    private Queue<Double> lastLevels;
    private AudioManager audioManager = null;
    private MediaRecorder recorder = null;
    private boolean paused = false;
    private MediaPlayer mediaPlayer;

    private Runnable soundLevelChecker = new Runnable() {
        @Override
        public void run() {
            getSample(0);
        }
    };

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main_layout);
    }

    @Override
    public void onPause() {
        paused = true;
        super.onPause();
        stopRecording();
        lastLevels = null;

        mediaPlayer = null;
    }

    @Override
    public void onResume() {
        paused = false;
        super.onResume();
        lastLevels = new Queue<>();
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        startRecording();
        Context context = getApplicationContext();
        mediaPlayer = MediaPlayer.create(context, R.raw.dzwonekalarmowy);
        soundLevelChecker.run();
    }


    public void getSample(final int a) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (!paused) {
                    double sampleInDb = toDb(recorder.getMaxAmplitude());
                    if (lastLevels.size() == 10)
                        lastLevels.dequeue();
                    lastLevels.enqueue(sampleInDb);

                    if (audioManager.isWiredHeadsetOn() || audioManager.isBluetoothA2dpOn()) {
                        enableMicSensitivityButtons();
                        if (isSnoreDetected()) {
                            if (!mediaPlayer.isPlaying())
                                mediaPlayer.start();
                        }
                    } else {
                        if (mediaPlayer.isPlaying())
                            mediaPlayer.stop();
                        disableMicSensitivityButtons();
                        if (a == 0)
                            demandHeadphonesOrEquivalent();
                    }
                    Log.w("myApp", "a = " + a + ", Max sound Level is: " + sampleInDb + ", SNOR EN= " + RonQuitoParams.SNORING_ENERGY);
                    getSample((a + 1) % 5);
                }
            }
        }, RonQuitoParams.SNORE_CHECKING_INTERVAL);

    }

    private void enableMicSensitivityButtons() {
        setEnabledOfMicSensitivityButtons(true);
    }

    private void disableMicSensitivityButtons() {
        setEnabledOfMicSensitivityButtons(false);
    }

    private void setEnabledOfMicSensitivityButtons(boolean enabled) {
        LayoutInflater inflater = this.getLayoutInflater();
        View rootView = inflater.inflate(R.layout.main_layout, null);
        rootView.findViewById(R.id.increaseMicSensitivityButton).setEnabled(enabled);
        rootView.findViewById(R.id.lowerMicSensitivityButton).setEnabled(enabled);
    }

    private void demandHeadphonesOrEquivalent() {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, getResources().getString(R.string.connect_headphones), duration);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if(v != null)
            v.setGravity(Gravity.CENTER);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private boolean isSnoreDetected() {
        return lastLevels.size() >= RonQuitoParams.NUMBER_OF_SAMPLES_CONSIDERED && atLeastXpercentAbove(25, RonQuitoParams.SNORING_ENERGY);
    }

    private boolean atLeastXpercentAbove(int x, int snoringEnergy) {
        Iterator<Double> samples = lastLevels.iterator();
        int count = 0;
        while (samples.hasNext())
            if (samples.next() > snoringEnergy)
                count++;
        return (double) count / lastLevels.size() > (double) x / 100.0;

    }

    public void startRecording() {
        if (recorder == null) {
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile("/dev/null");
            try {
                recorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            recorder.start();
        }
    }

    public void stopRecording() {
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
        }
    }

    private double toDb(int maxSoundLevel) {
        return 20 * Math.log10(maxSoundLevel);
    }

    public void onIncreaseClick(View v){
        f(-5);
    }

    public void onLowerClick(View v){
        f(5);
    }

    private void f(int changeBy) {
        lastLevels = new Queue<>();
        RonQuitoParams.SNORING_ENERGY += changeBy;
        CharSequence text = "Snoring sensitivity level is " + RonQuitoParams.SNORING_ENERGY + " dB";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, text, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }



}