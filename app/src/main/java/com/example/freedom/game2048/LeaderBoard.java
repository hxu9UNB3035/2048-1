package com.example.freedom.game2048;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;


import android.app.Activity;
import android.widget.TextView;


public class LeaderBoard extends Activity {

    private Button buttonBack;

    private int[] rank;

    private TextView rank1;
    private TextView rank2;
    private TextView rank3;
    private TextView rank4;
    private TextView rank5;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState) ;
        setContentView(R.layout.leaderboard);
        buttonBack = (Button) findViewById(R.id.button_back2);

        rank1 = (TextView) findViewById(R.id.num_1);
        rank2 = (TextView) findViewById(R.id.num_2);
        rank3 = (TextView) findViewById(R.id.num_3);
        rank4 = (TextView) findViewById(R.id.num_4);
        rank5 = (TextView) findViewById(R.id.num_5);

        // initial rank list
        rank = new int[5];

        if(getIntent().getIntArrayExtra("rankList") != null){
            rank = getIntent().getIntArrayExtra("rankList");
        }

        setRankText();

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setRankText(){
        rank1.setText(""+rank[0]);
        rank2.setText(""+rank[1]);
        rank3.setText(""+rank[2]);
        rank4.setText(""+rank[3]);
        rank5.setText(""+rank[4]);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
}
