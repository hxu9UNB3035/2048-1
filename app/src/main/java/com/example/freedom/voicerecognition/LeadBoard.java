package com.example.freedom.voicerecognition;

import android.os.Bundle;

import android.view.GestureDetector;
import android.view.View;
import android.widget.Button;


import android.app.Activity;


    public class LeadBoard extends Activity {

    private Button buttonBack;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState) ;
        setContentView(R.layout.leadboard);

        buttonBack = (Button) findViewById(R.id.button_back2);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
}
