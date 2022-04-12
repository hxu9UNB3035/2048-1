package com.example.freedom.game2048;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;


import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private EditText et_input;

    private int score = 0;
    private TextView tvScore;

    private Button buttonNewGame;
    private Button buttonHome;
    private Button buttonHelp;

    private Button buttonUp;
    private Button buttonDown;
    private Button buttonLeft;
    private Button buttonRight;

    private Switch buttonSound;

    private GameView gameView;
    private LeaderBoard leaderBoard = new LeaderBoard();

    Boolean soundIsAvailable = true;

    private SoundPool soundPool;
    private int soundId_newGame;
    private int soundId_move;

    int[] rank;

    private WindowManager wm;
    DisplayMetrics dm;

    private static MainActivity mainActivity = null;

    public MainActivity() {
        mainActivity = this;
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvScore = (TextView) findViewById(R.id.tvScore);
        et_input = (EditText) findViewById(R.id.et_input);

        gameView = (GameView) findViewById(R.id.gameView);

        buttonNewGame = (Button) findViewById(R.id.button_newGame);
        buttonHome = (Button) findViewById(R.id.button_home);
        buttonHelp = (Button) findViewById(R.id.button_help);

        buttonUp = (Button) findViewById(R.id.button_up);
        buttonDown = (Button) findViewById(R.id.button_down);
        buttonLeft = (Button) findViewById(R.id.button_left);
        buttonRight = (Button) findViewById(R.id.button_right);

        buttonSound = (Switch) findViewById(R.id.button_Sound);

        rank = new int[5];

        // buttons listeners
        buttonNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (soundIsAvailable == true)
                    soundPool.play(soundId_newGame, 1, 1, 0, 0, 1);
                gameView.startGame();
            }
        });

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Homepage.class);
                startActivity(intent);
            }
        });

        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Introduction.class);
                startActivity(intent);
            }
        });

        buttonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameView.swipeUp();
            }
        });

        buttonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameView.swipeDown();
            }
        });

        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameView.swipeLeft();
            }
        });

        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameView.swipeRight();
            }
        });

        buttonSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    // enable
                    soundIsAvailable = true;
                } else {
                    soundIsAvailable = false;
                }
            }
        });


        et_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String direction = s.toString();
                if ("up".equals(direction)) {
                    gameView.swipeUp();
                } else if ("down".equals(direction)) {
                    gameView.swipeDown();
                } else if ("left".equals(direction)) {
                    gameView.swipeLeft();
                } else if ("right".equals(direction)) {
                    gameView.swipeRight();
                }
            }
        });

        soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
        soundId_newGame = soundPool.load(this, R.raw.sound_newgame, 1);//sound of press newGame
        soundId_move = soundPool.load(this, R.raw.sound_move, 1);//sound of moving
    }

    public int getScore() {
        return score;
    }

    public void clearScore() {
        score = 0;
        showScore();
    }

    public void showScore() { // current scores
        tvScore.setText(score + " ");
    }

    public int printScore() {
        return score;
    }

    public void addScore(int s) {
        score += s;
        showScore();
    }

    public void moveVoice() {
        if (soundIsAvailable == true)
            soundPool.play(soundId_move, 1, 1, 0, 0, 1);
    }

    public int min_height_weight() {
        wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        return (Math.min(width, height) - 10) / 4; //Calculate the width and height of each card according to the mobile phone screen
    }

    public void setNewRank(int newScore) {

        // if new score less than the smallest best score then return
        if (newScore < rank[4]) {
            return;
        }

        int i = 0;
        while (i < rank.length) {
            if (rank[i] < newScore) {
                for (int j = rank.length - 1; j >= i; j--) {
                    if (j == i) { // change the score for target position

                        rank[j] = newScore;
                        System.out.println("1213");
                    } else{ // replace the rest score one behind
                        int temp = rank[j-1];
                        rank[j] = temp;
                    }
                }
                break;
            } else
                i++;
        }
    }

    public void saveScore() {
        setNewRank(score);
        updateLeaderBoard();
    }

    public void updateLeaderBoard(){
        Intent intent = new Intent(MainActivity.this, LeaderBoard.class);
        intent.putExtra("rankList", rank);
        startActivity(intent);
    }
}
