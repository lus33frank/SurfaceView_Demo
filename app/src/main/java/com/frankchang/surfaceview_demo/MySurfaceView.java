package com.frankchang.surfaceview_demo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    // 物件
    private final SurfaceHolder holder;
    private final Paint pen1;
    private final Paint pen2;
    // 變數
    private int x1, x2;
    private int y1, y2;
    private int r1, r2;
    private int speedX1, speedX2;
    private int speedY1;
    private boolean chkBall1 = true;
    private boolean chkBall2 = true;


    public MySurfaceView(Context context) {
        super(context);

        // 建立 SurfaceView
        holder = getHolder();
        holder.addCallback(this);

        // 建立第一支畫筆
        pen1 = new Paint();
        pen1.setColor(Color.RED);
        pen1.setAntiAlias(true);

        // 建立第二支畫筆
        pen2 = new Paint();
        pen2.setColor(Color.BLUE);
        pen2.setAntiAlias(true);

        // 初始化變數
        initData();
    }

    // 初始化變數
    private void initData() {
        // Ball 1
        x1 = 250;
        y1 = 320;
        r1 = 50;
        speedX1 = 20;
        speedY1 = 10;

        // Ball 2
        x2 = 50;
        y2 = 120;
        r2 = 80;
        speedX2 = 10;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // 建立畫面呈現
        new Ball01().start();
        new Ball02().start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // 畫面異動呈現
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // 結束畫面呈現
    }

    // 繪製
    private void drawCanvas() {
        Canvas canvas = holder.lockCanvas();
        canvas.drawColor(Color.BLACK);      // 背景色：黑色
        canvas.drawCircle(x1, y1, r1, pen1);
        canvas.drawCircle(x2, y2, r2, pen2);
        holder.unlockCanvasAndPost(canvas);
    }

    // 檢查是否碰撞
    private boolean detectBall() {
        double detect = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
        return detect <= (r2 + r2);
    }


    private class Ball01 extends Thread {

        @Override
        public void run() {
            super.run();

            int count = 0;

            while (chkBall1) {
                x1 = x1 + speedX1;
                y1 = y1 + speedY1;

                // X
                if (x1 + r1 >= getWidth()) {
                    x1 = getWidth() - r1;
                    speedX1 = -speedX1;
                    count++;

                } else if (x1 - r1 <= 0) {
                    x1 = r1;
                    speedX1 = -speedX1;
                    count++;
                }

                // Y
                if (y1 + r1 >= getHeight()) {
                    y1 = getHeight() - r1;
                    speedY1 = -speedY1;
                    count++;

                } else if (y1 - r1 <= 0) {
                    y1 = r1;
                    speedY1 = -speedY1;
                    count++;
                }

                // 繪製
                drawCanvas();

                // 檢查是否碰撞
                boolean chkTouch = detectBall();
                if (chkTouch) {
                    chkBall1 = false;
                    chkBall2 = false;
                }

                // 撞牆10次後停止移
                if (count == 10) {
                    chkBall1 = false;
                }

                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    private class Ball02 extends Thread {

        @Override
        public void run() {
            super.run();

            while (chkBall2) {
                x2 = x2 + speedX2;
                if (x2 + r2 >= getWidth()) {
                    x2 = getWidth() - r2;
                    speedX2 = -speedX2;

                } else if (x2 - r2 <= 0) {
                    x2 = r2;
                    speedX2 = -speedX2;
                }

                // 繪製
                drawCanvas();

                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
