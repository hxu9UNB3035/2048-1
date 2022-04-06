package com.example.freedom.voicerecognition;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

public class GameView extends GridLayout {//因为使用的布局是，GridLayout

    public GameView(Context context, AttributeSet attrs, int defStyle) {//传入三个构造方法
        super(context, attrs, defStyle);
        initGameView();
        addCards(card_width, card_width);
    }

    public GameView(Context context) {
        super(context);
        initGameView();
        addCards(card_width, card_width);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView();
        addCards(card_width, card_width);
    }

    private void initGameView() {//初始化
        setColumnCount(4);
        setBackgroundColor(0xffbbada0);//设置背景颜色

        setOnTouchListener(new View.OnTouchListener(){//设置监听器监听手指按下的位置和离开的位置

            private float startX, startY, offsetX, offsetY;//记录手指按下的位置和离开的位置

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN://手指放下
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP://手指离开
                        offsetX = event.getX() - startX;
                        offsetY = event.getY() - startY;

                        if(Math.abs(offsetX) > Math.abs(offsetY)) {//x轴的移动距离与y轴移动距离比较
                            if(offsetX < -5) {//小于0 往左，误差范围-5
                                swipeLeft();
                            } else if(offsetX > 5) {
                                swipeRight();
                            }
                        } else {
                            if(offsetY < -5) {
                                swipeUp();
                            } else if(offsetY > 5) {
                                swipeDown();
                            }
                        }

                        break;
                }

                return true;
            }
        });

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {//自动获取屏幕大小

        super.onSizeChanged(w, h, oldw, oldh);

        startGame();
    }

    private void addCards(int cardWidth, int cardHeight) {//加载卡片

        Card c;

        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                c = new Card(getContext());
                c.setNum(0);
                addView(c, cardWidth, cardHeight);

                cardsMap[x][y] = c;
            }
        }
    }

    public void startGame() {  // 开始游戏

        MainActivity.getMainActivity().clearScore();//先清理残局

        for(int y = 0; y < 4; y++) {
            for(int x = 0; x < 4; x++) {
                cardsMap[x][y].setNum(0);
            }
        }

        addRandomNum();//每次随机产生2个随机数
        addRandomNum();
    }

    private void addRandomNum() {//随机产生一个数字

        emptyPoints.clear();

        for(int y = 0; y < 4; y++) {
            for(int x = 0; x < 4; x++) {
                if(cardsMap[x][y].getNum() <= 0) {//往空卡片添加随机数
                    emptyPoints.add(new Point(x, y));
                }
            }
        }

        Point p = emptyPoints.remove((int)(Math.random() * emptyPoints.size()));//强制类型转换
        cardsMap[p.x][p.y].setNum(Math.random() > 0.1 ? 2 : 4);//随机产生2或4
    }

    public void swipeLeft() {//向左滑动

        boolean move = false;//默认不添加随机数字

        for(int y = 0; y < 4; y++) {
            for(int x = 0; x < 4; x++) {//数字循环左移

                for(int x1 = x+1; x1 < 4; x1++) {
                    if(cardsMap[x1][y].getNum() > 0) {//遇到相同数字就相加 数字不同则同时左移 直到本行最左边的一列数字到达左边界

                        if(cardsMap[x][y].getNum() <= 0) {
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);//不相同

                            x--;//避免特殊情况（右边数字与之前数字相同，但移动后该数字放到之前数字的位置但并没有合并）再遍历一遍
                            move = true;//有移动就添加新的随机数
                        } else if(cardsMap[x][y].equals(cardsMap[x1][y])) {
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);//相同数字合并
                            cardsMap[x1][y].setNum(0);//清除右边数字

                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());//合并之后计分
                            move = true;//有移动就添加新的随机数
                        }

                        break;
                    }
                }
            }
        }

        if(move) {
            MainActivity.getMainActivity().moveVoice();//合并就加分
            addRandomNum();//有移动就添加新的随机数
            checkFinish();//检查游戏是否结束
        }
    }
    public void swipeRight() {//向右滑动

        boolean move = false;

        for(int y = 0; y < 4; y++) {
            for(int x = 3; x >= 0; x--) {

                for(int x1 = x-1; x1 >= 0; x1--) {
                    if(cardsMap[x1][y].getNum() > 0) {

                        if(cardsMap[x][y].getNum() <= 0) {
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);

                            x++;
                            move = true;
                        } else if(cardsMap[x][y].equals(cardsMap[x1][y])) {
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x1][y].setNum(0);

                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            move = true;
                        }

                        break;
                    }
                }
            }
        }

        if(move) {
            MainActivity.getMainActivity().moveVoice();
            addRandomNum();
            checkFinish();
        }
    }
    public void swipeUp() {//向上滑动

        boolean move = false;

        for(int x = 0; x < 4; x++) {
            for(int y = 0; y < 4; y++) {

                for(int y1 = y+1; y1 < 4; y1++) {
                    if(cardsMap[x][y1].getNum() > 0) {

                        if(cardsMap[x][y].getNum() <= 0) {
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);

                            y--;
                            move = true;
                        } else if(cardsMap[x][y].equals(cardsMap[x][y1])) {
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x][y1].setNum(0);

                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            move = true;
                        }

                        break;
                    }
                }
            }
        }

        if(move) {
            MainActivity.getMainActivity().moveVoice();
            addRandomNum();
            checkFinish();
        }
    }
    public void swipeDown() {//向下滑动

        boolean move = false;

        for(int x = 0; x < 4; x++) {
            for(int y = 3; y >= 0; y--) {

                for(int y1 = y-1; y1 >= 0; y1--) {
                    if(cardsMap[x][y1].getNum() > 0) {

                        if(cardsMap[x][y].getNum() <= 0) {
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);

                            y++;
                            move = true;
                        } else if(cardsMap[x][y].equals(cardsMap[x][y1])) {
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x][y1].setNum(0);

                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            move = true;
                        }

                        break;
                    }
                }
            }
        }

        if(move) {
            MainActivity.getMainActivity().moveVoice();
            addRandomNum();
            checkFinish();
        }
    }

    private void checkFinish() {//检查结束游戏

        boolean finish = true;

        ALL:
        for(int y = 0; y < 4; y++) {
            for(int x = 0; x < 4; x++) {
                if(cardsMap[x][y].getNum() == 0 ||  //判断是否还有空位置
                        x>0 && cardsMap[x][y].equals(cardsMap[x-1][y]) ||  //判断四个方向上是否有相同数字
                        x<3 && cardsMap[x][y].equals(cardsMap[x+1][y]) ||
                        y>0 && cardsMap[x][y].equals(cardsMap[x][y-1]) ||
                        y<3 && cardsMap[x][y].equals(cardsMap[x][y+1])) {

                    finish = false;
                    break ALL;//跳出所有循环
                }
            }
        }

        if(finish) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Game Over！！！")
                    .setMessage("Final Score " + MainActivity.getMainActivity().printScore() + " #")
                    .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startGame(); //重新开始一局
                        }})
                    .setCancelable(false)
                    .show();
        }
    }

    private Card[][] cardsMap = new Card[4][4];//二维数组 记录16个卡片的值
    private List<Point> emptyPoints = new ArrayList<Point>();
    private int card_width = MainActivity.getMainActivity().min_height_weight();
    EditText et_input;
}
