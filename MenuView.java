package com.example.zhangwenqiang.rjks_final_pro;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

public class MenuView extends SurfaceView implements SurfaceHolder.Callback, OnTouchListener {

	SurfaceHolder holder;
	Canvas canvas;
	boolean threadFlag = true;
	Bitmap background;
	Context context;
	private int x = 270;
	private int y = 50;
	private Bitmap[] menuItems;
	public MenuView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	private void init() {
		menuItems = new Bitmap[3];
		holder = getHolder();
		background = BitmapFactory.decodeResource(context.getResources(), R.drawable.menu_bg);
		menuItems[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.menu1);
		menuItems[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.menu2);
		menuItems[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.menu3);
//		new GameView(context);
		this.getHolder().addCallback(this);
		this.setOnTouchListener(this);
	}

	Thread menuThread = new Thread() {
		@SuppressLint("WrongCall")
		@Override
		public void run() {

			while (threadFlag) {
				try {
					canvas = holder.lockCanvas();
					synchronized (this) {
						onDraw(canvas);
					}
					// System.out.println("menuThread");
				} finally {
						holder.unlockCanvasAndPost(canvas);
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	};

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {

		Rect src = new Rect();
		Rect des = new Rect();
		src.set(0, 0, background.getWidth(), background.getHeight());
		// System.out.println("menu:" + background.getWidth() + "X" +
		// background.getHeight());
		des.set(0, 0, MainActivity.SCREEN_WIDTH, MainActivity.SCREEN_HEIGHT);
		Paint paint = new Paint();
		canvas.drawBitmap(background, src, des, paint);
		for (int i = 0; i < menuItems.length; i++) {
			canvas.drawBitmap(menuItems[i], (int) (x * MainActivity.SCALE_HORIAONTAL),
					(int) ((y + i * 80) * MainActivity.SCALE_VERTICAL), paint);
		}

	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		threadFlag = true;
		menuThread.start();
		System.out.println("surfaceCreated");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		threadFlag = false;
		boolean retry = true;
		while (retry) {// ѭ��
			try {
				menuThread.join();// �ȴ��߳̽���
				retry = false;// ֹͣѭ��
			} catch (InterruptedException e) {
			}// ���ϵ�ѭ����ֱ��ˢ֡�߳̽���
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int ex = (int) event.getX();
		int ey = (int) event.getY();
		System.out.println(event.getX() + "," + event.getY());
		int selectIndex = -1;
		for (int i = 0; i < menuItems.length; i++) {
			System.out.println(x + "  " + (y + i * 80));
			if (CardsManager.inRect(ex, ey, (int) (x * MainActivity.SCALE_HORIAONTAL),
					(int) ((y + i * 80) * MainActivity.SCALE_VERTICAL),
					(int) (206 * MainActivity.SCALE_HORIAONTAL),
					(int) (43 * MainActivity.SCALE_VERTICAL))) {
				selectIndex = i;
				break;
			}
		}
		System.out.println(selectIndex);
		switch (selectIndex) {
			case 0 :
				MainActivity.handler.sendEmptyMessage(MainActivity.GAME);
				break;
			case 1 :
				MainActivity.handler.sendEmptyMessage(MainActivity.EXIT);
				break;
			case 2 :
				MainActivity.handler.sendEmptyMessage(MainActivity.START);
				break;
		}
		return super.onTouchEvent(event);
	}
}
