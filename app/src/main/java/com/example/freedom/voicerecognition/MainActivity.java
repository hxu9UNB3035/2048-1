package com.example.freedom.voicerecognition;

import android.app.Activity ;
import android.os.Bundle ;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View ;
import android.widget.Button ;
import android.widget.EditText ;
import android.widget.Toast ;

import com.iflytek.cloud.ErrorCode ;
import com.iflytek.cloud.InitListener ;
import com.iflytek.cloud.RecognizerResult ;
import com.iflytek.cloud.SpeechConstant ;
import com.iflytek.cloud.SpeechError ;
import com.iflytek.cloud.SpeechUtility ;
import com.iflytek.cloud.ui.RecognizerDialog ;
import com.iflytek.cloud.ui.RecognizerDialogListener ;

import org.json.JSONException ;
import org.json.JSONObject ;

import java.util.HashMap ;
import java.util.LinkedHashMap ;


import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.TextView;


public class MainActivity extends Activity  {

    private static final String TAG = MainActivity.class .getSimpleName();
    private EditText et_input;
    private Button btn_startspeech;

    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String , String>();

    private int score = 0;
    private TextView tvScore;
    private Button buttonNewGame;
    private Button buttonConfirmDirection;
    private GameView gameView;

    private SoundPool soundPool;
    private int soundId_newgame;
    private int soundId_move;

    private WindowManager wm;
    DisplayMetrics dm;

    private static MainActivity mainActivity = null;

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public MainActivity() {
        mainActivity = this;
    }

    public void homeButton(View view){
        setContentView(R.layout.homepage);


    }

    public void introduction(View view){
        setContentView(R.layout.introduction);
    }

    public void startgame(View view){
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {//创建布局
        super .onCreate(savedInstanceState) ;
        setContentView(R.layout.activity_main);

        tvScore = (TextView) findViewById(R.id.tvScore);//得分
        et_input = (EditText) findViewById(R.id.et_input);//文本框

        gameView = (GameView) findViewById(R.id.gameView);//游戏界面

        buttonNewGame = (Button) findViewById(R.id.button_newgame);//新游戏
        buttonConfirmDirection = (Button) findViewById(R.id.btn_confirm_direction);//确认方向

        //btn_startspeech = (Button) findViewById(R.id.btn_startspeech );//开始拼写
        //btn_startspeech .setOnClickListener(this) ;

        buttonNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//新游戏按钮监听器
                //soundPool.play(soundId_newgame, 1, 1, 0, 0, 1);
                gameView.startGame();
            }
        });

        buttonConfirmDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//文本框输入方向
                String direction = et_input.getText().toString();
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
       // soundId_newgame = soundPool.load(this, R.raw.sound_newgame, 1);//语音 新游戏
        soundId_move = soundPool.load(this, R.raw.sound_move, 1);//语音移动

        //initSpeech() ;
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









    /*private void initSpeech() {
        // 将“ 5ee30bc1 ”替换成您申请的 APPID，申请地址： http://www.xfyun.cn
        // 请勿在 “ =”与 appid 之间添加任务空字符或者转义符
        SpeechUtility. createUtility( this, SpeechConstant. APPID + "=5ee30bc1" );
    }

    /*@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_startspeech: //语音识别（把声音转文字）
                startSpeechDialog();
                break;
            default:
                break;
        }

    }*/
    /*
    private void startSpeechDialog() {
        //1. 创建RecognizerDialog对象
        RecognizerDialog mDialog = new RecognizerDialog(this, new MyInitListener()) ;
        //2. 设置accent、 language等参数
        mDialog.setParameter(SpeechConstant. LANGUAGE, "zh_cn" );// 设置中文
        mDialog.setParameter(SpeechConstant. ACCENT, "mandarin" );
        // 若要将UI控件用于语义理解，必须添加以下参数设置，设置之后 onResult回调返回将是语义理解
        // 结果
        // mDialog.setParameter("asr_sch", "1");
        // mDialog.setParameter("nlp_version", "2.0");
        //3.设置回调接口
        mDialog.setListener( new MyRecognizerDialogListener()) ;
        //4. 显示dialog，接收语音输入
        mDialog.show() ;
    }

    class MyRecognizerDialogListener implements RecognizerDialogListener {

        /**
         * @param results
         * @param isLast  是否说完了
         */
    //@Override
        /*public void onResult(RecognizerResult results, boolean isLast) {
            String result = results.getResultString(); //为解析的
            showTip(result) ;
            System. out.println(" No Parse :" + result);

            String text = JsonParser.parseIatResult(result) ;//解析过后的
            System. out.println(" After Parse :" + text);

            String sn = null;
            // 读取json结果中的 sn字段
            try {
                JSONObject resultJson = new JSONObject(results.getResultString()) ;
                sn = resultJson.optString("sn" );
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mIatResults .put(sn, text) ;//没有得到一句，添加到

            StringBuffer resultBuffer = new StringBuffer();
            for (String key : mIatResults.keySet()) {
                resultBuffer.append(mIatResults .get(key));
            }

            et_input.setText(resultBuffer.toString());// 设置输入框的文本
            et_input.setSelection(et_input.length()) ;//把光标定位末尾
        }

        @Override
        public void onError(SpeechError speechError) {

        }
    }

    /*class MyInitListener implements InitListener {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                showTip("defeat fail  ");
            }

        }
    }

    /**
     * 语音识别
     */

   /* private void showTip (String data) {
        Toast.makeText( this, data, Toast.LENGTH_SHORT).show() ;
    }
    */
}
