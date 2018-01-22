package com.hillavas.filmvazhe.utils;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import com.hillavas.filmvazhe.MyApplication;

import java.util.Locale;

/**
 * Created by Arash on 16/07/16.
 */
public class TTS {

    public TextToSpeech tts;

    public TTS(final Context context) {
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    tts.setLanguage(Locale.US);
                    tts.setSpeechRate((float) MyApplication.getSharedPreferences().getInt("speech",5)/10);
                } else {
                    Toast.makeText(context, "Fail!",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    public void RunSpeech(String text){
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void onPause() {
        if (tts != null) {
            tts.stop();

        }
    }

    public boolean isSpeaking() {
       return tts.isSpeaking();
    }

}
