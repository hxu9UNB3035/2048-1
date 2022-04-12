package com.example.freedom.voicerecognition;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class Homepage extends Activity {


    private Button buttonStartGame;
    private Button buttonHelp;
    private Button buttonLeadBoard;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState) ;
        setContentView(R.layout.homepage);

        buttonStartGame = (Button) findViewById(R.id.button_StartGame);
        buttonHelp = (Button) findViewById(R.id.button_Help);
        buttonLeadBoard = (Button) findViewById(R.id.button_Leaderboarder);
        buttonStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, MainActivity.class);
                startActivity(intent);
            }
        });

        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, Introduction.class);
                startActivity(intent);
            }
        });
        buttonLeadBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Homepage.this, LeaderBoard.class);
                startActivity(intent);
            }
        });


    }
}
