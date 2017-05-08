package com.example.sergio.falarapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener{


    private TextToSpeech tts;
    private Button btnSpeak;
    private EditText txtText;
    private TextView txtListen;
    private Button btnListen;
    private final int REQ_CODE_SPEECH_INPUT = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tts = new TextToSpeech(this,this);

        btnSpeak = (Button) findViewById(R.id.btnSpeak);
        txtText = (EditText) findViewById(R.id.txtText);
        
        //evento botão falar
        
        btnSpeak.setOnClickListener(new View.OnClickListener()
                                    {
                                        public void onClick(View arg0){
                                            speakOut();
                                        }
                                    }
        );
        txtListen = (TextView) findViewById(R.id.txtListen);
        btnListen = (Button) findViewById(R.id.btnListen);
        
        //evento botão ouvir
        btnListen.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                ListenOut();
            }
        });
    }

    private void ListenOut() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Diga algo");
        try {
            startActivityForResult(intent,REQ_CODE_SPEECH_INPUT);
        }catch (ActivityNotFoundException a){
            Toast.makeText(getApplicationContext(),"Sem suporte",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requesCode, int resultCode, Intent data){
        super.onActivityResult(requesCode,resultCode, data);
        switch (requesCode){
            case REQ_CODE_SPEECH_INPUT:
            {
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtListen.setText(result.get(0));
                }
            }
        }
    }


    private void speakOut() {
        String text = txtText.getText().toString();
        tts.speak(text,TextToSpeech.QUEUE_ADD,null);
    }

    @Override
    public void onInit(int status) {

        if(status == TextToSpeech.SUCCESS){
            int result = tts.setLanguage(new Locale("pt_BR"));
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
               // Log.e("TTS","Lingua não suportada");
            }
        }

    }
}
