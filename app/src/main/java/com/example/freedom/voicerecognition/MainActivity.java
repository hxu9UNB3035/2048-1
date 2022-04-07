package com.example.freedom.voicerecognition;

import android.app.Activity ;
import android.content.Intent;
import android.os.Bundle ;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View ;
import android.widget.Button ;
import android.widget.CompoundButton;
import android.widget.EditText ;
import android.widget.Switch;


import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.freedom.voicerecognition.R;

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

    Boolean soundIsAvailable = true;

    private SoundPool soundPool;
    private int soundId_newGame;
    private int soundId_move;

    private WindowManager wm;
    DisplayMetrics dm;

    private static MainActivity mainActivity = null;

    public MainActivity() {
        mainActivity = this;
    }

    public void introduction(View view){
        setContentView(R.layout.introduction);
    }

    public void startGame(View view){
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {//创建布局
        super.onCreate(savedInstanceState) ;
        setContentView(R.layout.activity_main);

        tvScore = (TextView) findViewById(R.id.tvScore);//得分
        et_input = (EditText) findViewById(R.id.et_input);//文本框

        gameView = (GameView) findViewById(R.id.gameView);//游戏界面

        buttonNewGame = (Button) findViewById(R.id.button_newGame);//新游戏
        buttonHome = (Button) findViewById(R.id.button_home);
        buttonHelp = (Button) findViewById(R.id.button_help);

        buttonUp = (Button) findViewById(R.id.button_up);
        buttonDown = (Button) findViewById(R.id.button_down);
        buttonLeft = (Button) findViewById(R.id.button_left);
        buttonRight = (Button) findViewById(R.id.button_right);

        buttonSound = (Switch) findViewById(R.id.button_Sound);


        buttonNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//新游戏按钮监听器
                if(soundIsAvailable == true)
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
                if(isChecked){
                    // enable
                    soundIsAvailable = true;
                }else{
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
            public void afterTextChanged(Editable s) {//语音输入方向
                String direction = s.toString();
                if("up".equals(direction) ) {
                    gameView.swipeUp();
                } else if("down".equals(direction) ) {
                    gameView.swipeDown();
                } else if("left".equals(direction) ) {
                    gameView.swipeLeft();
                } else if("right".equals(direction) ) {
                    gameView.swipeRight();
                }
            }
        });

        soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
        soundId_newGame = soundPool.load(this, R.raw.sound_newgame, 1);//语音 新游戏
        soundId_move = soundPool.load(this, R.raw.sound_move, 1);//语音移动
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public void clearScore() {//当前得分
        score = 0;
        showScore();
    }

    public void showScore() {
        tvScore.setText(score+" ");
    } //消除的卡片分数相加

    public int printScore() { return score; }//

    public void addScore(int s) {
        score += s;
        showScore();
    }

    public void moveVoice() {
        if(soundIsAvailable == true)
            soundPool.play(soundId_move, 1, 1, 0, 0, 1);
    }

    public int min_height_weight() {
        wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        return (Math.min(width, height) - 10) / 4;//根据手机屏幕得出每张卡片宽高
    }
}
