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
    // Define the object that holds the ImageView
    private ImageView Iv;

    //Define the gesture detector object
    private GestureDetector gestureDetector;
    //Define the resource array of the image
    private int[] ResId = new int[]{
            R.mipmap.picture_1_foreground,
            R.mipmap.picture_2_foreground,
            R.mipmap.picture_3_foreground,
            R.mipmap.picture_4_foreground
    };
    //Defines the subscript of the currently displayed picture
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
        //The processing effect of setting the gesture listener is handled by onGestureListener
        gestureDetector = new GestureDetector(Introduction.this,
                onGestureListener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Callback when the current Activity is touched
        return gestureDetector.onTouchEvent(event);
    }

    private void findView() {
        //得到当前页面的imageview控件
        Iv = (ImageView) findViewById(R.id.imageView);
        Iv = (ImageView) findViewById(R.id.imageView2);
        Iv = (ImageView) findViewById(R.id.imageView3);
        Iv = (ImageView) findViewById(R.id.imageView4);
    }

    private GestureDetector.OnGestureListener onGestureListener
            = new GestureDetector.SimpleOnGestureListener() {
        //当识别的收拾是滑动手势时回调onFinger方法
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            //Get the x, y coordinates of the actual and end point of the swipe gesture, and calculate
            float x = e2.getX() - e1.getX();
            float y = e2.getY() - e1.getY();

            //Determine whether the user is swiping left or right by the calculation result
            if (x > 0) {
                count++;
                count %= 3;
            } else if (x < 0) {
                count--;
                count = (count + 3) % 3;
            }

            changeImg();
            return true;
        }
    };

    public void changeImg() {
        //Set the image resource of the current location
        Iv.setImageResource(ResId[count]);
    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }
}
