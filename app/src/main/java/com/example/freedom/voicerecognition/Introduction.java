package com.example.freedom.voicerecognition;

import android.graphics.Color;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.text.Html;
import android.text.Html.ImageGetter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Button;


import android.app.Activity;
import android.content.Intent;


import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;


public class Introduction extends Activity {

    private Button buttonBack;
    // 定义保存ImageView的对象
    private ImageView Iv;

    //定义手势检测器对象
    private GestureDetector gestureDetector;
    //定义图片的资源数组
    private int[] ResId = new int[]{
            R.mipmap.picture_1_foreground,
            R.mipmap.picture_2_foreground,
            R.mipmap.picture_3_foreground,
            R.mipmap.picture_4_foreground
    };
    //定义当前显示的图片的下标
    private int count = 0;



    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState) ;
        setContentView(R.layout.introduction);


        buttonBack = (Button) findViewById(R.id.button_back);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        findView();
        setListener();
    }

    private void setListener() {
        //设置手势监听器的处理效果由onGestureListener来处理
        gestureDetector = new GestureDetector(Introduction.this,
                onGestureListener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //当前Activity被触摸时回调
        return gestureDetector.onTouchEvent(event);
    }

    private void findView() {
        //得到当前页面的imageview控件
        Iv = (ImageView) findViewById(R.id.imageView);
        Iv = (ImageView) findViewById(R.id.imageView2);
        Iv = (ImageView) findViewById(R.id.imageView3);
        Iv = (ImageView) findViewById(R.id.imageView4);
    }
    //定义了GestureDetector的手势识别监听器
    private GestureDetector.OnGestureListener onGestureListener
            = new GestureDetector.SimpleOnGestureListener() {
        //当识别的收拾是滑动手势时回调onFinger方法
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            //得到滑动手势的其实和结束点的x，y坐标，并进行计算
            float x = e2.getX() - e1.getX();
            float y = e2.getY() - e1.getY();

            //通过计算结果判断用户是向左滑动或者向右滑动
            if (x > 0) {
                count++;
                count %= 3;
            } else if (x < 0) {
                count--;
                count = (count + 3) % 3;
            }
            //切换imageview的图片
            changeImg();
            return true;
        }
    };

    public void changeImg() {
        //设置当前位置的图片资源
        Iv.setImageResource(ResId[count]);
    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
}
